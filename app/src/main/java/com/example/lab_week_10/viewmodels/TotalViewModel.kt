package com.example.lab_week_10.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab_week_10.database.TotalObject
import java.text.SimpleDateFormat
import java.util.*

class TotalViewModel : ViewModel() {

    private val _total = MutableLiveData<TotalObject>()
    val total: LiveData<TotalObject> = _total

    init {
        _total.value = TotalObject(
            value = 0,
            date = currentTime()
        )
    }

    fun incrementTotal() {
        val current = _total.value!!
        _total.value = current.copy(
            value = current.value + 1,
            date = currentTime()
        )
    }

    fun setTotal(obj: TotalObject) {
        _total.value = obj
    }

    private fun currentTime(): String {
        return SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date())
    }
}
