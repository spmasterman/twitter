package com.fourchimps.hashtag


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mu.KotlinLogging
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter

@Configuration
class RabbitMQConfiguration(
        @Value("\${queue.twitter}") private val queue: String,
        @Value("\${exchange.twitter}") private val exchange: String,
        @Value("\${routing_key.track}") private val routingKey: String) {

    private val logger = KotlinLogging.logger {}

    @Bean
    fun queue(): Queue {
        logger.info { "Queue: $queue" }

        return Queue(this.queue, false)
    }

    @Bean
    fun exchange(): TopicExchange{
        logger.info { "Exchange: $exchange "}
        return TopicExchange(this.exchange)
    }

    @Bean
    fun binding(queue: Queue, exchange: TopicExchange): Binding {
        logger.info { "Binding with Key: $routingKey " }

        return BindingBuilder.bind(queue).to(exchange).with(this.routingKey)
    }

    @Bean
    fun converter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter(ObjectMapper().registerModule(KotlinModule()))
    }
}
