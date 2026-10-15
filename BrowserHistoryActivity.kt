package com.parentguard.ui.parent

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.parentguard.data.HistoryItem
import com.parentguard.databinding.ActivityBrowserHistoryBinding
import com.parentguard.ui.parent.adapter.HistoryAdapter

/**
 * NOTE ON PLATFORM LIMITS:
 * Since Android 7, third-party apps cannot read another app's (e.g. Chrome's)
 * browsing history via a content provider on a non-rooted device — there is
 * no public API for this. The only reliable, policy-compliant ways to see
 * browsing activity are:
 *   1) Ship your own in-app browser and log URLs yourself (what's done here
 *      as a placeholder data source), or
 *   2) Use a DNS/VPN-based content filter (e.g. implement VpnService) to log
 *      visited domains at the network layer, or
 *   3) Enroll the device under Android Enterprise / a licensed MDM, which can
 *      expose managed-browser history through its own reporting APIs.
 * This screen reads from your own Firestore "browsingHistory" collection,
 * which you'd populate via option 1 or 2 above — it does not reach into
 * another browser's private data.
 */
class BrowserHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBrowserHistoryBinding
    private val adapter = HistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowserHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        loadFromFirestore()
    }

    private fun loadFromFirestore() {
        val uid = intent.getStringExtra("childUid") ?: return
        com.google.firebase.firestore.FirebaseFirestore.getInstance()
            .collection("users").document(uid).collection("browsingHistory")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(100)
            .get()
            .addOnSuccessListener { snap ->
                val items = snap.documents.map { d ->
                    HistoryItem(
                        domain = d.getString("domain") ?: "unknown",
                        timestampMillis = d.getLong("timestamp") ?: 0L
                    )
                }
                adapter.submitList(items)
            }
    }
}
