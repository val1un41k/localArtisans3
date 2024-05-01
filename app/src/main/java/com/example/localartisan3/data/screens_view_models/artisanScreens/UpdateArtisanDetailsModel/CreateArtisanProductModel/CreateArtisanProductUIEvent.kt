package com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.CreateArtisanProductModel

import android.net.Uri

sealed class CreateArtisanProductUIEvent {

    data class ProductNameChanged(val productName: String) : CreateArtisanProductUIEvent()

    data class ProductDescriptionChanged(val productDescription: String) : CreateArtisanProductUIEvent()

    data class ProductPriceChanged(val productPrice: Double) : CreateArtisanProductUIEvent()

    data class ProductDiscountChanged(val productDiscount: Double) : CreateArtisanProductUIEvent()

    data class ProductImageChanged(val productImage: Uri) : CreateArtisanProductUIEvent()

    data class ProductCategoryChanged(val productCategory: String) : CreateArtisanProductUIEvent()

    class QtyOnHandChanged (val qtyOnHand: Int) : CreateArtisanProductUIEvent()

    object ProductCreateRecordButtonClicked : CreateArtisanProductUIEvent()

    object NextProductRecordItemButtonClicked : CreateArtisanProductUIEvent()

    object BackToDashboardButtonClicked : CreateArtisanProductUIEvent()

    object DataFetchedFromFirestore: CreateArtisanProductUIEvent()





}