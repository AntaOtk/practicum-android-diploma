package ru.practicum.android.diploma.data.dto.filter

data class IndustryDto(
    val id:String,
    val industries:List<IndustriesDto>,
    val name:String
)
data class IndustriesDto(
    val id:String,
    val name: String
)