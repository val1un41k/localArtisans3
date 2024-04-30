package com.example.localartisan3.data.screens_view_models.login

data class LoginUIState (

    val email: String ="",
    val password: String = "",
    val confirmPassword: String = "",
    val userRole: String = "",

    val uid: String = "",

    var FirstTimeArtisan: Boolean = true,
    val customerExists: Boolean = false,
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val emailValidationError: String = "",
    val passwordValidationError: String = ""
)