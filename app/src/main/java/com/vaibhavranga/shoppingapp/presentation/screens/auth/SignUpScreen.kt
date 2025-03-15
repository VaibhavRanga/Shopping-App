package com.vaibhavranga.shoppingapp.presentation.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vaibhavranga.shoppingapp.R
import com.vaibhavranga.shoppingapp.domain.model.UserDataModel
import com.vaibhavranga.shoppingapp.presentation.components.CustomTextField
import com.vaibhavranga.shoppingapp.presentation.viewModel.ViewModel
import com.vaibhavranga.shoppingapp.ui.theme.Pink
import com.vaibhavranga.shoppingapp.ui.theme.ShoppingAppTheme

@Composable
fun SignUpScreen(
    onSignUpSuccessful: () -> Unit,
    onSignInButtonClick: () -> Unit,
    viewModel: ViewModel = hiltViewModel()
) {
    val createUser = viewModel.createUserState.collectAsStateWithLifecycle()
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val context = LocalContext.current
    val verticalScrollState = rememberScrollState()

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .align(alignment = Alignment.TopEnd)
            ) {
                drawCircle(
                    color = Pink,
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
                    color = Pink,
                    radius = size.width,
                    center = Offset(
                        x = size.width.times(0.2f),
                        y = size.height.times(1.4f)
                    )
                )
            }
            MainSignUp(
                firstName = firstName,
                onFirstNameValueChange = {
                    firstName = it
                },
                lastName = lastName,
                onLastNameValueChange = {
                    lastName = it
                },
                email = email,
                onEmailValueChange = {
                    email = it
                },
                password = password,
                onPasswordValueChange = {
                    password = it
                },
                confirmPassword = confirmPassword,
                onConfirmPasswordChange = {
                    confirmPassword = it
                },
                onSignInButtonClick = onSignInButtonClick,
                onSignUpButtonClick = {
                    if (firstName.isNotBlank()
                        && lastName.isNotBlank()
                        && email.isNotBlank()
                        && password.isNotBlank()
                        && confirmPassword.isNotBlank()
                    ) {
                        if (password.trim() == confirmPassword.trim()) {
                            val userData = UserDataModel(
                                firstName = firstName.trim(),
                                lastName = lastName.trim(),
                                email = email.trim(),
                                password = password.trim()
                            )
                            viewModel.createUser(userData = userData)
                        } else {
                            Toast.makeText(context, "Password and confirm password are different", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Please enter all details", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(verticalScrollState)
                    .padding(16.dp)
            )
            when {
                createUser.value.isLoading -> CircularProgressIndicator()
                createUser.value.error != null -> {
                    Toast.makeText(context, createUser.value.error.toString(), Toast.LENGTH_SHORT)
                        .show()
                    viewModel.clearCreateUserState()
                }

                createUser.value.data != null -> {
                    onSignUpSuccessful()
                }
            }
        }
    }
}

@Composable
fun MainSignUp(
    firstName: String,
    onFirstNameValueChange: (firstName: String) -> Unit,
    lastName: String,
    onLastNameValueChange: (lastName: String) -> Unit,
    password: String,
    onPasswordValueChange: (password: String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (confirmPassword: String) -> Unit,
    email: String,
    onEmailValueChange: (email: String) -> Unit,
    onSignInButtonClick: () -> Unit,
    onSignUpButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 24.dp,
            alignment = Alignment.CenterVertically
        ),
        modifier = modifier
    ) {
        Text(
            text = "Sign Up",
            style = MaterialTheme.typography.displaySmall
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CustomTextField(
                value = firstName,
                onValueChange = onFirstNameValueChange,
                label = "First Name",
                modifier = Modifier
                    .weight(1f)
            )
            CustomTextField(
                value = lastName,
                onValueChange = onLastNameValueChange,
                label = "Last Name",
                modifier = Modifier
                    .weight(1f)
            )
        }
        CustomTextField(
            value = email,
            onValueChange = onEmailValueChange,
            label = "Email",
            keyboardType = KeyboardType.Email,
            modifier = Modifier
                .fillMaxWidth()
        )
        CustomTextField(
            value = password,
            onValueChange = onPasswordValueChange,
            label = "Create Password",
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
        )
        CustomTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Confirm Password",
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
        )
        Button(
            onClick = onSignUpButtonClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Pink),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Sign Up")
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Already have an account?")
            TextButton(
                onClick = onSignInButtonClick,
            ) {
                Text(
                    text = "Login",
                    color = Pink
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            HorizontalDivider(
                color = Pink,
                modifier = Modifier
                    .weight(1f)
            )
            Text(text = "OR")
            HorizontalDivider(
                color = Pink,
                modifier = Modifier
                    .weight(1f)
            )
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .border(
                    width = 1.dp,
                    color = Pink,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 16.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.google_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                )
                Text(text = "Login with Google")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SignUpScreenPreview() {
    ShoppingAppTheme {
        MainSignUp(
            email = "email@gmail.com",
            onEmailValueChange = {},
            password = "password",
            onPasswordValueChange = {},
            onSignInButtonClick = {},
            onSignUpButtonClick = {},
            firstName = "",
            onFirstNameValueChange = {},
            lastName = "",
            onLastNameValueChange = {},
            confirmPassword = "pass",
            onConfirmPasswordChange = {}
        )
    }
}