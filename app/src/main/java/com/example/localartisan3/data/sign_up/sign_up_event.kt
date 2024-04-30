package com.example.localartisan3.data.sign_up

sealed class SignUpUIEvent {

    data class FirstNameChanged(val firstName:String) : SignUpUIEvent()
    data class LastNameChanged(val lastName:String) : SignUpUIEvent()
    data class EmailChanged(val email:String): SignUpUIEvent()
    data class PasswordChanged(val password: String) : SignUpUIEvent()
    data class ConfirmPasswordChanged ( val confirmPassword: String ) : SignUpUIEvent()
    data class PhoneNumberChanged (val phoneNumber: String) : SignUpUIEvent()


    class UserRoleChanged(val userRole: String) : SignUpUIEvent()

    object RegisterButtonClicked : SignUpUIEvent()
}