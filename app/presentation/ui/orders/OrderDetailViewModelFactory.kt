package com.example.gestiontienda2.presentation.ui.orders

import androidx.lifecycle.SavedStateHandle
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface OrderDetailViewModelFactory {
    fun create(
        @Assisted savedStateHandle: SavedStateHandle
    ): OrderDetailViewModel
}
