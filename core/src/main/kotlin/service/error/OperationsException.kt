package service.error


open class OperationsException(override val message: String) : Exception(message)