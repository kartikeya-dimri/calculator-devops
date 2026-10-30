# Calculator DevOps CI/CD Pipeline

A Java-based console calculator project demonstrating an automated CI/CD pipeline using Jenkins, Maven, JUnit, Docker, Docker Compose, GitHub, and ngrok.

## Tech Stack

- **Java 21** – Calculator application
- **Maven** – Build and dependency management
- **JUnit** – Automated unit testing
- **Jenkins** – CI/CD pipeline automation
- **Docker & Docker Compose** – Containerization and deployment
- **Docker Hub** – Container image registry
- **GitHub** – Source code management
- **ngrok** – Secure GitHub webhook forwarding

## CI/CD Pipeline

The Jenkins pipeline performs the following stages:

1. **Checkout** – Fetch source code from GitHub.
2. **Build** – Compile and package the application using Maven.
3. **Test** – Execute JUnit tests.
4. **Docker Build** – Build an image tagged with the Jenkins build number.
5. **Docker Push** – Push the image to Docker Hub.
6. **Deploy** – Deploy the application using Docker Compose.
7. **Post-Deployment Verification** – Verify container status and deployed image.
8. **Email Notification** – Send build success or failure notifications.

## Webhook Automation

GitHub push events automatically trigger Jenkins through ngrok.

```text
GitHub
   ↓
ngrok Cloud Endpoint
   ↓  (Webhook signature verification)
ngrok Internal Endpoint
   ↓  (Secure tunnel)
ngrok Agent
   ↓
Jenkins (localhost:8080)
   ↓
CI/CD Pipeline
```

The ngrok Cloud Endpoint validates GitHub webhook signatures using a secret stored in an ngrok Secrets vault.

Start the local ngrok agent:

```bash
ngrok http 8080 --url https://jenkins.internal
```

## Build and Test Locally

```bash
mvn clean package
mvn test
```

## Docker Deployment

Build the image:

```bash
docker build -t calculator:latest .
```

Deploy a published image using Docker Compose:

```bash
export DOCKER_IMAGE=kartikeyadimri/calculator-devops
export DOCKER_TAG=<image-tag>

docker compose pull calculator
docker compose up -d --no-build calculator
```

Check the container:

```bash
docker compose ps
docker logs calculator
```

## Docker Hub

Images are published to:

`kartikeyadimri/calculator-devops`

Each successful pipeline build uses the Jenkins build number as its Docker image tag.

## Repository

https://github.com/kartikeya-dimri/calculator-devops

## Project Objective

Demonstrate continuous integration, automated testing, container image publishing, webhook-triggered deployment, and post-deployment verification for a Java console application.