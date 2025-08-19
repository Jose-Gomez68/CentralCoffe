package com.example.salestapapp.login.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.R
import com.example.salestapapp.databinding.ActivityLoginBinding
import com.example.salestapapp.login.data.UserRepository
import com.example.salestapapp.login.data.domain.GetUserLoginUseCase
import com.example.salestapapp.login.view.viewmodel.LoginUserViewModel
import com.example.salestapapp.login.view.viewmodel.LoginUserViewModelFactory
import com.example.salestapapp.menu.ui.view.MenuActivity
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.user.data.domain.usecase.EditUserUseCase
import com.example.salestapapp.user.data.domain.usecase.GetUserByIDUseCase
import com.example.salestapapp.user.ui.viewmodel.EditUserViewModel
import com.example.salestapapp.user.ui.viewmodel.EditUserViewModelFactory
import com.example.salestapapp.util.SharedPreferencesUtil
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var isPasswordVisible: Boolean = false
    private var sharedPref: SharedPreferencesUtil? = null
    private lateinit var db: CyberCoffeDatabase
    private lateinit var viewModel: LoginUserViewModel
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = CyberCoffeAppDatabase.CyberCoffeAppDatabase.getInstance(applicationContext)
        val loginRepository = UserRepository(db)
        val viewModelProviderFactory = LoginUserViewModelFactory(
            GetUserLoginUseCase(loginRepository)
        )

        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        )[LoginUserViewModel::class.java]


        sharedPref = SharedPreferencesUtil(applicationContext)
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
            binding.btnLogin.setTextColor(ContextCompat.getColor(this, R.color.gray))
            binding.btnLogin.background = ContextCompat.getDrawable(this, R.drawable.button_transparent)
        }

        // 👇 El listener se queda aquí, siempre se ejecuta
        binding.btnLogin.setOnClickListener {
            if (userText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.getUser(userText, passwordText)
            viewModel.loginUserModel.observe(this) { result ->
                if (result.id != 0) {
                    //Toast.makeText(this, "Inicio Sesion ${result.userName}", Toast.LENGTH_SHORT).show()
                    sharedPref!!.saveSession(result.id, result.userName, "${result.name} ${result.lastName}", result.userType)
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                    finish() // cerrar login
                } else {
                    //Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
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

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finishAffinity() // cierra toda la app
            return
        } else {
            Toast.makeText(this, "Presiona atrás de nuevo para salir", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

}