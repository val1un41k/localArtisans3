package com.example.localartisan3.components

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage

import java.io.ByteArrayOutputStream

@Composable
fun selectPhotoFromGallaryforProfile(uid: String) {

    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri.value = uri
    }
    Button(onClick = { pickImageLauncher.launch("image/*") }) {
        Text("Select Photo")

        Spacer(modifier = Modifier.height(10.dp))
    }

    imageUri.value?.let { uri ->
        // Display the selected image
        val image: Painter = rememberImagePainter(data = uri)
        Image(painter = image, contentDescription = "Selected Image")

        Button(onClick = { uploadProfileImageToFirebaseStorage(context, uri, uid ) }) {
            Text("Upload Image")


        }
    }
}

@Composable

fun selectPhotoFromGallaryforProduct(uid: String, productName: String){

    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri.value = uri
    }
    Button(onClick = { pickImageLauncher.launch("image/*") }) {
        Text("Select Photo")

        Spacer(modifier = Modifier.height(10.dp))
    }

    imageUri.value?.let { uri ->
        // Display the selected image
        val image: Painter = rememberImagePainter(data = uri)
        Image(painter = image, contentDescription = "Selected Image")

        Button(onClick = { uploadProductImageToFirebaseStorage(context, uri, uid, productName) }) {
            Text("Upload Image")


        }
    }
}


fun uploadProfileImageToFirebaseStorage(context: Context, imageUri: Uri, uid: String) {
    // Get the image from the URI
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
    val data = baos.toByteArray()

    val storageRef = Firebase.storage.reference

    // Create a reference to the file you want to upload
    val imageRef = storageRef.child("images/${uid}_profile.jpg")

    val uploadTask = imageRef.putBytes(data)
    uploadTask.addOnFailureListener {
        // Handle unsuccessful uploads
        Log.d(TAG, "Image upload failed")
        Toast.makeText(context, "Image upload failed", Toast.LENGTH_LONG).show()
    }.addOnSuccessListener { taskSnapshot ->
        // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
        Log.d(TAG, "Image uploaded successfully")
        Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_LONG).show()

        // After a successful upload, get the download URL
        imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
            // Now you have the download URL, you can store it in Firestore
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("artisan").document(uid)

            var update = hashMapOf(
                "profile_image_url" to downloadUri.toString()
            )

            docRef.update(update as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated with image URL!")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error updating document", e)
                }
        }
    }
}

fun uploadProductImageToFirebaseStorage(context: Context, imageUri: Uri, uid: String, productCategory: String) {
    // Get the image from the URI
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
    val data = baos.toByteArray()

    val storageRef = Firebase.storage.reference

    // Create a reference to the file you want to upload
    val imageRef = storageRef.child("images/${uid}_${productCategory}_profile.jpg")

    val uploadTask = imageRef.putBytes(data)
    uploadTask.addOnFailureListener {
        // Handle unsuccessful uploads
        Log.d(TAG, "Image upload failed")
        Toast.makeText(context, "Image upload failed", Toast.LENGTH_LONG).show()
    }.addOnSuccessListener { taskSnapshot ->
        // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
        Log.d(TAG, "Image uploaded successfully")
        Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_LONG).show()

        // After a successful upload, get the download URL
        imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
            // Now you have the download URL, you can store it in Firestore
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("artisan_product_item_within_category").document(uid)

            var update = hashMapOf(
                "productImage" to downloadUri.toString()
            )

            docRef.update(update as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated with image URL!")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error updating document", e)
                }
        }
    }
}


//
//@Composable
//fun UploadImageToFirebase(uid: String) {
//    val context = LocalContext.current
//    val imageUri = remember { mutableStateOf<Uri?>(null) }
//
//    val pickImageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
//        imageUri.value = uri
//    }
//
//    Button(onClick = { pickImageLauncher.launch("image/*") }) {
//        Text("Select Photo")
//    }
//
//    imageUri.value?.let { uri ->
//        Button(onClick = { uploadImageToFirebase(context, uri, uid) }) {
//            Text("Upload Image")
//        }
//    }
//}
//
//suspend fun uploadImageToFirebase(context: Context, imageUri: Uri, uid: String) {
//    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
//    val baos = ByteArrayOutputStream()
//    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//    val data = baos.toByteArray()
//
//    val storageRef = Firebase.storage.reference
//    val imageRef = storageRef.child("images/$uid/${UUID.randomUUID()}.jpg")
//
//    val uploadTask = imageRef.putBytes(data)
//    uploadTask.addOnFailureListener {
//        // Handle unsuccessful uploads
//    }.addOnSuccessListener { taskSnapshot ->
//        // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
//        // ...
//    }
//}

//@Composable
//fun SelectPhotoFromGallery() {
//  val context = LocalContext.current
//  val imageUri = remember { mutableStateOf<Uri?>(null) }
//
//  val pickImageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
//    imageUri.value = uri
//  }
//
//  Button(onClick = { pickImageLauncher.launch("image/*") }) {
//    Text("Select Photo")
//  }
//
//  imageUri.value?.let { uri ->
//    val image: Painter = rememberImagePainter(data = uri)
//    Image(painter = image, contentDescription = "Selected Image")
//  }
//}

//    galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
//            // Get image URI
//            val imageUri = result.data?.data
//
//            // Get a reference to Firestore
//            val db = FirebaseFirestore.getInstance()
//
//            // Create a reference to the image file
//            val fileName = "${editTextName.text}.jpg"
//            val imageRef = db.collection("images").document(fileName)
//
//            // Upload the image file to Firestore
//            val uploadTask = imageRef.set(imageUri)
//            uploadTask.addOnSuccessListener {
//                // Get the download URL of the uploaded image
//                imageRef.get().addOnSuccessListener { document ->
//                    if (document != null) {
//                        svcPic = document.data.toString()
//                        val descRef = db.collection("Businesses")
//                        descRef.document(uid).collection("services").document(currentSnap).update("svcPic", svcPic)
//                    }
//                }
//            }
//
//            Toast.makeText(this, "Image uploaded", Toast.LENGTH_LONG).show()
//        }
//    }
//    uploadPicBtn.setOnClickListener {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        galleryLauncher.launch(intent)
//    }


//    galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
//            // Get image URI
//            val imageUri = result.data?.data
//
//            // Get a reference to Firebase Storage
//            val storageRef = FirebaseStorage.getInstance().reference
//
//            // Create a reference to the image file
//            val fileName = "${editTextName.text}.jpg"
//            val imageRef = storageRef.child("images/$fileName")
//
//            // Upload the image file to Firebase Storage
//            val uploadTask = imageRef.putFile(imageUri!!)
//            uploadTask.addOnSuccessListener { taskSnapshot ->
//                // Get the download URL of the uploaded image
//                imageRef.downloadUrl.addOnSuccessListener { uri ->
//                    svcPic = uri.toString()
//                    val descRef = database.getReference("Businesses")
//                    descRef.child(uid).child("services").child(currentSnap).child("svcPic").setValue(svcPic)
//                }
//            }
//
//            Toast.makeText(this, "Image uploaded", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    uploadPicBtn.setOnClickListener {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        galleryLauncher.launch(intent)
//    }

