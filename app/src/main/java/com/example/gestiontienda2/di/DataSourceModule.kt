package com.example.gestiontienda2.di

import com.example.gestiontienda2.data.remote.firebase.datasource.source.OrderFirebaseDataSource
import com.example.gestiontienda2.data.remote.firebase.datasource.source.OrderFirebaseDataSourceImpl
// Import other data source interfaces and impls here if they follow the same pattern
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindOrderFirebaseDataSource(
        impl: OrderFirebaseDataSourceImpl
    ): OrderFirebaseDataSource

    // Add bindings for ProductFirebaseDataSource and others here later
}
