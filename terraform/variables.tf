variable "region" {
  description = "AWS Region"
  default     = "us-east-1"
}

variable "s3_bucket_name" {
  description = "Name of the S3 bucket for data lake"
  default     = "jscience-datalake-prod"
}

variable "db_password" {
  description = "Database password"
  type        = string
  sensitive   = true
}
