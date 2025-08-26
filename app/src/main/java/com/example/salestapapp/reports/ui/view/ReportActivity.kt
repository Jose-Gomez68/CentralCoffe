package com.example.salestapapp.reports.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.salestapapp.R
import com.example.salestapapp.databinding.ActivityReportBinding

class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnReportCaja.setOnClickListener {
            val intent = Intent(this, CashCutActivity::class.java)
            startActivity(intent)
        }

    }
}