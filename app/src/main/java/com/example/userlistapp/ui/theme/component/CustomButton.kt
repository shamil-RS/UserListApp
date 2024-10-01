package com.example.userlistapp.ui.theme.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) = Box(
    modifier = modifier
        .width(160.dp)
        .height(50.dp)
        .clip(RoundedCornerShape(2.dp))
        .border(2.dp, Color.Black)
        .background(Color.Transparent)
        .clickable { onClick() },
    contentAlignment = Alignment.Center
) {
    Text(text, fontWeight = FontWeight.Bold)
}

@Composable
fun CustomTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) = TextButton(
    modifier = modifier,
    onClick = onClick,
    enabled = enabled
) {
    Text(text = text, fontWeight = FontWeight.Bold, color = Color.Black)
}