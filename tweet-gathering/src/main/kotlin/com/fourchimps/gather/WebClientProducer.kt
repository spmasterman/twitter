package com.fourchimps.gather

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientProducer {

    @Bean
    fun webClient(): WebClient? {
        return WebClient.create()
    }
}