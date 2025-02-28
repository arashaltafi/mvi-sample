package com.arash.altafi.mvisample.ui.presentation.testDetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.arash.altafi.mvisample.data.model.TestDetailEntity
import com.arash.altafi.mvisample.ui.base.ApiState
import com.arash.altafi.mvisample.ui.base.BaseScreen

@Composable
fun TestDetail(
    userId: String,
    navController: NavController,
    viewModel: TestDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = userId) {
        viewModel.loadUserDetail(userId)
    }

    val apiState by viewModel.apiState.collectAsState()

    BaseScreen(apiState = apiState, onRetry = { viewModel.loadUserDetail(userId) }) {
        when (apiState) {
            is ApiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ApiState.Success -> {
                val user = (apiState as ApiState.Success<TestDetailEntity>).data
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    AsyncImage(
                        model = user.avatar,
                        contentDescription = "Avatar",
                        modifier = Modifier.size(128.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Name: ${user.name}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Family: ${user.family}", style = MaterialTheme.typography.bodyLarge)
                }
            }
            is ApiState.Error -> {
                // Error is shown via bottom sheet.
            }
        }
    }
}