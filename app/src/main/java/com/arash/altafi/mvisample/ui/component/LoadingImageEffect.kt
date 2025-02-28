package com.arash.altafi.mvisample.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val gradient = listOf(
    Color.Gray.copy(alpha = 0.6f), //darker grey (90% opacity)
    Color.Gray.copy(alpha = 0.3f), //lighter grey (30% opacity)
    Color.Gray.copy(alpha = 0.6f)
)

@Composable
fun LoadingImageEffect() {
    val transition = rememberInfiniteTransition(label = "")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        label = "",
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutLinearInEasing
            )
        )
    )
    val brush = linearGradient(
        colors = gradient,
        start = Offset(200f, 200f),
        end = Offset(
            x = translateAnimation.value,
            y = translateAnimation.value
        )
    )
    ShimmerContainer(brush)
}

@Composable
private fun ShimmerContainer(brush: Brush) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush)
            .aspectRatio(ratio = 0.67f),
    )
}

@Composable
fun LoadingShimmerEffect(modifier: Modifier = Modifier) {
    Card(modifier = modifier.padding(8.dp)) {
        LoadingImageEffect()
    }
}