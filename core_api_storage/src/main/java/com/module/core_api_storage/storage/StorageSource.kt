package com.module.core_api_storage.storage

import com.google.firebase.storage.FirebaseStorage

object StorageSource {
    fun getStorageDownloadUrl(
        path: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val ref = FirebaseStorage.getInstance()
            .reference
            .child(path)

        ref.downloadUrl
            .addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    }
}