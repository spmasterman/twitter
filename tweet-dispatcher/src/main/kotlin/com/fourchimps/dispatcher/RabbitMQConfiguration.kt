package com.fourchimps.dispatcher


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.rabbitmq.client.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.rabbitmq.RabbitFlux
import reactor.rabbitmq.Receiver
import reactor.rabbitmq.ReceiverOptions

@Configuration
class RabbitMQConfiguration(@Value("\${spring.rabbitmq.host}") private val host: String,
                            @Value("\${spring.rabbitmq.port}") private val port: Int,
                            @Value("\${spring.rabbitmq.username}") private val username: String,
                            @Value("\${spring.rabbitmq.password}") private val password: String) {

    @Bean
    fun mapper(): ObjectMapper = ObjectMapper().registerModule(KotlinModule())

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val connectionFactory = ConnectionFactory()
        connectionFactory.username = this.username
        connectionFactory.password = this.password
        connectionFactory.host = this.host
        connectionFactory.port = this.port
        connectionFactory.useNio()
        return connectionFactory
    }

    @Bean
    fun receiver(connectionFactory: ConnectionFactory): Receiver {
        val options = ReceiverOptions()
        options.connectionFactory(connectionFactory)
        return RabbitFlux.createReceiver(options)
    }

}