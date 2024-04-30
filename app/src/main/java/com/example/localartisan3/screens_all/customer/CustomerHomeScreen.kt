package com.example.local_artisan.screens.screens_all.customer

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.local_artisan.components.HeadingTextComponentWithLogOut
import com.example.local_artisan.components.ScreenNavigationDropdown

import com.example.localartisan3.data.screens_view_models.cusotmerScreensViewModels.CustomerMainViewModel
import com.example.localartisan3.data.screens_view_models.users_data.ArtisanUIState
import com.example.localartisan3.navigation.Screen
import com.example.localartisan3.navigation.customerScreens
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.DragState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

private const val TAG = "CustomerHomeDashboardScreen"

private val singapore = LatLng(1.35, 103.87)
private val singapore2 = LatLng(1.40, 103.77)
private val singapore3 = LatLng(1.45, 103.77)
private val limerick = LatLng(52.66, -8.63)

private val test2 = LatLng(52.65294374183368, -8.6150916562515)
private val test1 = LatLng(52.6502269525512, -8.631892488864397)
private val defaultCameraPosition = CameraPosition.fromLatLngZoom(limerick, 11f)



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
            populateArtisansAsMarkers(listOfArtisans)


            Marker(
                state = rememberMarkerState(position = test1),
                draggable = true,
                title = "Marker 1",
                snippet = "Marker in Singapore",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
            )

            Marker( state = rememberMarkerState(position = test2),
                draggable = true,
                title = "Marker 1",
                snippet = "Marker in Singapore",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
            )
            content()
        }

    }
}

@Composable
fun populateArtisansAsMarkers(listOfArtisans: List<ArtisanUIState>){
    var TAG = "populateArtisansAsMarkers"
    listOfArtisans.forEach {artisan ->
        //TODO Create Marker for each Artisan
        val position = LatLng(artisan.addressLongitude.toDouble(),artisan.addressLatitude.toDouble())
        Log.d(TAG, "Artisan: ${artisan.fname} ${artisan.lname} has position: $position")

        val markerState = rememberMarkerState(position = position)
        val markerClick: (Marker) -> Boolean = {
            Log.d(TAG, "${it.title} was clicked")
           // clickedMarkerInfoWindowContent(artisan)
            false
        }
        Marker(
            state = markerState,
            title = artisan.fname + "   " + artisan.lname,
            onClick = markerClick,
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        )
        Log.d(TAG, "Marker: ${artisan.fname} ${artisan.lname} has been created")
    }
}


@Composable
fun clickedMarkerInfoWindowContent(artisan: ArtisanUIState){

    //TODO Creating popped up window for clicked marker
    Column(
        modifier = Modifier.padding(8.dp)
    ){
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Gray)
        ) {
            Text(text = "Artisan Name: ${artisan.fname} ${artisan.lname}")
            Text(text = "Artisan Address: ${artisan.address}")
            Text(text = "Artisan Phone Number: ${artisan.pnum}")
            Text(text = "Artisan Email: ${artisan.email}")
        }
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Gray)
        ){
            //TODO DropDown Menu from having product categories

        }
    }
}


