package com.example.crochetapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.UUID

class PatternViewModel : ViewModel() {
    var patternMutableLiveData: MutableLiveData<ArrayList<CrochetPattern>?> = MutableLiveData()
    private var patternArrayList: ArrayList<CrochetPattern>? = null

    fun init() {
        populateList()
        patternMutableLiveData.value = patternArrayList
    }

    fun add(pattern: CrochetPattern) {
        patternArrayList?.add(pattern)
        patternMutableLiveData.value = patternArrayList
    }

    fun updatePattern(updatedPattern: CrochetPattern) {
        val currentList = patternMutableLiveData.value ?: return

        println("Updated pattern id: ${updatedPattern.id}")
        currentList.forEach { println("Current list id: ${it.id}") }

        val index = currentList.indexOfFirst { it.id == updatedPattern.id }
        if (index != -1) {
            currentList[index] = updatedPattern
            patternMutableLiveData.value = currentList
        }
    }

    fun deletePatternById(patternId: String) {
        val currentList = patternMutableLiveData.value ?: return
        val updatedList = currentList.filter { it.id != UUID.fromString(patternId) }
        patternMutableLiveData.value = ArrayList(updatedList)
    }

    fun getPatterns(): ArrayList<CrochetPattern>? {
        return patternArrayList
    }

    private fun populateList() {
        patternArrayList = ArrayList()

        var pattern = CrochetPattern("Bell Sleeves", "Sweater", "Sleeves", "Intermediate", null, null)
        patternArrayList!!.add(pattern)

        pattern = CrochetPattern("Ribbed Trim", "Sweater", "Trim", "Beginner", null, null)
        patternArrayList!!.add(pattern)

        pattern = CrochetPattern("Cropped", "Sweater", "Torso", "Intermediate", null, null)
        patternArrayList!!.add(pattern)
    }
}