package com.fourchimps.gather

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.publisher.Hooks

@SpringBootApplication
class GatherApplication

fun main(args: Array<String>) {
	Hooks.onOperatorDebug();
	runApplication<GatherApplication>(*args)
}
