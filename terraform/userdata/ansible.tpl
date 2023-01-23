#!/bin/bash

yum update -y

sudo useradd jumia-user
sudo echo "jumia-user ALL=(ALL) NOPASSWD:ALL" | sudo tee /etc/sudoers.d/jumia-user

sudo su - jumia-user


sudo dnf install python3 python3-pip -y


sudo dnf -y install https://dl.fedoraproject.org/pub/epel/epel-release-latest-8.noarch.rpm

sudo yum install epel-release -y


sudo yum -y install ansible 

