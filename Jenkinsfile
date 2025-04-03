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






     stage('Tests - JUnit/Mockito') {
            steps {
                sh 'mvn test'
            }
        }


       
        stage('Nexus') {
            steps {
                sh 'mvn deploy'
            }
        }
                stage('JaCoCo coverage report') {
                     steps {
                       step([$class: 'JacocoPublisher',
                             execPattern: '**/target/jacoco.exec',
                             classPattern: '**/classes',
                             sourcePattern: '**/src',
                              exclusionPattern: '*/target/**/,**/*Test*,**/*_javassist/**'
                             ])
                            }
                }


        stage('SonarQube Analysis') {  
            steps {
                withSonarQubeEnv('scanner') { 
                    sh 'mvn sonar:sonar'
                }
            }
        }


        stage('Docker Image') {
                    steps {
                        sh 'docker build -t asmariahi/kaddem:1.0.0 .'
                    }
                }
                stage('Docker Login') {
                    steps {
                        sh 'echo $DOCKER_CREDENTIALS_ID_PSW | docker login -u $DOCKER_CREDENTIALS_ID_USR --password-stdin'
                    }
                }


                stage("Docker Compose") {
                    steps {
                        sh 'docker compose up -d'
                    }
                }



    
    }
}
