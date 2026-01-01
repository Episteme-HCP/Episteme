provider "aws" {
  region = var.region
}

# VPC
module "vpc" {
  source = "terraform-aws-modules/vpc/aws"
  name   = "jscience-vpc"
  cidr   = "10.0.0.0/16"

  azs             = ["${var.region}a", "${var.region}b", "${var.region}c"]
  private_subnets = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
  public_subnets  = ["10.0.101.0/24", "10.0.102.0/24", "10.0.103.0/24"]

  enable_nat_gateway = true
}

# EKS Cluster
module "eks" {
  source          = "terraform-aws-modules/eks/aws"
  cluster_name    = "jscience-cluster"
  cluster_version = "1.27"
  vpc_id          = module.vpc.vpc_id
  subnet_ids      = module.vpc.private_subnets

  eks_managed_node_groups = {
    workers = {
      min_size     = 1
      max_size     = 10
      desired_size = 3

      instance_types = ["t3.xlarge"] # Cost effective general purpose
      capacity_type  = "SPOT"
    }
  }
}

# RDS Postgres (for Metadata)
module "db" {
  source  = "terraform-aws-modules/rds/aws"
  version = "~> 5.0"

  identifier = "jscience-db"

  engine            = "postgres"
  engine_version    = "14"
  instance_class    = "db.t3.medium"
  allocated_storage = 20

  db_name  = "jscience"
  username = "jscience_admin"
  port     = 5432

  subnet_ids             = module.vpc.private_subnets
  vpc_security_group_ids = [module.vpc.default_security_group_id]

  family = "postgres14"
}

# S3 Bucket (for Large Data)
resource "aws_s3_bucket" "jscience_data" {
  bucket = var.s3_bucket_name
}
