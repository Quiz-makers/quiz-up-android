package com.quizmakers.quizup.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.quizmakers.core.domain.session.SessionManager
import com.quizmakers.quizup.ui.theme.QuizUpTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val sessionManager: SessionManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizUpTheme {
                MainScreen(sessionManager.getToken())
            }
        }
    }
}
