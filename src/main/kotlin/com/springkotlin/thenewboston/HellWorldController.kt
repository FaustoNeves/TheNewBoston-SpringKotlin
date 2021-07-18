package com.springkotlin.thenewboston

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class HellWorldController {

    @GetMapping("hello")
    fun helloWorld() = "Hello, world!"
}