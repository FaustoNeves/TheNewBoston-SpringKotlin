package com.springkotlin.thenewboston.datasource

import com.springkotlin.thenewboston.model.Bank

interface BankDataSource {

    fun retrieveBanks(): Collection<Bank>
}