pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'kartikeyadimri/calculator-devops'
        DOCKER_TAG = '1.0'
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
                sh 'docker build -t $DOCKER_IMAGE:$DOCKER_TAG .'
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
                        docker push "$DOCKER_IMAGE:$DOCKER_TAG"
                        docker logout
                    '''
                }
            }
        }
    }

    post {
        success {
            emailext(
                to: 'YOUR_EMAIL@example.com',
                subject: "SUCCESS: ${JOB_NAME} #${BUILD_NUMBER}",
                body: """The Jenkins pipeline completed successfully.

                Job: ${JOB_NAME}
                Build Number: ${BUILD_NUMBER}
                Build URL: ${BUILD_URL}
                Status: SUCCESS
                """
                )
        }

        failure {
            emailext(
                to: 'YOUR_EMAIL@example.com',
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