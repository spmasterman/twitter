package com.fourchimps.hashtag.domain.service

import com.fourchimps.hashtag.domain.TrackedHashTag
import com.fourchimps.hashtag.domain.repository.TrackedHashTagRepository
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.concurrent.CompletableFuture

@Service
class TrackedHashTagService(private val repository: TrackedHashTagRepository,
                            private val rabbitTemplate: RabbitTemplate,
                            @Value("\${exchange.twitter}") private val exchange: String,
                            @Value("\${routing_key.track}") private val routingKey: String) {


    fun save(hashTag: TrackedHashTag) {
        this.repository.save(hashTag).subscribe {
            Mono.fromFuture(CompletableFuture.runAsync {
                this.rabbitTemplate.convertAndSend(this.exchange, this.routingKey, hashTag)
            })
        }
    }

    fun all() = this.repository.findAll()

}