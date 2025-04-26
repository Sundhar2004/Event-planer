package com.sundhar.eventplanner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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


        binding.calendarView.setOnDateChangeListener{ _, year, month, day ->
            val date = String.format("%04d-%02d-%02d", year, month + 1, day)
            viewModel.setDate(date)

        }

        viewModel.eventsByDate.observe(this) { events ->
            adapter.submitList(events)
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
        val etDate = dialogView.findViewById<EditText>(R.id.etDate)

        var selectedTime = ""
        var selectedDate: String? = null

        val defaultDate = selectedDate ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
            Date()
        )
        etDate.setText(defaultDate)

        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedCal = Calendar.getInstance()
                    selectedCal.set(year, month, dayOfMonth)

                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    selectedDate = sdf.format(selectedCal.time)
                    etDate.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Set min date to today (disable past dates!)
            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
            datePicker.show()
        }

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

                val event = EventPlanerModel(title = title, description = desc, date = selectedDate!!, time = selectedTime)
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
        val etDate = dialogView.findViewById<EditText>(R.id.etDate)

        titleField.setText(event.title)
        descField.setText(event.description)
        timeField.setText(event.time)
        etDate.setText(event.date)

        var selectedTime = event.time
        var selectedDate: String? = null

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

        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedCal = Calendar.getInstance()
                    selectedCal.set(year, month, dayOfMonth)

                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    selectedDate = sdf.format(selectedCal.time)
                    etDate.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Set min date to today (disable past dates!)
            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
            datePicker.show()
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Event")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                showUpdateConfirmationDialog(event,titleField.text.toString(),descField.text.toString(),selectedDate ?: event.date,selectedTime)
                /*val updatedEvent = event.copy(
                    title = titleField.text.toString(),
                    description = descField.text.toString(),
                    time = selectedTime,
                    date = selectedDate!!
                )
                viewModel.update(updatedEvent)*/
            }
            .setNeutralButton("Delete") { _, _ ->
                showDeleteConfirmationDialog(event)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showUpdateConfirmationDialog(event: EventPlanerModel, newTitle: String, newDesc: String, newDate: String, newTime: String) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Update")
            .setMessage("Are you sure you want to update this event?")
            .setPositiveButton("Yes") { _, _ ->
                val updatedEvent = event.copy(
                    title = newTitle,
                    description = newDesc,
                    time = newTime,
                    date = newDate
                )
                viewModel.update(updatedEvent)
                Toast.makeText(this, "Event Updated!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(event: EventPlanerModel) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Delete")
            .setMessage("Are you sure you want to delete this event?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.delete(event)
                Toast.makeText(this, "Event Deleted!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }



}