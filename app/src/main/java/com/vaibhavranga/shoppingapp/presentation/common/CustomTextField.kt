package com.vaibhavranga.shoppingapp.presentation.common

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vaibhavranga.shoppingapp.ui.theme.Gray
import com.vaibhavranga.shoppingapp.ui.theme.Pink
import com.vaibhavranga.shoppingapp.ui.theme.ShoppingAppTheme

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector?,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(text = placeholder, color = Gray) },
        leadingIcon = {
            if (leadingIcon != null) {
                Icon(imageVector = leadingIcon, contentDescription = null)
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedLeadingIconColor = Gray,
            focusedLeadingIconColor = Gray,
            unfocusedTextColor = Gray,
            focusedTextColor = Gray,
            cursorColor = Pink
        ),
        modifier = modifier
            .border(
                width = 2.dp,
                color = Pink,
                shape = RoundedCornerShape(16.dp)
            )
    )
}

@Preview(showSystemUi = true, device = Devices.PIXEL_7)
@Composable
private fun CustomTextFieldPreview() {
    ShoppingAppTheme {
        CustomTextField(
            value = "Hello",
            onValueChange = {},
            placeholder = "Name",
            leadingIcon = Icons.Default.Search
        )
    }
}