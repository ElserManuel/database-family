pipeline {
    agent any

    tools {
        maven 'Maven_3.8.6'
        jdk 'jdk17'
    }

    environment {
        SONAR_TOKEN = credentials('SONAR_TOKEN') // Token creado en Jenkins
    }

    stages {
        stage('Clonar repositorio') {
            steps {
                git url: 'https://github.com/ElserManuel/database-family.git', branch: 'PRS_person'
            }
        }

        stage('Compilar Proyecto') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Ejecutar pruebas') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Generar Artefacto') {
            steps {
                sh 'mvn package'
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }

        stage('Análisis SonarCloud') {
            steps {
                sh """
                    mvn sonar:sonar \
                    -Dsonar.projectKey=ELSERMANUEL_database-family \
                    -Dsonar.organization=ELSERMANUEL \
                    -Dsonar.host.url=https://sonarcloud.io \
                    -Dsonar.login=$SONAR_TOKEN
                """
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
