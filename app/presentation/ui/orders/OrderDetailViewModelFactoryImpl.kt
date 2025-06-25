package com.example.gestiontienda2.presentation.ui.orders

import androidx.lifecycle.SavedStateHandle
import dagger.assisted.Assisted
import javax.inject.Inject

class OrderDetailViewModelFactoryImpl @Inject constructor(
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val updateOrderUseCase: UpdateOrderUseCase,
    private val deleteOrderUseCase: DeleteOrderUseCase,
    private val getClientsUseCase: GetClientsUseCase,
    private val getProductsUseCase: GetProductsUseCase
) : OrderDetailViewModelFactory {
    override fun create(savedStateHandle: SavedStateHandle): OrderDetailViewModel {
        return OrderDetailViewModel(
            savedStateHandle = savedStateHandle,
            getOrderByIdUseCase = getOrderByIdUseCase,
            updateOrderUseCase = updateOrderUseCase,
            deleteOrderUseCase = deleteOrderUseCase,
            getClientsUseCase = getClientsUseCase,
            getProductsUseCase = getProductsUseCase
        )
    }
}
