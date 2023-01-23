#
# EKS Cluster Resources
#  * IAM Role to allow EKS service to manage other AWS services
#  * EC2 Security Group to allow networking traffic with EKS cluster
#  * EKS Cluster
#

# ------------- Creating of Identity and accesss managment role named " dev-cluster "-----------------
resource "aws_iam_role" "dev-cluster" {
  name = "terraform-eks-dev-cluster"

  assume_role_policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "eks.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
POLICY
}


# ------------- Creating of IAM Policy named " dev-cluster-AmazonEKSClusterPolicy "-----------------
resource "aws_iam_role_policy_attachment" "dev-cluster-AmazonEKSClusterPolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
  role       = aws_iam_role.dev-cluster.name
}


# ------------- Creating of IAM Policy named " dev-cluster-AmazonEKSVPCResourceController "-----------------
resource "aws_iam_role_policy_attachment" "dev-cluster-AmazonEKSVPCResourceController" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSVPCResourceController"
  role       = aws_iam_role.dev-cluster.name
}



# ------------- Creating of security group called dev-cluster for cluster to communicate with worker nodes "-----------------
resource "aws_security_group" "dev-cluster" {
  name        = "terraform-eks-dev-cluster"
  description = "Cluster communication with worker nodes"
  vpc_id      = aws_vpc.eks_vpc.id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    description = "HTTP connection"
    cidr_blocks = ["0.0.0.0/0"]
  }

ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    description = "HTTPs connection"
    cidr_blocks = ["0.0.0.0/0"]
  }

ingress {
    from_port   = 1337
    to_port     = 1337
    protocol    = "tcp"
    description = "ssh connection"
    cidr_blocks = ["0.0.0.0/0"]
  }

egress {
  from_port   = 0
  to_port     = 0
  protocol    = "-1"
  cidr_blocks = ["0.0.0.0/0"]
}

  tags = {
    Name = "terraform-eks-dev"
  }
}


# ------------- Creating security group for workstation to commuinicate with the cluster API Server "-----------------

resource "aws_security_group_rule" "dev-cluster-ingress-workstation-https" {
  cidr_blocks       = [local.workstation-external-cidr]
  description       = "Allow workstation to communicate with the cluster API Server"
  from_port         = 443
  protocol          = "tcp"
  security_group_id = aws_security_group.dev-cluster.id
  to_port           = 443
  type              = "ingress"
}


# ------------- Creating eks cluster named "dev-cluster" and assigning security group, and the two IAMs-----------------

#---------------The cluster will be depended on the two IAM.

resource "aws_eks_cluster" "dev-cluster" {
  name     = var.cluster-name
  role_arn = aws_iam_role.dev-cluster.arn


  vpc_config {
    security_group_ids = [aws_security_group.dev-cluster.id]
    subnet_ids         = aws_subnet.eks_subnet[*].id
  }

  depends_on = [
    aws_iam_role_policy_attachment.dev-cluster-AmazonEKSClusterPolicy,
    aws_iam_role_policy_attachment.dev-cluster-AmazonEKSVPCResourceController,
  ]
}
