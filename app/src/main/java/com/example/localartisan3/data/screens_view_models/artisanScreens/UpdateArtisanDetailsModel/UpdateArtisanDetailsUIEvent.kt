package com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel

sealed class UpdateArtisanDetailsUIEvent {
    data class ArtisansNameChanged (val ArtisansName: String) : UpdateArtisanDetailsUIEvent()

    data class ArtisansSurnameChanged (val ArtisansSurname: String) : UpdateArtisanDetailsUIEvent()

    data class ArtsisansPhoneNumberChanged (val ArtsisansPhoneNumber: String) : UpdateArtisanDetailsUIEvent()

   // TODO data class ArtisansPhotoUpdated when photo will be updated

    data class ArtisansStreetAddressChanged (val ArtisansAddress: String) : UpdateArtisanDetailsUIEvent()


    data class ArtisansAddressLongitudeChanged (val ArtisansAddressLongitude: String) : UpdateArtisanDetailsUIEvent()

    data class ArtisansAddressLatitudeChanged (val ArtisansAddressLatitude: String) : UpdateArtisanDetailsUIEvent()

    object ArtisansUpdateDetailsButtonClicked: UpdateArtisanDetailsUIEvent()

    object ArtisansUpdateDetailsCencelButtonClicked: UpdateArtisanDetailsUIEvent()


}