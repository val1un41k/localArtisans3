package com.example.localartisan3.data.screens_view_models.cusotmerScreensViewModels

sealed class CusotmerSelectedProductItemEvent {

    data class SelectedProductArtisanIDChanged(val artisanID: String): CusotmerSelectedProductItemEvent()

    data class SelectedProductArtsanProductCategoryChanged(val artisanProductCategory: String): CusotmerSelectedProductItemEvent()


    data class SelectedProductProductItemDiscountPriceChanged(val artisanProductItemDiscountPrice: Double): CusotmerSelectedProductItemEvent()

    data class SelectedProductArtisanProductItemIDChanged(val artisanProductItemID: String): CusotmerSelectedProductItemEvent()

    data class SelectedProductQuantityChanged(val quantityRequested: Int): CusotmerSelectedProductItemEvent()

    data class SelectedProductRequestedTotalChanged(val requestedTotal: Double): CusotmerSelectedProductItemEvent()


}