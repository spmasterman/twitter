package com.fourchimps.gather

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GatherApplication

fun main(args: Array<String>) {
	runApplication<GatherApplication>(*args)
}
