package com.example.localartisan3.data.screens_view_models.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.localartisan3.data.rules.Validator
import com.example.localartisan3.navigation.LocalArtisansRouter
import com.example.localartisan3.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel : ViewModel() {

    private val TAG = LoginViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())

    var allValidationsPassed = mutableStateOf(false)

    var loginInProgress = mutableStateOf(false)

    var LoginOutMessage = mutableStateOf("")

    var LoginErrorMessage = mutableStateOf("")

    var emailValidationMessage = mutableStateOf("")

    var passwordValidationMessage = mutableStateOf("")


    fun onEvent(event: LoginUIEvent) {

        when (event) {
            is LoginUIEvent.EmailChanged -> {
                emailValidationMessage.value = validateEmail(event.email)
                loginUIState.value = loginUIState.value.copy(
                    email = event.email,
                    emailValidationError = emailValidationMessage.value
                )
            }
            is LoginUIEvent.PasswordChanged -> {
                passwordValidationMessage.value = validatePassword(event.password)
                loginUIState.value = loginUIState.value.copy(
                    password = event.password,
                    passwordValidationError = passwordValidationMessage.value
                )
            }
            is LoginUIEvent.ConfirmPasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    confirmPassword = event.confirmPassword
                )
            }
            is LoginUIEvent.LoginButtonClicked -> {
                login()
            }
            is LoginUIEvent.ArtisanLoginButtonClicked -> {
                artisanLogin()
            }

            is LoginUIEvent.UserRoleChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    userRole = "Customer"
                )
            }
            is LoginUIEvent.CustomerExists -> {
                loginUIState.value = loginUIState.value.copy(
                    customerExists = event.customerExists
                )
            }
            is LoginUIEvent.ResetPasswrodButtonClicked -> {
                sendPasswordResetEmail(loginUIState.value.email)
            }
        }
        validateLoginUIDataWithRules()

    }

    private fun validateLoginUIDataWithRules() {
        val emailResult = Validator.validateEmail(
            email = loginUIState.value.email
        )
        val passwordResult = Validator.validatePassword(
            password = loginUIState.value.password
        )
        loginUIState.value = loginUIState.value.copy(
            emailError = emailResult.status,

            passwordError = passwordResult.status
        )
        allValidationsPassed.value = emailResult.status && passwordResult.status

    }

    fun login() {
        loginInProgress.value = true
        val email = loginUIState.value.email
        val password = loginUIState.value.password
        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG, "Inside_login_success")
                Log.d(TAG, "${it.isSuccessful}")
                if (it.isSuccessful) {
                    loginInProgress.value = false
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Inside_login_failure")
                Log.d(TAG, "${it.localizedMessage}")
                LoginErrorMessage.value = "Invalied Email or Password"
                loginInProgress.value = false
            }
        checkCustomerLoginInFirestore()
    }

    fun getCustomerUID(): String {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            return uid
        }
        return ""
    }

    private fun artisanLogin() {
        loginInProgress.value = true
        val email = loginUIState.value.email
        val password = loginUIState.value.password

        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG, "Inside_login_success")
                Log.d(TAG, "${it.isSuccessful}")

                if (it.isSuccessful) {
                    loginInProgress.value = false

                    loginUIState.value = LoginUIState(
                        uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    )
                    checkArtisanLoginInFirestore()

                    checkIfArtisanLoginFirstTime()
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Inside_login_failure")
                Log.d(TAG, "${it.localizedMessage}")

                loginInProgress.value = false

            }

    }
    
    fun checkIfArtisanLoginFirstTime() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {
            val db = FirebaseFirestore.getInstance()

            db.collection("artisan").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists() && document.contains("firstTime")) {

                       var firstTimeUser = document.data?.get("firstTime") as Boolean
                        Log.d(TAG, "First Time User = $firstTimeUser")
                        loginUIState.value.FirstTimeArtisan = firstTimeUser
                        Log.d(TAG, "First Time User = ${loginUIState.value.FirstTimeArtisan}")
                        if (firstTimeUser){
                            LocalArtisansRouter.navigateTo(Screen.UpdateArtisansPersonalDetails)
                        }
                        else{
                            LocalArtisansRouter.navigateTo(Screen.ArtisanHomeScreen)
                        }

                    } else {
                        Log.d(TAG, "No such document or field")

                    }


                }.addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                    LoginErrorMessage.value = "Customer does not exist"
                }
        }


    }

    fun navigateFirstTimeArtisantoUpdateProfile(){
        val firstTimeArtisan = loginUIState.value.FirstTimeArtisan

        if (firstTimeArtisan) {
            //navigate to update profile screen
            LocalArtisansRouter.navigateTo(Screen.UpdateArtisansPersonalDetails)
        }
    }

    fun navigateToCustomerHomeScreen() {
        val useRole = loginUIState.value.userRole

        if (useRole == "Customer") {
            //navigate to customer screen
            LocalArtisansRouter.navigateTo(Screen.CustomerHomeDashboardScreen)
        }

    }


    fun showingLoginMessage(): String{
        if (loginUIState.value.customerExists) {
            return "Customer Exists"
        } else {
            return "Customer does not exist"
        }
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
        var emailValidationMessage = ""

        if (!email.contains(Regex(".*[@].*"))) {
            emailValidationMessage+="Email must contain @\n"
        }

        if (!email.contains(Regex(".*[.com].*"))
            || !email.contains(Regex(".*.ie"))
            || !email.contains(Regex(".*.org"))
            || !email.contains(Regex(".*.net"))
            ||  !email.contains(Regex(".*.gov"))
            || !email.contains(Regex(".*.edu"))
        ){
            emailValidationMessage+="Email must contain proper ending"
        }

        if (!email.contains(Regex(".*gmail.*"))
            || !email.contains(Regex(".*yahoo.*"))
            || !email.contains(Regex(".*hotmail.*"))
            || !email.contains(Regex(".*outlook.*"))
            || !email.contains(Regex(".*icloud.*"))
            || !email.contains(Regex(".*live.*"))) {
            emailValidationMessage+="Email must contain proper domain \n" +
                    "like google yahoo hotmail outlook icloud live.\n"
        }
        return emailValidationMessage
    }


    fun checkCustomerLoginInFirestore()  {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        Log.d(TAG, "UID = $uid")
        if (uid != null) {
            val db = FirebaseFirestore.getInstance()

            db.collection("customer").document().get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    //change value in customerExists to True
                        loginUIState.value = loginUIState.value.copy(
                            customerExists = true
                        )
                    } else {
                        Log.d(TAG, "No such Customer Found")
                        //change value in customerExists to False
                        loginUIState.value = loginUIState.value.copy(
                            customerExists = false
                        )
                    }

                }
        }

    }

    fun checkArtisanLoginInFirestore()  {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        Log.d(TAG, "UID = $uid")
        if (uid != null) {
            val db = FirebaseFirestore.getInstance()

            db.collection("artisan").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        //change value in customerExists to True
                        loginUIState.value = loginUIState.value.copy(
                            customerExists = true
                        )
                    } else {
                        Log.d(TAG, "No such Customer Found")
                        //change value in customerExists to False
                        loginUIState.value = loginUIState.value.copy(
                            customerExists = false
                        )
                    }

                }
        }


    }


    //function that taking the email address and send reset password link to such email

    fun sendPasswordResetEmail(email: String) : String {
        val auth = FirebaseAuth.getInstance()
        var message = ""

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                    message = "Email sent."
                }
                else{
                    Log.d(TAG, "Account not exists.")
                    message = "Account not exists."

                }
            }
        return message
    }


}





