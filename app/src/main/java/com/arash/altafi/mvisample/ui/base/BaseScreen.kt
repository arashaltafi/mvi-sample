package com.arash.altafi.mvisample.ui.base

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BaseScreen(
    apiState: ApiState<*>,
    onRetry: () -> Unit,
    content: @Composable () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Error: ${apiState.message}", style = MaterialTheme.typography.h6)
                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                }
            }
        }
    ) {
        content()
    }
}