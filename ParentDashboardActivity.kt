package com.parentguard.ui.parent

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.parentguard.databinding.ActivityParentDashboardBinding

class ParentDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardMessages.setOnClickListener { open(MessageLogActivity::class.java) }
        binding.cardScreenTime.setOnClickListener { open(ScreenTimeActivity::class.java) }
        binding.cardGeofence.setOnClickListener { open(GeofenceActivity::class.java) }
        binding.cardBrowserHistory.setOnClickListener { open(BrowserHistoryActivity::class.java) }
    }

    private fun open(target: Class<*>) = startActivity(Intent(this, target))
}
