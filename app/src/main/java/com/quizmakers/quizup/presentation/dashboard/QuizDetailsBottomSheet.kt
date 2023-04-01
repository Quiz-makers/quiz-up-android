package com.quizmakers.quizup.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.quizmakers.quizup.R
import com.quizmakers.quizup.presentation.destinations.QuizScreenDestination
import com.quizmakers.quizup.ui.common.BaseButtonWithIcon
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle

@Destination(
    style = DestinationStyle.BottomSheet::class
)
@Composable
fun QuizDetailsBottomSheet(navigator: DestinationsNavigator, quizId: String) {
    QuizDetailsBottomSheet(startQuiz = { navigator.navigateToQuizScreen(quizId) })
}

@Composable
private fun QuizDetailsBottomSheet(startQuiz: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Text(text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
        Spacer(modifier = Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            if (false) {
                BaseButtonWithIcon(
                    modifier = Modifier.weight(0.5f),
                    label = stringResource(R.string.edit),
                    icon = Icons.Default.Edit,
                    onClick = {})
                Spacer(modifier = Modifier.weight(0.1f))
            } else Spacer(modifier = Modifier.weight(0.5f)) //TODO ADD EDITABLE LOGIC
            BaseButtonWithIcon(
                modifier = Modifier.weight(0.5f),
                label = stringResource(R.string.start),
                icon = Icons.Default.PlayArrow,
                onClick = startQuiz
            )
        }

    }
}

private fun DestinationsNavigator.navigateToQuizScreen(quizId: String) {
    navigate(QuizScreenDestination(quizId))
}
