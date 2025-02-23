package com.arash.altafi.mvisample.ui.presentation.user

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.arash.altafi.mvisample.data.model.UserResponse
import kotlinx.coroutines.delay
import com.arash.altafi.mvisample.R
import com.arash.altafi.mvisample.ui.component.CoilImage

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun UserScreen(
    navController: NavHostController,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val state by userViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        if (state !is UserState.Success) {
            userViewModel.sendIntent(
                UserIntent.FetchUsers(pageNumber = 1, pageSize = 20)
            )
        }
    }

    var refreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            refreshing = true
            userViewModel.sendIntent(
                UserIntent.FetchUsers(pageNumber = 1, pageSize = 20)
            )
        }
    )

    LaunchedEffect(state) {
        if (state is UserState.Success || state is UserState.Error) {
            delay(1000L)
            refreshing = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        when (state) {
            is UserState.Idle -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Idle")
            }

            is UserState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is UserState.Success -> {
                val users = (state as UserState.Success).users
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(users) { user ->
                        UserItem(user)
                    }
                }
            }

            is UserState.Error -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: ${(state as UserState.Error).message}")
            }
        }

        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            scale = true,
            backgroundColor = colorResource(R.color.gray_700),
            contentColor = Color.White
        )
    }
}

@Composable
fun UserItem(user: UserResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ID: ${user.id}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
            )

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = user.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = user.family,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                )
            }

            CoilImage(
                url = user.avatar,
                alt = user.name + " " + user.family,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
        }
    }
}