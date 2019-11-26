package com.vivekvishwanath.bitterskotlin.util

const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
const val NETWORK_TIMEOUT_MESSAGE = "Network timed out"
const val UNABLE_TODO_OPERATION_WO_INTERNET = "Can't do that operation without an internet connection"
const val ERROR_SOMETHING_WRONG_WITH_IMAGE = "Something went wrong with the image."
const val ERROR_MUST_SELECT_IMAGE = "You must select an image."
const val ERROR_CHECK_NETWORK_CONNECTION = "Check network connection."
const val ERROR_UNKNOWN = "Unknown error"
const val ERROR_LOGIN_TIMEOUT = "It's taking longer than expected, thanks for your patience"


fun isNetworkError(msg: String): Boolean{
    when{
        msg.contains(UNABLE_TO_RESOLVE_HOST) -> return true
        else-> return false
    }
}