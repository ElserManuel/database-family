pipeline {
    agent any

    tools {
        maven 'Maven_3.8.6'
        jdk 'jdk17'
    }

    environment {
        // Variables de entorno para SonarCloud - el token debe estar configurado como credencial en Jenkins
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
                withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN')]) {
                    sh '''
                        mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \\
                        -Dsonar.projectKey=ElserManuel_database-family \\
                        -Dsonar.host.url=https://sonarcloud.io \\
                        -Dsonar.login=${SONAR_TOKEN}
                    '''
                }
            }
        }

        stage('Esperar análisis en Sonar') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    timeout(time: 2, unit: 'MINUTES') {
                        waitForQualityGate abortPipeline: false
                    }
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