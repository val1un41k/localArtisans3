package com.example.localartisan3.data.screens_view_models.cusotmerScreensViewModels

import com.example.local_artisan.screens.screens_all.customer.LoadedProductFromDB
import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.ArtisansHomeScreen.ProductCategory
import com.example.localartisan3.data.screens_view_models.users_data.ArtisanToPutOnMarkers
import com.example.localartisan3.data.screens_view_models.users_data.ArtisanUIState
import com.example.localartisan3.data.screens_view_models.users_data.CategoriesWithProducts

sealed class CusotmerSelectedProductItemEvent {

    data class SelectedProductArtisanIDChanged(val artisan : ArtisanToPutOnMarkers): CusotmerSelectedProductItemEvent()

    data class SelectedProductArtsanProductCategoryChanged(val artisanProductCategory: CategoriesWithProducts): CusotmerSelectedProductItemEvent()

    data class SelectedProductChanged(val selectedProduct: LoadedProductFromDB): CusotmerSelectedProductItemEvent()

    data class SelectedProductQuantityChanged(val quantityRequested: Int): CusotmerSelectedProductItemEvent()

    data class SelectedProductRequestedTotalChanged(val requestedTotal: Double): CusotmerSelectedProductItemEvent()


}