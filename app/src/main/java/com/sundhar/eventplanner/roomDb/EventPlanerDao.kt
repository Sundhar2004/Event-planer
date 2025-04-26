package com.sundhar.eventplanner.roomDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sundhar.eventplanner.model.EventPlanerModel

@Dao
interface EventPlanerDao {
    @Insert
     fun insert(event: EventPlanerModel)

    @Update
     fun update(event: EventPlanerModel)

    @Delete
     fun delete(event: EventPlanerModel)

    @Query("SELECT * FROM events WHERE date = :date")
    fun getEventsByDate(date: String): LiveData<List<EventPlanerModel>>

    @Query("SELECT * FROM events ORDER BY date, time")
    fun getAllUpcomingEvents(): LiveData<List<EventPlanerModel>>
}