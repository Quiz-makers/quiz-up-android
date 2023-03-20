package com.quizmakers.core.api

import com.quizmakers.core.base.ApiType

data class EnvironmentModel(
    val apiType: ApiType
) {

    var baseUrl = ""

    init {
        setBaseUrl(apiType)
    }

    private fun setBaseUrl(
        apiType: ApiType,
    ) {
        baseUrl = apiType.url
    }

    override fun toString(): String {
        return baseUrl
    }

}