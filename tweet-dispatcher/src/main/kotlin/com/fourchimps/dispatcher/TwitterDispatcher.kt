package com.fourchimps.dispatcher

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.rabbitmq.Receiver

@Service
class TwitterDispatcher(@Value("\${queue.twitter}") private val queue: String,
                        private val receiver: Receiver,
                        private val mapper: ObjectMapper) {

    private val logger = KotlinLogging.logger {}

    fun dispatch(): Flux<Tweet> {
        return this.receiver
                .consumeAutoAck(this.queue)
                .flatMap { message ->
                    logger.info { "dispatching tweet ${message.body} "}
                    Mono.just(mapper.readValue<Tweet>(String(message.body), Tweet::class.java))
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class TwitterUser(val id: String, val name: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Tweet(val id: String = "",
                 val text: String = "",
                 @JsonProperty("created_at") val createdAt: String = "",
                 val user: TwitterUser = TwitterUser("", ""))
