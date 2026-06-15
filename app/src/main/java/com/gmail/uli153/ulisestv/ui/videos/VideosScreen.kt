package com.gmail.uli153.ulisestv.ui.videos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Button
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.gmail.uli153.ulisestv.R
import com.gmail.uli153.ulisestv.domain.model.Video
import com.gmail.uli153.ulisestv.ui.common.ErrorMessage
import com.gmail.uli153.ulisestv.ui.common.LoadingIndicator
import com.gmail.uli153.ulisestv.ui.common.VideoCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun VideosScreen(
    onVideoClick: (Video) -> Unit,
    onSearchClick: () -> Unit,
    categoryId: Int,
    modifier: Modifier = Modifier,
    viewModel: VideosViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(categoryId) {
        viewModel.setCategory(categoryId)
    }

    Column(modifier = modifier.fillMaxSize().padding(48.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.videos),
                style = MaterialTheme.typography.headlineLarge,
            )
            // Search within this category.
            Button(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search),
                )
                Text(
                    text = stringResource(R.string.search),
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        }

        when {
            state.isLoading && state.videos.isEmpty() -> LoadingIndicator()
            state.error != null && state.videos.isEmpty() ->
                ErrorMessage(message = state.error!!, onRetry = viewModel::refresh)
            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp),
            ) {
                items(state.videos, key = { it.id }) { video ->
                    VideoCard(
                        video = video,
                        onClick = { onVideoClick(video) },
                        modifier = Modifier.padding(8.dp),
                    )
                }
            }
        }
    }
}
