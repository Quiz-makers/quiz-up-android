package com.quizmakers.quizup.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.quizmakers.quizup.ui.theme.QuizUpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizUpTheme {
                MainScreen()
            }
        }
    }
}
