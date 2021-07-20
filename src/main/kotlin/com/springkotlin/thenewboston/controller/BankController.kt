package com.springkotlin.thenewboston.controller

import com.springkotlin.thenewboston.model.Bank
import com.springkotlin.thenewboston.service.BankService
import org.apache.coyote.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/banks")
class BankController(private val bankService: BankService) {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.NOT_FOUND)
    }

    @GetMapping
    fun requestToBankService() = bankService.getBanks()

    @GetMapping("{accountNumber}")
    fun getBank(@PathVariable accountNumber: String) = bankService.getBank(accountNumber)
}