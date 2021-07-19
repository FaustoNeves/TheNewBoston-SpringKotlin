package com.springkotlin.thenewboston.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest {

    @Autowired
    lateinit var mockMVC: MockMvc

    @Test
    fun `should return all banks`() {
        //when/then
        mockMVC.get("/api/hello")
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