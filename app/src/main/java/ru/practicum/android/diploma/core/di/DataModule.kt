package ru.practicum.android.diploma.core.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.ExternalNavigatorImpl
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.ResourceProvider
import ru.practicum.android.diploma.data.ResourceProviderImpl
import ru.practicum.android.diploma.data.db.AppDataBase
import ru.practicum.android.diploma.data.filter.local.LocalStorage
import ru.practicum.android.diploma.data.filter.local.SharedPreferenceClient
import ru.practicum.android.diploma.data.network.ApiService
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.ExternalNavigator


val dataModule = module {
    factory { Gson() }

    single<ApiService> {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level =
                HttpLoggingInterceptor.Level.BODY  // Используйте уровень, который соответствует вашим потребностям
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.hh.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)  // Указываете здесь ваш экземпляр OkHttpClient с интерсептором
            .build()
            .create(ApiService::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "database.db")
            .build()
    }

    single {
        val database = get<AppDataBase>()
        database.vacancyDao()
    }

    single<NetworkClient> { RetrofitNetworkClient(get(), get()) }

    single<ResourceProvider> { ResourceProviderImpl(androidContext()) }

    singleOf(::ExternalNavigatorImpl).bind<ExternalNavigator>()

    single {
        androidContext()
            .getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    single<LocalStorage> {
        SharedPreferenceClient(get(), get())
    }
}



