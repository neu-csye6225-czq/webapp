packer {
  required_plugins {
    amazon = {
      version = ">= 0.0.2"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

variable "profile" {
  type    = string
  default = "dev"
}

variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "source_ami" {
  type    = string
  default = "ami-06db4d78cb1d3bbf9" # Debian 12 64-bit (x86)
}

variable "vpc_id" {
  type    = string
  default = "vpc-0174254c2acac80fc" # Default vpc
}

variable "shared_account_id" {
  type    = string
  default = "530294904375"
}

variable "ssh_username" {
  type    = string
  default = "admin"
}

source "amazon-ebs" "my-ami" {
  profile         = "${var.profile}"
  region          = "${var.aws_region}"
  ami_regions     = ["us-east-1"]
  ami_name        = "csye6225_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  ami_description = "ami for csye6225 as05"
  instance_type   = "t2.micro"
  source_ami      = "${var.source_ami}"
  ssh_username    = "${var.ssh_username}"
  vpc_id          = "${var.vpc_id}"


  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/xvda"
    volume_size           = 8
    volume_type           = "gp2"
  }

  ami_users = ["${var.shared_account_id}"]
}

build {
  sources = ["source.amazon-ebs.my-ami"]

  provisioner "shell" {
    script = "packer/setupenv.sh"
  }

  provisioner "file" {
    source      = "target/classes/application.properties"
    destination = "~/application.properties"
  }

  provisioner "file" {
    source      = "target/webapp-0.0.1-SNAPSHOT.jar"
    destination = "~/webapp-0.0.1-SNAPSHOT.jar"
  }

}
