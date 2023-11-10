package ru.practicum.android.diploma.domain.models.filter

data class Industry(
    override val id: String,
    override val name: List<Unit>,
    val industries: String = null,
    override var isChecked: Boolean = false
) : IndustryAreaModel(id, name)
