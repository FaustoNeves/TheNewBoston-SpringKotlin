package com.springkotlin.thenewboston.datasource

import com.springkotlin.thenewboston.model.Bank

interface BankDataSource {

    fun retrieveBanks(): Collection<Bank>
    fun retrieveBank(accountNumber: String): Bank
    fun insertBank(bank: Bank): Bank
    fun updateBank(bank: Bank): Bank
}