package com.example.crochetapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.crochetapp.databinding.ActivityAddPatternBinding

class AddPatternActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPatternBinding
    private var selectedImageUri: Uri? = null
    private var selectedPdfUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            Toast.makeText(this, "Image selected: $it", Toast.LENGTH_SHORT).show()
        }
    }

    private val pdfPickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedPdfUri = it
            Toast.makeText(this, "PDF selected: $it", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPatternBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up button click listeners
        
        binding.addFab.setOnClickListener {
            if (validateInputs()) {
                val pattern = savePattern()
                val resultIntent = Intent().apply {
                    putExtra("newPattern", pattern)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }

        binding.uploadImageButton.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.uploadPdfButton.setOnClickListener {
            pdfPickerLauncher.launch("application/pdf")
        }
    }

    private fun validateInputs(): Boolean {
        val patternName = binding.patternNameInput.text.toString().trim()
        return if (patternName.isEmpty()) {
            binding.patternNameInput.error = "Pattern name is required"
            false
        } else {
            true
        }
    }

    private fun savePattern(): CrochetPattern {
        val patternName = binding.patternNameInput.text.toString().trim()
        val patternCategory = binding.categorySpinner.selectedItem.toString()
        val patternPart = binding.partSpinner.selectedItem.toString()
        val patternDifficulty = binding.difficultySpinner.selectedItem.toString()

        return CrochetPattern(
            name = patternName,
            category = patternCategory,
            part = patternPart,
            difficulty = patternDifficulty,
            imageUri = selectedImageUri,
            pdfUri = selectedPdfUri
        )
    }
}
