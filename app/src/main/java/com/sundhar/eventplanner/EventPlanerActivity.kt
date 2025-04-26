package com.sundhar.eventplanner

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.util.copy
import com.sundhar.eventplanner.adapter.EventAdapter
import com.sundhar.eventplanner.databinding.ActivityEventPlanerBinding
import com.sundhar.eventplanner.model.EventPlanerModel
import java.util.Calendar

class EventPlanerActivity : AppCompatActivity() {
    lateinit var binding: ActivityEventPlanerBinding
    private lateinit var viewModel: EventPlannerViewModel
    private lateinit var adapter: EventAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEventPlanerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[EventPlannerViewModel::class.java]

        adapter = EventAdapter { event ->
              showEditDialog(event)
        }

        binding.eventRV.adapter = adapter
        binding.eventRV.layoutManager = LinearLayoutManager(this)

        viewModel.allUpcomingEvents.observe(this) { events ->
            adapter.submitList(events)
        }

        binding.calendarView.setOnDateChangeListener{ _, year, month, day ->
            val date = String.format("%04d-%02d-%02d", year, month + 1, day)
            viewModel.setDate(date)

        }

        binding.addEventFab.setOnClickListener {
            showAddDialog()
        }

    }

    private fun showAddDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.event_add_dialog, null)
        val titleField = dialogView.findViewById<EditText>(R.id.eventTitle)
        val descField = dialogView.findViewById<EditText>(R.id.eventDescription)
        val timeField = dialogView.findViewById<EditText>(R.id.eventTime)

        var selectedTime = ""

        timeField.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, minute ->
                selectedTime = String.format("%02d:%02d", hour, minute)
                timeField.setText(selectedTime)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        AlertDialog.Builder(this)
            .setTitle("Add Event")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val title = titleField.text.toString()
                val desc = descField.text.toString()
                val date = viewModel.selectedDate.value ?: return@setPositiveButton

                val event = EventPlanerModel(title = title, description = desc, date = date, time = selectedTime)
                viewModel.insert(event)

            }
            .setNegativeButton("Cancel", null)
            .show()

    }

    private fun showEditDialog(event: EventPlanerModel) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.event_add_dialog, null)
        val titleField = dialogView.findViewById<EditText>(R.id.eventTitle)
        val descField = dialogView.findViewById<EditText>(R.id.eventDescription)
        val timeField = dialogView.findViewById<EditText>(R.id.eventTime)

        titleField.setText(event.title)
        descField.setText(event.description)
        timeField.setText(event.time)

        var selectedTime = event.time

        timeField.setOnClickListener {
            val calendar = Calendar.getInstance()
            val parts = selectedTime.split(":")
            val hour = parts[0].toIntOrNull() ?: calendar.get(Calendar.HOUR_OF_DAY)
            val minute = parts.getOrNull(1)?.toIntOrNull() ?: calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, h, m ->
                selectedTime = String.format("%02d:%02d", h, m)
                timeField.setText(selectedTime)
            }, hour, minute, true).show()
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Event")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedEvent = event.copy(
                    title = titleField.text.toString(),
                    description = descField.text.toString(),
                    time = selectedTime
                )
                viewModel.update(updatedEvent)
            }
            .setNeutralButton("Delete") { _, _ ->
                viewModel.delete(event)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}