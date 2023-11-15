package ru.practicum.android.diploma.data.filter.local

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.models.filter.Area
import ru.practicum.android.diploma.domain.models.filter.Country
import ru.practicum.android.diploma.domain.models.filter.Industry
import ru.practicum.android.diploma.util.SALARY_KEY
import ru.practicum.android.diploma.util.SELECTED_AREA_KEY
import ru.practicum.android.diploma.util.SELECTED_COUNTRY_KEY
import ru.practicum.android.diploma.util.SELECTED_INDUSTRY_KEY

class SharedPreferensClient(val gson: Gson, private val sharedPreferences: SharedPreferences) :
    LocalStorage {

    override fun setSalary(salary: String) {
        sharedPreferences.edit()
            .putString(SALARY_KEY, salary)
            .apply()
    }

    override fun getSalary(): String {
        return sharedPreferences.getString(SALARY_KEY, "") ?: ""
    }

    override fun setSelectedCountry(country: Country?) {
        val countryJson = gson.toJson(country)
        sharedPreferences.edit()
            .putString(SELECTED_COUNTRY_KEY, countryJson)
            .apply()
    }

    override fun getSelectedCountry(): Country? {
        val countryJson = sharedPreferences.getString(SELECTED_COUNTRY_KEY, null)
        return gson.fromJson(countryJson, Country::class.java)
    }

    override fun setSelectedArea(area: Area?) {
        val areaJson = gson.toJson(area)
        sharedPreferences.edit()
            .putString(SELECTED_AREA_KEY, areaJson)
            .apply()
    }

    override fun getSelectedArea(): Area? {
        val areaJson = sharedPreferences.getString(SELECTED_AREA_KEY, null)
        return gson.fromJson(areaJson, Area::class.java)
    }

    override fun setSelectedIndustry(industry: Industry?) {
        val industryJson = gson.toJson(industry)
        sharedPreferences.edit()
            .putString(SELECTED_AREA_KEY, industryJson)
            .apply()
    }

    override fun getSelectedIndustry(): Industry? {
        val industryJson = sharedPreferences.getString(SELECTED_INDUSTRY_KEY, null)
        return gson.fromJson(industryJson, Industry::class.java)
    }

}
