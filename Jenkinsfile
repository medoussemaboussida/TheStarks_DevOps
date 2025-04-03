pipeline {
    agent any

    stages {
        stage('Clean Workspace') {
            steps {
                echo 'Cleaning workspace to avoid Git corruption'
                deleteDir() // Supprime tout le contenu du workspace
            }
        }

        stage('Checkout GIT') {
            steps {
                echo 'Pulling from branch roumaissa...'
                git branch: 'roumaissa',
                    url: 'https://github.com/medoussemaboussida/4twin7_TheStarks_Kaddem.git'
            }
        }

        stage('MAVEN BUILD') {
            steps {
                echo 'Lancement de mvn clean et mvn compile'
                sh 'mvn clean compile'
            }
        }

        stage('SONARQUBE') {
            steps {
                echo 'Analyse de code'
                // Remplacez YOUR_SONAR_TOKEN par un token valide
                sh 'mvn sonar:sonar -Dsonar.token=sqa_81266d80b7436da886b5d0c01efe6d912b3f1265'
            }
        }

    stage('MOCKITO') {
                steps {
                    echo 'Lancement de mvn test avec Mockito'
                    sh 'mvn test -Dtest=ContratServiceMockTests'
                }
            }



        stage('NEXUS') {
            steps {
                echo 'Deploying artifacts to Nexus repository'
                sh 'mvn deploy'
            }
        }

        stage('DOCKER IMAGE') {
            steps {
                echo 'Building Docker image'
                sh 'docker build -t roumaissa1/roumaissazdiri:1.0.0 .'
            }
        }

        stage('DOCKER HUB') {
            steps {
                echo 'Pushing Docker image to Docker Hub'
                sh '''
                    docker login -u roumaissa.zdiri@esprit.tn -p 234AFT1597
                    docker push roumaissa1/roumaissazdiri:1.0.0
                '''
            }
        }
    }
}