package ru.practicum.android.diploma.data.filter.local

import ru.practicum.android.diploma.data.dto.filter.FiltersDto

interface LocalStorage {
    fun doRequest(): FiltersDto
    fun doWrite(filtersDto: FiltersDto)
}
