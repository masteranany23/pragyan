package com.example.pragyan.di

import android.content.Context
import com.example.pragyan.network.DynamicBaseUrlProvider
import com.example.pragyan.network.RobotApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Factory for creating dynamic API instances that update when the base URL changes
 */
@Singleton
class DynamicApiFactory @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val baseUrlProvider: DynamicBaseUrlProvider
) {
    /**
     * Create an API instance that uses the current base URL
     */
    fun <T> create(apiClass: Class<T>): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrlProvider.getCurrentBaseUrl())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(apiClass)
    }

    /**
     * Get the current base URL
     */
    fun getCurrentBaseUrl(): String {
        return baseUrlProvider.getCurrentBaseUrl()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideBaseUrlProvider(@ApplicationContext context: Context): DynamicBaseUrlProvider {
        return DynamicBaseUrlProvider(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideDynamicApiFactory(
        okHttpClient: OkHttpClient,
        baseUrlProvider: DynamicBaseUrlProvider
    ): DynamicApiFactory {
        return DynamicApiFactory(okHttpClient, baseUrlProvider)
    }

    @Provides
    @Singleton
    fun provideRobotApi(apiFactory: DynamicApiFactory): RobotApi {
        return apiFactory.create(RobotApi::class.java)
    }
}