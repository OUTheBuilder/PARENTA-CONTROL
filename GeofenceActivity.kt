package com.parentguard.ui.parent

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.parentguard.data.GeoZone
import com.parentguard.databinding.ActivityGeofenceBinding
import com.parentguard.ui.parent.adapter.ZoneAdapter

/**
 * Lists safe zones stored in Firestore under users/{childUid}/zones.
 * Actual geofence *monitoring* (entering/exiting) is registered by
 * MonitoringService using the Play Services GeofencingClient, on the
 * child's device.
 */
class GeofenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGeofenceBinding
    private val adapter = ZoneAdapter()
    private val db = FirebaseFirestore.getInstance()

    // Replace with the actual child UID you're viewing (e.g. passed via Intent extra).
    private val childUid: String? get() = intent.getStringExtra("childUid")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeofenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.addZoneFab.setOnClickListener {
            Toast.makeText(this, "Hook this up to a map picker to add a zone", Toast.LENGTH_SHORT).show()
        }

        loadZones()
    }

    private fun loadZones() {
        val uid = childUid ?: return
        db.collection("users").document(uid).collection("zones")
            .get()
            .addOnSuccessListener { snapshot ->
                val zones = snapshot.documents.map { doc ->
                    doc.toObject(GeoZone::class.java)?.copy(id = doc.id) ?: GeoZone(id = doc.id)
                }
                adapter.submitList(zones)
            }
    }
}
