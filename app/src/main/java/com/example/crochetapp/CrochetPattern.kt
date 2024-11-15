package com.example.crochetapp

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class CrochetPattern (
    val name: String,
    val category: String,
    val part: String,
    val difficulty: String,
    val imageUri: Uri?,
    val pdfUri: Uri?,
    var id: UUID? = UUID. randomUUID()
) : Parcelable