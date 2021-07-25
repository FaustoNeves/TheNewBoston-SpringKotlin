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
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*

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
        @DirtiesContext
        fun `should return all banks`() {
            val listOfBanks = listOf(
                Bank("a", 1.1, 1),
                Bank("b", 1.2, 2),
                Bank("c", 1.3, 3)
            )
            postValidBank(listOfBanks)
            mockMVC.get(baseUrl)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { json(objectMapper.writeValueAsString(listOfBanks)) }
                }
        }
    }

    @Nested
    @DisplayName("GET /api/banks/{accountNumber}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBank {

        @Test
        @DirtiesContext
        fun `should return the bank with given account number`() {
            //given
            val listOfBanks = listOf(
                Bank("d", 3.0, 5)
            )
            postValidBank(listOfBanks)
            val accountNumber = "d"
            //when
            mockMVC.get("$baseUrl/$accountNumber")
                //then
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.transactionFee") {
                        value(5)
                    }
                }
        }

        @Test
        fun `should return NOT FOUND if the account number doesn't exist`() {
            val accountNumber = "doesn't_exist"
            mockMVC.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                    content { contentType("text/plain;charset=UTF-8") }
                }
        }
    }

    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PostNewBank {

        @Test
        @DirtiesContext
        fun `should add new bank`() {
            val newValidBank = Bank("e", 12.15, 49)
            mockMVC.post(
                baseUrl, dsl = {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(newValidBank)
                })
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType("application/json")
                        json(objectMapper.writeValueAsString(newValidBank))
                    }
                    /** Checks the entire json
                     *jsonPath("$.accountNumber") {
                     *value("hij")
                     *}
                     *jsonPath("$.trust") {
                     *value(12.15)
                     *}
                     *jsonPath("$.transactionFee") {
                     *value(49)
                     *}
                     * */
                }
            mockMVC.get("$baseUrl/${newValidBank.accountNumber}").andExpect {
                status { isOk() }
                content { json(objectMapper.writeValueAsString(newValidBank)) }
            }
        }

        @Test
        @DirtiesContext
        fun `should return BAD REQUEST if bank already exists`() {
            val listOfBanks = listOf(
                Bank("abc", 12.50, 30)
            )
            postValidBank(listOfBanks)
            val invalidBank = Bank("abc", 12.50, 30)
            mockMVC.post(
                baseUrl, dsl = {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(
                        invalidBank
                    )
                })
                .andDo { print() }
                .andExpect { status { isBadRequest() } }
        }
    }

    @Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PatchExistingBank {

        @Test
        @DirtiesContext
        fun `should update bank by accountNumber`() {
            //given
            val listOfBanks = listOf(
                Bank("gni", 3.0, 5)
            )
            postValidBank(listOfBanks)
            val bankToUpdate = Bank("gni", 25.00, 60)
            //when
            mockMVC.patch(baseUrl, dsl = {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(bankToUpdate)
            })
                //then
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(bankToUpdate))
                    }
                }
            mockMVC.get("$baseUrl/${bankToUpdate.accountNumber}").andExpect {
                status { isOk() }
                content { json(objectMapper.writeValueAsString(bankToUpdate)) }
            }
        }

        @Test
        fun `should return NOT FOUND when giving bank can't be found`() {
            val bankToUpdate = Bank("non existent bank", 100.50, 200)
            mockMVC.patch(baseUrl, dsl = {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(bankToUpdate)
            })
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("DELETE /api/banks/{accoutNumber}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class DeleteExistingBank {

        @Test
        fun `should delete bank with respective account number`() {
            //given
            val listOfBanks = listOf(
                Bank("dfg", 3.0, 5)
            )
            postValidBank(listOfBanks)
            val accountNumber = "dfg"
            //when
            mockMVC.delete("$baseUrl/$accountNumber")
                //then
                .andDo { print() }
                .andExpect { status { isNoContent() } }
            mockMVC.get("$baseUrl/$accountNumber")
                .andExpect { status { isNotFound() } }
        }

        @Test
        fun `should not delete with non existent account number`() {
            //given
            val accountNumber = "non existent"
            //when
            mockMVC.delete("$baseUrl/$accountNumber")
                //then
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }

    fun postValidBank(listOfBanks: Collection<Bank>) {
        for (bank in listOfBanks) {
            mockMVC.post(
                baseUrl, dsl = {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(Bank(bank.accountNumber, bank.trust, bank.transactionFee))
                })
        }
    }
}