package com.springkotlin.thenewboston.datasource.mock

import com.springkotlin.thenewboston.datasource.BankDataSource
import com.springkotlin.thenewboston.model.Bank
import org.springframework.stereotype.Repository

@Repository
class MockBankDataSource : BankDataSource {

    val banksList = mutableListOf(Bank("abc", 12.50, 30), Bank("dfg", 26.75, 30))

    override fun retrieveBanks(): Collection<Bank> {
        return banksList
    }

    override fun retrieveBank(accountNumber: String): Bank {
        return banksList.firstOrNull {
            it.accountNumber == accountNumber
        } ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")
    }

    override fun insertBank(bank: Bank): Bank {
        if (banksList.any {
                it.accountNumber == bank.accountNumber
            }) {
            throw IllegalArgumentException("Bank already registered")
        } else {
            banksList.add(bank)
            return bank
        }
    }

    override fun updateBank(bank: Bank): Bank {
        when {
            banksList.any { it.accountNumber == bank.accountNumber && it.transactionFee == bank.transactionFee && it.trust == bank.trust } -> {
                throw IllegalArgumentException("No updates have been made")
            }
            banksList.any { it.accountNumber == bank.accountNumber } -> {
                banksList.find {
                    it.accountNumber == bank.accountNumber
                }.let {
                    it?.trust = bank.trust
                    it?.transactionFee = bank.transactionFee
                }
                return bank
            }
            else -> throw NoSuchElementException("Could not find a bank with account number ${bank.accountNumber}")
        }

        /**  Solution according to the tutorial
         *        val currentBank = banksList.firstOrNull { it.accountNumber == bank.accountNumber }
         *            ?: throw IllegalArgumentException("Could no update")
         *        banksList.remove(currentBank)
         *        banksList.add(bank)
         *        return bank
         */
    }
}