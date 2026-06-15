package com.gmail.uli153.ulisestv.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gmail.uli153.ulisestv.R
import com.gmail.uli153.ulisestv.domain.model.Video
import com.gmail.uli153.ulisestv.ui.common.LabeledTextField
import com.gmail.uli153.ulisestv.ui.common.VideoCard
import com.gmail.uli153.ulisestv.ui.videos.VideosViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Search videos within a single category. Reuses [VideosViewModel], which
 * already combines a text query (via SearchVideoUseCase) with the category
 * filter.
 */
@Composable
fun SearchScreen(
    onVideoClick: (Video) -> Unit,
    categoryId: Int,
    modifier: Modifier = Modifier,
    viewModel: VideosViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }

    LaunchedEffect(categoryId) {
        viewModel.setCategory(categoryId)
    }

    Column(modifier = modifier.fillMaxSize().padding(48.dp)) {
        LabeledTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.onQueryChange(it)
            },
            label = stringResource(R.string.search),
            imeAction = ImeAction.Search,
            modifier = Modifier.fillMaxWidth(0.6f),
        )

        LazyVerticalGrid(
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
