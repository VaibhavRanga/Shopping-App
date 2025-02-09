package com.vaibhavranga.shoppingapp.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vaibhavranga.shoppingapp.presentation.viewModel.ViewModel

@Composable
fun SignInScreen(
    onSignUpButtonClick: () -> Unit,
    onSignInWithEmailAndPasswordSuccess: () -> Unit,
    viewModel: ViewModel = hiltViewModel()
) {
    val signInWithEmailAndPassword =
        viewModel.signInWithEmailAndPasswordState.collectAsStateWithLifecycle()
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Canvas(
            modifier = Modifier
                .size(200.dp)
                .align(alignment = Alignment.TopEnd)
        ) {
            drawCircle(
                color = Color(red = 232, green = 144, blue = 142),
                radius = size.width,
                center = Offset(
                    x = size.width.times(0.6f),
                    y = size.height.times(-0.5f)
                )
            )
        }
        Canvas(
            modifier = Modifier
                .size(100.dp)
                .align(alignment = Alignment.BottomStart)
        ) {
            drawCircle(
                color = Color(red = 232, green = 144, blue = 142),
                radius = size.width,
                center = Offset(
                    x = size.width.times(0.2f),
                    y = size.height.times(1.4f)
                )
            )
        }
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
            TextButton(
                onClick = onSignUpButtonClick
            ) {
                Text(text = "Sign Up")
            }
        }
        when {
            signInWithEmailAndPassword.value.isLoading -> CircularProgressIndicator()
            signInWithEmailAndPassword.value.error != null -> {
                showToast(
                    context = context,
                    message = signInWithEmailAndPassword.value.error.toString()
                )
                viewModel.clearSignInWithEmailAndPasswordState()
            }

            signInWithEmailAndPassword.value.data != null -> {
                showToast(
                    context = context,
                    message = signInWithEmailAndPassword.value.data.toString()
                )
                onSignInWithEmailAndPasswordSuccess()
            }
        }
    }
}