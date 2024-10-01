package com.example.userlistapp.domain.model

data class User(
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val profilePicture: String?,
    val birthDate: String?
)