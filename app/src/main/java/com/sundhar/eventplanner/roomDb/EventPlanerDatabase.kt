package com.sundhar.eventplanner.roomDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sundhar.eventplanner.model.EventPlanerModel

@Database(entities = [EventPlanerModel::class], version = 2)
abstract class EventPlanerDatabase: RoomDatabase() {
    abstract fun eventDao(): EventPlanerDao

    companion object {
        @Volatile private var INSTANCE: EventPlanerDatabase? = null

        fun getDatabase(context: Context): EventPlanerDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    EventPlanerDatabase::class.java,
                    "event_database"
                )
                    .build().also { INSTANCE = it }
            }
        }

    }
}