### Spring Boot Application with CI/CD Pipeline, Docker, and Kubernetes
===================================================================

Table of Contents
-----------------

- [Table of Contents](#table-of-contents)
- [Project Overview](#project-overview)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Steps Followed](#steps-followed)
  - [1. Setup Spring Boot Application](#1-setup-spring-boot-application)
  - [2. Dockerization](#2-dockerization)
  - [3. Kubernetes Deployment](#3-kubernetes-deployment)
  - [4. CI/CD Pipeline with GitHub Actions](#4-cicd-pipeline-with-github-actions)
- [How to Run the Application Locally](#how-to-run-the-application-locally)
- [How to Deploy](#how-to-deploy)
- [CI/CD Pipeline Flow](#cicd-pipeline-flow)
- [License](#license)

Project Overview
----------------

This project demonstrates the end-to-end process of building, containerizing, and deploying a Spring Boot application to a Kubernetes cluster with a robust CI/CD pipeline. The application is built using Java 17 and Maven. We utilize Docker for containerization, Kubernetes for orchestration, and GitHub Actions for continuous integration and deployment.

Prerequisites
-------------

To follow along with this project, ensure you have the following installed:

*   Java 17 (or later)
*   Maven (for building the Spring Boot application)
*   Docker (for containerizing the application)
*   Kubernetes (or access to a Kubernetes cluster)
*   kubectl (for managing Kubernetes deployments)
*   GitHub account
*   GitHub CLI (optional for local interaction with GitHub)

Project Structure
-----------------

    bash . ├── Dockerfile # Dockerfile to build the Docker image ├── Jenkinsfile # If Jenkins was used for pipeline (not in this example) ├── k8s/ # Kubernetes deployment files │ ├── deployment.yaml # Deployment configuration for Kubernetes │ └── service.yaml # Service configuration for Kubernetes ├── src/ # Source code of the Spring Boot application ├── .github/ # GitHub Actions CI/CD pipeline configuration │ └── workflows/ # CI/CD pipeline YAML file │ └── ci-cd-pipeline.yml ├── pom.xml # Maven project configuration file └── README.md # This readme file 

Steps Followed
--------------

### 1\. Setup Spring Boot Application

A Spring Boot application was created using Spring Initializr or manually with the necessary dependencies (e.g., Spring Web, Spring Boot DevTools). The application was structured with basic RESTful endpoints to simulate a simple backend.

### 2\. Dockerization

A Dockerfile was created to containerize the Spring Boot application. This file specifies the base image, dependencies, and how to build the application within the container.

**Key sections of Dockerfile:**

*   Use of `maven:3.8.1-jdk-11` image for building the application.
*   Running `mvn clean package` to build the application.
*   Copying the JAR file and defining entry points.

    dockerfile FROM maven:3.8.1-jdk-11 AS build COPY . /app WORKDIR /app RUN mvn clean package FROM openjdk:11-jre-slim COPY --from=build /app/target/my-spring-boot-app.jar /app/my-spring-boot-app.jar ENTRYPOINT ["java", "-jar", "/app/my-spring-boot-app.jar"] 

### 3\. Kubernetes Deployment

Kubernetes manifests (`deployment.yaml` and `service.yaml`) were created to define how the application should be deployed and accessed in a Kubernetes environment.

    yaml apiVersion: apps/v1 kind: Deployment metadata: name: spring-boot-app spec: replicas: 3 selector: matchLabels: app: spring-boot-app template: metadata: labels: app: spring-boot-app spec: containers: - name: spring-boot-app image: siddharth133/my-spring-boot-app:latest ports: - containerPort: 8080 

### 4\. CI/CD Pipeline with GitHub Actions

A CI/CD pipeline was configured using GitHub Actions. The pipeline automates the build, test, and deployment process of the Spring Boot application.

**Pipeline Steps:**

*   **Build Job:**

*   Checkout the code.
*   Set up Java 17 environment.
*   Build the application with Maven.
*   Dockerize the app and push it to Docker Hub.

text*   **Deploy Job:**

*   Set up kubectl.
*   Apply Kubernetes configurations to deploy the application to a Kubernetes cluster.

    yaml name: CI/CD Pipeline on: push: branches: - main pull_request: branches: - main jobs: build: runs-on: ubuntu-latest steps: - name: Checkout Code uses: actions/checkout@v3 - name: Set up JDK 17 uses: actions/setup-java@v3 with: java-version: 17 distribution: 'adoptopenjdk' - name: Build with Maven run: mvn clean package - name: Log into Docker uses: docker/login-action@v2 with: username: ${{ secrets.DOCKER_USERNAME }} password: ${{ secrets.DOCKER_PASSWORD }} - name: Build and Push Docker Image run: | docker build -t siddharth133/my-spring-boot-app:latest . docker push siddharth133/my-spring-boot-app:latest deploy: runs-on: ubuntu-latest needs: build steps: - name: Checkout Code uses: actions/checkout@v3 - name: Set up Kubectl uses: kubectl/kubectl@v1.22.0 with: k8s-version: 1.22.0 - name: Set up Kubeconfig run: echo "${{ secrets.KUBE_CONFIG }}" > $HOME/.kube/config - name: Deploy to Kubernetes run: | kubectl apply -f k8s/deployment.yaml kubectl apply -f k8s/service.yaml 

How to Run the Application Locally
----------------------------------

    bash git clone https://github.com/your-username/your-repo.git cd your-repo docker build -t my-spring-boot-app . docker run -p 8080:8080 my-spring-boot-app Open your browser and navigate to http://localhost:8080 to access the application. 

How to Deploy
-------------

**Deploy to Kubernetes using GitHub Actions:**

*   Push your changes to the main branch of the GitHub repository.
*   GitHub Actions will trigger the pipeline to:

*   Build the Spring Boot app.
*   Dockerize and push the image to Docker Hub.
*   Deploy the application to your Kubernetes cluster.

**Manually Deploying to Kubernetes:**

If you prefer manual deployment, ensure kubectl is configured and your Kubernetes cluster is accessible. Apply the Kubernetes manifests:

    bash kubectl apply -f k8s/deployment.yaml kubectl apply -f k8s/service.yaml 

CI/CD Pipeline Flow
-------------------

*   **Code Push:** Pushing code to the main branch triggers the pipeline.
*   **Build Job:** The pipeline checks out the code, sets up JDK, builds the app, and pushes the Docker image to Docker Hub.
*   **Deploy Job:** The pipeline deploys the Docker image to a Kubernetes cluster using kubectl.

License
-------

This project is licensed under the MIT License - see the LICENSE file for details.