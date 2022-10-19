package com.salad.latte.ClientManagement

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.salad.latte.ClientManagement.ViewModels.ActivitySignupViewModel
import com.salad.latte.Objects.Client
import com.salad.latte.databinding.ActivitySignupBinding
import kotlinx.coroutines.launch

class SignupActivity: AppCompatActivity() {
    lateinit var binding : ActivitySignupBinding
    lateinit var viewModel : ActivitySignupViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        viewModel = ActivitySignupViewModel()
        setContentView(binding.root)

        binding.apply {
            btnSignupCancel.setOnClickListener {
                finish()
            }
            btnSignup.setOnClickListener {
                if(isValidFirstName()){
                    if(isValidLastName()){
                        if(isValidEmail()){

                        }
                        else {
                            displayToast("Invalid email, please include @ and .")
                        }
                        if(isValidPassword()){
                            //Signup
                            var client = Client()
                            client.client_name = etFirstname.text.toString()
                            client.client_email = etEmailSignup.text.toString()

                            lifecycleScope.launch {
                                viewModel.createClient(client, etPasswordSignup.text.toString(), this@SignupActivity)
                            }

                        }else
                        {
                            displayToast("password must be atleast 6 characters ")
                        }
                    }
                    else {
                        displayToast("Empty last name")
                    }
                } else {
                    displayToast("Empty first name")
                }
            }
        }
    }

    fun displayToast(msg :String){
        Log.d("SignupActivity",msg)
        Toast.makeText(baseContext,msg,Toast.LENGTH_LONG).show()
    }
    fun isValidFirstName() : Boolean {
        if(binding.etFirstname.text.toString().length > 0){
            return true
        }
        return false
    }

    fun isValidLastName() : Boolean {
        if(binding.etLastname.text.toString().length > 0){
            return true
        }
        return false
    }
    fun isValidEmail() : Boolean {
        var email = binding.etEmailSignup.text.toString()
        if(email.length > 0 && email.contains("@") && email.contains(".")){
            return true
        }
        return false
    }
    fun isValidPassword() : Boolean {
        var password = binding.etPasswordSignup.text.toString()
        var confirmPass = binding.etPasswordRetypeSignup.text.toString()
        if(password.length > 5 && password.equals(confirmPass)){
            return true
        }
        return false
    }
}