package ws


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