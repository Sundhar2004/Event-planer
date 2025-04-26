package com.sundhar.eventplanner.roomDb

import androidx.room.Insert
import androidx.room.Update
import com.sundhar.eventplanner.model.EventPlanerModel

interface EventPlanerDao {
    @Insert
    suspend fun insert(event: EventPlanerModel)

    @Update
    suspend fun update(event: EventPlanerModel)
}