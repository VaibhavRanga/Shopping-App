package com.vaibhavranga.shoppingapp.presentation.screens.home

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vaibhavranga.shoppingapp.presentation.viewModel.ViewModel

@Composable
fun ProductScreen(
    productId: String,
    viewModel: ViewModel = hiltViewModel()
) {
    val productState by viewModel.getProductByIdState.collectAsStateWithLifecycle()
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (isShowingProduct) {
                Text(text = productState.data?.name ?: "")
            }
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
    }
}