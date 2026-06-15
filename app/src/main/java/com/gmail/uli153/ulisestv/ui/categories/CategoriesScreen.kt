package com.gmail.uli153.ulisestv.ui.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.gmail.uli153.ulisestv.R
import com.gmail.uli153.ulisestv.domain.model.Category
import com.gmail.uli153.ulisestv.ui.common.ContentCard
import com.gmail.uli153.ulisestv.ui.common.ErrorMessage
import com.gmail.uli153.ulisestv.ui.common.LoadingIndicator
import org.koin.androidx.compose.koinViewModel

/** Full vertical listing of every category (reached from the "All" entry in Home). */
@Composable
fun CategoriesScreen(
    onCategoryClick: (Category) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoriesViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize().padding(48.dp)) {
        Text(
            text = stringResource(R.string.categories),
            style = MaterialTheme.typography.headlineLarge,
        )

        when {
            state.isLoading && state.categories.isEmpty() -> LoadingIndicator()
            state.error != null && state.categories.isEmpty() ->
                ErrorMessage(message = state.error!!, onRetry = viewModel::refresh)
            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(state.categories, key = { it.id }) { category ->
                    ContentCard(
                        title = category.name,
                        subtitle = category.description,
                        onClick = { onCategoryClick(category) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                    )
                }
            }
        }
    }
}
