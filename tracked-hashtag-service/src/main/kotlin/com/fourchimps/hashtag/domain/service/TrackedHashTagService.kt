package com.fourchimps.hashtag.domain.service

import com.fourchimps.hashtag.domain.TrackedHashTag
import com.fourchimps.hashtag.domain.repository.TrackedHashTagRepository
import mu.KotlinLogging
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


    private val logger = KotlinLogging.logger {}

    fun save(hashTag: TrackedHashTag) {
        logger.info {"Service ${hashTag.hashTag} Queue: ${hashTag.queue}"}

        this.repository.save(hashTag).subscribe {
            Mono.fromFuture(CompletableFuture.runAsync {
                logger.info {"Send ${hashTag.hashTag} Queue: ${hashTag.queue}"}
                this.rabbitTemplate.convertAndSend(this.exchange, this.routingKey, hashTag)
            })
        }
    }

    fun all() = this.repository::findAll

}