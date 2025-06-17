terraform {
  required_version = ">=1.1.7"

  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "6.40.0"
    }
  }
}

provider "google" {
  region = "europe-north1"
}