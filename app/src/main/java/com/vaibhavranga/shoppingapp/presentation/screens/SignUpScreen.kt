package com.vaibhavranga.shoppingapp.presentation.screens

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vaibhavranga.shoppingapp.domain.model.UserDataModel
import com.vaibhavranga.shoppingapp.presentation.viewModel.ViewModel
import com.vaibhavranga.shoppingapp.ui.theme.ShoppingAppTheme

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: ViewModel = hiltViewModel()
) {
    val createUser = viewModel.createUserState.collectAsStateWithLifecycle()
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
        ) {
            Text(text = "Sign Up")
            OutlinedTextField(
                value = firstName.value,
                onValueChange = {
                    firstName.value = it
                },
                label = {
                    Text(text = "First name")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = lastName.value,
                onValueChange = {
                    lastName.value = it
                },
                label = {
                    Text(text = "Last name")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
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
            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = {
                    confirmPassword.value = it
                },
                label = {
                    Text(text = "Confirm password")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Button(
                onClick = {
                    if (firstName.value.isNotBlank()
                        && lastName.value.isNotBlank()
                        && email.value.isNotBlank()
                        && password.value.isNotBlank()
                        && confirmPassword.value.isNotBlank()
                    ) {
                        if (password.value == confirmPassword.value) {
                            val userData = UserDataModel(
                                firstName = firstName.value,
                                lastName = lastName.value,
                                email = email.value,
                                password = password.value
                            )
                            viewModel.createUser(userData = userData)
                        } else {
                            showToast(
                                context = context,
                                message = "Password and confirm password are different"
                            )
                        }
                    } else {
                        showToast(context = context, message = "Please enter all details")
                    }
                }
            ) {
                Text(text = "Sign Up")
            }
        }
        when {
            createUser.value.isLoading -> CircularProgressIndicator()
            createUser.value.error != null -> showToast(
                context = context,
                message = createUser.value.error.toString()
            )

            createUser.value.data != null -> showToast(
                context = context,
                message = createUser.value.data.toString()
            )
        }
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Preview(showSystemUi = true)
@Composable
private fun SignUpScreenPreview() {
    ShoppingAppTheme {
        SignUpScreen()
    }
}