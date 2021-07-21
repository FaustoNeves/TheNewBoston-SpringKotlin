package com.springkotlin.thenewboston.service

import com.springkotlin.thenewboston.datasource.BankDataSource
import com.springkotlin.thenewboston.model.Bank
import org.springframework.stereotype.Service

@Service
class BankService(private val bankDataSource: BankDataSource) {

    fun getBanks(): Collection<Bank> = bankDataSource.retrieveBanks()

    fun getBank(accountNumber: String) = bankDataSource.retrieveBank(accountNumber)

    fun addNewBank(bank: Bank) = bankDataSource.insertBank(bank)

    fun updateBank(bank: Bank) = bankDataSource.updateBank(bank)
}