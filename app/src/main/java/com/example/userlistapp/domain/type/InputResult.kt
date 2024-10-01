package com.example.userlistapp.domain.type

sealed interface InputResult {
    data object Success: InputResult
    data class Error(val inputError: InputError): InputResult
}