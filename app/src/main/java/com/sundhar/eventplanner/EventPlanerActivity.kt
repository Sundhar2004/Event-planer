package com.sundhar.eventplanner

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sundhar.eventplanner.adapter.EventAdapter
import com.sundhar.eventplanner.databinding.ActivityEventPlanerBinding

class EventPlanerActivity : AppCompatActivity() {
    lateinit var binding: ActivityEventPlanerBinding
    private lateinit var eventViewModel: EventViewModel
    private lateinit var eventAdapter: EventAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEventPlanerBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}