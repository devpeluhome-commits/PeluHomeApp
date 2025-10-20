package com.peluhome.project.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun MessageValidationComponent(
    modifier: Modifier = Modifier,
    field: String?
) {
    AnimatedVisibility(
        visible = field != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        TextComponentBasic(
            text = field.orEmpty(),
            style = TextStyle(fontSize = 14.sp),
            color = Color.Red,
            //modifier = modifier.padding(vertical = 4.dp),
        )
    }
}