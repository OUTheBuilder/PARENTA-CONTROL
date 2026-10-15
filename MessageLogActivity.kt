package com.parentguard.ui.parent

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.parentguard.data.SmsMessageItem
import com.parentguard.databinding.ActivityMessageLogBinding
import com.parentguard.ui.parent.adapter.MessageAdapter

/**
 * Reads on-device SMS via the standard Telephony content provider.
 *
 * IMPORTANT: Google Play only allows READ_SMS for apps that are the user's
 * default SMS handler (or a small set of exempt categories). Ship this only
 * if you've built the default-SMS-app flow, or gate it to sideloaded/
 * enterprise (MDM) distribution.
 */
class MessageLogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageLogBinding
    private val adapter = MessageAdapter()

    private val requestSmsPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) loadMessages() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
            == PackageManager.PERMISSION_GRANTED) {
            loadMessages()
        } else {
            requestSmsPermission.launch(Manifest.permission.READ_SMS)
        }
    }

    private fun loadMessages() {
        val items = mutableListOf<SmsMessageItem>()
        val uri = Uri.parse("content://sms/inbox")
        val projection = arrayOf("address", "body", "date")
        contentResolver.query(uri, projection, null, null, "date DESC LIMIT 100")?.use { cursor ->
            val addrIdx = cursor.getColumnIndexOrThrow("address")
            val bodyIdx = cursor.getColumnIndexOrThrow("body")
            val dateIdx = cursor.getColumnIndexOrThrow("date")
            while (cursor.moveToNext()) {
                items.add(
                    SmsMessageItem(
                        sender = cursor.getString(addrIdx) ?: "Unknown",
                        body = cursor.getString(bodyIdx) ?: "",
                        timestampMillis = cursor.getLong(dateIdx)
                    )
                )
            }
        }
        adapter.submitList(items)
    }
}
