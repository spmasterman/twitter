package com.fourchimps.hashtag.domain.repository

import com.fourchimps.hashtag.domain.TrackedHashTag
import mu.KotlinLogging
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class TrackedHashTagRepository(private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>) {

    private val logger = KotlinLogging.logger {}

    fun save(trackedHashTag: TrackedHashTag): Mono<TrackedHashTag> {
        logger.info {"Redis: Save Tag: ${trackedHashTag.hashTag} Queue: ${trackedHashTag.queue}"}
        return this.reactiveRedisTemplate.opsForSet()
                .add("hash-tags", "${trackedHashTag.hashTag}:${trackedHashTag.queue}")
                .flatMap { Mono.just(trackedHashTag) }
    }

    fun findAll(): Flux<TrackedHashTag> {
        logger.info {"Redis: Find all..."}
        return this.reactiveRedisTemplate.opsForSet().members("hash-tags").flatMap { el ->
            val data = el.split(":")
            Flux.just(TrackedHashTag(hashTag = data[0], queue = data[1]))
        }
    }


}