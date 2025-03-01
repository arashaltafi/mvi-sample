package com.arash.altafi.mvisample.ui.base

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arash.altafi.mvisample.R
import com.arash.altafi.mvisample.ui.theme.CustomFont
import kotlinx.coroutines.launch

@Composable
fun BaseScreen(
    apiState: ApiState<*>,
    onRetry: () -> Unit,
    content: @Composable () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(apiState) {
        if (apiState is ApiState.Error) {
            scope.launch { sheetState.show() }
        } else {
            scope.launch { sheetState.hide() }
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            if (apiState is ApiState.Error) {
                Column(
                    modifier = Modifier
                        .background(
                            color = colorResource(R.color.gray_100),
                        )
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "خطایی رخ داده است",
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CustomFont
                    )

                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = apiState.message,
                        fontSize = 14.sp,
                        fontFamily = CustomFont
                    )

                    Row(
                        modifier = Modifier.padding(top = 32.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(R.color.red_500),
                                contentColor = Color.White
                            ),
                            onClick = {
                                scope.launch { sheetState.hide() }
                            }
                        ) {
                            Text(
                                text = "بازگشت",
                                fontSize = 14.sp,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Bold,
                                fontFamily = CustomFont
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(R.color.green_500),
                                contentColor = Color.White
                            ),
                            onClick = onRetry
                        ) {
                            Text(
                                text = "تلاش مجدد",
                                fontSize = 14.sp,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Bold,
                                fontFamily = CustomFont
                            )
                        }
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            content()
        }
    }
}