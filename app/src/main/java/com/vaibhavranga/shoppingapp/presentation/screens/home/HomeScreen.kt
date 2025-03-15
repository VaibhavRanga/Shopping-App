package com.vaibhavranga.shoppingapp.presentation.screens.home

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.vaibhavranga.shoppingapp.domain.model.CategoryModel
import com.vaibhavranga.shoppingapp.domain.model.ProductModel
import com.vaibhavranga.shoppingapp.presentation.components.CustomTextFieldWithLeadingIcon
import com.vaibhavranga.shoppingapp.presentation.viewModel.ViewModel
import com.vaibhavranga.shoppingapp.ui.theme.Gray
import com.vaibhavranga.shoppingapp.ui.theme.Pink
import com.vaibhavranga.shoppingapp.ui.theme.ShoppingAppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    onCategoryClick: (categoryName: String) -> Unit,
    onSeeMoreCategoriesClick: () -> Unit,
    onProductClick: (productId: String) -> Unit,
    onBrowseAllProductsClick: () -> Unit,
    viewModel: ViewModel = hiltViewModel()
) {
    val allCategories by viewModel.getAllCategoriesState.collectAsStateWithLifecycle()
    val flashSaleProducts by viewModel.getFlashSaleProductsState.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchProductState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var isShowingCategories by remember { mutableStateOf(false) }
    var isShowingProducts by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var isSheetShowing by remember { mutableStateOf(false) }
    val notificationsPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            android.Manifest.permission.POST_NOTIFICATIONS
        )
    } else {
        null
    }
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllCategories()
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.getFlashSaleProducts()
    }
    LaunchedEffect(key1 = Unit) {
        if (notificationsPermissionState?.status?.isGranted != true) {
            notificationsPermissionState?.launchPermissionRequest()
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                SearchBarRow(
                    value = searchQuery,
                    onSearchValueChange = { viewModel.updateSearchQuery(it) },
                    onNotificationsButtonClick = {
                        isSheetShowing = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                if (searchQuery.isNotEmpty() && searchResults.data?.isNotEmpty() == true) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(items = searchResults.data ?: emptyList()) { product ->
                            SearchResultProductItem(
                                product = product,
                                onProductClick = onProductClick
                            )
                        }
                    }
                } else {
                    CategoriesBlock(
                        isShowingCategories = isShowingCategories,
                        categories = allCategories.data ?: emptyList(),
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
                        products = flashSaleProducts.data ?: emptyList(),
                        onProductClick = onProductClick,
                        onBrowseAllProductsClick = onBrowseAllProductsClick,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
            if (isSheetShowing) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = {
                        isSheetShowing = false
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(text = "Notifications")
                    }
                }
            }
            when {
                allCategories.isLoading -> CircularProgressIndicator()
                allCategories.error != null -> {
                    Toast.makeText(context, allCategories.error.toString(), Toast.LENGTH_SHORT)
                        .show()
                    viewModel.clearGetAllCategoriesState()
                }

                allCategories.data != null -> {
                    isShowingCategories = true
                }
            }
            when {
                flashSaleProducts.isLoading -> CircularProgressIndicator()
                flashSaleProducts.error != null -> {
                    Toast.makeText(context, flashSaleProducts.error.toString(), Toast.LENGTH_SHORT).show()
                    viewModel.clearGetFlashSaleProductsState()
                }

                flashSaleProducts.data != null -> {
                    isShowingProducts = true
                }
            }
            when {
                searchResults.isLoading -> CircularProgressIndicator()
                searchResults.error != null -> {
                    Toast.makeText(context, searchResults.error, Toast.LENGTH_SHORT).show()
                    viewModel.clearSearchProductState()
                }
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
        CustomTextFieldWithLeadingIcon(
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
    onProductClick: (productId: String) -> Unit,
    onBrowseAllProductsClick: () -> Unit,
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
            TextButton(
                onClick = onBrowseAllProductsClick
            ) {
                Text(
                    text = "Browse all products",
                    color = Pink
                )
            }
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
                            .clickable {
                                onProductClick(product.productId)
                            }
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

@Composable
fun SearchResultProductItem(
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

@Preview(showSystemUi = true, device = Devices.PIXEL_7)
@Composable
private fun HomeScreenPreview() {
    ShoppingAppTheme {
        FlashSaleBlock(
            isShowingProducts = true,
            onProductClick = {},
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
            ),
            onBrowseAllProductsClick = {}
        )
    }
}