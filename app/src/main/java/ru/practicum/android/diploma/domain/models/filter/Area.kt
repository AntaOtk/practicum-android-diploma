package ru.practicum.android.diploma.domain.models.filter

data class Area(
    val id:String,
    val name:String,
    val areas: List<Region>
)