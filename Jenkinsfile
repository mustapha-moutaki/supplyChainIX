pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'Maven 3.9.0'
    }

    environment {
        IMAGE_NAME = "supplychainx-app"
        CONTAINER_NAME = "supplychainx-container"
    }

    stages {
        stage('Checkout') {
            steps {
                echo ' Checking out the code...'
                checkout scm
            }
        }


        stage('Build') {
            steps {
                echo ' Building the application...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo ' Running unit & integration tests...'
                sh 'mvn test'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                sh "docker build -t $IMAGE_NAME ."
            }
        }

        stage('Docker Run') {
            steps {
                echo ' Running Docker container...'
                sh """
                    docker stop $CONTAINER_NAME || true
                    docker rm $CONTAINER_NAME || true
                    docker run -d --name $CONTAINER_NAME -p 8080:8080 $IMAGE_NAME
                """
            }
        }

        stage('Clean') {
            steps {
                echo '🧹 Cleaning old Docker containers/images (optional)...'
//             i won't use it right now
            }
        }
    }

    post {
        success {
            echo ' Pipeline finished successfully!'
        }
        failure {
            echo ' Pipeline failed. Check logs!'
        }
    }
}
