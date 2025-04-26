package com.sundhar.eventplanner

import com.sundhar.eventplanner.model.EventPlanerModel
import com.sundhar.eventplanner.roomDb.EventPlanerDao

class EventPlanerRepository(private val dao: EventPlanerDao) {

    fun getEventsByDate(date: String) = dao.getEventsByDate(date)

    fun getAllUpcomingEvents() = dao.getAllUpcomingEvents()

     fun insert(event: EventPlanerModel) = dao.insert(event)

     fun update(event: EventPlanerModel) = dao.update(event)

     fun delete(event: EventPlanerModel) = dao.delete(event)

}