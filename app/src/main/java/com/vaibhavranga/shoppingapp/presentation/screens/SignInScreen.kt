package com.vaibhavranga.shoppingapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vaibhavranga.shoppingapp.presentation.viewModel.ViewModel

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: ViewModel = hiltViewModel()
) {
    val signInWithEmailAndPassword =
        viewModel.signInWithEmailAndPasswordState.collectAsStateWithLifecycle()
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(text = "Sign In")
            OutlinedTextField(
                value = email.value,
                onValueChange = {
                    email.value = it
                },
                label = {
                    Text(text = "Email")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = password.value,
                onValueChange = {
                    password.value = it
                },
                label = {
                    Text(text = "Password")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Button(
                onClick = {
                    if (
                        email.value.isNotBlank()
                        && password.value.isNotBlank()
                    ) {
                        viewModel.signInWithEmailAndPassword(
                            email = email.value,
                            password = password.value
                        )
                    } else {
                        showToast(
                            context = context,
                            message = "Please enter all values"
                        )
                    }
                }
            ) {
                Text(text = "Sign In")
            }
        }
        when {
            signInWithEmailAndPassword.value.isLoading -> CircularProgressIndicator()
            signInWithEmailAndPassword.value.error != null -> showToast(
                context = context,
                message = signInWithEmailAndPassword.value.error.toString()
            )

            signInWithEmailAndPassword.value.data != null -> showToast(
                context = context,
                message = signInWithEmailAndPassword.value.data.toString()
            )
        }
    }
}