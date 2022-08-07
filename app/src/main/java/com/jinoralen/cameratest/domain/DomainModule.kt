package com.jinoralen.cameratest.domain

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun provideUploadRepository(repository: UploadRepositoryImpl): UploadRepository

    @Binds
    abstract fun provideStoreRepository(repository: StoreRepositoryImpl): StoreRepository

    companion object {
        @Provides
        @Singleton
        fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }).build()

        @Provides
        @Singleton
        fun provideRetrofit(
            okHttpClient: OkHttpClient,
        ): Retrofit = Retrofit.Builder()
            .baseUrl("https://www.example.com")
            .client(okHttpClient)
            .build()
    }
}
