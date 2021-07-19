package com.springkotlin.thenewboston.controller

import com.springkotlin.thenewboston.model.Bank
import com.springkotlin.thenewboston.service.BankService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class BankController(private val bankService: BankService) {

    @GetMapping("hello")
    fun requestToBankService() = bankService.getBanks()
}