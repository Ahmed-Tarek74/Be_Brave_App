package com.compose.presentation.views.composeScreens
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.presentation.R

@Composable
fun StartScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = stringResource(id = R.string.chattingAnimation))

    val animatedAlphaValues = (0..7).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 800,
                    delayMillis = index * 100,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ), label = stringResource(id = R.string.chattingAnimation)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        stringResource(id = R.string.chatting).forEachIndexed { index, char ->
            BasicText(
                text = char.toString(),
                style = TextStyle(
                    color = colorResource(id = R.color.primary_dark_blue).copy(alpha = animatedAlphaValues[index].value),
                    fontSize = 40.sp
                ),
                modifier = Modifier.padding(2.dp)
            )
        }
    }
}