terraform {
  required_providers {
    twc = {
      source = "tf.timeweb.cloud/timeweb-cloud/timeweb-cloud"
    }
  }
  required_version = ">= 0.13"
}

data "twc_presets" "preset" {
  location    = "ru-1"
  preset_type = "premium"
  cpu         = 1
  ram         = 1024

  price_filter {
    from = 300
    to   = 400
  }
}

data "twc_software" "docker" {
  name = "Docker"

  os {
    name    = "ubuntu"
    version = "22.04"
  }
}

resource "twc_project" "wg-mgr-project" {
  name = "WireGuard Manager"
}

resource "twc_floating_ip" "main" {
  availability_zone = twc_server.main.availability_zone
}

resource "twc_server" "main" {
  name              = "Main Server"
  os_id             = data.twc_software.docker.os[0].id
  software_id       = data.twc_software.docker.id
  availability_zone = "spb-3"
  floating_ip_id = resource.twc_floating_ip.main

  preset_id = data.twc_presets.preset.id

  project_id = resource.twc_project.wg-mgr-project.id
}
