package com.peluhome.project.presentation.sign_in

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.peluhome.project.presentation.components.AlertComponent
import com.peluhome.project.presentation.sign_in.components.SignUpFooter
import com.peluhome.project.presentation.sign_in.components.SignUpForm
import com.peluhome.project.presentation.sign_in.components.SignUpHeader
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import peluhome.composeapp.generated.resources.Res
import peluhome.composeapp.generated.resources.background
import peluhome.composeapp.generated.resources.login_top_image

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = koinViewModel(),
    onNavigationNewUser:()->Unit,
    onNavigationHome:()->Unit
) {
    val state = viewModel.state

    var documentNumber by remember { mutableStateOf("46079567") }
    var password by remember { mutableStateOf("123456789") }
    var passwordVisible by remember { mutableStateOf(true) }

    var showDialogWarningOrError by remember { mutableStateOf(false) }
    var showDialogSuccess by remember { mutableStateOf(false) }
    var messageWarningOrError by remember { mutableStateOf("") }
    var messageSuccess by remember { mutableStateOf("") }

    LaunchedEffect(key1 = state.success, key2 = state.error) {
        if (state.success != null) {
            viewModel.clear()
            onNavigationHome()
        }
        if (state.error != null) {
            showDialogWarningOrError = true
            messageWarningOrError = state.error
        }
    }

    if (showDialogWarningOrError) {
        AlertComponent(
            title = "INFORMATIVO",
            dismiss = {
                viewModel.clear()
                showDialogWarningOrError = false
            },
            action = {
                viewModel.clear()
                showDialogWarningOrError = false
            },
            message = messageWarningOrError,
            textAlign = TextAlign.Center,
            paddingValues = PaddingValues(16.dp)
        )
    }

    if (showDialogSuccess) {
        AlertComponent(
            title = "INFORMATIVO",
            dismiss = {
                viewModel.clear()
                showDialogSuccess = false
            },
            action = {
                viewModel.clear()
                showDialogSuccess = false
            },
            message = messageSuccess,
            textAlign = TextAlign.Center,
            paddingValues = PaddingValues(16.dp)
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent,

        ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(Res.drawable.background), // usa tu imagen de fondo aquí
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // o FillBounds si quieres que se estire
            )

            /*Image(
                modifier = Modifier.align(Alignment.TopEnd).width(200.dp),
                painter = painterResource(Res.drawable.login_top_image),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )*/

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                SignUpHeader()

                Spacer(modifier = Modifier.height(1.dp))

                SignUpForm(
                    user = documentNumber,
                    password = password,
                    visualTransformation = passwordVisible,
                    onSignIn = {
                        val errors = mutableListOf<String>()

                        if (documentNumber.isEmpty()) errors.add("* El campo número de documento es requerido")
                        if (password.isEmpty()) errors.add("* El campo contraseña es requerido")

                        if (errors.isNotEmpty()) {
                            showDialogWarningOrError = true
                            messageWarningOrError = errors.joinToString(separator = "\n")
                            return@SignUpForm
                        }

                        viewModel.signIn(documentNumber, password)
                    },
                    onChangeDocument = {
                        documentNumber = it
                    },
                    onChangePassword = {
                        password = it
                    },
                    onChangePasswordVisibility = {
                        passwordVisible = it
                    },
                    isLoading = state.isLoading
                )

                Spacer(modifier = Modifier.height(24.dp))

                SignUpFooter{
                    onNavigationNewUser()
                }


            }
        }
    }
}