
pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        DOCKER_IMAGE = 'kartikeyadimri/calculator-devops'
        DOCKER_CREDENTIALS = 'dockerhub-creds'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            tools {
                jdk 'JDK21'
                maven 'Maven'
            }
            steps {
                sh 'java -version'
                sh 'mvn -version'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            tools {
                jdk 'JDK21'
                maven 'Maven'
            }
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t "$DOCKER_IMAGE:$BUILD_NUMBER" .'
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: "${DOCKER_CREDENTIALS}",
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login \
                            -u "$DOCKER_USER" --password-stdin
                        docker push "$DOCKER_IMAGE:$BUILD_NUMBER"
                        docker logout
                    '''
                }
            }
        }

        stage('Verify Email Configuration') {
            steps {
                sh 'echo "Notification email configured: ${NOTIFICATION_EMAIL:+YES}"'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    export DOCKER_TAG="$BUILD_NUMBER"

                    docker compose config
                    docker compose pull calculator
                    docker compose up -d --no-build calculator
                    docker compose ps calculator

                    status=$(docker inspect \
                        --format='{{.State.Status}}' calculator)

                    if [ "$status" != "running" ]; then
                        echo "Deployment failed: calculator status is $status"
                        docker logs calculator
                        exit 1
                    fi

                    echo "Calculator container is running."
                '''
            }
        }

        
    stage('Post-Deployment Verification') {
        steps {
            sh '''
                set -eu

                echo "Waiting 5 seconds to verify container stability..."
                sleep 5

                if ! docker inspect calculator >/dev/null 2>&1; then
                    echo "FAIL: Calculator container does not exist."
                    exit 1
                fi

                status=$(docker inspect \
                    --format='{{.State.Status}}' calculator)

                image=$(docker inspect \
                    --format='{{.Config.Image}}' calculator)

                echo "Container status: $status"
                echo "Deployed image: $image"

                if [ "$status" != "running" ]; then
                    echo "FAIL: Container is not running."
                    docker logs --tail 50 calculator || true
                    exit 1
                fi

                expected_image="${DOCKER_IMAGE}:${BUILD_NUMBER}"

                if [ "$image" != "$expected_image" ]; then
                    echo "FAIL: Unexpected image deployed."
                    echo "Expected: $expected_image"
                    echo "Actual:   $image"
                    exit 1
                fi

                echo "PASS: Container is running with the expected image."
                docker compose ps calculator
                docker logs --tail 20 calculator
            '''
        }
    }

    }

    post {
        success {
            emailext(
                to: "${env.NOTIFICATION_EMAIL}",
                subject: "SUCCESS: ${JOB_NAME} #${BUILD_NUMBER}",
                body: """The Jenkins pipeline completed successfully.

    Job: ${JOB_NAME}
    Build Number: ${BUILD_NUMBER}
    Build URL: ${BUILD_URL}
    Docker Image: ${DOCKER_IMAGE}:${BUILD_NUMBER}
    Status: SUCCESS
    """
                )
            }

            failure {
                emailext(
                    to: "${env.NOTIFICATION_EMAIL}",
                    subject: "FAILURE: ${JOB_NAME} #${BUILD_NUMBER}",
                    body: """The Jenkins pipeline failed.

    Job: ${JOB_NAME}
    Build Number: ${BUILD_NUMBER}
    Build URL: ${BUILD_URL}
    Status: FAILURE

    Please check the Console Output for details.
    """
                )
            }

            always {
                echo 'Calculator CI/CD pipeline finished.'
            }
        }
}
