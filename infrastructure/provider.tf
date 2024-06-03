terraform {
  required_version = ">=1.1.7"

  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "5.32.0"
    }
  }
}

provider "google" {
  region = "europe-north1"
}