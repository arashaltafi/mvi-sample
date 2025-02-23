package com.arash.altafi.mvisample.ui.presentation.celebrity

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.arash.altafi.mvisample.R
import com.arash.altafi.mvisample.data.model.CelebrityResponse
import com.arash.altafi.mvisample.ui.component.CoilImage
import com.arash.altafi.mvisample.ui.navigation.Route
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun CelebrityScreen(
    navController: NavHostController,
    celebrityViewModel: CelebrityViewModel = hiltViewModel()
) {
    val state by celebrityViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        if (state !is CelebrityState.Success) {
            celebrityViewModel.sendIntent(
                CelebrityIntent.FetchCelebrities(
                    pageNumber = 1,
                    pageSize = 20
                )
            )
        }
    }

    var refreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            refreshing = true
            celebrityViewModel.sendIntent(
                CelebrityIntent.FetchCelebrities(
                    pageNumber = 1,
                    pageSize = 20
                )
            )
        }
    )

    LaunchedEffect(state) {
        if (state is CelebrityState.Success || state is CelebrityState.Error) {
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
            is CelebrityState.Idle -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Idle")
            }

            is CelebrityState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is CelebrityState.Success -> {
                val celebrities = (state as CelebrityState.Success).celebrities
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(celebrities) { celebrity ->
                        CelebrityItem(navController, celebrity)
                    }
                }
            }

            is CelebrityState.Error -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: ${(state as CelebrityState.Error).message}")
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
fun CelebrityItem(navController: NavHostController, celebrity: CelebrityResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box {
            CoilImage(
                url = celebrity.image,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clickable(
                        onClick = {
                            navController.navigate(
                                Route.ImageScreen(celebrity.id, celebrity.image)
                            )
                        }
                    )
            )

            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = Color.Black,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp),
                text = "ID: ${celebrity.id}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
            )
        }
    }
}