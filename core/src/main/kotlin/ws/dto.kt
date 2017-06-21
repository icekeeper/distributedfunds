package ws

import model.transaction.TransactionStatus

interface Dto

data class DtoCollection(val collection: Iterable<Dto>)

data class UserDto(val id: Long,
                   val login: String,
                   val name: String) : Dto

data class CreateUserRequestDto(val login: String,
                                val name: String) : Dto

data class FundDto(val id: Long,
                   val name: String,
                   val description: String,
                   val supervisor: UserDto,
                   val users: List<UserDto>) : Dto

data class FundDescriptionDto(val id: Long,
                              val name: String,
                              val description: String) : Dto

data class CreateFundRequestDto(val name: String,
                                val description: String,
                                val supervisorId: Long,
                                val userIds: List<Long>) : Dto

data class TransactionShareDto(val user: UserDto,
                               val amount: Int) : Dto

data class TransactionDto(val id: Long,
                          val fundId: Long,
                          val amount: Int,
                          val description: String,
                          val timestamp: Long,
                          val status: TransactionStatus,
                          val shares: List<TransactionShareDto>) : Dto

data class TransactionsPageDto(val data: List<TransactionDto>,
                               val fromTransactionId: Long,
                               val limit: Int,
                               val totalCount: Int) : Dto

data class CreateTransactionShareDto(val userId: Long,
                                     val amount: Int) : Dto

data class CreateTransactionRequestDto(val fundId: Long,
                                       val amount: Int,
                                       val description: String,
                                       val shares: List<CreateTransactionShareDto>) : Dto

data class FundUserBalanceDto(val fundId: Long,
                              val userId: Long,
                              val balance: Int) : Dto