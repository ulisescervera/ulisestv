package com.gmail.uli153.ulisestv.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Button
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.gmail.uli153.ulisestv.R
import com.gmail.uli153.ulisestv.domain.model.Video
import com.gmail.uli153.ulisestv.ui.common.LoadingIndicator
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(
    videoId: Int,
    onPlay: (Video) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = koinViewModel(),
) {
    LaunchedEffect(videoId) {
        viewModel.setVideoId(videoId)
    }

    val video by viewModel.video.collectAsStateWithLifecycle()
    val current = video

    if (current == null) {
        LoadingIndicator(modifier = modifier)
        return
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(48.dp),
        horizontalArrangement = Arrangement.spacedBy(48.dp),
    ) {
        AsyncImage(
            model = null,
            contentDescription = current.name,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.movie),
            error = painterResource(R.drawable.movie),
            fallback = painterResource(R.drawable.movie),
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(12.dp)),
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = current.name,
                style = MaterialTheme.typography.headlineLarge,
            )
            Text(
                text = current.category.name,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = current.description,
                style = MaterialTheme.typography.bodyLarge,
            )
            Button(
                onClick = { onPlay(current) },
                modifier = Modifier.padding(top = 12.dp),
            ) {
                Text(text = stringResource(R.string.play))
            }
        }
    }
}
