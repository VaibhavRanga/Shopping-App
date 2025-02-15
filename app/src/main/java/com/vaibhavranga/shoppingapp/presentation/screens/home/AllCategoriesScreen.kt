package com.vaibhavranga.shoppingapp.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.vaibhavranga.shoppingapp.presentation.screens.auth.showToast
import com.vaibhavranga.shoppingapp.presentation.viewModel.ViewModel
import com.vaibhavranga.shoppingapp.ui.theme.Gray
import com.vaibhavranga.shoppingapp.ui.theme.Pink
import com.vaibhavranga.shoppingapp.ui.theme.ShoppingAppTheme

@Composable
fun AllCategoriesScreen(
    onCategoryClick: (categoryName: String) -> Unit,
    viewModel: ViewModel = hiltViewModel()
) {
    val allCategories by viewModel.getAllCategoriesState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllCategories()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier
                .height(32.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(
                    items = allCategories.data ?: emptyList(),
                    key = { it.categoryName }
                ) { category ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            space = 8.dp,
                            alignment = Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .size(85.dp)
                            .clickable {
                                onCategoryClick(category.categoryName)
                            }
                    ) {
                        AsyncImage(
                            model = category.categoryImageUrl,
                            contentDescription = category.categoryName,
                            modifier = Modifier
                                .size(55.dp)
                                .background(color = Color.White)
                                .clip(shape = CircleShape)
                                .border(
                                    width = 2.dp,
                                    color = Pink,
                                    shape = CircleShape
                                )
                                .padding(8.dp)
                        )
                        Text(
                            text = category.categoryName,
                            style = MaterialTheme.typography.bodySmall,
                            color = Gray,
                            maxLines = 2
                        )
                    }
                }
            }
        }
        when {
            allCategories.isLoading -> CircularProgressIndicator()
            allCategories.error != null -> {
                showToast(
                    context = context,
                    message = allCategories.error.toString()
                )
                viewModel.clearGetAllCategoriesState()
            }
        }
    }
}

@Preview
@Composable
private fun AllCategoriesPreview() {
    ShoppingAppTheme {
    }
}