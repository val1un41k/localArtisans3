package com.example.localartisan3.data.screens_view_models.login

sealed class LoginUIEvent {
    data class EmailChanged(val email: String) : LoginUIEvent()

    data class PasswordChanged(val password: String) : LoginUIEvent()

    data class ConfirmPasswordChanged(val confirmPassword: String) : LoginUIEvent()



    data class UserRoleChanged(val userRole: String) : LoginUIEvent()

    data class CustomerExists(val customerExists: Boolean) : LoginUIEvent()

    object LoginButtonClicked : LoginUIEvent()

    object ArtisanLoginButtonClicked : LoginUIEvent()

    object ResetPasswrodButtonClicked : LoginUIEvent()
}