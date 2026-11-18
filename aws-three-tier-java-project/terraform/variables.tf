variable "aws_region" {
  type    = string
  default = "eu-west-1"
}
variable "project_name" {
  type    = string
  default = "three-tier-java"
}
variable "environment" {
  type    = string
  default = "dev"
}
variable "db_instance_class" {
  type    = string
  default = "db.t3.micro"
}
variable "app_instance_type" {
  type    = string
  default = "t3.micro"
}
variable "db_name" {
  type    = string
  default = "appdb"
}
variable "db_username" {
  type      = string
  default   = "appadmin"
  sensitive = true
}
variable "allowed_http_cidr" {
  description = "Use your public IPv4 address with /32 when possible."
  type        = string
  default     = "0.0.0.0/0"
}
variable "application_repo_url" {
  description = "Public HTTPS URL of your GitHub repository, for example https://github.com/USER/aws-three-tier-java-project.git"
  type        = string
}
variable "application_branch" {
  type    = string
  default = "main"
}
