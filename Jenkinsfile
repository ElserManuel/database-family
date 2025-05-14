pipeline{
    agent any

    tools{
        maven 'Maven_3.8.6'
        jdk 'jdk17'
    }

    environment  {
        SONARQUBE = 'SonarQubeServer'
    }

    stages {
        stage('clonar repositorio'){
            steps {
                git url: 'https://github.com/ElserManuel/database-family.git' , branch:'PRS_person'
            }
        }

        stage('Copilar Proyecto'){
            steps{
                sh 'mvn clean compile'
            }
        }

        stage('Ejecutar pruebas') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Generar Artefacto'){
            steps {
                sh 'mvn package'
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }

        stage('Analisis SonarQube'){
            steps{
                withSonarQubeEnv('SonarQubeServer') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=mi-proyecto -Dsonar.host.url=http://localhost:9000 -Dsonar.login=TOKEN'
                }
            }
        }

        stage('Esperar análisis en Sonar') {
            steps {
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}