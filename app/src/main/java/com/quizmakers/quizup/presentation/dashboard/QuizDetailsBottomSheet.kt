package com.quizmakers.quizup.presentation.dashboard

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.quizmakers.quizup.R
import com.quizmakers.quizup.presentation.destinations.QuizScreenDestination
import com.quizmakers.quizup.ui.common.BaseButtonWithIcon
import com.quizmakers.quizup.ui.theme.DarkBlue
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet

@Destination(
    style = DestinationStyleBottomSheet::class
)
@Composable
fun QuizDetailsBottomSheet(
    navigator: DestinationsNavigator,
    quizId: String,
    desc: String,
    shareCode: String?
) {
    QuizDetailsBottomSheet(
        startQuiz = { navigator.navigateToQuizScreen(quizId) },
        desc = desc,
        shareCode = shareCode
    )
}

@Composable
private fun QuizDetailsBottomSheet(
    startQuiz: () -> Unit, desc: String,
    shareCode: String?
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        shareCode?.let {
            Row(modifier = Modifier
                .clickable {
                    clipboardManager.setText(AnnotatedString(shareCode))
                    Toast
                        .makeText(
                            context,
                            "Pomyślnie skopiowano : $shareCode  ! 🫶",
                            Toast.LENGTH_LONG
                        )
                        .show()
                }
                .padding(vertical = 4.dp)) {
                Text(text = shareCode, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = Icons.Default.Close.name,
                    tint = DarkBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Text(text = desc)
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
