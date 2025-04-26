package com.sundhar.eventplanner

import android.app.Application
import android.view.animation.Transformation
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.sundhar.eventplanner.model.EventPlanerModel
import com.sundhar.eventplanner.roomDb.EventPlanerDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class EventPlannerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventPlanerRepository
    private val selected_Date = MutableLiveData<String>()


    val eventsByDate: LiveData<List<EventPlanerModel>>
    val allUpcomingEvents: LiveData<List<EventPlanerModel>>


    init {
        val eventDao = EventPlanerDatabase.getDatabase(application).eventDao()
        repository = EventPlanerRepository(eventDao)
        selected_Date.value = getTodayDate()

        eventsByDate = selected_Date.switchMap{ date ->
            repository.getEventsByDate(date)
        }
        allUpcomingEvents = repository.getAllUpcomingEvents()
    }

    fun setDate(date: String) {
        selected_Date.value = date
    }

    private fun getTodayDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun insert(event: EventPlanerModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(event)
        }
    }

    fun update(event: EventPlanerModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(event)
        }
    }

    fun delete(event: EventPlanerModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(event)
        }
    }
}
