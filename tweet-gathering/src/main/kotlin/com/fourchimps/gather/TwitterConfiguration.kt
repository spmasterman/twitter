package com.fourchimps.gather


import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TwitterConfiguration(
        @Value("\${consumer-key}") val consumerKey : String,
        @Value("\${consumer-secret}") val consumerSecret : String,
        @Value("\${access-token}") val accessToken : String,
        @Value("\${access-token-secret}") val accessTokenSecret : String
) {
    private val logger = KotlinLogging.logger {}

    @Bean
    fun twitterAppSettings(): TwitterAppSettings {
        logger.info { "consumerKey: $consumerKey consumerSecret: $consumerSecret" }

        return TwitterAppSettings(consumerKey, consumerSecret)
    }

    @Bean
    fun twitterToken():TwitterToken {
        logger.info { "accessToken: $accessToken accessTokenSecret: $accessTokenSecret" }

        return TwitterToken(accessToken, accessTokenSecret)
    }
}