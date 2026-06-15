package com.gmail.uli153.ulisestv.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Button
import androidx.tv.material3.Carousel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.gmail.uli153.ulisestv.R
import com.gmail.uli153.ulisestv.domain.model.Category
import com.gmail.uli153.ulisestv.domain.model.Video
import com.gmail.uli153.ulisestv.ui.categories.CategoriesViewModel
import com.gmail.uli153.ulisestv.ui.common.ContentCard
import com.gmail.uli153.ulisestv.ui.videos.VideosViewModel
import org.koin.androidx.compose.koinViewModel

private const val FEATURED_COUNT = 6

@Composable
fun HomeScreen(
    onFeaturedClick: (Video) -> Unit,
    onCategoryClick: (Category) -> Unit,
    onAllCategoriesClick: () -> Unit,
    modifier: Modifier = Modifier,
    categoriesViewModel: CategoriesViewModel = koinViewModel(),
    videosViewModel: VideosViewModel = koinViewModel(),
) {
    val categoriesState by categoriesViewModel.uiState.collectAsStateWithLifecycle()
    val videosState by videosViewModel.uiState.collectAsStateWithLifecycle()

    val featured = videosState.videos.take(FEATURED_COUNT)

    Column(modifier = modifier.fillMaxSize()) {
        if (featured.isNotEmpty()) {
            FeaturedCarousel(
                items = featured,
                onItemClick = onFeaturedClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp),
            )
        }

        Text(
            text = stringResource(R.string.categories),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 48.dp, top = 24.dp, bottom = 12.dp),
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Leading "All" entry → full categories listing.
            item {
                ContentCard(
                    title = stringResource(R.string.all),
                    subtitle = "",
                    onClick = onAllCategoriesClick,
                    modifier = Modifier.width(260.dp),
                )
            }
            items(categoriesState.categories, key = { it.id }) { category ->
                ContentCard(
                    title = category.name,
                    subtitle = category.description,
                    onClick = { onCategoryClick(category) },
                    modifier = Modifier.width(260.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun FeaturedCarousel(
    items: List<Video>,
    onItemClick: (Video) -> Unit,
    modifier: Modifier = Modifier,
) {
    Carousel(
        itemCount = items.size,
        modifier = modifier,
    ) { index ->
        val video = items[index]
        Box(modifier = Modifier.fillMaxSize()) {
            // Placeholder artwork (the Video model has no banner image yet).
            AsyncImage(
                model = null,
                contentDescription = video.name,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.movie),
                error = painterResource(R.drawable.movie),
                fallback = painterResource(R.drawable.movie),
                modifier = Modifier.fillMaxSize(),
            )
            // Scrim so the text is readable over the artwork.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)),
                        ),
                    ),
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(48.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = video.name,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = video.description,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Button(
                    onClick = { onItemClick(video) },
                    modifier = Modifier.padding(top = 8.dp),
                ) {
                    Text(text = stringResource(R.string.details))
                }
            }
        }
    }
}
