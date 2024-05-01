package com.example.local_artisan.screens.screens_all.customer

import android.content.Context
import android.graphics.Bitmap
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.local_artisan.components.ArtisanCategoryDropdown
import com.example.local_artisan.components.HeadingTextComponentWithLogOut
import com.example.local_artisan.components.ScreenNavigationDropdown
import com.example.localartisan3.R
import com.example.localartisan3.data.screens_view_models.cusotmerScreensViewModels.CusotmerSelectedProductItemEvent

import com.example.localartisan3.data.screens_view_models.cusotmerScreensViewModels.CustomerMainViewModel
import com.example.localartisan3.data.screens_view_models.users_data.ArtisanUIState
import com.example.localartisan3.navigation.Screen
import com.example.localartisan3.navigation.customerScreens
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
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


private val limerick = LatLng(52.66, -8.63)

private val homeLocation = LatLng(52.658664940642296, -8.633123277485389)
private val defaultCameraPosition = CameraPosition.fromLatLngZoom(limerick, 11f)

var listOfProductsFromDB = mutableStateOf( ArrayList<LoadedProductFromDB>())



@Composable
fun CustomerHomeDashboardScreen(customerMainViewModel: CustomerMainViewModel = viewModel()) {
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
                   position = defaultCameraPosition
               }

               Box(Modifier.fillMaxSize()) {
                   GoogleMapView(
                        customerMainViewModel = customerMainViewModel,
                       customerMainViewModel.artisanList.value,
                       modifier = Modifier.matchParentSize(),
                       cameraPositionState = cameraPositionState,
                       onMapLoaded = {
                           isMapLoaded = true
                       },
                   )
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
    listOfArtisans: List<ArtisanUIState>,
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapLoaded: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val limerick = rememberMarkerState(position = limerick)


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
            populateArtisansAsMarkers(customerMainViewModel ,listOfArtisans)


            Marker(
                state = rememberMarkerState(position = homeLocation),
                draggable = true,
                title = "Current Location",
                snippet = "Marker in Limerick",
                icon = BitmapDescriptorFactory.fromResource(R.drawable.pin2),
            )

            content()
        }

    }
}

@Composable
fun populateArtisansAsMarkers(customerMainViewModel: CustomerMainViewModel = viewModel(),
    listOfArtisans: List<ArtisanUIState>){
    var TAG = "populateArtisansAsMarkers"
    listOfArtisans.forEach {artisan ->

        val position = LatLng(artisan.addressLongitude.toDouble(),artisan.addressLatitude.toDouble())
        Log.d(TAG, "Artisan: ${artisan.fname} ${artisan.lname} has position: $position")

        val markerState = rememberMarkerState(position = position)

        val icon = bitmapDescriptorFromVector(
            LocalContext.current, R.drawable.pin
        )

        MarkerInfoWindow(
            state = markerState,
            icon = icon,
        ) { marker ->
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(35.dp, 35.dp, 35.dp, 35.dp)
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.map),
                        //TODO take image from firestore artisan_Profile
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(80.dp)
                            .fillMaxWidth(),

                        )

                    ClickedMarkerInfoWindowContent(artisan)

                    //TODO add Event on Category Changed
                    customerMainViewModel.onEvent(CusotmerSelectedProductItemEvent
                        .SelectedProductArtsanProductCategoryChanged(
                    ArtisanCategoryDropdown(artisan.categories) {
                       //create popped up windows related to the products inside
                   // category of the artisan
                    }
                        ))
                    if (listOfProductsFromDB.value.isNullOrEmpty()) {
                        Toast.makeText(LocalContext.current, "No Products Found" +
                                "\n please try another category" +
                                "\n or another craft maker", Toast.LENGTH_SHORT).show()
                    }else{

                        //TODO Create IVENT on Category Selected


                        //Create Popped Window of Artisan`s products
                        //     PoppedUpWindowOfArtisansProducts(artisan, selectedCategoryOfArtisan.value)
                    }


                    Spacer(modifier = Modifier.height(8.dp))
                    //.........................Text : description
                    Text(
                        text = "Customizing a marker's info window",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    //.........................Spacer
                    Spacer(modifier = Modifier.height(24.dp))

                }

            }

        }
        Log.d(TAG, "Marker: ${artisan.fname} ${artisan.lname} has been created")
    }
}

@Composable
fun PoppedUpWindowOfArtisansProducts(artisan: ArtisanUIState, category: String) {

   Box(
       modifier = Modifier.fillMaxSize(),
   )
   {
       Column(){

       }
   }

   // TakingDataFromFirestoreToPopulateProducts(artisan, category)

}


@Composable
fun ClickedMarkerInfoWindowContent(artisan: ArtisanUIState){
    //.........................Spacer
    Spacer(modifier = Modifier.height(24.dp))
    //.........................Text: title
    Text(
        text = "Artisan name: ${artisan.fname} ${artisan.lname}" +
                "\n" +
                "\n Artisan Address: ${artisan.address}" +
                "\n" +
                "\n Artisan Phone Number: ${artisan.pnum}" +
                "\n" +
                "\n Artisan Email: ${artisan.email}" +
                "\n",
        textAlign = TextAlign.Left,
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth(),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary,
    )
}

fun TakingDataFromFirestoreToPopulateProducts(){

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
    var artisanName: String = "",
    var artisanSurname: String = "",
    var artisanID: String = "",
    var productName: String = "",
    var productDescription: String = "",
    var productPrice: Double = 0.0,
    var productDiscountPrice: Double = 0.0,
    var productImage: String = "",
    var productCategory: String = "",
    var qtyOnHand: Int = 0



)

data class CustomerSelectedProductItem(

    var customerID: String = "",
    var artisanID: String = "",
    var artisanProductCategory: String = "",
    var artisanProductItemID: String = "",
    var artisanProductItemName: String = "",
    var artisanProductItemDescription: String = "",
    var artisanProductItemPrice: Double = 0.0,
    var artisanProductItemDiscountPrice: Double = 0.0,
    var artisanProductItemDownloadUri: String = "",

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
