package com.arash.altafi.mvisample.ui.presentation.testList

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.arash.altafi.mvisample.data.model.TestDetailEntity
import com.arash.altafi.mvisample.ui.base.ApiState
import com.arash.altafi.mvisample.ui.base.BaseScreen
import com.arash.altafi.mvisample.ui.navigation.Route
import com.arash.altafi.mvisample.ui.theme.CustomFont

@Composable
fun TestList(
    navController: NavController,
    viewModel: TestListViewModel = hiltViewModel()
) {
    val apiState by viewModel.apiState.collectAsState()

    BaseScreen(apiState = apiState, onRetry = { viewModel.loadUsers() }) {
        when (apiState) {
            is ApiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ApiState.Success -> {
                val users = (apiState as ApiState.Success<List<TestDetailEntity>>).data
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(users) { user ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate(
                                    Route.TestDetail(user.id.toString())
                                ) }
                                .padding(16.dp)
                        ) {
                            AsyncImage(
                                model = user.avatar,
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "${user.name} ${user.family}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            is ApiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text(
                        text = "خطایی رخ داده است",
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CustomFont
                    )
                }
            }
        }
    }
}