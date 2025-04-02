pipeline {
    agent any
  
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
    }
}
