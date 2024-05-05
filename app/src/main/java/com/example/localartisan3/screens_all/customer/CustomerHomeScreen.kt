package com.example.local_artisan.screens.screens_all.customer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter

import com.example.local_artisan.components.ArtisanCategoryWithProductsDropdown
import com.example.local_artisan.components.DisplayOnlyTextField
import com.example.local_artisan.components.HeadingTextComponentWithLogOut

import com.example.local_artisan.components.ScreenNavigationDropdown
import com.example.local_artisan.components.SelectQuantityOfProductDropdown
import com.example.local_artisan.components.SelectedProductDropdown
import com.example.localartisan3.R

import com.example.localartisan3.data.screens_view_models.cusotmerScreensViewModels.CusotmerSelectedProductItemEvent

import com.example.localartisan3.data.screens_view_models.cusotmerScreensViewModels.CustomerMainViewModel

import com.example.localartisan3.navigation.Screen
import com.example.localartisan3.navigation.customerScreens
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

private var TAG = "CustomerHomeDashboardScreen"

@Composable
fun CustomerHomeDashboardScreen(customerMainViewModel: CustomerMainViewModel = viewModel()) {
    // Populating Products  from DB

    val TAG = "CustomerHomeDashboardScreen"
    Surface(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            HeadingTextComponentWithLogOut(value = "Customer Home Dashboard Screen")
            Spacer(modifier = Modifier.height(10.dp))
            Log.d(TAG, "ArtisansList: ${customerMainViewModel.artisanList.value}")

            ScreenNavigationDropdown(customerScreens, Screen.CustomerHomeDashboardScreen)

            Spacer(modifier = Modifier.height(10.dp))

               var isMapLoaded by remember { mutableStateOf(false) }
               // Observing and controlling the camera's state can be done with a CameraPositionState
               val cameraPositionState = rememberCameraPositionState {
                   position = customerMainViewModel.defaultCameraPosition
               }

               Box(Modifier.fillMaxSize()) {
                   GoogleMapView(
                        customerMainViewModel = customerMainViewModel,
                       modifier = Modifier.matchParentSize(),
                       cameraPositionState = cameraPositionState,
                       onMapLoaded = {
                           isMapLoaded = true
                       },
                   )
                   {
                   }
                   if (!isMapLoaded) {
                       androidx.compose.animation.AnimatedVisibility(
                           modifier = Modifier
                               .matchParentSize(),
                           visible = !isMapLoaded,
                           enter = EnterTransition.None,
                           exit = fadeOut()
                       ) {
                           CircularProgressIndicator(
                               modifier = Modifier
                                   .background(MaterialTheme.colorScheme.background)
                                   .wrapContentSize()
                           )
                       }
                   }
               }

            showingDialogWindows(customerMainViewModel)

            Spacer(modifier = Modifier.height(80.dp))
        }
    }

}

@Preview
@Composable

fun CustomerHomeDashboardScreenPreview(){
    CustomerHomeDashboardScreen()
}

@Composable
fun GoogleMapView(
    customerMainViewModel: CustomerMainViewModel = viewModel(),
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapLoaded: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val limerick = rememberMarkerState(position = customerMainViewModel.limerick)


    var uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
    var shouldAnimateZoom by remember { mutableStateOf(true) }
    var ticker by remember { mutableStateOf(0) }
    var mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    var mapVisible by remember { mutableStateOf(true) }

    if (mapVisible) {
        GoogleMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings,
            onMapLoaded = onMapLoaded,
            onPOIClick = {
                Log.d(TAG, "POI clicked: ${it.name}")
            }
        ) {
            // Drawing on the map is accomplished with a child-based API
            val markerClick: (Marker) -> Boolean = {
                Log.d(TAG, "${it.title} was clicked")
                cameraPositionState.projection?.let { projection ->
                    Log.d(TAG, "The current projection is: $projection")
                }
                false
            }
            //TODO Create Function that will populate arrayOfArtisans as Markers
           populateArtisansAsMarkers(customerMainViewModel)
            Marker(
                state = rememberMarkerState(position = customerMainViewModel.homeLocation),
                draggable = true,
                title = "Current Location",
                snippet = "Marker in Limerick",
                icon =bitmapDescriptorFromVector(LocalContext.current, R.drawable.pin2, 80, 80),
            )

            content()
        }

    }
}

@Composable
fun showingDialogWindows(customerMainViewModel: CustomerMainViewModel = viewModel()){
    if(customerMainViewModel.showSelectCategoryDialog) {
        selectingPoruductCateogyDialogWindow()
    }

    if(customerMainViewModel.showSelectProductWithinCategoryDialog){
        customerMainViewModel.productsToShow.value =
            customerMainViewModel.customerSelectedProductItem.value.artisanProductCategory.products
        if (customerMainViewModel.productsToShow.value.isNotEmpty()
            && customerMainViewModel.productsToShow.value != null){
            SelectingProductForCustomerRequest()
        } else {
            Toast.makeText(LocalContext.current, "No Products in this Category", Toast.LENGTH_SHORT).show()
            customerMainViewModel.showSelectCategoryDialog = false
            customerMainViewModel.showSelectProductWithinCategoryDialog = false
            //repopulate the markers
            //remove parameters from selected product

        }
    }
}

@Composable
fun populateArtisansAsMarkers(customerMainViewModel: CustomerMainViewModel = viewModel()){
    var TAG = "populateArtisansAsMarkers"
    customerMainViewModel.artisanList.value.forEach {artisan ->

        val position = LatLng(artisan.addressLongitude.toDouble(),artisan.addressLatitude.toDouble())
        Log.d(TAG, "Artisan: ${artisan.fname} ${artisan.lname} has position: $position")
        val markerState = rememberMarkerState(position = position)
        val icon = bitmapDescriptorFromVector(LocalContext.current, R.drawable.pin, 80, 80)


        MarkerInfoWindow(
            state = markerState,
            icon = icon,
            onInfoWindowClick = {
                //open Alert window with Artisan`s catrogy to select
                //onComplete: open another window with Artisan`s products
                customerMainViewModel.onEvent(
                    CusotmerSelectedProductItemEvent
                        .SelectedProductArtisanIDChanged(artisan))

                customerMainViewModel.showSelectCategoryDialog = true



            }
        )
        { marker ->
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(35.dp, 35.dp, 35.dp, 35.dp)
                    )
                    .scale(0.7f),
            ) {
                LazyColumn(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
                ){

                    item {

                        Image(
                            painter = rememberImagePainter(artisan.profile_image_url),
                            // painterResource(

                                //id = R.drawable.map),
                            //TODO take image from firestore artisan_Profile
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .height(80.dp)
                                .fillMaxWidth()
                                .scale(0.4f),
                            )
                    }
                    item {
                        ClickedMarkerInfoWindowContent(artisan)
                        //put button that will lead to popped up window of artisan`s products
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    item {
                        DisplayOnlyTextField("", "Click on window to See Artisan`s Products")
                    }

                    }

                }
            }
        Log.d(TAG, "Marker: ${artisan.fname} ${artisan.lname} has been created")
        }
    }


fun bitmapDescriptorFromVector(context: Context, vectorResId: Int, width: Int, height: Int): BitmapDescriptor? {
    val bitmap = BitmapFactory.decodeResource(context.resources, vectorResId)
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
    return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
}



@Composable
fun ClickedMarkerInfoWindowContent(artisan: ArtisanToPutOnMarkers){
    //.........................Spacer
    Spacer(modifier = Modifier.height(5.dp))
    //.........................Text: title
    Text(
        text = "Artisan name: ${artisan.fname} ${artisan.lname}" +

                "\n Artisan Address: ${artisan.address}" +

                "\n Artisan Phone Number: ${artisan.pnum}" +

                "\n Artisan Email: ${artisan.email}",

        textAlign = TextAlign.Left,
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth(),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary,
    )
}



@Composable
fun selectingPoruductCateogyDialogWindow(customerMainViewModel: CustomerMainViewModel = viewModel()){

  var avalialbleCategories =
    customerMainViewModel
        .customerSelectedProductItem
        .value
        .artisan.categories
    avalialbleCategories.removeIf{it.products.isEmpty()}


    Dialog(

        onDismissRequest = {
            customerMainViewModel.showSelectCategoryDialog = false
            customerMainViewModel.showSelectProductWithinCategoryDialog = false}){
        Box(modifier = Modifier
            .size(300.dp)
            .background(Color.Gray, RoundedCornerShape(10.dp))){
            Column(){
                // Checking the product Category
                Text("Select Artisan`s Category")
                if (avalialbleCategories.isNullOrEmpty()){
                    DisplayOnlyTextField("",
                        "No Products Available from This Artisan")
                }
                else {
                    customerMainViewModel.onEvent(CusotmerSelectedProductItemEvent
                        .SelectedProductArtsanProductCategoryChanged(
                            ArtisanCategoryWithProductsDropdown(avalialbleCategories) {
                            //Leave Empty For a time being
                            }
                        ))
                }
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = {
                        customerMainViewModel.showSelectProductWithinCategoryDialog = true
                        customerMainViewModel.showSelectCategoryDialog = false}){
                    Text("Open Artisan`s Products")
                }
                Button (
                    onClick = {
                        customerMainViewModel.showSelectCategoryDialog = false
                        customerMainViewModel.showSelectProductWithinCategoryDialog = false
                    }){
                    Text("Close")
                }

            }
        }
    }
}



@Composable
fun SelectingProductForCustomerRequest(customerMainViewModel: CustomerMainViewModel = viewModel()){

    Dialog(
        onDismissRequest = { customerMainViewModel.showSelectCategoryDialog = false}){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray, RoundedCornerShape(10.dp))){
            Column() {
                // Checking the product Category
                var availableProducts = customerMainViewModel.customerSelectedProductItem.value
                    .artisanProductCategory
                if (availableProducts.products.isNullOrEmpty()) {
                    DisplayOnlyTextField(
                        "",
                        "No Products Available from This Artisan"
                    )
                } else {

                    customerMainViewModel.onEvent(
                        CusotmerSelectedProductItemEvent.SelectedProductChanged(
                            SelectedProductDropdown(
                                //take products from Artisans Category
                                //products Within category of the Artisan
                                availableProducts.products

                            ) {

                            })
                    )
                    Button(
                        onClick = {
                            customerMainViewModel.showSelectProductQuantityDialog = true
                        }) {
                        Text("See Product Details")
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                }
                Button(
                    onClick = {
                        customerMainViewModel.showSelectCategoryDialog = false
                        customerMainViewModel.showSelectProductWithinCategoryDialog = false
                    }) {
                    Text("Close")
                }
            }
            }
        }
    }


@Composable
fun PopulateProductDetails(customerMainViewModel: CustomerMainViewModel = viewModel()){
    Text(
        text = "Product Item name:  ${customerMainViewModel.customerSelectedProductItem.value.selectedProduct.product_name}" +

                "\n Product Item Description: ${ customerMainViewModel.customerSelectedProductItem.value.selectedProduct.product_description}" +

                "\n Product Item Price: ${customerMainViewModel.customerSelectedProductItem.value.selectedProduct.product_price}" +

                "\n Product Item Discounted Price: ${ customerMainViewModel.customerSelectedProductItem.value.selectedProduct.product_discount_price }" +
                "" +
                "\n Product Item Quantity on Hand: ${ customerMainViewModel.customerSelectedProductItem.value.selectedProduct.qty_on_hand }" +

                "\n Product Item Picture ${ customerMainViewModel.customerSelectedProductItem.value.selectedProduct.product_picture  }",

        textAlign = TextAlign.Left,
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth(),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary,
    )

    // place image that will be taken from firestorage and displayed here
    Image(
        painter = rememberImagePainter(customerMainViewModel.selectedProduct.value.productImage),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .scale(0.4f),
    )

    //select Quantity dropdown window
    customerMainViewModel.onEvent(CusotmerSelectedProductItemEvent.SelectedProductQuantityChanged(
        SelectQuantityOfProductDropdown(customerMainViewModel.selectedProduct.value.qtyOnHand)))

    Button(
        onClick = {
            //TODO Create request for the Selected product
        }){
        Text("Place Request")
    }
    Spacer(modifier = Modifier.height(10.dp))
    Button (
        onClick = {
            customerMainViewModel.showSelectCategoryDialog = false
        }){
        Text("Close")
    }

}

fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}



data class LoadedProductFromDB(
    var productID: String = "",
    var artisan_name: String = "",
    var artisan_surname: String = "",
    var artisan_uid: String = "",
    var product_name: String = "",
    var product_description: String = "",
    var product_price: Double = 0.0,
    var product_discount_price: Double = 0.0,
    var product_picture: String = "",
    var product_category_ID: String = "",
    var product_category_name: String = "",
    var qty_on_hand: Int = 0

)

data class CustomerSelectedProductItem(

    var artisan: ArtisanToPutOnMarkers = ArtisanToPutOnMarkers(),
    var artisanProductCategory: CategoriesWithProducts = CategoriesWithProducts(),
    var selectedProduct: LoadedProductFromDB = LoadedProductFromDB(),

    var quantityRequested: Int = 0,
    var requestedTotal: Double = 0.0,  // var requestedTotal: Double = 0.0, requestQuantity * productPrice
)

data class CustomerRequestForArtisansProductItem(
    var customerRequestId: String = "",
    var customerID: String = "",
    var artisanID: String = "",
    var artisanProductItemID: String = "",
    var quantityRequested: Int = 0,
    var requestedTotal: Double = 0.0,  // var requestedTotal: Double = 0.0, requestQuantity * productPrice
    var requestStatus: String = ""
)



data class ArtisanToPutOnMarkers(
    var address :String = "",
    var addressLatitude: String = "",
    var addressLongitude: String = "",
    var artisanID: String = "",
    var email: String = "",
    var firstTime: Boolean = false,
    var fname: String = "",
    var lname: String = "",
    var pnum: String = "",
    var profile_image_url :String = "",
    var categories: ArrayList<CategoriesWithProducts> = ArrayList(),
)

data class CategoriesWithProducts(
    var categoryID: String = "",
    var prodCatName: String = "",
    var products: ArrayList<LoadedProductFromDB> = ArrayList(),
)

