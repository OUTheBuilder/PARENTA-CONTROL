package com.parentguard.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.parentguard.data.FirebaseRepository
import com.parentguard.databinding.ActivityRegisterBinding
import com.parentguard.util.Role
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val repo = FirebaseRepository()
    private var selectedRole = Role.PARENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.parentToggle.setOnClickListener { selectedRole = Role.PARENT; refreshToggle() }
        binding.childToggle.setOnClickListener { selectedRole = Role.CHILD; refreshToggle() }
        refreshToggle()

        binding.registerBtn.setOnClickListener { attemptRegister() }
    }

    private fun refreshToggle() {
        binding.parentToggle.isSelected = selectedRole == Role.PARENT
        binding.childToggle.isSelected = selectedRole == Role.CHILD
    }

    private fun attemptRegister() {
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString()
        val name = binding.nameInput.text.toString().trim().ifEmpty { "New User" }

        if (email.isEmpty() || password.length < 6) {
            Toast.makeText(this, "Enter a valid email and a 6+ char password", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            repo.register(email, password, selectedRole, name)
                .onSuccess {
                    Toast.makeText(this@RegisterActivity, "Account created. Please log in.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .onFailure {
                    Toast.makeText(this@RegisterActivity, it.message ?: "Registration failed", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
