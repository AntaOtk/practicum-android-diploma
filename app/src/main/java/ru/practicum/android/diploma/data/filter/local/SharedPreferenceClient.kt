package ru.practicum.android.diploma.data.filter.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.domain.models.filter.Area
import ru.practicum.android.diploma.domain.models.filter.Country
import ru.practicum.android.diploma.domain.models.filter.Industry
import ru.practicum.android.diploma.util.SALARY_FLAG
import ru.practicum.android.diploma.util.SALARY_KEY
import ru.practicum.android.diploma.util.SELECTED_AREA_KEY
import ru.practicum.android.diploma.util.SELECTED_COUNTRY_KEY
import ru.practicum.android.diploma.util.SELECTED_INDUSTRY_KEY
import java.lang.reflect.Type

class SharedPreferenceClient(
    private val gson: Gson,
    private val sharedPreferences: SharedPreferences
) :
    LocalStorage {

    private val editor get() = sharedPreferences.edit()

    override fun setSalary(salary: String) {
        editor
            .putString(SALARY_KEY, salary)
            .apply()
    }

    override fun getSalary(): String {
        return sharedPreferences.getString(SALARY_KEY, "") ?: ""
    }

    override fun setSelectedCountry(country: Country?) {
        val countryJson = gson.toJson(country)
        editor
            .putString(SELECTED_COUNTRY_KEY, countryJson)
            .apply()
    }

    override fun getSelectedCountry(): Country? {
        val countryJson = sharedPreferences.getString(SELECTED_COUNTRY_KEY, null)
        return gson.fromJson(countryJson, Country::class.java)
    }

    override fun setSelectedArea(area: Area?) {
        val areaJson = gson.toJson(area)
        editor
            .putString(SELECTED_AREA_KEY, areaJson)
            .apply()
    }

    override fun getSelectedArea(): Area? {
        val areaJson = sharedPreferences.getString(SELECTED_AREA_KEY, null)
        return gson.fromJson(areaJson, Area::class.java)
    }

    override fun setSelectedIndustries(industries: List<Industry>?) {
        val industryJson = gson.toJson(industries)
        editor
            .putString(SELECTED_INDUSTRY_KEY, industryJson)
            .apply()
    }

    override fun getSelectedIndustries(): MutableList<Industry>? {
        val industryJson =
            sharedPreferences.getString(SELECTED_INDUSTRY_KEY, null) ?: return mutableListOf()
        val listOfMyClassObject: Type = object : TypeToken<ArrayList<Industry>?>() {}.type
        return gson.fromJson(industryJson, listOfMyClassObject)

    }

    override fun clear() {
        editor.clear().apply()
    }

    override fun getCheckedStatus(): Boolean {
        return sharedPreferences.getBoolean(SALARY_FLAG, false)
    }

    override fun setCheckedStatus(isChecked: Boolean) {
        editor.putBoolean(SALARY_FLAG, isChecked).apply()
    }

    override fun removeSelectedCountry() {
        editor.remove(SELECTED_COUNTRY_KEY).apply()
    }

    override fun removeSelectedArea() {
        editor.remove(SELECTED_AREA_KEY).apply()
    }

    override fun removeSelectedIndustry() {
        editor.remove(SELECTED_INDUSTRY_KEY).apply()
    }
}
