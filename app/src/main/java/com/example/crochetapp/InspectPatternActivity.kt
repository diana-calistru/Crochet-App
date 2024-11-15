package com.example.crochetapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.crochetapp.databinding.ActivityInspectPatternBinding

class InspectPatternActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInspectPatternBinding

    // Define a launcher to get the result from EditPatternActivity
    private val editPatternLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val updatedPattern: CrochetPattern? =
                    result.data?.getParcelableExtra("updatedPattern")
                updatedPattern?.let {
                    // Update the UI with the new values
                    binding.patternNameInput.text = it.name
                    binding.categorySpinner.text = it.category
                    binding.partSpinner.text = it.part
                    binding.difficultySpinner.text = it.difficulty
                    binding.patternImage.setImageURI(it.imageUri)

                    // Set the result to pass back to PatternsFragment
                    val resultIntent = Intent().apply {
                        putExtra("updatedPattern", it)
                    }
                    setResult(RESULT_OK, resultIntent)

                    finish()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInspectPatternBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedPattern: CrochetPattern? = intent.getParcelableExtra("selectedPattern")

        selectedPattern?.let {
            binding.patternNameInput.text = it.name
            binding.categorySpinner.text = it.category
            binding.partSpinner.text = it.part
            binding.difficultySpinner.text = it.difficulty
            binding.patternImage.setImageURI(it.imageUri)
        }

        binding.openPdfButton.setOnClickListener {
            selectedPattern?.pdfUri?.let { pdfUri ->
                openPdf(pdfUri)
            }
        }

        binding.editPatternButton.setOnClickListener {
            // Open EditPatternActivity to allow editing the selected pattern
            val editIntent = Intent(this, EditPatternActivity::class.java)
            editIntent.putExtra("selectedPattern", selectedPattern)
            editPatternLauncher.launch(editIntent)
        }

        binding.deletePatternButton.setOnClickListener {
            selectedPattern?.let { pattern ->
                // Create and show a confirmation dialog
                AlertDialog.Builder(this)
                    .setTitle("Delete Pattern")
                    .setMessage("Are you sure you want to delete this pattern?")
                    .setPositiveButton("Delete") { dialog, _ ->
                        // Send the ID of the selected pattern back to the fragment
                        val resultIntent = Intent().apply {
                            putExtra("deletedPatternId", pattern.id.toString())
                        }
                        setResult(RESULT_OK, resultIntent)
                        dialog.dismiss()
                        finish() // Close the activity and return the result
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        // Dismiss the dialog without taking any action
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    private fun openPdf(pdfUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, pdfUri).apply {
            setDataAndType(pdfUri, "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        }
        startActivity(intent)
    }
}
