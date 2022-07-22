package com.salad.latte

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.R

class FeebackActivity : AppCompatActivity() {

    lateinit var cancelBtn :Button
    lateinit var submitBtn :Button
    lateinit var emailEt :EditText
    lateinit var feedbackEt :EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback);
        cancelBtn = findViewById(R.id.cancel_feedback_btn)
        submitBtn = findViewById(R.id.cancel_feedback_btn)
        cancelBtn.setOnClickListener(View.OnClickListener {
            finish()
        })
        submitBtn.setOnClickListener(View.OnClickListener {
            finish()
            if(isEmailValid(emailEt.text.toString())){
             if(isFeedbackValid(feedbackEt.text.toString())){
                 //Valid, send to firebase
                     var fb = FirebaseDB()
                 fb.sendFeedback(emailEt.text.toString(),feedbackEt.text.toString())
                 val builder = AlertDialog.Builder(this)
                 builder.setTitle("Feeback Sent!")
                 builder.setMessage("We will reply to your feedback/comment shortly, thank you")
                 builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
                 builder.show()
             }
                else{
                    //Feedback Invalid
                 val builder = AlertDialog.Builder(this)
                 builder.setTitle("Feeback Invalid!")
                 builder.setMessage("Please enter atleast 3 characters for feedback to be sent")
                 builder.setPositiveButton("OK",null)
                 builder.show()
             }
            }else {
                //Email Invalid
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Email Invalid!")
                builder.setMessage("Please ensure email is atleast 3 characters long and @ is in the email address")
                builder.setPositiveButton("OK",null)
                builder.show()
            }
        })
    }

    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        finish()
    }
    fun isEmailValid(email :String) : Boolean{
        if(email.length < 3 || !email.contains("@"))
        {
            return false
        }
        return true
    }
    fun isFeedbackValid(feedback :String) : Boolean {
        if(feedback.length < 3)
        {
            return false
        }
        return true
    }


}
