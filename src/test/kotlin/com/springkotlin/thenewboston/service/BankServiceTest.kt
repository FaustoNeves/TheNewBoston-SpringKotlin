package com.springkotlin.thenewboston.service

import com.springkotlin.thenewboston.datasource.BankDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BankServiceTest {

    private val dataSource: BankDataSource = mockk(relaxed = true)
    private val bankService = BankService(dataSource)

    @Test
    fun `should call its data source to retrieve banks`() {
        //given
        /**every { dataSource.retrieveBanks() } returns emptyList()*/
        //when
        val banks = bankService.getBanks()
        //then
        verify { dataSource.retrieveBanks() }
    }
}