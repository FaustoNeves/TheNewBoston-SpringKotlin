package com.springkotlin.thenewboston.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.springkotlin.thenewboston.model.Bank
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
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor(
    private val mockMVC: MockMvc,
    private val objectMapper: ObjectMapper
) {
    private val baseUrl = "/api/banks"

    @Nested
    @DisplayName("GET /api/banks")
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
    @DisplayName("GET /api/banks/{accountNumber}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBank {

        @Test
        fun `should return the bank with given account number`() {
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

    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PostNewBank {
        @Test
        fun `should add new bank`() {
            //given
            val newValidBank = Bank("hij", 12.15, 49)
            //when
            mockMVC.post(
                baseUrl, dsl = {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(
                        newValidBank
                    )
                })
                //then
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content { contentType("application/json") }
                    jsonPath("$.accountNumber") {
                        value("hij")
                    }
                    jsonPath("$.trust") {
                        value(12.15)
                    }
                    jsonPath("$.transactionFee") {
                        value(49)
                    }
                }
        }

        @Test
        fun `should return BAD REQUEST if bank already exists`() {
            //given
            val invalidBank = Bank("abc", 18.25, 60)
            //when
            mockMVC.post(
                baseUrl, dsl = {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(
                        invalidBank
                    )
                })
                //then
                .andDo { print() }
                .andExpect { status { isBadRequest() } }
        }
    }
}