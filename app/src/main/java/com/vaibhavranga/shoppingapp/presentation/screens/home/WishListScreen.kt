package com.vaibhavranga.shoppingapp.presentation.screens.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
    val wishListState by viewModel.getAllWishListItemsState.collectAsStateWithLifecycle()
    val allProductsInWishListState by viewModel.getAllProductsInWishListState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var isWishListShowing by remember { mutableStateOf(false) }
    var isWishDeleteDialogShowing by remember { mutableStateOf(false) }
    var productName by remember { mutableStateOf("") }
    var productId by remember { mutableStateOf("") }

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
                    items(items = allProductsInWishListState.data ?: emptyList()) { product ->
                        WishListProductItem(
                            product = product!!,
                            onProductClick = onProductClick,
                            onProductLongClickForDeletion = { name, id ->
                                productName = name
                                productId = id
                                isWishDeleteDialogShowing = true
                            }
                        )
                    }
                }
            }
        }
//        if (isWishDeleteDialogShowing) {
//            DeleteFromWishListConfirmAlertDialog(
//                onConfirmDelete = {
//                    viewModel.deleteWishListItem(wishId = )
//                },
//                onDismissRequest = {},
//                productName = productName
//            )
//        }
        when {
            wishListState.isLoading -> CircularProgressIndicator()
            wishListState.error != null -> {
                Toast.makeText(context, wishListState.error, Toast.LENGTH_SHORT).show()
                viewModel.clearGetAllWishListItemsState()
            }

            wishListState.data != null -> {
                viewModel.getAllProductsInWishList(
                    productIdListInWishList = wishListState.data!!
                )
                viewModel.clearGetAllWishListItemsState()
            }
        }
        when {
            allProductsInWishListState.isLoading -> CircularProgressIndicator()
            allProductsInWishListState.data != null -> {
                isWishListShowing = true
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WishListProductItem(
    product: ProductModel,
    onProductClick: (productId: String) -> Unit,
    onProductLongClickForDeletion: (productName: String, productId: String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .combinedClickable(
                onClick = { onProductClick(product.productId) },
                onLongClick = {onProductLongClickForDeletion(product.name, product.productId)}
            )
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

@Composable
fun DeleteFromWishListConfirmAlertDialog(
    onConfirmDelete: () -> Unit,
    onDismissRequest: () -> Unit,
    productName: String
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            onConfirmDelete()
        },
        dismissButton = {
            onDismissRequest()
        },
        title = {
            Text(text = productName)
        },
        text = {
            Text(text = "Delete this product from your wish list")
        }
    )
}