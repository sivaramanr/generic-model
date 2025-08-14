job "generic-model-service" {

  datacenters = ["dc1"]

  type        = "service"

  group "generic-model" {
    count = 1

    network {
      port "http" {}
    }

    task "generic-model-app" {
      driver = "raw_exec"

      config {
        command = "/usr/bin/java"
        args = [
          "-Xms256m",
          "-Xmx512m",
          "-jar",
          "/opt/sivaramanr/generic-model.jar",
          "--server.port=${NOMAD_PORT_http}"
        ]
      }

      env {
        SPRING_PROFILES_ACTIVE = "local"
      }

      resources {
        cpu    = 500
        memory = 512
        network {
          mbits = 10
        }
      }

      service {
        name = "generic-model"
        port = "http"

        check {
          type     = "http"
          path     = "/actuator/health"
          interval = "10s"
          timeout  = "2s"
        }
      }
    }
  }
}
