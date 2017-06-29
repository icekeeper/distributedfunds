package ws

data class CreateRedistributionTransactionRequest(val fundId: Long,
                                                  val toUserId: Long,
                                                  val description: String,
                                                  val amount: Long) : Dto


data class CreateGiftTransactionRequest(val fundId: Long,
                                        val receiverId: Long,
                                        val description: String,
                                        val price: Long) : Dto