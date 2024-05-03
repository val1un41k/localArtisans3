package com.example.local_artisan.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.local_artisan.screens.screens_all.customer.CategoriesWithProducts
import com.example.local_artisan.screens.screens_all.customer.LoadedProductFromDB
import com.example.localartisan3.R
import com.example.localartisan3.data.NavigationItem
import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.ArtisansHomeScreen.ProductCategory
import com.example.localartisan3.navigation.LocalArtisansRouter
import com.example.localartisan3.navigation.Screen
import com.example.localartisan3.ui.theme.AccentColor
import com.example.localartisan3.ui.theme.GrayColor
import com.example.localartisan3.ui.theme.Primary
import com.example.localartisan3.ui.theme.Secondary
import com.example.localartisan3.ui.theme.TextColor
import com.example.localartisan3.ui.theme.WhiteColor
import com.example.localartisan3.ui.theme.componentShapes
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayOnlyTextField(labelValue: String, textValue: String) {
    TextField(
        modifier = Modifier
            .clip(componentShapes.small)
            .fillMaxWidth(),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Secondary,
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
            cursorColor = Primary,
        ),
        keyboardOptions = KeyboardOptions.Default,
        value = textValue,
        onValueChange = {},
        enabled = false
    )
}

@Composable
fun HeadingTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ), color = TextColor,
        textAlign = TextAlign.Center
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextFieldComponent(
    labelValue: String, painterResource: Painter,
    onTextChanged: (String) -> Unit,
    errorStatus: Boolean = false
) {

    val textValue = remember {
        mutableStateOf("")
    }
    val localFocusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(componentShapes.small),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
            cursorColor = Primary
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            onTextChanged(it)
        },
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "")
        },
        isError = !errorStatus
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextFieldComponent(
    labelValue: String, painterResource: Painter,
    onTextSelected: (String) -> Unit,
    errorStatus: Boolean = false
) {

    val localFocusManager = LocalFocusManager.current
    val password = remember {
        mutableStateOf("")
    }

    val passwordVisible = remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(componentShapes.small),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
            cursorColor = Primary,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        maxLines = 1,
        value = password.value,
        onValueChange = {
            password.value = it
            onTextSelected(it)
        },
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "")
        },
        trailingIcon = {

            val iconImage = if (passwordVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }

            val description = if (passwordVisible.value) {
                stringResource(id = R.string.hide_password)
            } else {
                stringResource(id = R.string.show_password)
            }

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }

        },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        isError = !errorStatus
    )
}

@Composable
fun CheckboxComponent(
    value: String,
    onTextSelected: (String) -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        val checkedState = remember {
            mutableStateOf(false)
        }

        Checkbox(checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = !checkedState.value
                onCheckedChange.invoke(it)
            })

        ClickableTextComponent(value = value, onTextSelected)
    }
}

@Composable
fun ClickableTextComponent(value: String, onTextSelected: (String) -> Unit) {
    val initialText = "By continuing you accept our "
    val privacyPolicyText = "Privacy Policy"
    val andText = " and "
    val termsAndConditionsText = "Term of Use"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = Primary)) {
            pushStringAnnotation(tag = privacyPolicyText, annotation = privacyPolicyText)
            append(privacyPolicyText)
        }
        append(andText)
        withStyle(style = SpanStyle(color = Primary)) {
            pushStringAnnotation(tag = termsAndConditionsText, annotation = termsAndConditionsText)
            append(termsAndConditionsText)
        }
    }

    ClickableText(text = annotatedString, onClick = { offset ->

        annotatedString.getStringAnnotations(offset, offset)
            .firstOrNull()?.also { span ->
                Log.d("ClickableTextComponent", "{${span.item}}")

                if ((span.item == termsAndConditionsText) || (span.item == privacyPolicyText)) {
                    onTextSelected(span.item)
                }
            }

    })
}

@Composable
fun ButtonComponent(value: String, onButtonClicked: () -> Unit, isEnabled: Boolean = true) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        onClick = {
            onButtonClicked.invoke()
        },
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = RoundedCornerShape(50.dp),
        enabled = isEnabled
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Secondary, Primary)),
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

        }

    }
}

@Composable
fun DividerTextComponent() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = GrayColor,
            thickness = 1.dp
        )

        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.or),
            fontSize = 18.sp,
            color = TextColor
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = GrayColor,
            thickness = 1.dp
        )
    }
}

@Composable
fun ClickableCusotmerLoginTextComponent(tryingToLogin: Boolean = true, onTextSelected: (String) -> Unit) {
    val initialText =
        if (tryingToLogin) "Already have an account? " else "Don’t have an account yet? "
    val loginText = if (tryingToLogin) "Login" else "Register"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = Primary)) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        text = annotatedString,
        onClick = { offset ->

            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableTextComponent", "{${span.item}}")

                    if (span.item == loginText) {
                        onTextSelected(span.item)
                    }
                }

        },
    )
}

@Composable
fun ClickableLoginTextComponent(tryingToLogin: Boolean = true, onTextSelected: (String) -> Unit) {
    val initialText =
        if (tryingToLogin) "Already have an account? " else "Don’t have an account yet? "
    val loginText = if (tryingToLogin) "Login" else "Register"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = Primary)) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        text = annotatedString,
        onClick = { offset ->

            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableTextComponent", "{${span.item}}")

                    if (span.item == loginText) {
                        onTextSelected(span.item)
                    }
                }

        },
    )
}



@Composable
fun UnderLinedTextComponent(tryingToLogin: Boolean = true, onTextSelected: (String) -> Unit) {
    val initialText = "If you forgot your Password, please click "
    val loginText = "here"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = Primary)) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        text = annotatedString,
        onClick = { offset ->

            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableTextComponent", "{${span.item}}")

                    if (span.item == loginText) {
                        onTextSelected(span.item)
                    }
                }

        },
    )
    }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(
    toolbarTitle: Int, logoutButtonClicked: () -> Unit,
    navigationIconClicked: () -> Unit
) {

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = toolbarTitle),
                color = WhiteColor,
                fontSize = 18.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                navigationIconClicked.invoke()
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.menu),
                    tint = WhiteColor
                )
            }

        },
        actions = {
            IconButton(onClick = {
                logoutButtonClicked.invoke()
            }) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = stringResource(id = R.string.logout),
                )
            }
        }
    )
}

@Composable
fun NavigationDrawerHeader(value: String?) {
    Box(
        modifier = Modifier
            .background(
                Brush.horizontalGradient(
                    listOf(Primary, Secondary)
                )
            )
            .fillMaxWidth()
            .height(180.dp)
            .padding(32.dp)
    ) {

        NavigationDrawerText(
            title = value?:stringResource(R.string.navigation_header), 28.sp , AccentColor
        )

    }
}

@Composable
fun NavigationDrawerBody(navigationDrawerItems: List<NavigationItem>,
                         onNavigationItemClicked:(NavigationItem) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {

        items(navigationDrawerItems) {
            NavigationItemRow(item = it,onNavigationItemClicked)
        }

    }
}

@Composable
fun NavigationItemRow(item: NavigationItem,
                      onNavigationItemClicked:(NavigationItem) -> Unit) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onNavigationItemClicked.invoke(item)
            }
            .padding(all = 16.dp)
    ) {

        Icon(
            imageVector = item.icon,
            contentDescription = item.description,
        )

        Spacer(modifier = Modifier.width(18.dp))

        NavigationDrawerText(title = item.title, 18.sp, Primary)


    }
}

@Composable
fun NavigationDrawerText(title: String, textUnit: TextUnit, color: Color) {

    val shadowOffset = Offset(4f, 6f)

    Text(
        text = title, style = TextStyle(
            color = Color.Black,
            fontSize = textUnit,
            fontStyle = FontStyle.Normal,
            shadow = Shadow(
                color = Primary,
                offset = shadowOffset, 2f
            )
        )
    )
}

@Composable
fun HeadingTextComponentWithLogOut(value: String){
    Row(horizontalArrangement = Arrangement.SpaceBetween)
    {
        Image(
            painter = painterResource(id = R.drawable.local_artisans_logo),
            contentDescription = null,
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        )
        {
            logoutButton( onButtonClicked = {

                //TODO Create function that will logaut the user and send them back to the Home Screen

                FirebaseAuth.getInstance().signOut().also {     LocalArtisansRouter.navigateTo(Screen.HomeScreen) }
            })
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Local Artisans",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                ),
                color = colorResource(id = R.color.black),
                textAlign = TextAlign.Center
            )
            Text(
                text = value,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal
                ),
                color = colorResource(id = R.color.black),
                textAlign = TextAlign.Center
            )
        }
    }
}



@Composable
fun HeadingTextComponentWithoutLogout(value: String){
    Row(horizontalArrangement = Arrangement.SpaceBetween)
    {
        Image(
            painter = painterResource(id = R.drawable.local_artisans_logo),
            contentDescription = null,
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        )
        {

            Text(
                text = "Local Artisans",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                ),
                color = colorResource(id = R.color.black),
                textAlign = TextAlign.Center
            )
            Spacer (modifier = Modifier.height(20.dp))
            Text(
                text = value,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                ),
                color = colorResource(id = R.color.black),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun CustomerClickableTextComponent(value: String, navController: NavHostController){
    val innitialText = "if you First Time user, please "
    val toRegister = "register"
    val andText = " \n If you forgot your Password, please click "
    val toResetPassword = "here "
    val andText2 = "to reset the Password"

    val annotatedString = buildAnnotatedString {
        append(innitialText)
        withStyle(style = SpanStyle(color = Primary)){
            pushStringAnnotation(tag = "toRegister", annotation = toRegister)
            append(toRegister)
        }
        append(andText)
        withStyle(style = SpanStyle(color = Primary)){
            pushStringAnnotation(tag = "here", annotation = toResetPassword)
            append(toResetPassword)
        }
        append(andText2)
    }
    ClickableText(text = annotatedString, onClick = {offset ->
        //which part of the text was clicked
        annotatedString.getStringAnnotations(offset,offset).firstOrNull()?.also { span ->
            Log.d("ClickableTextComponent", "{$span")
            if (span.tag == toRegister){
                navController.navigate("CustomerSignUpScreen")
            }
            if (span.tag == toResetPassword){
                navController.navigate("ResetPasswordScreen")
            }
        }

    })
}

@Composable
fun ClickableTextResetPasswordArtisan(value: String , onTextSelected: (String) -> Unit){
    val innitianText = "If you forgot your Password, please click "
    val toResetPassword = " here"

    val annotatedString = buildAnnotatedString {
        append(innitianText)
        withStyle(style = SpanStyle(color = Primary)){
            pushStringAnnotation(tag = "here", annotation = toResetPassword)
            append(toResetPassword)
        }
    }
    ClickableText(text = annotatedString, onClick = {offset ->
        annotatedString.getStringAnnotations(offset,offset).firstOrNull()?.also { span ->
            Log.d("ClickableTextComponent", "{$span")
            if (span.tag == toResetPassword){
                onTextSelected(span.item)
            }
        }
    })
}


@Composable
fun logoutButton( onButtonClicked: () -> Unit){
    Button(
        onClick = { onButtonClicked() },
        modifier = Modifier
            .heightIn(14.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent)

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(20.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Secondary, Primary)),
                    RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Log Out",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtisanCategoryDropdown(elements: ArrayList<ProductCategory>,
                            onCategorySelected: () -> Unit): ProductCategory {
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(elements.get(0)) }

    Column() {
        Box(modifier = Modifier.wrapContentSize()) {

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }) {

                TextField(
                    modifier = Modifier.menuAnchor(),
                    value = selectedCategory.ProdCatName,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = !expanded }) {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        elements.forEach { category ->
                            DropdownMenuItem(onClick = {
                                selectedCategory = category
                                expanded = false
                            }) {
                                Text(text = category.ProdCatName)
                            }
                        }
                    }
                }

            }
        }
    }
    return selectedCategory
    onCategorySelected()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtisanCategoryWithProductsDropdown(elements: ArrayList<CategoriesWithProducts>,
                                        onCategorySelected: () -> Unit): CategoriesWithProducts {
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(elements.get(0)) }

    Column() {
        Box(modifier = Modifier.wrapContentSize()) {

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }) {

                TextField(
                    modifier = Modifier.menuAnchor(),
                    value = selectedCategory.prodCatName,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = !expanded }) {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        elements.forEach { category ->
                            DropdownMenuItem(onClick = {
                                selectedCategory = category
                                expanded = false
                            }) {
                                Text(text = category.prodCatName)
                            }
                        }
                    }
                }

            }
        }
    }
    return selectedCategory
    onCategorySelected()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedProductDropdown(elements: ArrayList<LoadedProductFromDB>,
                            onProductSelected: @Composable () -> Unit): LoadedProductFromDB {
    var expanded by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf(elements[0]) }

    Column() {
        Box(modifier = Modifier.wrapContentSize()) {

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }) {

                TextField(
                    modifier = Modifier.menuAnchor(),
                    value = selectedProduct.product_name,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = !expanded }) {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        elements.forEach { product ->
                            DropdownMenuItem(onClick = {
                                selectedProduct = product
                                expanded = false
                            }) {
                                Text(text = product.product_name)
                            }
                        }
                    }
                }

            }
        }
    }
    return selectedProduct
    onProductSelected()
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntegerDropdown(): Int {
    var expanded by remember { mutableStateOf(false) }
    val numbers = (1..10).toList()
    var selectedNumber by remember { mutableStateOf(numbers[0]) }

    Column() {
        Box(modifier = Modifier.wrapContentSize()) {
            Text(
                text = selectedNumber.toString(),
                modifier = Modifier.clickable { expanded = true }
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    value = selectedNumber.toString(),
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = !expanded }) {

                    numbers.forEach { number ->
                        DropdownMenuItem(onClick = {
                            selectedNumber = number
                            expanded = false
                        }) {
                            Text(text = number.toString())
                        }
                    }
                }
            }
        }
    }
    return selectedNumber
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectQuantityOfProductDropdown(qunatityOfProducts: Int) : Int{
var expanded by remember { mutableStateOf(false) }
val numbers = (1..qunatityOfProducts).toList()
var selectedNumber by remember { mutableStateOf(numbers[0]) }

Column() {
    Box(modifier = Modifier.wrapContentSize()) {
        Text(
            text = selectedNumber.toString(),
            modifier = Modifier.clickable { expanded = true }
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = selectedNumber.toString(),
                onValueChange = { },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = !expanded }) {

                numbers.forEach { number ->
                    DropdownMenuItem(onClick = {
                        selectedNumber = number
                        expanded = false
                    }) {
                        Text(text = number.toString())
                    }
                }
            }
        }
    }
}
return selectedNumber
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenNavigationDropdown(screens: List<Screen>, currentScreen: Screen) {
    var expanded by remember { mutableStateOf(false) }
    var selectedScreen by remember { mutableStateOf(currentScreen) }
    val availableScreens = remember { mutableStateOf(screens.toMutableList()) }

    //Remove current Screen from the List
    availableScreens.value.remove(currentScreen)
    Column() {

        Box(modifier = Modifier.wrapContentSize()) {
            Text(
                text = "Navigation Menu",
            )
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange =
            { expanded = !expanded }) {

                TextField(
                    modifier = Modifier.menuAnchor(),
                    value = selectedScreen.name,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    availableScreens.value.forEach { screen ->
                        DropdownMenuItem(
                            onClick = {
                                selectedScreen = screen
                                expanded = false

                                LocalArtisansRouter.navigateTo(screen)
                            },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding){
                            Text(text = screen.name)
                        }

                    }

                }
            }
        }

    }
}