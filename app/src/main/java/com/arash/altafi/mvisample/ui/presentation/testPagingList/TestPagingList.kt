package com.arash.altafi.mvisample.ui.presentation.testPagingList

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import androidx.paging.compose.collectAsLazyPagingItems
import com.arash.altafi.mvisample.ui.navigation.Route

@Composable
fun TestPagingList(
    navController: NavController,
    viewModel: TestPagingListViewModel = hiltViewModel()
) {

    val lazyPagingItems = viewModel.pagingData.collectAsLazyPagingItems()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(lazyPagingItems.itemCount) { index ->
            lazyPagingItems[index]?.let { user ->
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
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${user.name} ${user.family}")
                }
            }
        }

        lazyPagingItems.apply {
            when {
                loadState.refresh is androidx.paging.LoadState.Loading -> {
                    item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                }

                loadState.append is androidx.paging.LoadState.Loading -> {
                    item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                }

                loadState.refresh is androidx.paging.LoadState.Error -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Error: ${(loadState.refresh as androidx.paging.LoadState.Error).error.localizedMessage}")
                            Button(onClick = { retry() }) {
                                Text("Retry")
                            }
                        }
                    }
                }

                loadState.append is androidx.paging.LoadState.Error -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Error: ${(loadState.append as androidx.paging.LoadState.Error).error.localizedMessage}")
                            Button(onClick = { retry() }) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }
    }

}