package com.example.localartisan3.data.sign_up

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.localartisan3.data.RegistrationUiState
import com.example.localartisan3.data.rules.Validator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpViewModel : ViewModel(
){

    private val TAG = SignUpViewModel::class.simpleName

    val registrationUIState = mutableStateOf(RegistrationUiState())
    val allValidationsPassed = mutableStateOf(false)
    val signUpInProgress = mutableStateOf(false)

    var emailValidationMessage = mutableStateOf(String())

    fun onEvent(event: SignUpUIEvent){

        when(event){
            is SignUpUIEvent.FirstNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    firstName = event.firstName
                )
                printState()
            }
            is SignUpUIEvent.LastNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    lastName = event.lastName
                )
                printState()
            }
            is SignUpUIEvent.EmailChanged -> {
                emailValidationMessage.value = validateEmail(event.email)
                registrationUIState.value = registrationUIState.value.copy(
                    email = event.email,
                    emailValidationError = emailValidationMessage.value
                )
                printState()

            }
            is SignUpUIEvent.PasswordChanged -> {
                val passwordValidationMessage = validatePassword(event.password)
                registrationUIState.value = registrationUIState.value.copy(
                    password = event.password,
                    passwordValidationError = passwordValidationMessage
                )
                printState()

            }

            is SignUpUIEvent.PhoneNumberChanged -> {
                val phoneNymberValidationMessage = checkPhoneNumberValidation(event.phoneNumber)
                registrationUIState.value = registrationUIState.value.copy(
                    phoneNumber = event.phoneNumber,
                    phoneNymberValidationError = phoneNymberValidationMessage
                )
                printState()

            }
            is SignUpUIEvent.ConfirmPasswordChanged -> {
                val confirmPasswordValidationMessage = validateConfirmPasswordWithMessage(event.confirmPassword)
                registrationUIState.value = registrationUIState.value.copy(
                    confirmPassword = event.confirmPassword,
                            confirmPasswordValidationError = confirmPasswordValidationMessage
                )
                printState()
            }
            is SignUpUIEvent.UserRoleChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    userRole = "Customer"
                )
                printState()
            }
            is SignUpUIEvent.RegisterButtonClicked -> {
                signUp()
            }
            }
        validateDataWithRules()

    }

    private fun signUp() {
        Log.d(TAG, "Inside_signUp")
        printState()
        createUserInFirebase(
            email = registrationUIState.value.email,
            password = registrationUIState.value.password
        )


    }

    private fun validateDataWithRules() {
        val fNameResult = Validator.validateFirstName(
            fName = registrationUIState.value.firstName
        )

        val lNameResult = Validator.validateLastName(
            lName = registrationUIState.value.lastName
        )

        val emailResult = Validator.validateEmail(
            email = registrationUIState.value.email
        )


        val passwordResult = Validator.validatePassword(
            password = registrationUIState.value.password
        )

        val phoneNumberResult = Validator.validatePhoneNumber(
            phoneNumber = registrationUIState.value.phoneNumber
        )

        val confirmPasswordResult = Validator.validateConfirmPassword(
            password = registrationUIState.value.password,
            confirmPassword = registrationUIState.value.confirmPassword
        )


        Log.d(TAG, "fNameResult= $fNameResult")
        Log.d(TAG, "lNameResult= $lNameResult")
        Log.d(TAG, "emailResult= $emailResult")
        Log.d(TAG, "passwordResult= $passwordResult")
        Log.d(TAG, "phoneNumberResult= $phoneNumberResult")
        Log.d(TAG, "confirmPasswordResult= $confirmPasswordResult")

        registrationUIState.value = registrationUIState.value.copy(
            firstNameError = fNameResult.status,
            lastNameError = lNameResult.status,
            emailError = emailResult.status,
            passwordError = passwordResult.status,
            phoneNumberError = phoneNumberResult.status,
            confirmPasswordError = confirmPasswordResult.status,

        )


        allValidationsPassed.value = fNameResult.status && lNameResult.status &&
                emailResult.status && passwordResult.status && phoneNumberResult.status
                && confirmPasswordResult.status

    }

    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, registrationUIState.value.toString())
    }

    private fun createUserInFirebase(email: String, password: String) {

        signUpInProgress.value = true

        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG, "Inside_OnCompleteListener")
                Log.d(TAG, " isSuccessful = ${it.isSuccessful}")
                signUpInProgress.value = false

                if (it.isSuccessful) {
                    createCustomerUserInFirestore()
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Inside_OnFailureListener")
                Log.d(TAG, "Exception= ${it.message}")
                Log.d(TAG, "Exception= ${it.localizedMessage}")
            }
    }


    private fun createCustomerUserInFirestore() {

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val db = FirebaseFirestore.getInstance()

        if (uid != null) {

            val customer = hashMapOf(
                "firstName" to registrationUIState.value.firstName,
                "lastName" to registrationUIState.value.lastName,
                "email" to registrationUIState.value.email,
                "phoneNumber" to registrationUIState.value.phoneNumber,
                "userRole" to "Customer",
                "userID" to uid,
            )

            db.collection("customer")
                .document(uid)
                .set(customer)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error writing document", e)
                }
        }



    }

    fun validateConfirmPasswordWithMessage( confirmPassword: String): String {
        val password = registrationUIState.value.password
        val confirmPasswordValidationMessage = StringBuilder()

        if (confirmPassword != password) {
            confirmPasswordValidationMessage.append("Passwords do not match,\n" +
                    " Please Try Again")
        }

        return confirmPasswordValidationMessage.toString()
    }

    fun validatePassword(password: String): String {
        val passwordValidationMessage = StringBuilder()

        if (password.length < 8) {
            passwordValidationMessage.append("Password must be at least 8 characters long\n")
        }

        if (!password.contains(Regex(".*[0-9].*"))) {
            passwordValidationMessage.append("Password must contain at least one number\n")
        }

        if (!password.contains(Regex(".*[a-z].*"))) {
            passwordValidationMessage.append("Password must contain at least one lowercase letter\n")
        }

        if (!password.contains(Regex(".*[A-Z].*"))) {
            passwordValidationMessage.append("Password must contain at least one uppercase letter\n")
        }

        if (!password.contains(Regex(".*[!@#\$%^&*].*"))) {
            passwordValidationMessage.append("Password must contain at least one special character\n")
        }

        return passwordValidationMessage.toString()
    }

    fun validateEmail(email: String): String {
        val emailValidationMessage = StringBuilder()


        if (!email.contains(Regex(".*[@].*"))) {
            emailValidationMessage.append("Email must contain @\n")
        }

        if (!email.contains(".com")
            || !email.contains(".ie")
            || !email.contains(".org")
            || !email.contains(".net")
            ||  !email.contains(".gov")
            || !email.contains(".edu")
            ){
            emailValidationMessage.append("Email must contain proper ending")
        }

        if (!email.contains("gmail")
            || !email.contains("yahoo")
            || !email.contains("hotmail")
            || !email.contains("outlook")
            || !email.contains("icloud")
            || !email.contains("live")) {
            emailValidationMessage.append("Email must contain proper domain \n" +
                    "like google yahoo hotmail outlook icloud live.\n")
        }
        return emailValidationMessage.toString()
    }

    fun checkPhoneNumberValidation (phoneNumber: String) : String{
        var message = ""

        if (!Validator.validatePhoneNumber(phoneNumber).status){
            message = "Phone number must be 10 digits long and start with 085, 083, 086, 087 or 089"
        }

    return message
    }




}