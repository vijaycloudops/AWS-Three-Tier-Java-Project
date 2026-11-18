output "application_url" {
  description = "Open after EC2 bootstrapping and ALB health checks complete."
  value       = "http://${aws_lb.app.dns_name}"
}
output "load_balancer_dns" {
  value = aws_lb.app.dns_name
}
output "rds_endpoint" {
  value       = aws_db_instance.postgres.address
  description = "Private RDS endpoint; not accessible directly from the public internet."
}
output "s3_bucket_name" {
  value = aws_s3_bucket.artifacts.bucket
}
output "vpc_id" {
  value = aws_vpc.main.id
}
