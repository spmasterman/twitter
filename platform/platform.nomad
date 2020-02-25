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
        CONSUMER_KEY = "Q4Pms9lh3BzK8beH7kirCsDgZ"
        CONSUMER_SECRET = "4ZJ13VgHPlLaUyYB2MzaUObkv4XCKvExR88QRtnSmMOSqjoWp7"
        ACCESS_TOKEN="16837725-d7beho1JKgcUp8om2diIEA3PfNw9GR8xCxxjMr59w"
        ACCESS_TOKEN_SECRET = "9AX4qtVxqjP9Q08UXMAgIezxF9Iv89ECftL5RQG0QxJRv"
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