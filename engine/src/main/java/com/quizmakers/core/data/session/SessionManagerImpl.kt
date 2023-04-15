package com.quizmakers.core.data.session

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.quizmakers.core.base.TOKEN_NAME_SP
import com.quizmakers.core.base.USER_NAME_SP
import com.quizmakers.core.domain.session.SessionManager
import org.koin.core.annotation.Factory

@Factory
class SessionManagerImpl(
    private val sharedPreferences: SharedPreferences,
    private val editor: Editor
) : SessionManager {
    override fun getToken(): String? = sharedPreferences.getString(TOKEN_NAME_SP, null)
    override fun getUserName(): String = sharedPreferences.getString(USER_NAME_SP, "")!!
    override fun saveUserName(userName: String) {
        editor.putString(USER_NAME_SP, userName)
        editor.commit()
    }

    override fun saveToken(token: String) {
        editor.putString(TOKEN_NAME_SP, token)
        editor.commit()
    }

    override fun deleteToken() {
        editor.putString(TOKEN_NAME_SP, null)
        editor.putString(USER_NAME_SP, null)
        editor.commit()
    }

}