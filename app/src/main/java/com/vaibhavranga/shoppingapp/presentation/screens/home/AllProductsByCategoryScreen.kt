package com.vaibhavranga.shoppingapp.presentation.screens.home

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.vaibhavranga.shoppingapp.domain.model.ProductModel
import com.vaibhavranga.shoppingapp.presentation.viewModel.ViewModel
import com.vaibhavranga.shoppingapp.ui.theme.Pink
import com.vaibhavranga.shoppingapp.ui.theme.ShoppingAppTheme

@Composable
fun AllProductsByCategoryScreen(
    categoryName: String,
    onProductClick: (productId: String) -> Unit,
    viewModel: ViewModel = hiltViewModel()
) {
    val productsState by viewModel.getAllProductsByCategoryState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllProductsByCategory(categoryName = categoryName)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = categoryName,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                items(items = productsState.data ?: emptyList()) { product ->
                    ProductItem(
                        product = product,
                        onProductClick = onProductClick
                    )
                }
            }
        }
        when {
            productsState.isLoading -> CircularProgressIndicator()
            productsState.error != null -> {
                Toast.makeText(context, productsState.error.toString(), Toast.LENGTH_SHORT).show()
                viewModel.clearGetAllProductsByCategoryState()
            }
        }
    }
}

@Composable
fun ProductItem(
    product: ProductModel,
    onProductClick: (productId: String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable {
                onProductClick(product.productId)
            }
    ) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .width(120.dp)
                .border(
                    width = 1.dp,
                    color = Pink,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(shape = RoundedCornerShape(16.dp))
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.CenterVertically
                ),
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    text = product.name,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.description,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(text = "Rs ${product.finalPrice}")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AllProductsScreenPreview() {
    ShoppingAppTheme {
        ProductItem(
            product = ProductModel(),
            onProductClick = {}
        )
    }
}