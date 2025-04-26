package com.sundhar.eventplanner.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
class EventPlanerModel (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val date: Long,
    val time: String
    )