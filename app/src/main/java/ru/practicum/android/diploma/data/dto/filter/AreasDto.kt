package ru.practicum.android.diploma.data.dto.filter

data class AreasDto(
    val id:String,
    val name:String,
    val areas:List<RegionDto>
)
data class RegionDto(
    val id:String,
    val name:String,
    val parent_id:String
)