package com.fourchimps.gather

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.function.Consumer

@Service
class TweetGatherService(private val twitterAppSettings: TwitterAppSettings,
                         private val twitterToken: TwitterToken,
                         private val webClient: WebClient) {

    private val logger = KotlinLogging.logger {}

    fun streamFrom(query: String): Flux<Tweet> {
        val url = "https://stream.twitter.com/1.1/statuses/filter.json"
        val auth = Twitter.buildAuthHeader(twitterAppSettings, twitterToken, "POST", url, query)
        val body = BodyInserters.fromFormData("track", query)

        logger.info { "streamFrom $query body: $body auth: $auth" }

        return this.webClient
                .mutate()
                .baseUrl(url)
                .filter(logRequest())
                .filter(logResponse())
                .build()
                .post()
                .body(body)
                .header("Authorization", auth)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Tweet::class.java)


    }


    // This method returns filter function which will log request data
    private fun logRequest(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest ->
            logger.info {"Request: ${clientRequest.method()} ${clientRequest.url()}"}
            clientRequest.headers()
                    .forEach { name: String?, values: List<String?> -> values.forEach(
                            Consumer { value: String? -> logger.info {"${name}=${value}"} }) }
            Mono.just(clientRequest)
        }
    }

    // This method returns filter function which will log request data
    private fun logResponse(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofResponseProcessor { clientResponse: ClientResponse ->
            logger.info { "Response: ${clientResponse.statusCode()}" }
            clientResponse.headers().asHttpHeaders()
                    .forEach { name: String?, values: List<String?> ->
                        values.forEach(
                                Consumer { value: String? -> logger.info { "${name}=${value}" } })
                    }
            Mono.just(clientResponse)
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class TwitterUser (val id:String, val name:String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Tweet(val id: String = "",
                 val text: String = "",
                 @JsonProperty("created_at") val createdAt: String = "",
                 val user: TwitterUser = TwitterUser("", ""))