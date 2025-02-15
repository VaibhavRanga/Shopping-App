package com.vaibhavranga.shoppingapp.presentation.screens.home

import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.vaibhavranga.shoppingapp.domain.model.CategoryModel
import com.vaibhavranga.shoppingapp.domain.model.ProductModel
import com.vaibhavranga.shoppingapp.presentation.common.CustomTextField
import com.vaibhavranga.shoppingapp.presentation.screens.auth.showToast
import com.vaibhavranga.shoppingapp.presentation.viewModel.ViewModel
import com.vaibhavranga.shoppingapp.ui.theme.Gray
import com.vaibhavranga.shoppingapp.ui.theme.Pink
import com.vaibhavranga.shoppingapp.ui.theme.ShoppingAppTheme

@Composable
fun HomeScreen(
    onNotificationsButtonClick: () -> Unit,
    onCategoryClick: (categoryName: String) -> Unit,
    onSeeMoreCategoriesClick: () -> Unit,
    viewModel: ViewModel = hiltViewModel()
) {
    val allCategories = viewModel.getAllCategoriesState.collectAsStateWithLifecycle()
    val allProducts = viewModel.getAllProductsState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    var isShowingCategories by remember { mutableStateOf(false) }
    var isShowingProducts by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllCategories()
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllProducts()
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            SearchBarRow(
                value = searchQuery,
                onSearchValueChange = { searchQuery = it },
                onNotificationsButtonClick = onNotificationsButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
            )
            CategoriesBlock(
                isShowingCategories = isShowingCategories,
                categories = allCategories.value.data ?: emptyList(),
                onCategoryClick = {
                    onCategoryClick(it)
                },
                onSeeMoreCategoriesClick = onSeeMoreCategoriesClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
            FlashSaleBlock(
                isShowingProducts = isShowingProducts,
                products = allProducts.value.data ?: emptyList(),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        when {
            allCategories.value.isLoading -> CircularProgressIndicator()
            allCategories.value.error != null -> {
                showToast(
                    context = context,
                    message = allCategories.value.error.toString()
                )
                viewModel.clearGetAllCategoriesState()
            }

            allCategories.value.data != null -> {
                isShowingCategories = true
            }
        }
        when {
            allProducts.value.isLoading -> CircularProgressIndicator()
            allProducts.value.error != null -> {
                showToast(
                    context = context,
                    message = allProducts.value.error.toString()
                )
                viewModel.clearGetAllProductsState()
            }

            allProducts.value.data != null -> {
                isShowingProducts = true
            }
        }
    }
}

@Composable
fun SearchBarRow(
    value: String,
    onSearchValueChange: (String) -> Unit,
    onNotificationsButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        CustomTextField(
            value = value,
            onValueChange = onSearchValueChange,
            placeholder = "Search",
            leadingIcon = Icons.Default.Search,
            modifier = Modifier
                .weight(1f)
        )
        Spacer(
            modifier = Modifier
                .width(16.dp)
        )
        IconButton(
            onClick = onNotificationsButtonClick
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
            )
        }
    }
}

@Composable
fun CategoriesBlock(
    isShowingCategories: Boolean,
    categories: List<CategoryModel>,
    onCategoryClick: (categoryName: String) -> Unit,
    onSeeMoreCategoriesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleLarge
            )
            TextButton(
                onClick = onSeeMoreCategoriesClick
            ) {
                Text(text = "See more", color = Pink)
            }
        }
        if (isShowingCategories) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                items(
                    items = categories,
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
                            colorFilter = ColorFilter.tint(
                                color = if (isSystemInDarkTheme())
                                    Color.White
                                else
                                    Color.Black
                            ),
                            modifier = Modifier
                                .size(55.dp)
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
    }
}

@Composable
fun FlashSaleBlock(
    isShowingProducts: Boolean,
    products: List<ProductModel>,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Flash Sale",
                style = MaterialTheme.typography.titleLarge
            )
        }
        if (isShowingProducts) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                items(
                    items = products,
                    key = { it.name }
                ) { product ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
                        modifier = Modifier
                            .height(300.dp)
                            .width(120.dp)
                    ) {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(shape = RoundedCornerShape(size = 16.dp))
                                .border(
                                    width = 1.dp,
                                    color = Pink,
                                    shape = RoundedCornerShape(size = 16.dp)
                                )
                        )
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .border(
                                    width = 1.dp,
                                    color = Pink,
                                    shape = RoundedCornerShape(size = 16.dp)
                                )
                                .padding(8.dp)
                        ) {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = Gray,
                                    fontWeight = FontWeight.Normal
                                ),
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                            Column {
                                Row(
                                    verticalAlignment = Alignment.Bottom,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Text(
                                        text = "Rs ",
                                        style = MaterialTheme.typography.titleMedium.copy(color = Pink)
                                    )
                                    Text(
                                        text = product.finalPrice,
                                        color = Pink,
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Text(
                                        text = "Rs ",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = product.price,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, device = Devices.PIXEL_7)
@Composable
private fun HomeScreenPreview() {
    ShoppingAppTheme {
        FlashSaleBlock(
            isShowingProducts = true,
            products = listOf(
                ProductModel(
                    name = "Nike Sportswear",
                    price = "4295",
                    finalPrice = "3995",
                    description = "Women's Velour High-Waisted Wide-Leg Trousers",
                    imageUrl = "https://firebasestorage.googleapis.com/v0/b/shopping-app-4ae90.firebasestorage.app/o/products%2F-1738663946249?alt=media&token=0039329d-f8f7-4925-a030-14fdfec6780a",
                    category = "Ladies Pants",
                    date = 1738663952022,
                    availableUnits = 39
                )
            )
        )
    }
}