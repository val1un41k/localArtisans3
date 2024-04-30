package com.example.localartisan3.data.rules

object Validator {

    fun validateFirstName(fName: String): ValidationResult {
        return ValidationResult(
            (!fName.isNullOrEmpty() && fName.length >= 2)
        )

    }

    fun validateLastName(lName: String): ValidationResult {
        return ValidationResult(
            (!lName.isNullOrEmpty() && lName.length >= 2)
        )
    }

    fun validateEmail(email: String): ValidationResult {
        return ValidationResult(
            (!email.isNullOrEmpty()
                    && email.contains(Regex(".*@.*"))
                    && (
                    email.contains(Regex(".*.com.*"))
                            || email.contains(Regex(".*.ie"))
                            || email.contains(Regex(".*.org"))
                            || email.contains(Regex(".*.net"))
                            ||  email.contains(Regex(".*.gov"))
                            || email.contains(Regex(".*.edu"))
                            )
                    &&(email.contains(Regex(".*gmail.*"))
                    || email.contains(Regex(".*yahoo.*"))
                    || email.contains(Regex(".*hotmail.*"))
                    || email.contains(Regex(".*outlook.*"))
                    || email.contains(Regex(".*icloud.*"))
                    || email.contains(Regex(".*live.*"))
                            )
        )



        )
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return ValidationResult(
            (!confirmPassword.isNullOrEmpty()
                    && confirmPassword == password
        ))
    }


    fun validatePassword(password: String): ValidationResult {
        return ValidationResult(
            (!password.isNullOrEmpty()
                    && (password.length >= 8)
                    && password.contains(Regex(".*[0-9].*"))
                    && password.contains(Regex(".*[a-z].*"))
                    && password.contains(Regex(".*[A-Z].*"))
                    && password.contains(Regex(".*[$!@#%^&*/()_+{}:;?.].*"))
        ))
    }

    fun validatePhoneNumber(phoneNumber: String): ValidationResult {
        return ValidationResult(
            (!phoneNumber.isNullOrEmpty()
                    && phoneNumber.length == 10
                    && (phoneNumber.contains(Regex("085.*"))
                            || phoneNumber.contains(Regex("083.*"))
                            || phoneNumber.contains(Regex("086.*"))
                    || phoneNumber.contains(Regex("087.*"))
                    || phoneNumber.contains(Regex("089.*"))
        ))
        )
    }




}

data class ValidationResult(
    val status: Boolean = false
)