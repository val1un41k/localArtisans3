package com.example.localartisan3.data

data class RegistrationUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val phoneNumber: String = "",
    val userRole: String = "",
    val passwordValidationError: String = "",
    val emailValidationError: String = "",
    val phoneNymberValidationError: String = "",
    val confirmPasswordValidationError: String = "",

    val firstNameError: Boolean = false,
    val lastNameError: Boolean = false,
    val emailError: Boolean = false,
    val confirmPasswordError: Boolean = false,
    val passwordError: Boolean = false,
    val phoneNumberError: Boolean = false
)