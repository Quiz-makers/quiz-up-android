package com.quizmakers.core.base


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Api(
    val value: ApiType
)

enum class ApiType(
    val url: String
) {
    AUTH("http://localhost:8190"),
    QUIZZES("")
}