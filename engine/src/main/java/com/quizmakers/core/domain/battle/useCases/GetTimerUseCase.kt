package com.quizmakers.core.domain.battle.useCases

import android.os.CountDownTimer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.koin.core.annotation.Factory
import kotlin.math.ceil

@Factory
class GetTimerUseCase {
    operator fun invoke(time: Int = 10): Flow<TimerState> =
        callbackFlow {
            object : CountDownTimer(time * 1000L, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    val seconds = ceil(millisUntilFinished / 1000.0).toLong()
                    trySend(
                        TimerState.Running(seconds)
                    )
                }

                override fun onFinish() {
                    trySend(
                        TimerState.None
                    )
                    close()
                }

            }.start()

            awaitClose()
        }
}


sealed class TimerState {
    data class Running(val sec: Long) : TimerState()
    object None : TimerState()
}