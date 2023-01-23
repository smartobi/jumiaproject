#
# EKS Worker Nodes Resources
#  * IAM role allowing Kubernetes actions to access other AWS services
#  * EKS Node Group to launch worker nodes
#


# ------------- Creating IAM Role  for worker node named "dev-node" "-----------------
resource "aws_iam_role" "dev-node" {
  name = "terraform-eks-dev-node"

  assume_role_policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
POLICY
}

# ------------- Creating IAM policy for worker node named "dev-node-AmazonEKSWorkerNodePolicy" "-----------------
resource "aws_iam_role_policy_attachment" "dev-node-AmazonEKSWorkerNodePolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy"
  role       = aws_iam_role.dev-node.name
}

# ------------- Creating IAM policy for worker node named "dev-node-AmazonEKS_CNI_Policy" "-----------------
resource "aws_iam_role_policy_attachment" "dev-node-AmazonEKS_CNI_Policy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy"
  role       = aws_iam_role.dev-node.name
}

# ------------- Creating IAM policy for worker node named "dev-node-AmazonEKS_CNI_Policy" "-----------------
resource "aws_iam_role_policy_attachment" "dev-node-AmazonEC2ContainerRegistryReadOnly" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
  role       = aws_iam_role.dev-node.name
}


# ------------- Creating a Workder Node group named "dev" which will depend on the three IAM policies "-----------------
resource "aws_eks_node_group" "dev" {
  cluster_name    = aws_eks_cluster.dev-cluster.name
  node_group_name = "dev"
  node_role_arn   = aws_iam_role.dev-node.arn
  subnet_ids      = aws_subnet.eks_subnet[*].id
  disk_size       = 10
  instance_types  = ["t2.medium"]


  scaling_config {
    desired_size = 3
    max_size     = 4
    min_size     = 1
  }

  depends_on = [
    aws_iam_role_policy_attachment.dev-node-AmazonEKSWorkerNodePolicy,
    aws_iam_role_policy_attachment.dev-node-AmazonEKS_CNI_Policy,
    aws_iam_role_policy_attachment.dev-node-AmazonEC2ContainerRegistryReadOnly,
  ]
}
