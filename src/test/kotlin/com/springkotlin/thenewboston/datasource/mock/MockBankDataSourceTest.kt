package com.springkotlin.thenewboston.datasource.mock

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MockBankDataSourceTest {

    private val mockDataSource = MockBankDataSource()

    @Test
    fun `should provide a collection of banks`() {
        //when
        val banks = mockDataSource.retrieveBanks()
        //then
        assertThat(banks).isNotEmpty
        assertThat(banks.size).isGreaterThanOrEqualTo(2)
    }

    @Test
    fun `should provide some mock data`() {
        //when
        val banks = mockDataSource.retrieveBanks()
        //then
        assertThat(banks).allMatch {
            it.accountNumber != ""
        }
        assertThat(banks).allMatch {
            it.transactionFee > 20
        }
        assertThat(banks).anyMatch {
            it.trust > 1.0
        }
    }
}