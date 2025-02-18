package com.vaibhavranga.shoppingapp.presentation.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vaibhavranga.shoppingapp.R
import com.vaibhavranga.shoppingapp.presentation.common.CustomTextField
import com.vaibhavranga.shoppingapp.presentation.viewModel.ViewModel
import com.vaibhavranga.shoppingapp.ui.theme.Pink
import com.vaibhavranga.shoppingapp.ui.theme.ShoppingAppTheme

@Composable
fun SignInScreen(
    onSignUpButtonClick: () -> Unit,
    onSignInWithEmailAndPasswordSuccess: () -> Unit,
    viewModel: ViewModel = hiltViewModel()
) {
    val signInWithEmailAndPassword =
        viewModel.signInWithEmailAndPasswordState.collectAsStateWithLifecycle()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val verticalScrollState = rememberScrollState()

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
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
            MainSignIn(
                email = email,
                onEmailValueChange = {
                    email = it
                },
                password = password,
                onPasswordValueChange = {
                    password = it
                },
                onSignInButtonClick = {
                    viewModel.signInWithEmailAndPassword(
                        email = email.trim(),
                        password = password.trim()
                    )
                },
                onSignUpButtonClick = onSignUpButtonClick,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(verticalScrollState)
                    .padding(16.dp)
            )
            when {
                signInWithEmailAndPassword.value.isLoading -> CircularProgressIndicator()
                signInWithEmailAndPassword.value.error != null -> {
                    Toast.makeText(context, signInWithEmailAndPassword.value.error.toString(), Toast.LENGTH_SHORT).show()
                    viewModel.clearSignInWithEmailAndPasswordState()
                }

                signInWithEmailAndPassword.value.data != null -> {
                    onSignInWithEmailAndPasswordSuccess()
                }
            }
        }
    }
}

@Composable
fun MainSignIn(
    email: String,
    onEmailValueChange: (email: String) -> Unit,
    password: String,
    onPasswordValueChange: (password: String) -> Unit,
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
            text = "Login",
            style = MaterialTheme.typography.displaySmall
        )
        CustomTextField(
            value = email,
            onValueChange = onEmailValueChange,
            label = "Email",
            keyboardType = KeyboardType.Email,
            modifier = Modifier
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CustomTextField(
                value = password,
                onValueChange = onPasswordValueChange,
                label = "Password",
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
            )
            TextButton(
                onClick = {},
                modifier = Modifier
                    .align(alignment = Alignment.End)
            ) {
                Text(
                    text = "Forgot Password?",
                    color = Pink
                )
            }
        }
        Button(
            onClick = onSignInButtonClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Pink),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Login")
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Don't have an account?")
            TextButton(
                onClick = onSignUpButtonClick,
            ) {
                Text(
                    text = "Sign Up",
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
private fun SignInScreenPreview() {
    ShoppingAppTheme {
        MainSignIn(
            email = "email@gmail.com",
            onEmailValueChange = {},
            password = "password",
            onPasswordValueChange = {},
            onSignInButtonClick = {},
            onSignUpButtonClick = {}
        )
    }
}