package com.devmasterteam.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.room.PrimaryKey
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityLoginBinding
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.viewmodel.LoginViewModel
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var token: String
    private lateinit var personKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // Layout
        setContentView(binding.root)

        supportActionBar?.hide()

        // Eventos
        binding.buttonLogin.setOnClickListener(this)
        binding.textRegister.setOnClickListener(this)

        // Observadores
        observe()
    }

    override fun onResume() {
        viewModel.verifyLoggedUser()
        super.onResume()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_login) {
            handleLogin()
        } else if (v.id == R.id.text_register) {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }
    }

    private fun observe() {
        viewModel.login.observe(this) {
            if (it.status()) {
                biometricAuthentication()
//                startActivity(Intent(applicationContext, MainActivity::class.java))
//                finish()
            } else {
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loggedUser.observe(this) {
            if (it) {
                biometricAuthentication()
            }
        }


    }


    private fun biometricAuthentication() {
        val executor: Executor = ContextCompat.getMainExecutor(this)

        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
            })

        // Informações apresentadas no momento da autenticação
        val info: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.autenticacao_biometrica))
            // .setSubtitle("Subtítulo")
            // .setDescription("Descrição")
            .setNegativeButtonText(getString(R.string.cancelar))
            .build()

        // Exibe para o usuário
        biometricPrompt.authenticate(info)
    }


    private fun handleLogin() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        viewModel.doLogin(email, password)
    }


}