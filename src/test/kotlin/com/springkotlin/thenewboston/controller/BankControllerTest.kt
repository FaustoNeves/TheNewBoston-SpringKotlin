package com.springkotlin.thenewboston.controller

import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest {

    val baseUrl = "/api/banks"

    @Autowired
    lateinit var mockMVC: MockMvc

    @Nested
    @DisplayName("getBanks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBanks {

        @Test
        fun `should return all banks`() {
            //when
            mockMVC.get(baseUrl)
                //then
                .andDo { print() }
                .andExpect {
                    this.status {
                        this.isOk()
                    }
                    this.jsonPath("$[0].transactionFee") {
                        this.isNumber()
                        this.value(30)
                    }
                }
        }
    }

    @Nested
    @DisplayName("getBank")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBank {

        @Test
        fun `should return the bank with the given account number`() {
            //given
            val accountNumber = "dfg"
            //when
            mockMVC.get("$baseUrl/$accountNumber")
                //then
                .andDo { print() }
                .andExpect {
                    status {
                        this.isOk()
                    }
                    this.content {
                        this.contentType(MediaType.APPLICATION_JSON)
                    }
                    this.jsonPath("$.transactionFee") {
                        value(30)
                    }
                }
        }
    }

    @Nested
    @DisplayName("error on getBank")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class ErrorOnGetBank {

        @Test
        fun `should return NOT FOUND if the account number doesn't exist`() {
            //given
            val accountNumber = "doesn't_exist"
            //when
            mockMVC.get("$baseUrl/$accountNumber")
                //then
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                    content {
                        contentType("text/plain;charset=UTF-8")
                    }
                }
        }
    }
}