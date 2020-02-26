job "twitter" {
  datacenters = ["dc1"]

  group "twitter" {
    task "hashtag" {
      driver = "docker"

      config {
        image = "spmasterman/tracked-hashtag"
        port_map {
          track = 9090
        }
      }

      resources {
        network {
          mbits = 1
          port "track" {
            static = "9090"
          }
        }
      }
    }

    task "gather" {
      driver = "docker"

      config {
        image = "spmasterman/tweet-gathering"
        port_map {
          gather = 8081
        }
      }

      env {
        CONSUMER_KEY = "..."
        CONSUMER_SECRET = "..."
        ACCESS_TOKEN="..."
        ACCESS_TOKEN_SECRET = "..."
      }

      resources {
        network {
          mbits = 1
          port "gather" {
            static = "8081"
          }
        }
      }
    }

    task "dispatch" {
      driver = "docker"

      config {
        image = "spmasterman/tweet-dispatcher"
        port_map {
          dispatch = 9091
        }
      }

      resources {
        network {
          mbits = 1
          port "dispatch" {
            static = "9091"
          }
        }
      }
    }

    task "redis" {
      driver = "docker"

      config {
        image = "redis"
        port_map {
          redis = 6379
        }
      }

      resources {
        network {
          mbits = 1
          port "redis" {
            static = "6379"
          }
        }
      }
    }

    task "rabbit" {
      driver = "docker"

      config {
        image = "rabbitmq:management"
        port_map {
          rabbitmq = 5672,
          rabbitui = 15672
        }
      }

      resources {
        network {
          mbits = 1
          port "rabbitmq" {
            static = "5672"
          }
          port "rabbitui" {
            static = "15672"
          }
        }
      }
    }
  }
}