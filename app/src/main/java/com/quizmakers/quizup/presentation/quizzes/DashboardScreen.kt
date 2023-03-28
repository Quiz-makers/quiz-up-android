package com.quizmakers.quizup.presentation.quizzes


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizmakers.quizup.ui.common.BaseTextFieldWithIcon
import com.quizmakers.quizup.ui.theme.DarkBlue
import com.quizmakers.quizup.ui.theme.MediumBlue
import com.quizmakers.quizup.ui.theme.QuizUpTheme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun DashboardScreen() {
    val search = remember { mutableStateOf(TextFieldValue("")) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 0.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item { CodeCard(search) }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                Text(
                    text = "Dostępne Quizzy", fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                MyCardList(2)
            }

            item {
                Text(
                    text = "Moje Quizz", fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
            }
            item {
                MyCardList(1)
            }



        }
    }
}

@Composable
fun MyCardList(number:Int) {
    val cardData = remember { generateFakeCards() }
    LazyHorizontalGrid(
        modifier = Modifier.height(if (number == 2) 240.dp else 120.dp),
        rows = GridCells.Fixed(number),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(10.dp),
    ) {
        cardData.forEach { card ->
            item(span = { GridItemSpan(1) }) {
                MyCard(title = card.first)
            }

        }
    }
}

@Composable
@OptIn(ExperimentalTextApi::class)
private fun CodeCard(search: MutableState<TextFieldValue>) {
    Card(elevation = 10.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Hej, ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            brush = Brush.linearGradient(
                                colors = listOf(MediumBlue, DarkBlue)
                            )
                        )
                    ) {
                        append("Paweł 👋 ")
                    }
                }
            )
            Text(text = "Jeśli posiadasz kod dostępu, możesz wpisać go w tym miejscu:")
            Spacer(modifier = Modifier.height(8.dp))
            BaseTextFieldWithIcon(
                valueState = search,
                trailingIcon = Icons.Default.Search,
                placeholderText = "kod",
                labelText = "Podaj kod i dołącz do quizu"
            )
        }
    }
}

@Composable
fun MyCard(
    title: String
) {
    Card(
        elevation = 5.dp,
        modifier = Modifier
            .height(80.dp)
            .width(80.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = Icons.Default.Face.name,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light
            )
        }
    }
}

private fun generateFakeCards(): List<Pair<String, String>> {
    return MutableList(10) { index ->
        val cardNumber = index + 1
        "Title $cardNumber" to "Subtitle $cardNumber"
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun SignInScreenPreview() {
    QuizUpTheme {
        DashboardScreen()
    }
}