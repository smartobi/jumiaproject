#!/bin/bash

sudo hostnamectl set-hostname "k8smaster"

#Set hostname and add entries in the hosts file
sudo cat << EOF > /etc/hosts
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6

172.31.11.52    k8smaster.jumia.local k8smaster
172.31.33.33    k8sworker1.jumia.local k8sworker1
172.31.34.222   k8sworker2.jumia.local k8sworker2

EOF

#Disable swap & add kernel settings

sudo swapoff -a
sudo sed -i '/ swap / s/^\(.*\)$/#\1/g' /etc/fstab

sudo tee /etc/modules-load.d/containerd.conf <<EOF
overlay
br_netfilter
EOF

sudo modprobe overlay
sudo modprobe br_netfilter

#Setting parameter for kubernetes run time.
# Setup required sysctl params, these persist across reboots.
cat > /etc/sysctl.d/99-kubernetes-cri.conf <<EOF
net.bridge.bridge-nf-call-iptables  = 1
net.ipv4.ip_forward                 = 1
net.bridge.bridge-nf-call-ip6tables = 1
EOF
sysctl --system

### Install required packages
dnf install -y  yum-utils device-mapper-persistent-data lvm2


## Add docker repository
dnf config-manager --add-repo=https://download.docker.com/linux/centos/docker-ce.repo Adding repo from: https://download.docker.com/linux/centos/docker-ce.repo

 dnf update -y && dnf install -y containerd.io

## Configure containerd
mkdir -p /etc/containerd 
containerd config default > /etc/containerd/config.toml

# Restart containerd 
systemctl restart containerd

# Enable containerd
systemctl enable containerd

#Add yum repo file for Kubernetes 

cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://packages.cloud.google.com/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://packages.cloud.google.com/yum/doc/yum-key.gpg https://packages.cloud.google.com/yum/doc/rpm-package-key.gpg

EOF

# Set SELinux in permissive mode (effectively disabling it)
sudo setenforce 0
sudo sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config


# Install Kubernetes (kubeadm, kubelet, kubectl)

sudo yum install -y kubelet kubeadm kubectl --disableexcludes=kubernetes

echo 'KUBELET_EXTRA_ARGS="--fail-swap-on=false"' > /etc/sysconfig/kubelet


# Only on control plan Master with Kubeadm
kubeadm init --pod-network-cidr=192.168.0.0/16

# to get the nodes ready
kubectl apply -f https://docs.projectcalico.org/manifests/calico.yaml