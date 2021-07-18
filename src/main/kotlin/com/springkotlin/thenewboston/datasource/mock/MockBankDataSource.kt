package com.springkotlin.thenewboston.datasource.mock

import com.springkotlin.thenewboston.datasource.BankDataSource
import com.springkotlin.thenewboston.model.Bank
import org.springframework.stereotype.Repository

@Repository
class MockBankDataSource : BankDataSource {

    val banksList = listOf(Bank("abc", 12.50, 30), Bank("dfg", 26.75, 30))

    override fun retrieveBanks(): Collection<Bank> {
        return banksList
    }
}