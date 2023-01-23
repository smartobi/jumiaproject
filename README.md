# THIS IS A RUNDOWN READ ME FILE CONTAINING THE PROJECT WORKFLOW.

## STEP ONE.
##=============
I created a github repo for the new project.
https://github.com/smartobi/jumiaproject

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

### Second pipeline  || CICD STAGES ||
This second pipeline was actually the longest pipeline in my deployment. which is CI Continous Integration.
1)It build the two decker image by runing mvn clean
2) MVN install to produce target folder which contains the artifact.
3) Building the image from the Dockerfile
4) Tagging the build image.
5) Pushing the Tagged image to Docker Hub
6) Cleaning Up the WorkSpace.

### Second pipeline  || CICD STAGES ||
This pipeline install deployed the Microservice to EKS using Helm chart.
1)AFter git cloning and copying to Ansible server,
2) Run an Ansible_Playbook to deploy the Authenticate Ansible server to EKS Kubernetes cluster.
3) Create a helm chart for the Microservice project both Frontend and Backend deployment with their respective services.


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
 
 
 
 
