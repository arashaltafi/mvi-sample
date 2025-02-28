package com.arash.altafi.mvisample.ui.presentation.paging

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.arash.altafi.mvisample.data.model.UserResponse
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.SubcomposeAsyncImage
import com.arash.altafi.mvisample.ui.component.LoadingImageEffect
import com.arash.altafi.mvisample.ui.component.LoadingShimmerEffect
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

@Composable
fun PagingScreen(
    navController: NavHostController,
    viewModel: PagingViewModel = hiltViewModel()
) {
    val pagingItems = viewModel.usersFlow.collectAsLazyPagingItems()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val context = LocalContext.current
    // Flag to control bottom loading indicator visibility
    var bottomLoading by remember { mutableStateOf(false) }

    // Handle append load state: show bottom loading for at least 1 second.
    LaunchedEffect(pagingItems.loadState.append) {
        if (pagingItems.loadState.append is LoadState.Loading) {
            bottomLoading = true
            delay(1000L) // ensure at least 1 second of loading indicator
            // If append has finished after the delay, hide the indicator.
            if (pagingItems.loadState.append !is LoadState.Loading) {
                bottomLoading = false
            }
        } else {
            bottomLoading = false
        }
    }

    // When append load state is Error, show a toast and auto-retry after delay.
    LaunchedEffect(pagingItems.loadState.append) {
        val loadState = pagingItems.loadState.append
        if (loadState is LoadState.Error) {
            Toast.makeText(
                context,
                "Error loading page: ${loadState.error.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
            // Optionally wait for network recovery; here we delay and then retry.
            delay(2000L)
            pagingItems.retry()
        }
    }

    // When refresh load state is Error, show a toast.
    LaunchedEffect(pagingItems.loadState.refresh) {
        val loadState = pagingItems.loadState.refresh
        if (loadState is LoadState.Error) {
            Toast.makeText(
                context,
                "Error loading data: ${loadState.error.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.refresh() }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            contentPadding = PaddingValues(8.dp)
        ) {
            // Show grid items
            items(pagingItems.itemCount) { index ->
                pagingItems[index]?.let { user ->
                    UserItemPaging(user)
                }
            }
            // If bottom loading is active, show shimmer effects
            if (bottomLoading) {
                items(1) {
                    LoadingShimmerEffect()
                }
            }
        }

        // When refresh is loading, show full-screen loading shimmers
        if (pagingItems.loadState.refresh is LoadState.Loading) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(3) { // show 3 shimmer items (3 rows x 1 columns)
                    LoadingShimmerEffect()
                }
            }
        }
    }
}

@Composable
fun UserItemPaging(user: UserResponse) {
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

            SubcomposeAsyncImage(
                model = user.avatar,
                loading = { LoadingImageEffect() },
                contentDescription = user.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
        }
    }
}