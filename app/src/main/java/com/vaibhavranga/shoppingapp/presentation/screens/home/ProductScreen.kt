package com.vaibhavranga.shoppingapp.presentation.screens.home

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.vaibhavranga.shoppingapp.domain.model.CartItemModel
import com.vaibhavranga.shoppingapp.domain.model.ProductModel
import com.vaibhavranga.shoppingapp.domain.model.WishListModel
import com.vaibhavranga.shoppingapp.presentation.common.StarRatingBar
import com.vaibhavranga.shoppingapp.presentation.viewModel.ViewModel
import com.vaibhavranga.shoppingapp.ui.theme.Gray
import com.vaibhavranga.shoppingapp.ui.theme.Pink
import com.vaibhavranga.shoppingapp.ui.theme.ShoppingAppTheme

@Composable
fun ProductScreen(
    productId: String,
    viewModel: ViewModel = hiltViewModel()
) {
    val productState by viewModel.getProductByIdState.collectAsStateWithLifecycle()
    val wishListState by viewModel.addToWishListState.collectAsStateWithLifecycle()
    val addToCartState by viewModel.addProductToCartState.collectAsStateWithLifecycle()
    var isShowingProduct by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.getProductById(productId = productId)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (isShowingProduct) {
            ProductImage(
                product = productState.data ?: ProductModel(),
                onAddToWishListClick = {
                    viewModel.addToWishList(wishListModel = it)
                },
                onAddToCartClick = {
                    val cartItemModel = CartItemModel(
                        productId = it
                    )
                    viewModel.addProductToCart(cartItemModel = cartItemModel)
                }
            )
        }
        when {
            productState.isLoading -> CircularProgressIndicator()
            productState.error != null -> {
                Toast.makeText(context, productState.error, Toast.LENGTH_SHORT).show()
                viewModel.clearGetProductByIdState()
            }

            productState.data != null -> {
                isShowingProduct = true
            }
        }
        when {
            wishListState.isLoading -> CircularProgressIndicator()
            wishListState.error != null -> {
                Toast.makeText(context, wishListState.error, Toast.LENGTH_SHORT).show()
                viewModel.clearAddToWishListState()
            }
            wishListState.data != null -> {
                Toast.makeText(context, wishListState.data, Toast.LENGTH_SHORT).show()
                viewModel.clearAddToWishListState()
            }
        }
        when {
            addToCartState.isLoading -> CircularProgressIndicator()
            addToCartState.error != null -> {
                Toast.makeText(context, addToCartState.error, Toast.LENGTH_SHORT).show()
                viewModel.clearAddProductToCartState()
            }
            addToCartState.data != null -> {
                Toast.makeText(context, addToCartState.data, Toast.LENGTH_SHORT).show()
                viewModel.clearAddProductToCartState()
            }
        }
    }
}

@Composable
fun ProductImage(
    product: ProductModel,
    onAddToWishListClick: (wishListItem: WishListModel) -> Unit,
    onAddToCartClick: (productId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val verticalScrollState = rememberScrollState()
    val brushLightTheme = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color.White.copy(alpha = 0.4f),
            0.4f to Color.White.copy(alpha = 0.9f)
        )
    )
    val brushDarkTheme = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color.Black.copy(alpha = 0.4f),
            0.4f to Color.Black.copy(alpha = 0.9f)
        )
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = (-80).dp,
            alignment = Alignment.Top
        ),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState)
    ) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 400.dp, max = 450.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(
                    brush = if (isSystemInDarkTheme()) {
                        brushDarkTheme
                    } else {
                        brushLightTheme
                    },
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp
                    )
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 4.dp
                )
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                StarRatingBar(
                    maxStars = 5,
                    rating = 3.0f,
                    onRatingChanged = {}
                )
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Add to wishlist",
                    tint = Pink,
                    modifier = Modifier
                        .clickable {
                            val wishListItem = WishListModel(
                                productId = product.productId
                            )
                            onAddToWishListClick(wishListItem)
                        }
                )
            }
            Row {
                Text(
                    text = "Rs:",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.alignByBaseline()
                )
                Text(
                    text = product.finalPrice,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier
                        .alignByBaseline()
                )
            }
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
            Text(text = "Description:")
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
            Text(text = product.description)
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
            Button(
                onClick = {},
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Pink),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Buy now")
            }
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
            Button(
                onClick = {
                    onAddToCartClick(product.productId)
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Gray),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Add to Cart")
            }
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
        }
    }
}

@Preview(showSystemUi = true, name = "Light theme")
@Preview(showSystemUi = true, name = "Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProductDisplayPreview() {
    ShoppingAppTheme {
        ProductImage(
            product = ProductModel(
                name = "Nice Formal Pants",
                productId = "",
                price = "1399",
                finalPrice = "1199",
                description = "This is a formal trousers with nice shine",
                imageUrl = "",
                category = "",
                date = System.currentTimeMillis(),
                availableUnits = 30
            ),
            onAddToWishListClick = {},
            onAddToCartClick = {}
        )
    }
}