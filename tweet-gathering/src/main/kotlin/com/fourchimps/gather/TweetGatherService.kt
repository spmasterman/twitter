package com.fourchimps.gather

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Service
class TweetGatherService(private val twitterAppSettings: TwitterAppSettings,
                         private val twitterToken: TwitterToken,
                         private val webClient: WebClient) {

    fun streamFrom(query: String): Flux<Tweet> {
        val url = "http://stream.twitter.com/1.1/statuses/filter.json"
        return this.webClient.mutate().baseUrl(url).build()
                .post()
                .body(BodyInserters.fromFormData("track", query))
                .header("Authorization", Twitter.buildAuthHeader(twitterAppSettings, twitterToken, "POST", url, query))
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Tweet::class.java)
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class TwitterUser (val id:String, val name:String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Tweet(val id: String = "",
                 val text: String = "",
                 @JsonProperty("created_at") val createdAt: String = "",
                 val user: TwitterUser = TwitterUser("", ""))