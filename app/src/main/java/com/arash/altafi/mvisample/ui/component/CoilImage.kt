package com.arash.altafi.mvisample.ui.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.rememberAsyncImagePainter

@Composable
fun CoilImage(
    url: String,
    modifier: Modifier,
    alt: String = "",
    contentScale: ContentScale = ContentScale.Crop
) {
    val painter = rememberAsyncImagePainter(model = url)
    Image(
        painter = painter,
        contentDescription = alt,
        modifier = modifier,
        contentScale = contentScale
    )
}