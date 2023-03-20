package com.quizmakers.core.api

import com.quizmakers.core.base.ApiType

object EnvironmentManager {
    private val environments = mutableListOf(
        EnvironmentModel(
            apiType = ApiType.AUTH,
        ),
        EnvironmentModel(
            apiType = ApiType.QUIZZES
        )
    )

    fun getBaseUrl(apiType: ApiType): String {
        return environments.find { it.apiType == apiType }?.baseUrl ?: ApiType.AUTH.url
    }
}
