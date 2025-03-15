package com.vaibhavranga.shoppingapp.presentation.screens.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.vaibhavranga.shoppingapp.domain.model.ProductModel
import com.vaibhavranga.shoppingapp.presentation.viewModel.ViewModel
import com.vaibhavranga.shoppingapp.ui.theme.Pink

@Composable
fun WishListScreen(
    onProductClick: (productId: String) -> Unit,
    viewModel: ViewModel = hiltViewModel()
) {
    val wishListItems by viewModel.getAllWishListItemsState.collectAsStateWithLifecycle()
    val deleteWishItemState by viewModel.deleteWishListItemState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var isWishListShowing by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllWishListItems()
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
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            Text(
                text = "My Wish List",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
            )
            if (isWishListShowing) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                ) {
                    items(items = wishListItems.data ?: emptyList()) { product ->
                        WishListProductItem(
                            product = product,
                            onProductClick = onProductClick,
                            onProductDeleteClick = {
                                viewModel.deleteWishListItem(it)
                            }
                        )
                    }
                }
            }
        }
        if (wishListItems.data?.size == 0) {
            Text(
                text = "Your wish list is empty.",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
            )
        }
        when {
            wishListItems.isLoading -> CircularProgressIndicator()
            wishListItems.error != null -> {
                Toast.makeText(context, wishListItems.error, Toast.LENGTH_SHORT).show()
                viewModel.clearGetAllWishListItemsState()
            }
            wishListItems.data != null -> {
                isWishListShowing = true
            }
        }
        when {
            deleteWishItemState.isLoading -> CircularProgressIndicator()
            deleteWishItemState.error != null -> {
                Toast.makeText(context, deleteWishItemState.error, Toast.LENGTH_SHORT).show()
                viewModel.clearDeleteWishListItemState()
            }
            deleteWishItemState.data != null -> {
                Toast.makeText(context, deleteWishItemState.data, Toast.LENGTH_SHORT).show()
                viewModel.clearDeleteWishListItemState()
                viewModel.getAllWishListItems()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WishListProductItem(
    product: ProductModel,
    onProductClick: (productId: String) -> Unit,
    onProductDeleteClick: (productId: String) -> Unit
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
            IconButton(
                onClick = {
                    onProductDeleteClick(product.productId)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete item from wish list"
                )
            }
        }
    }
}
