package com.peluhome.project

import androidx.compose.ui.window.ComposeUIViewController
import com.peluhome.project.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) { App() }