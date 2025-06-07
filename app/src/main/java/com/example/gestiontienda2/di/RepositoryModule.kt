package com.example.gestiontienda2.di

import com.example.gestiontienda2.data.repository.ClientRepositoryImpl
import com.example.gestiontienda2.data.repository.OrderRepositoryImpl
import com.example.gestiontienda2.data.repository.ProductRepositoryImpl
import com.example.gestiontienda2.data.repository.ProviderRepositoryImpl
import com.example.gestiontienda2.data.repository.PurchaseRepositoryImpl
import com.example.gestiontienda2.data.repository.SaleRepositoryImpl
import com.example.gestiontienda2.data.repository.ServiceExpenseRepositoryImpl
import com.example.gestiontienda2.domain.repository.ClientRepository
import com.example.gestiontienda2.domain.repository.OrderRepository
import com.example.gestiontienda2.domain.repository.ProductRepository
import com.example.gestiontienda2.domain.repository.ProviderRepository
import com.example.gestiontienda2.domain.repository.PurchaseRepository
import com.example.gestiontienda2.domain.repository.SaleRepository
import com.example.gestiontienda2.domain.repository.ServiceExpenseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl,
    ): ProductRepository

    @Binds
    abstract fun bindClientRepository(
        impl: ClientRepositoryImpl,
    ): ClientRepository

    @Binds
    abstract fun bindSaleRepository(
        impl: SaleRepositoryImpl,
    ): SaleRepository

    @Binds
    abstract fun bindPurchaseRepository(
        impl: PurchaseRepositoryImpl,
    ): PurchaseRepository

    @Binds
    abstract fun bindProviderRepository(
        impl: ProviderRepositoryImpl,
    ): ProviderRepository

    @Binds
    abstract fun bindOrderRepository(
        impl: OrderRepositoryImpl,
    ): OrderRepository

    @Binds
    abstract fun bindServiceExpenseRepository(
        impl: ServiceExpenseRepositoryImpl,
    ): ServiceExpenseRepository
}
