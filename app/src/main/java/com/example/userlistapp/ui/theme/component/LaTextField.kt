package com.example.userlistapp.ui.theme.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CustomOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholderString: String? = null,
    description: String? = null,
    errorMessage: String? = null,
    enableVisualError: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enable: Boolean = true,
    readOnly: Boolean = false,
    enableWhiteSpace: Boolean = true,
    singleLine: Boolean = true,
    visualExpandLines: Int = 1,
    hideText: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) = Column(modifier = modifier) {
    val visualTransformation =
        if (hideText) PasswordVisualTransformation() else VisualTransformation.None

    description?.let {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = it,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(height = 2.dp))
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .border(2.dp, Color.Black)) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = { value ->
                if (enableWhiteSpace) onValueChange(value.clearDoubleWhitespace())
                else onValueChange(value.clearAllWhitespace())
            },
            colors = OutlinedTextFieldDefaults.colors().copy(
                focusedTextColor = Color.Black,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                )
            ,
            placeholder = if (placeholderString == null) null else {
                { Text(text = placeholderString) }
            },
            isError = errorMessage != null && enableVisualError,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            enabled = enable,
            readOnly = readOnly,
            maxLines = visualExpandLines,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
    }

    errorMessage?.let {
        Spacer(modifier = Modifier.height(height = 2.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = it,
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

private fun String.clearDoubleWhitespace(): String {
    val regex = "\\s{2,}".toRegex()
    return this.replace(regex = regex, replacement = " ")
}

private fun String.clearAllWhitespace(): String {
    return this.filterNot { it.isWhitespace() }
}