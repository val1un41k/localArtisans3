package com.example.localartisan3.data.screens_view_models.cusotmerScreensViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.localartisan3.data.rules.Validator
import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.ArtisansHomeScreen.ArtisanProductRecordCreateUIstate
import com.example.localartisan3.data.screens_view_models.login.LoginUIEvent
import com.example.localartisan3.data.screens_view_models.login.LoginUIState
import com.example.localartisan3.data.screens_view_models.users_data.ArtisanUIState
import com.example.localartisan3.data.screens_view_models.users_data.Customer
import com.example.localartisan3.navigation.LocalArtisansRouter
import com.example.localartisan3.navigation.Screen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomerMainViewModel : ViewModel(){

    private val TAG =CustomerMainViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())

    val customerUiState = mutableStateOf(Customer())

    var allValidationsPassed = mutableStateOf(false)

    var loginInProgress = mutableStateOf(false)

    var LoginOutMessage = mutableStateOf("")

    var LoginErrorMessage = mutableStateOf("")

    var emailValidationMessage = mutableStateOf("")

    var passwordValidationMessage = mutableStateOf("")

    var artisanList = mutableStateOf(ArrayList<ArtisanUIState>())

    val selectedArtisan = mutableStateOf(ArtisanUIState())

    val selectedProduct = mutableStateOf(ArtisanProductRecordCreateUIstate())

    var customerLocation = null


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
                    GlobalScope.launch {
                        toDelay(time = 3000)
                        withContext(Dispatchers.Main) {

                        }
                    }

            }
            is LoginUIEvent.ArtisanLoginButtonClicked -> {
             "   artisanLogin()"
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
            }.addOnCompleteListener { checkCustomerLoginInFirestore()  }

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

    fun checkCustomerLoginInFirestore()  {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        Log.d(TAG, "UID = $uid")
        if (uid != null) {
            val db = FirebaseFirestore.getInstance()

            db.collection("customer").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        //change value in customerExists to True
                        loginUIState.value = loginUIState.value.copy(
                            customerExists = true
                        )
                        //populate the Customer with data taken from Firebase

                        customerUiState.value.email = document.getString("email").toString()
                        customerUiState.value.fname = document.getString("firstName").toString()
                        customerUiState.value.lname = document.getString("lastName").toString()
                        customerUiState.value.pnum = document.getString("phoneNumber").toString()

                        Log.d(TAG, "Customer Data: ${customerUiState.value}")
                        //populate list of Artisans

                        Log.d(TAG, "Artisan List: ${artisanList.value}")

                        //navigate to Customer Home Screen
                //        LocalArtisansRouter.navigateTo(Screen.CustomerHomeDashboardScreen)

                        LoginErrorMessage.value = "Customer Exists"


                    } else {
                        Log.d(TAG, "No such Customer Found")
                        //change value in customerExists to False
                        loginUIState.value = loginUIState.value.copy(
                            customerExists = false
                        )
                        LoginErrorMessage.value = "Customer does not exist"
                    }

                }.addOnCompleteListener { populateArtisans(db) }
        }

    }


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

    fun populateArtisans(db: FirebaseFirestore,){
        db.collection("artisan")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val artisan = document.toObject(ArtisanUIState::class.java)
                    artisanList.value.add(artisan)

                    }
                artisanList.value.removeIf {it.categories.isNullOrEmpty()
                }
                Log.d(TAG, "Artisan List: ${artisanList.value}")
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
            .addOnCompleteListener { LocalArtisansRouter.navigateTo(Screen.CustomerHomeDashboardScreen) }

    }
    //////////////////////////////////////////////////////////////////////////////////////////
    //
    ////
    //////Code related to the Google Maps Artisans Location
    //
    ////
    //////////////////////////////////////////////////////////////////////////////////////////


}

suspend fun toDelay(time: Long) {
    delay(time)
}