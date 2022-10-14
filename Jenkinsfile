// ######################################################################### //

pipeline {
  agent {
    kubernetes {
      //cloud 'kubernetes.'
      defaultContainer 'jnlp'
      yaml '''
        kind: Pod
        metadata:
          name: jenkins-agent
        spec:
          containers:
          - name: maven
            image: maven:3.8.4-openjdk-8
            imagePullPolicy: Always
            volumeMounts:
              - name: maven-build-cache
                mountPath: /root/.m2/repository
            env:
            - name: DOCKER_REGISTRY_USER
              valueFrom:
                secretKeyRef:
                  name: artifactory-creds-generic
                  key: username
            - name: DOCKER_REGISTRY_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: artifactory-creds-generic
                  key: password
            - name: JFROG_USERNAME
              valueFrom:
                secretKeyRef:
                  name: artifactory-creds-generic
                  key: username
            - name: JFROG_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: artifactory-creds-generic
                  key: password
            command:
            - sleep
            args:
            - 9999999
            resources:
              limits:
                cpu: 2000m
                memory: 8Gi
              requests:
                cpu: 1000m
                memory: 6Gi
          - name: sonar
            image: sonarsource/sonar-scanner-cli
            imagePullPolicy: Always
            env:
            - name: SONAR_HOST_URL
              value: "http://sonarqube-prd.sonarqube.svc.cluster.local:8080"
            command:
            - sleep
            args:
            - 9999999
            resources:
              limits:
                cpu: 1
                memory: 1Gi
              requests:
                cpu: 0.5
                memory: 500Mi
          volumes:
          - name: artifactory-creds
            projected:
              sources:
              - secret:
                  name: artifactory-creds
                  items:
                    - key: .dockerconfigjson
                      path:  config.json
          - name: maven-build-cache
            persistentVolumeClaim:
              claimName: maven-build-cache-pvc
'''
    }
  }
  stages {
    stage('Compile and Check Style') {
      steps {
        container('maven') {
        }
      }
    }
    stage('Parallel Test and Scan') {
      parallel {
        stage('Run Tests') {
          steps {
            container('maven') {
              sh 'mvn test --batch-mode --settings mvn-settings.xml -U'
              junit skipPublishingChecks: true,
              testResults: 'target/surefire-reports/**/*.xml'
            }
          }
        }
        stage('Code Quality Check') {
          steps {
            container('maven') {
              sh 'mvn spotbugs:check --batch-mode --settings mvn-settings.xml -U'
              archiveArtifacts allowEmptyArchive: true,
              artifacts: 'target/spotbugs**.xml',
              onlyIfSuccessful: true
            }
          }
        }
        stage('Security Check') {
          steps {
            container('maven') {
              sh """
              mvn dependency-check:check -Dformats=HTML,JSON \
              --batch-mode --settings mvn-settings.xml -U
              """
              archiveArtifacts allowEmptyArchive: true,
              artifacts: 'target/dependency-check-report.html',
              onlyIfSuccessful: true
            }
          }
        }
      }
    }
    stage('SonarQube Scan') {
      steps {
        container('sonar') {
          sh """
          sonar-scanner -Dsonar.projectVersion=1.0.0-${BUILD_NUMBER} \
          -Dproject.settings=sonar-project.properties \
          -Dsonar.login="b82eb89bf882092bed6d7be1e05033b532832fac"
          """
        }
      }
    }
    stage('Build and Deploy Library') {
      steps {
        container('maven') {
          sh 'mvn deploy -DskipTests --batch-mode --settings mvn-settings.xml -U'
		  archiveArtifacts allowEmptyArchive: true,
		  artifacts: 'target/pmd.xml',
		  onlyIfSuccessful: true
        }
      }
    }
  }
}

// ######################################################################### //d