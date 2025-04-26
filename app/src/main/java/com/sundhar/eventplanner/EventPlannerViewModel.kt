package com.sundhar.eventplanner

class EventPlannerViewModel {
}

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventRepository
    val allEvents: LiveData<List<Event>>

    init {
        val eventDao = AppDatabase.getDatabase(application).eventDao()
        repository = EventRepository(eventDao)
        allEvents = repository.allEvents
    }

    fun getEventsByDate(date: Long): LiveData<List<Event>> {
        return repository.getEventsByDate(date)
    }

    fun insert(event: Event) {
        viewModelScope.launch {
            repository.insert(event)
        }
    }

    fun update(event: Event) {
        viewModelScope.launch {
            repository.update(event)
        }
    }

    fun delete(event: Event) {
        viewModelScope.launch {
            repository.delete(event)
        }
    }
}
