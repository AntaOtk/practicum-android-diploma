package ru.practicum.android.diploma.core.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.domain.detail.DetailInteractor
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.favorite.FavouriteInteractor
import ru.practicum.android.diploma.domain.favorite.FavouriteInteractorImpl
import ru.practicum.android.diploma.domain.impl.DetailInteractorImpl
import ru.practicum.android.diploma.domain.impl.FilterInteractorImpl
import ru.practicum.android.diploma.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.domain.impl.SimilarInteractorImpl
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.similar.SimilarInteractor
import ru.practicum.android.diploma.presentation.SalaryPresenter


val interactorModule = module {
    single<SearchInteractor> { SearchInteractorImpl(get(),get()) }
    single<FilterInteractor> { FilterInteractorImpl(get(), get()) }
    single<SimilarInteractor> { SimilarInteractorImpl(get()) }
    single<DetailInteractor> { DetailInteractorImpl(get(), get(), get()) }
    single<FavouriteInteractor> { FavouriteInteractorImpl(get()) }
    single { SalaryPresenter(androidContext()) }
}
