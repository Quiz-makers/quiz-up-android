package com.quizmakers.quizup.presentation.quizzes.manage

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizmakers.core.data.quizzes.remote.CategoryApi
import com.quizmakers.quizup.R
import com.quizmakers.quizup.ui.theme.DarkBlue
import com.quizmakers.quizup.ui.theme.LightBlue


@Composable
fun SpinnerCategory(
    selectedCategory: MutableState<Pair<Int, String>>,
    expanded: MutableState<Boolean>,
    categories: List<CategoryApi>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(10.dp)
            )
            .background(
                shape = RoundedCornerShape(10.dp), color = LightBlue
            )
            .padding(10.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = if (selectedCategory.value.second.isNotBlank()) selectedCategory.value.second else stringResource(
                R.string.add_category
            ),
            color = if (selectedCategory.value.second.isNotBlank()) Color.Black else Color.Gray,
            fontSize = 14.sp
        )
        IconButton(
            onClick = { expanded.value = true }, modifier = Modifier.clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = Icons.Default.ArrowDropDown.name,
                tint = DarkBlue,
                modifier = Modifier.size(30.dp)
            )
        }
    }
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        modifier = Modifier.fillMaxWidth()
    ) {
        categories.forEach { value ->
            DropdownMenuItem(onClick = {
                selectedCategory.value = Pair(value.categoryId, value.category)
                expanded.value = false
            }) {
                Text(value.category, fontSize = 12.sp)
            }
        }
    }
}
@Composable
fun HorizontalNumberPicker(
    height: Dp = 30.dp,
    min: Int = 0,
    max: Int = 10,
    default: Int = min,
    onValueChange: (Int) -> Unit = {}
) {
    val number = remember { mutableStateOf(default) }

    Row {
        PickerButton(
            size = height,
            drawable = R.drawable.ic_arrow_left,
            enabled = number.value > min,
            onClick = {
                if (number.value > min) number.value--
                onValueChange(number.value)
            }
        )
        Text(
            text = number.value.toString(),
            fontSize = (height.value / 2).sp,
            modifier = Modifier.padding(5.dp)
                .height(IntrinsicSize.Max)
                .align(CenterVertically)
        )
        PickerButton(
            size = height,
            drawable = R.drawable.ic_arrow_right,
            enabled = number.value < max,
            onClick = {
                if (number.value < max) number.value++
                onValueChange(number.value)
            }
        )
    }
}
@Composable
private fun PickerButton(
    size: Dp = 45.dp,
    @DrawableRes drawable: Int,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    val contentDescription = LocalContext.current.resources.getResourceName(drawable)
    val backgroundColor = if (enabled) DarkBlue else Color.Gray

    Image(
        painter = painterResource(id = drawable),
        contentDescription = contentDescription,
        modifier = Modifier.padding(8.dp).background(backgroundColor, CircleShape)
            .clip(CircleShape)
            .width(size).height(size)
            .clickable (
                enabled = enabled,
                onClick = { onClick() }
            )
    )
}