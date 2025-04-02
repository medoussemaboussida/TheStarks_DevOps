pipeline {
    agent any
        environment {
            DOCKER_CREDENTIALS_ID = credentials('docker-hub-credentials')
        }


  
  stages {
        stage('Fetch Source Code') {
            steps {
                echo 'Pulling latest changes...'
                git branch: 'asma',
                    url: 'https://github.com/medoussemaboussida/4twin7_TheStarks_Kaddem.git'
            }
        }

 stage('Clean Workspace') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('Compile Project') {
            steps {
                sh 'mvn compile'
            }
        }
     stage('Tests - JUnit/Mockito') {
            steps {
                sh 'mvn test'
            }
        }

      stage('Build package') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Maven Install') {
            steps {
                sh 'mvn install'
            }
        }
       
        stage('Nexus') {
            steps {
                sh 'mvn deploy'
            }
        }

        stage('SonarQube Analysis') {  
            steps {
                withSonarQubeEnv('scanner') { 
                    sh 'mvn sonar:sonar'
                }
            }
        }
stage('Docker Login') {
    steps {
        withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
            sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
        }
    }
}

stage('Push Docker Image') {
    steps {
        sh 'docker push asmariahi/kaddem:1.0.0'
    }
}

        stage("Docker Compose") {
            steps {
                sh 'docker compose up -d'
            }
        }
    
    }
}
