package com.fourchimps.gather

import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@Service
class TwitterGatherRunner(private val tweetGatherService: TweetGatherService,
                          private val rabbitTemplate: RabbitTemplate) {

    private val logger = KotlinLogging.logger {}

    @RabbitListener(queues = ["twitter-track-hashtag"])
    fun receive(hashTag: TrackedHashTag) {
        logger.info {"Receive ${hashTag.hashTag} Queue: ${hashTag.queue}"}

        val streamFrom = this.tweetGatherService.streamFrom(hashTag.hashTag)
                .filter { return@filter it.id.isNotEmpty() && it.text.isNotEmpty() }
        val subscribe = streamFrom.subscribe {
            println(it.text)
            Mono.fromFuture(CompletableFuture.runAsync {
                this.rabbitTemplate.convertAndSend("twitter-exchange", "track.${hashTag.queue}", it)
            })
        }
        Schedulers.elastic().schedule({ subscribe.dispose() }, 10L, TimeUnit.SECONDS)
    }
}