package com.parentguard.ui.parent

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.parentguard.databinding.ActivityScreenTimeBinding
import com.parentguard.util.AppUsageHelper
import java.util.concurrent.TimeUnit

class ScreenTimeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScreenTimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }

        if (!AppUsageHelper.hasUsageAccess(this)) {
            binding.summary.text = "Usage access not granted."
            binding.grantAccessBtn.setOnClickListener {
                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            }
        } else {
            binding.grantAccessBtn.visibility = android.view.View.GONE
            renderUsage()
        }
    }

    private fun renderUsage() {
        val stats = AppUsageHelper.getDailyUsage(this).take(10)
        val sb = StringBuilder()
        for (stat in stats) {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(stat.totalTimeInForeground)
            sb.append("${stat.packageName}\n$minutes min\n\n")
        }
        binding.summary.text = if (sb.isEmpty()) "No usage data yet." else sb.toString()
    }
}
