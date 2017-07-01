package ws

data class CreateRedistributionTransactionRequest(val fundId: Long,
                                                  val toUserId: Long,
                                                  val description: String,
                                                  val amount: Long) : Dto


data class CreateGiftTransactionRequest(val fundId: Long,
                                        val receiverId: Long,
                                        val description: String,
                                        val price: Long) : Dto

data class BalanceDto(val value: String,
                      val gradation: String) : Dto

data class UserFundDto(val id: Long,
                       val name: String,
                       val description: String,
                       val supervisor: UserDto,
                       val users: List<UserDto>,
                       val balance: BalanceDto) : Dto

data class UserFundDescriptionDto(val id: Long,
                                  val name: String,
                                  val description: String,
                                  val supervisor: UserDto,
                                  val balance: BalanceDto) : Dto

data class CreateFundRequestDto(val name: String,
                                val description: String,
                                val userIds: List<Long>) : Dto