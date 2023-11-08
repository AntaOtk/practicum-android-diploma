package ru.practicum.android.diploma.core.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.DetailInteractor
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.impl.DetailInteractorImpl
import ru.practicum.android.diploma.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.domain.impl.SimilarInteractorImpl
import ru.practicum.android.diploma.domain.similar.SimilarInteractor


val interactorModule = module {
    single<SearchInteractor> { SearchInteractorImpl(get()) }
    single<SimilarInteractor> { SimilarInteractorImpl(get()) }
    single<DetailInteractor> { DetailInteractorImpl(get()) }
}