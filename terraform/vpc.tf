#
# VPC Resources
#  * VPC
#  * Subnets
#  * Internet Gateway
#  * Route Table
#

# ------------- Creating of the VPC named " eks_vpc "-----------------
resource "aws_vpc" "eks_vpc" {
  cidr_block = "10.100.0.0/16"

  tags = tomap({
    "Name"                                      = "terraform-eks-dev-node",
    "kubernetes.io/cluster/${var.cluster-name}" = "shared",
  })
}

# ------------- Creating of Two Public subnets named " eks_subnet "-----------------
resource "aws_subnet" "eks_subnet" {
  count = 2

  availability_zone       = data.aws_availability_zones.available.names[count.index]
  cidr_block              = "10.100.${count.index}.0/24"
  map_public_ip_on_launch = true
  vpc_id                  = aws_vpc.eks_vpc.id

  tags = tomap({
    "Name"                                      = "terraform-eks-dev-node",
    "kubernetes.io/cluster/${var.cluster-name}" = "shared",
  })
}


# ------------- Creating of Internet gateway named " eks_ig "-----------------
resource "aws_internet_gateway" "eks_ig" {
  vpc_id = aws_vpc.eks_vpc.id

  tags = {
    Name = "terraform-eks-ig"
  }
}

# ------------- Creating of Route table named " eks_rt "-----------------
resource "aws_route_table" "eks_rt" {
  vpc_id = aws_vpc.eks_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.eks_ig.id
  }
}

# ------------- Creating of route table association for the two public subnets. named " eks_subnet "-----------------
resource "aws_route_table_association" "eks_rta" {
  count = 2

  subnet_id      = aws_subnet.eks_subnet[count.index].id
  route_table_id = aws_route_table.eks_rt.id
}
