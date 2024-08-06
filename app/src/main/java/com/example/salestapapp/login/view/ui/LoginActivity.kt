package com.example.salestapapp.login.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.example.salestapapp.R
import com.example.salestapapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var isPasswordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTogglePasswordVisibility.setOnClickListener {
            togglePasswordVisibility()
        }

        setupTextWatchers()

        /*binding.btnLogin.setTextColor(ContextCompat.getColor(this, R.color.blue))
        binding.btnLogin.background = ContextCompat.getDrawable(this, R.drawable.button_habiliti_login)*/

    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateFields()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etUserLogin.addTextChangedListener(textWatcher)
        binding.etPasswordLogin.addTextChangedListener(textWatcher)
    }

    private fun validateFields() {
        val userText = binding.etUserLogin.text.toString().trim()
        val passwordText = binding.etPasswordLogin.text.toString().trim()

        if (userText.isNotEmpty() && passwordText.isNotEmpty()) {
            // Ambos campos no están vacíos, habilita el botón
            binding.btnLogin.setTextColor(ContextCompat.getColor(this, R.color.blue))
            binding.btnLogin.background = ContextCompat.getDrawable(this, R.drawable.button_habiliti_login)
        } else {
            // Al menos uno de los campos está vacío, deshabilita el botón
            binding.btnLogin.setTextColor(ContextCompat.getColor(this, R.color.gray)) // O el color deshabilitado
            binding.btnLogin.background = ContextCompat.getDrawable(this, R.drawable.button_transparent) // Asegúrate de definir este drawable
            binding.btnLogin.setOnClickListener {
                Toast.makeText(this, "iniciar sesion",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            binding.etPasswordLogin.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.btnTogglePasswordVisibility.setImageResource(R.drawable.baseline_remove_red_eye_24)
        } else {
            binding.etPasswordLogin.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.btnTogglePasswordVisibility.setImageResource(R.drawable.baseline_remove_red_eye_24)
            //validar los campos que sean correctos y que la contraseña sea correcta
        }
        binding.etPasswordLogin.setSelection(binding.etPasswordLogin.text.length)
        isPasswordVisible = !isPasswordVisible
    }

}