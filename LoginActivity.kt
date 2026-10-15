package com.parentguard.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.parentguard.data.FirebaseRepository
import com.parentguard.databinding.ActivityLoginBinding
import com.parentguard.ui.child.ChildDashboardActivity
import com.parentguard.ui.parent.ParentDashboardActivity
import com.parentguard.util.Role
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val repo = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener { attemptLogin() }
        binding.goRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun attemptLogin() {
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            val result = repo.login(email, password)
            result.onSuccess { uid ->
                val role = repo.getRole(uid)
                val target = if (role == Role.PARENT) ParentDashboardActivity::class.java
                             else ChildDashboardActivity::class.java
                startActivity(Intent(this@LoginActivity, target))
                finish()
            }.onFailure {
                Toast.makeText(this@LoginActivity, it.message ?: "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
