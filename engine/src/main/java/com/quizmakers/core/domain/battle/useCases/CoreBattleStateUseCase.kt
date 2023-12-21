package com.quizmakers.core.domain.battle.useCases

import com.quizmakers.core.domain.battle.socketio.SocketEventHandler
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Factory

@Factory
class CoreBattleStateUseCase(private val socketEventHandler: SocketEventHandler) {
    operator fun invoke() = socketEventHandler.stateBattle.asStateFlow()

}