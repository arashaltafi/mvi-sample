package com.arash.altafi.mvisample.ui.component

import android.Manifest
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.arash.altafi.mvisample.R
import com.arash.altafi.mvisample.utils.PermissionUtils
import com.arash.altafi.mvisample.utils.ext.saveImage
import com.arash.altafi.mvisample.utils.ext.saveImageBlow29
import kotlinx.coroutines.withContext
import java.net.URL

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageScreen(navController: NavController, title: String, imageUrl: String) {
    if (title == "" || imageUrl == "") {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("خطایی رخ داده است")
        }
        return
    }

    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Permission launcher for WRITE_EXTERNAL_STORAGE Android 13 Above
    val storagePermissionLauncher13 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.all { it.value }
        if (granted) {
            scope.launch(Dispatchers.IO) {
                saveImageToStorage(context, imageUrl)
            }
        } else {
            Toast.makeText(
                context,
                "برای دانلود عکس لطفا دسترسی لازم را اعطا کنید",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Permission launcher for WRITE_EXTERNAL_STORAGE
    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.all { it.value }
        if (granted) {
            scope.launch(Dispatchers.IO) {
                saveImageToStorage(context, imageUrl)
            }
        } else {
            Toast.makeText(
                context,
                "برای دانلود عکس لطفا دسترسی لازم را اعطا کنید",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // TopBar
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp))
                .fillMaxWidth()
                .height(70.dp)
                .background(colorResource(R.color.blue_300))
                .padding(horizontal = 16.dp)
                .zIndex(2f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier
                    .fillMaxHeight(),
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (PermissionUtils.isPermissionGranted(
                                context,
                                Manifest.permission.READ_MEDIA_IMAGES
                            )
                        ) {
                            scope.launch(Dispatchers.IO) {
                                saveImageToStorage(context, imageUrl)
                            }
                        } else {
                            storagePermissionLauncher13.launch(
                                arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                            )
                        }
                    } else {
                        if (PermissionUtils.isPermissionGranted(
                                context,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ) &&
                            PermissionUtils.isPermissionGranted(
                                context,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        ) {
                            scope.launch(Dispatchers.IO) {
                                saveImageToStorage(context, imageUrl)
                            }
                        } else {
                            storagePermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                )
                            )
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }

            Row(
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp * 0.7f)
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.width(4.dp))

                IconButton(
                    modifier = Modifier
                        .fillMaxHeight(),
                    onClick = {
                        navController.navigateUp()
                    }
                ) {
                    Icon(
                        modifier = Modifier.rotate(180f),
                        painter = painterResource(id = R.drawable.round_arrow_back_24),
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        }

        // Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .combinedClickable(
                    interactionSource = null,
                    indication = null,
                    onClick = {},
                    onLongClick = {},
                    onDoubleClick = {
                        if (scale <= 2) {
                            scale *= 1.1f
                        } else {
                            scale = 1f
                        }
                    }
                )
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale *= zoom
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                }
        ) {
            CoilImage(
                url = imageUrl,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }

        // BottomBar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .zIndex(2f),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { scale *= 0.9f },
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(50.dp),
                    border = BorderStroke(
                        1.dp,
                        colorResource(R.color.red_300)
                    ),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = colorResource(R.color.red_500),
                        contentColor = colorResource(R.color.red_700)
                    )
                ) {
                    Text(
                        text = "-",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                OutlinedButton(
                    onClick = { scale *= 1.1f },
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(50.dp),
                    border = BorderStroke(
                        1.dp,
                        colorResource(R.color.blue_300)
                    ),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = colorResource(R.color.blue_500),
                        contentColor = colorResource(R.color.blue_700)
                    )
                ) {
                    Text(
                        text = "+",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

private suspend fun saveImageToStorage(context: Context, imageUrl: String) {
    try {
        val url = URL(imageUrl)
        val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        var msg: Int = R.string.app_name

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.saveImage(bitmap) {
                msg = if (it)
                    R.string.save_in_gallery_success
                else
                    R.string.save_in_gallery_error
            }
        } else {
            context.saveImageBlow29(bitmap) {
                msg = if (it)
                    R.string.save_in_gallery_success
                else
                    R.string.save_in_gallery_error
            }
        }

        withContext(Dispatchers.Main) {
            Toast.makeText(context, context.getString(msg), Toast.LENGTH_SHORT).show()
        }
    } catch (_: Exception) {
        withContext(Dispatchers.Main) {
            Toast.makeText(
                context,
                context.getString(R.string.save_in_gallery_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}