pipeline {
    agent any

    tools {
        maven 'Maven_3.8.6'
        jdk 'jdk17'
    }

    environment {
        SONAR_TOKEN = credentials('SONAR_TOKEN')

        // Variables de entorno para R2DBC
        R2DBC_URL = credentials('R2DBC_URL')
        R2DBC_USERNAME = credentials('R2DBC_USERNAME')
        R2DBC_PASSWORD = credentials('R2DBC_PASSWORD')

        // Variables para Kafka
        KAFKA_BOOTSTRAP_SERVERS = credentials('KAFKA_BOOTSTRAP_SERVERS')
        KAFKA_USERNAME = credentials('KAFKA_USERNAME')
        KAFKA_PASSWORD = credentials('KAFKA_PASSWORD')

        // Otras variables
        FAMILY_SERVICE_URL = credentials('FAMILY_SERVICE_URL')

        // Define los parámetros de SonarCloud como variables de entorno
        SONAR_PROJECT_KEY = 'ElserManuel_database-family'
        SONAR_ORGANIZATION = 'elsermanuel'
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
                withSonarQubeEnv('SonarCloud') {
                    // La forma segura de usar credenciales en Jenkins
                    sh '''
                        mvn sonar:sonar \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.organization=${SONAR_ORGANIZATION} \
                        -Dsonar.host.url=https://sonarcloud.io
                    '''
                }
            }
        }

        stage('Esperar análisis en Sonar') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        failure {
            echo 'El pipeline ha fallado'
        }
        success {
            echo 'El pipeline se completó con éxito'
        }
    }
}