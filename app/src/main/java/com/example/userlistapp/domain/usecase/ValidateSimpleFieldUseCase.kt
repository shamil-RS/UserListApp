package com.example.userlistapp.domain.usecase

import com.example.userlistapp.domain.type.InputError
import com.example.userlistapp.domain.type.InputResult
import javax.inject.Inject

interface ValidateSimpleFieldUseCase {
    operator fun invoke(
        string: String,
        minChar: Int? = null,
        maxChar: Int? = null
    ): InputResult
}

class ValidateSimpleFieldUseCaseImpl @Inject constructor() : ValidateSimpleFieldUseCase {
    override fun invoke(string: String, minChar: Int?, maxChar: Int?): InputResult {
        if (string.isBlank()) return InputResult.Error(
            inputError = InputError.FieldEmpty
        )

        if (minChar != null && string.length < minChar) return InputResult.Error(
            inputError = InputError.FieldLessMinCharacters
        )

        if (maxChar != null && string.length > maxChar) return InputResult.Error(
            inputError = InputError.FieldMoreMaxCharacters
        )

        return InputResult.Success
    }
}