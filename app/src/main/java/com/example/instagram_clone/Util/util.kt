package com.example.instagram_clone.Util

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

fun uploadimage(uri: Uri,folderName:String,callback:(String?)->Unit){
    var imageUrl:String?=null

    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnCompleteListener{
                imageUrl=it.toString()
                callback(imageUrl)

            }
    }
 }
