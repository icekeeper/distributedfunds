package ws.dto

import core.model.transaction.TransactionStatus

data class CreateRedistributionTransactionRequest(val fundId: Long,
                                                  val toUserId: Long,
                                                  val description: String,
                                                  val amount: Long) : Dto


data class CreateGiftTransactionRequest(val fundId: Long,
                                        val receiverId: Long,
                                        val description: String,
                                        val price: Long) : Dto

data class RedistributionTransactionDto(val id: Long,
                                        val amount: String,
                                        val description: String,
                                        val timestamp: Long,
                                        val status: TransactionStatus,
                                        val user: UserDto,
                                        val direction: RedistributionTransactionDirection) : Dto

enum class RedistributionTransactionDirection {
    INCOME, OUTCOME
}

data class GiftTransactionDto(val id: Long,
                              val price: String,
                              val balanceDelta: String,
                              val description: String,
                              val timestamp: Long,
                              val buyer: UserDto,
                              val partakers: List<UserDto>) : Dto



