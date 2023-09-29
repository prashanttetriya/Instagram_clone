package com.example.instagram_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instagram_clone.Modles.user
import com.example.instagram_clone.Util.USER_NODE
import com.example.instagram_clone.Util.USER_PROFILE_FOLDER
import com.example.instagram_clone.Util.uploadimage
import com.example.instagram_clone.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpactivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    lateinit var user:user
    private var launcher=registerForActivityResult(ActivityResultContracts.GetContent()){
        uri->
        uri?.let {
            uploadimage(uri, USER_PROFILE_FOLDER){
                if(it==null){

                }else{
                    user.image=it
                    binding.profileimg.setImageURI(uri)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        user=user()
        binding.btnlogin.setOnClickListener {
            if ((binding.name.editText?.text.toString() == "") or
                (binding.email.editText?.text.toString() == "") or
                (binding.passwoed.editText?.text.toString() == "")
            ){
                Toast.makeText(this,"enter your all details",Toast.LENGTH_SHORT).show()
            }
            else{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.email.editText?.text.toString(),
                    binding.passwoed.editText?.text.toString()
                ).addOnCompleteListener{
                    result->
                    if(result.isSuccessful){
                       user.name=binding.name.editText?.text.toString()
                        user.email=binding.email.editText?.text.toString()
                        user.password=binding.passwoed.editText?.text.toString()
                        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).set(user).addOnCompleteListener {

                            Toast.makeText(this, "login Successful", Toast.LENGTH_SHORT).show()
                        }

                    }else{
                        Toast.makeText(this, result.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.addimage.setOnClickListener {
            launcher.launch("image/*")
        }
    }
}