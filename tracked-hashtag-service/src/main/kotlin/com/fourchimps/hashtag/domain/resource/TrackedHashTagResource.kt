package com.fourchimps.hashtag.domain.resource

import com.fourchimps.hashtag.domain.TrackedHashTag
import com.fourchimps.hashtag.domain.service.TrackedHashTagService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tracked-hash-tag")
class TrackedHashTagResource(private val service: TrackedHashTagService) {

    @GetMapping
    fun all() = this.service::all

    @PostMapping
    fun save(@RequestBody hashTag: TrackedHashTag) = this.service::save
}