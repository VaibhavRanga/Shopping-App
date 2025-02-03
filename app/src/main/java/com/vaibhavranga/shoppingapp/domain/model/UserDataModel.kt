package com.vaibhavranga.shoppingapp.domain.model

data class UserDataModel(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val profileImage: String = ""
)
