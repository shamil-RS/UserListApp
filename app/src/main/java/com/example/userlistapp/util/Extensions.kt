package com.example.userlistapp.util

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.userlistapp.R
import java.math.BigInteger
import java.security.MessageDigest

@Composable
fun safeStringResource(@StringRes id: Int?): String? =
    if (id == null) null
    else stringResource(id = id)

@Composable
fun ClearTrailingButton(
    onClick: () -> Unit
) = IconButton(onClick = onClick) {
    Icon(
        imageVector = LaIcons.Clear,
        contentDescription = stringResource(id = R.string.clear)
    )
}

@Composable
fun ToggleTextVisibilityTrailingButton(
    onClick: () -> Unit,
    isVisible: Boolean
) = IconButton(onClick = onClick) {
    Icon(
        imageVector = if (isVisible) LaIcons.Visibility else LaIcons.VisibilityOff,
        contentDescription = stringResource(
            id = if (isVisible) R.string.show else R.string.hide
        )
    )
}