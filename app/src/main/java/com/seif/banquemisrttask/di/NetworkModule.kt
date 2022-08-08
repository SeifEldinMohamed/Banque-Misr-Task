package com.seif.banquemisrttask.di

import com.seif.banquemisrttask.data.network.TrendingRepositoriesApi
import com.seif.banquemisrttask.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

/*
@Singleton
@Provides
fun provideOkHttpClient(): OkHttpClient{
return OkHttpClient.Builder()
.readTimeout(15, TimeUnit.SECONDS)
.connectTimeout(15, TimeUnit.SECONDS)
.build()
}
*/

    @Singleton // application scope
    @Provides // we use Provides annotation bec we use retrofit library which is not created by us
    fun provideGsonConverterFactory(): GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
      //  okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }


    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): TrendingRepositoriesApi {
        return retrofit.create(TrendingRepositoriesApi::class.java)
    }
}

// readTimeout : Sets the default read timeout for new connections. A value of 0 means no timeout,
// otherwise values must be between 1 and Integer.MAX_VALUE when converted to milliseconds.
// The read timeout is applied to both the TCP socket and for individual read IO operations
// ]including on Source of the Response. The default value is 10 seconds.

// connectionTimeout: Sets the default connect timeout for new connections. A value of 0 means no timeout,
// otherwise values must be between 1 and Integer.MAX_VALUE when converted to milliseconds.
// The connect timeout is applied when connecting a TCP socket to the target host. The default value
// is 10 seconds.