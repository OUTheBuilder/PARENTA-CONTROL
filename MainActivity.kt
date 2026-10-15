package com.parentguard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.parentguard.data.FirebaseRepository
import com.parentguard.ui.auth.LoginActivity
import com.parentguard.ui.child.ChildDashboardActivity
import com.parentguard.ui.parent.ParentDashboardActivity
import com.parentguard.util.Role
import kotlinx.coroutines.launch

/** Entry point: routes to Login, or straight to the right dashboard if already signed in. */
class MainActivity : AppCompatActivity() {

    private val repo = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uid = repo.currentUserId
        if (uid == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        lifecycleScope.launch {
            val role = repo.getRole(uid)
            val target = if (role == Role.PARENT) ParentDashboardActivity::class.java
                         else ChildDashboardActivity::class.java
            startActivity(Intent(this@MainActivity, target))
            finish()
        }
    }
}
