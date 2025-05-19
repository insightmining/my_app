pipeline {
    agent any

    environment {
        GIT_URL = 'https://github.com/rde-devplace/devops-source.git'
        GIT_BRANCH = 'main' // 또는 master
        GIT_ID = 'skala-github-id' // GitHub PAT credential ID
        GIT_USER_NAME = 'himang10' // GitHub 사용자 이름
        GIT_USER_EMAIL = 'himang10@gmail.com'
        IMAGE_REGISTRY = 'amdp-registry.skala-ai.com/skala25a'
        IMAGE_NAME = 'sk000-my-app'
        IMAGE_TAG = '1.0.0'
        DOCKER_CREDENTIAL_ID = 'skala-image-registry-id'  // Harbor 인증 정보 ID
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: "${GIT_BRANCH}",
                    url: "${GIT_URL}",
                    credentialsId: "${GIT_ID}"   // GitHub PAT credential ID
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }


        stage('Docker Build & Push') {
            steps {
                script {
                    // 해시코드 12자리 생성
                    def hashcode = sh(
                        script: "date +%s%N | sha256sum | cut -c1-12",
                        returnStdout: true
                    ).trim()

                    // Build Number + Hash Code 조합 (IMAGE_TAG는 유지)
                    def FINAL_IMAGE_TAG = "${IMAGE_TAG}-${BUILD_NUMBER}-${hashcode}"
                    echo "Final Image Tag: ${FINAL_IMAGE_TAG}"

                    docker.withRegistry("https://${IMAGE_REGISTRY}", "${DOCKER_CREDENTIAL_ID}") {
                        def appImage = docker.build("${IMAGE_REGISTRY}/${IMAGE_NAME}:${FINAL_IMAGE_TAG}", "--platform linux/amd64 .")
                        appImage.push()
                    }
                
                    // 최종 이미지 태그를 env에 등록 (나중에 deploy.yaml 수정에 사용)
                    env.FINAL_IMAGE_TAG = FINAL_IMAGE_TAG
                }
            }
        }



        stage('Update deploy.yaml and Git Push') {
            steps {
                script {
                    def newImageLine = "        image: ${env.IMAGE_REGISTRY}/${env.IMAGE_NAME}:${env.FINAL_IMAGE_TAG}"
                    def gitRepoPath = env.GIT_URL.replaceFirst(/^https?:\/\//, '')

                    sh """
                        sed -i 's|^[[:space:]]*image:.*\$|${newImageLine}|g' ./argocd-k8s/deploy.yaml
                        cat ./argocd-k8s/deploy.yaml
                    """

                    sh """
                        git config user.name "$GIT_USER_NAME"
                        git config user.email "$GIT_USER_EMAIL"
                        git add ./argocd-k8s/deploy.yaml || true
                    """

                    withCredentials([usernamePassword(credentialsId: "${env.GIT_ID}", usernameVariable: 'GIT_PUSH_USER', passwordVariable: 'GIT_PUSH_PASSWORD')]) {
                        sh """
                            if ! git diff --cached --quiet; then
                                git commit -m "[AUTO] Update deploy.yaml with image ${env.FINAL_IMAGE_TAG}"
                                git remote set-url origin https://${GIT_PUSH_USER}:${GIT_PUSH_PASSWORD}@${gitRepoPath}
                                git push origin ${env.GIT_BRANCH}
                            else
                                echo "No changes to commit."
                            fi
                        """
                    }
                }
            }
        }


    }
}

