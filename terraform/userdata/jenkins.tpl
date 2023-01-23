#!/bin/bash

sudo yum update -y 
yum -y install unzip wget tree git
yum install java-11-openjdk -y

sudo useradd jumia-user

sudo echo "jumia-user ALL=(ALL) NOPASSWD:ALL" | sudo tee /etc/sudoers.d/jumia-user 

sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key
cd /etc/yum.repos.d/
sudo curl -O https://pkg.jenkins.io/redhat-stable/jenkins.repo

sudo yum -y install jenkins  --nobest

sudo systemctl start jenkins
sudo systemctl enable jenkins




