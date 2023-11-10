package ru.practicum.android.diploma.presentation.filter

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.filter.FiltersInteractor

class FiltersViewModel(val interactor: FiltersInteractor) : ViewModel() {

    fun setSalary(inputText: String){
        interactor.setSalary(inputText)
    }

    fun getSalary(): String {
        return interactor.getSalary()
    }
}
