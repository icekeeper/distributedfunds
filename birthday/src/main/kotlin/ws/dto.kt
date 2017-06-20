package ws

data class CreateRedistributionTransactionRequest(val fundId: Long,
                                                  val toUserId: Long,
                                                  val description: String,
                                                  val amount: Int) : Dto


data class CreateGiftTransactionRequest(val fundId: Long,
                                        val receiverId: Long,
                                        val description: String,
                                        val price: Int) : Dto