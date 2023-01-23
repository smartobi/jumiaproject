# THIS IS A RUNDOWN READ ME FILE CONTAINING THE PROJECT WORKFLOW.

## STEP ONE.
##=============
I created a github repo for the new project.
https://github.com/smartobi/jumiaproject

## GIT BRANCHES CREATED FOR THIS PRJECT
### 1) MAIN BRANCH HANDLES THE DEPLOYMENT OF POSTGRES RDS AWS INSTANCE
### 2) MICROSERVICE BRANCH  HANDLES BACKEND FRONTEND BUILD, TAG AND PUSHING OF DOCKER IMAGE TO DOCKER HUB
### 3) C_Deploy  BRANCH HANDLES THE DEPLOYING MICROSERICE TO KUBERNETES CLUSTER

### NOTE:
       Terraform deployment was done on from my local system and i have added the file contained in terraform folder.
---
## STEP TWO
1) I created a Terraform files to provision all the servers as required.
2) Ansible and Jenkins server was configured with terraform during provisioning.
3) Private sshkey was injected into each of the servers during provisioning.
4) I use terraform Output to display the credencials of the created resources.
5) An admin user named "jumia-user" aws created during provision to all the servers.
6) SSH port was changed during terraform provisioning from port 22 to 1337
7) I also configured the security groups to allow access as instructed so using 1337 i was able to access the servers with MobaXterm

### NOTE:
      I have added a screenshot of the terraform configuration file and created resources.
---      
## STEP THREE  || Ansible-playbook ||
After the infrasturcture provisoning, 
I use connected MobaXterm to connect to Jenkins and Ansible server via ssh with port 1337 
Since Ansible server was configured during provisioning, I was able to run create playbook to configure Jenkins firewall.

### NOTE:
      I have added a screenshot of the for this loacated at the image directory.
---

## STEP THREE  || Jenkins initals and Pipeline ||
Due to the nature of the project, i confugured Three Jenkins Pipeline.
### First pipeline
1) for Configuring of Subnet Group for Postgres RDS in AWS.
2) Provisioning of Postgres RDS instance in AWS  
3) Configuring uploading Sample.sql data to postgres
    
#### PIPELINE SCRIPT 1:
node {
    stage("Checking-out Jumia-phone-validator-public repoo"){
        git branch: 'main', url: 'https://github.com/smartobi/jumiaproject.git'
    }
    
    stage("Copying project file to  Ansible"){
        sshagent(['ansible_login']) {
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337"
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 rm -rf jumia_phone_validator_pipelineJob"
            sh "scp -rP 1337 /var/lib/jenkins/workspace/jumia_phone_validator_pipelineJob jumia-user@10.0.4.14:/home/jumia-user"
        }
    }
    
   stage("Create Postgres subnet-group"){
        sshagent(['ansible_login']) {
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 ansible-playbook jumia_phone_validator_pipelineJob/ansible_playbook/ansible_subnet.yaml"
        }
    }
    
    stage("Create Postgres DataBase Instance"){
        sshagent(['ansible_login']) {
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 ansible-playbook jumia_phone_validator_pipelineJob/ansible_playbook/ansible_postdb.yaml"
        }
    }
}

### Second pipeline  || CICD STAGES ||
This second pipeline was actually the longest pipeline in my deployment. which is CI Continous Integration.
1)It build the two decker image by runing mvn clean
2) MVN install to produce target folder which contains the artifact.
3) Building the image from the Dockerfile
4) Tagging the build image.
5) Pushing the Tagged image to Docker Hub
6) Cleaning Up the WorkSpace.

#### PIPELINE SCRIPT 2:
    node {
    stage("Checking-out Jumia-phone-validator-public repoo"){
        git branch: 'microservice', url: 'https://github.com/smartobi/jumiaproject.git'
    }
    
    stage("Copying project file to  Ansible"){
        sshagent(['ansible_login']) {
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337"
            sh "scp -rP 1337 /var/lib/jenkins/workspace/jumia_phone_validator_microservicedeployment/validator-backend/* jumia-user@10.0.4.14:/home/jumia-user"
        }
    }
    
   stage("MVN clean"){
        sshagent(['ansible_login']) {
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 mvn clean"
        }
    }
    
    stage("MVN install to produce artifact"){
        sshagent(['ansible_login']) {
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 mvn install"
        }
    }
    
    
    stage("Building Docker image for Validator-backend"){
        sshagent(['ansible_login']) {
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 docker build -t validator-backend-image:v1.$BUILD_ID ."
        }
    }
    
    stage("Docker image tagging"){
        sshagent(['ansible_login']) {
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 docker image tag validator-backend-image:v1.$BUILD_ID smartcloud2022/validator-backend-image:v1.$BUILD_ID"
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 docker image tag validator-backend-image:v1.$BUILD_ID smartcloud2022/validator-backend-image:latest"
        }
    }
    
    stage("PushingTagged image to Docker Hub"){
        sshagent(['ansible_login']){
        withCredentials([string(credentialsId: 'dockerpass', variable: 'dockerpass')]) {
            sh "ssh -t -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 docker login -u smartcloud2022 -p ${dockerpass}"
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 docker image push smartcloud2022/validator-backend-image:v1.$BUILD_ID"
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 docker image push smartcloud2022/validator-backend-image:latest"
            
            }
        }
    }
    
    stage("Cleaning Up WorkSpace"){
        sshagent(['ansible_login']) {
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 rm -rf src/"
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 rm Dockerfile"
        }
    }
    
    stage("Building Frontend NPM and NPM build"){
        sshagent(['ansible_login']) {
            sh "scp -rP 1337 /var/lib/jenkins/workspace/jumia_phone_validator_microservicedeployment/validator-frontend/* jumia-user@10.0.4.14:/home/jumia-user"
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 npm install"
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 npm run build"
        }
    }
    
    stage("Building Docker image for Validator-Frontend"){
        sshagent(['ansible_login']) {
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 docker build -t validator-frontend-image:v1.$BUILD_ID ."
        }
    }
    
    stage("Docker image tagging validator-frontend-image"){
        sshagent(['ansible_login']) {
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 docker image tag validator-frontend-image:v1.$BUILD_ID smartcloud2022/validator-frontend-image:v1.$BUILD_ID"
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 docker image tag validator-frontend-image:v1.$BUILD_ID smartcloud2022/validator-frontend-image:latest"
        }
    }
    
    stage("PushingTagged image to Docker Hub"){
        sshagent(['ansible_login']){
        withCredentials([string(credentialsId: 'dockerpass', variable: 'dockerpass')]) {
            sh "ssh -t -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 docker login -u smartcloud2022 -p ${dockerpass}"
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 docker image push smartcloud2022/validator-frontend-image:v1.$BUILD_ID"
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.14 -p 1337 docker image push smartcloud2022/validator-frontend-image:latest"
            
        }
    }
    
}
}
  



### Third pipeline  || CICD STAGES ||
This pipeline install deployed the Microservice to EKS using Helm chart.
1)AFter git cloning and copying to Ansible server,
2) Run an Ansible_Playbook to deploy the Authenticate Ansible server to EKS Kubernetes cluster.
3) Create a helm chart for the Microservice project both Frontend and Backend deployment with their respective services.

#### PIPELINE SCREPT 3:
         node {
    stage("Checking-out Jumia-phone-validator-public repoo"){
        git branch: 'C_Deploy', url: 'https://github.com/smartobi/jumiaproject.git'
    }
    
        stage("Copying project file to  Ansible"){
        sshagent(['ansible_login']) {
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.13 -p 1337"
            sh "scp -rP 1337 /var/lib/jenkins/workspace/continous_deployment/ansible_playbook/* jumia-user@10.0.4.13:/home/jumia-user"
            sh "scp -rP 1337 /var/lib/jenkins/workspace/continous_deployment/ansible_playbook jumia-user@10.0.4.13:/home/jumia-user"
        }
    }
    
     stage("Authenticating with EKS"){
        sshagent(['ansible_login']) {
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.13 -p 1337 ansible-playbook ./ansible_deploy.yaml"
        }
    }
    
    stage("Helm Installing Jumia-Phone-Validator MicroService"){
        sshagent(['ansible_login']) {
            sh "scp -rP 1337 /var/lib/jenkins/workspace/continous_deployment/ansible_playbook jumia-user@10.0.4.13:/home/jumia-user"
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.13 -p 1337 helm uninstall jumiaphonevalidator"

            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.13 -p 1337 ansible-playbook ./ansible_helm.yaml"
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.13 -p 1337 kubectl get pod"
        }
    }
    stage("Geting the State of the Microservice"){
        sshagent(['ansible_login']) {
            sh "ssh -o StrictHostKeyChecking=no jumia-user@10.0.4.13 -p 1337 kubectl get all -o wide"
        }
    }
    
    
}

# STEP4 
# VERIFCATION STAGE.
# STEP1 [ API QURERING ON THE BACKEND POD  with POSTMAN ]
 1)  I used PostMan to run api query ont backend pod to fetch the list of data entries in the database.
  to confire that the POD is runing.
 
 # STEP1 [ API QURERING ON THE FRONT POD  with POSTMAN ]
 2) I was able to access the frontend from UI and API with Postman and Chrome browser.
 
 ## =================================================================================
 
 # CHALLENGIES
 ===============
 I was expecting to querry the backend from the frontend web portal. but it was going. 
 
 # NOTE.
      That i confired added the LOADBalancer to the frontend configuration file so as to enable the frontend to talk to the backend.
 
 CONCLUSION
 ============
 I was suspecting that there maybe code issue from the frontend during codding.
 
 REMARK
 ========
I wanted to debug this but. I have limited time as to currect the code.


SALUTATION
===========
I appricate and love the code challenge for some reason which one of them is reactivation my critical reasoning.

Thanks.
 
 
 
 
