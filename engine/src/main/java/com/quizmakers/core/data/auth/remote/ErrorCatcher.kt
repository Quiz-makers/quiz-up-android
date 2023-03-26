package  com.quizmakers.core.data.auth.remote

data class ErrorCatcher(
    var errors: ArrayList<Errors> = arrayListOf()

)

data class Errors(
    var field: String,
    var message: String

)