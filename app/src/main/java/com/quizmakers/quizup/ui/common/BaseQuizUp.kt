package com.quizmakers.quizup.ui.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizmakers.quizup.ui.theme.DarkBlue
import com.quizmakers.quizup.ui.theme.RedError

@Composable
fun BaseTextField(
    valueState: MutableState<TextFieldValue>,
    labelText: String,
    placeholderText: String,
    focusManager: FocusManager,
    isPassword: Boolean = false,
    errorMessage: String,
    onResetError: () -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions(
        onNext = {
            focusManager.moveFocus(FocusDirection.Down)
        }
    ),
    isError: Boolean = false
) {
    Column {
        OutlinedTextField(
            value = valueState.value,
            onValueChange = { newText ->
                if (errorMessage.isNotBlank()) {
                    onResetError()
                }
                valueState.value = newText
            },
            isError = errorMessage.isNotBlank() || isError,
            singleLine = true,
            maxLines = 1,
            label = { Text(text = labelText) },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            placeholder = { Text(text = placeholderText) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
        if (errorMessage.isNotBlank())
            Text(text = errorMessage, color = RedError, fontSize = 12.sp)
    }
}

@Composable
fun BaseTextFieldWithIcon(
    valueState: MutableState<TextFieldValue>,
    trailingIcon: ImageVector,
    placeholderText: String,
    labelText: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    keyboardActions: KeyboardActions = KeyboardActions(
        onDone = {}
    ),
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { newText ->
            valueState.value = newText
        },
        placeholder = { Text(text = placeholderText) },
        label = { Text(text = labelText) },
        trailingIcon = {
            Icon(
                imageVector = trailingIcon,
                contentDescription = trailingIcon.name,
                tint = Color.Black,
                modifier = Modifier.size(30.dp)
            )
        },
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,

        )
}

@Composable
fun BaseButton(label: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(10.dp)
            )
            .height(58.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent
        ),
        onClick = onClick,
        elevation = ButtonDefaults.elevation(0.dp, 0.dp)
    ) {
        Text(text = label, fontSize = 20.sp, color = Color.White)
    }
}

@Composable
fun BaseButtonWithIcon(modifier: Modifier, label: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(10.dp))
            .background(
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .then(modifier),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent
        ),
        onClick = onClick,
        elevation = ButtonDefaults.elevation(0.dp, 0.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = label, color = Color.Black, fontWeight = FontWeight.Medium)
            Icon(
                imageVector = icon,
                contentDescription = icon.name,
                tint = DarkBlue,
                modifier = Modifier.size(30.dp)
            )
        }

    }
}

@Composable
fun BaseIndicator(
    size: Dp = 42.dp,
    sweepAngle: Float = 90f,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth
) {
    val transition = rememberInfiniteTransition()
    val currentArcStartAngle by transition.animateValue(
        0,
        360,
        Int.VectorConverter,
        infiniteRepeatable(
            animation = tween(
                durationMillis = 1100,
                easing = LinearEasing
            )
        )
    )
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Square)
    }
    Canvas(
        Modifier
            .progressSemantics()
            .size(size) // canvas size
            .padding(strokeWidth / 2)
    ) {
        drawCircle(Color.LightGray, style = stroke)
        drawArc(
            color,
            startAngle = currentArcStartAngle.toFloat() - 90,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = stroke
        )
    }
}