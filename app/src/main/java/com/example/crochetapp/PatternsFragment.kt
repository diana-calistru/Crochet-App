package com.example.crochetapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import java.util.*
import com.example.crochetapp.databinding.FragmentPatternsBinding

class PatternsFragment : Fragment(), RecyclerViewInterface {

    private var viewModel: PatternViewModel? = null
    private lateinit var binding: FragmentPatternsBinding
    private var patternAdapter: CrochetPatternRVAdapter? = null
    private var originalPatternId: UUID? = null

    private val addPatternLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                viewModel!!.add(intent?.getParcelableExtra("newPattern")!!)
            }
        }

    private val inspectPatternLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    // Check if an updated pattern was returned
                    val updatedPattern: CrochetPattern? = intent.getParcelableExtra("updatedPattern")
                    if (updatedPattern != null) {
                        updatedPattern.id = originalPatternId // Ensure the ID is set
                        viewModel?.updatePattern(updatedPattern)
                    }

                    // Check if a deleted pattern ID was returned
                    val deletedPatternId: String? = intent.getStringExtra("deletedPatternId")
                    if (deletedPatternId != null) {
                        viewModel?.deletePatternById(deletedPatternId)
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatternsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(PatternViewModel::class.java)
        viewModel?.patternMutableLiveData?.observe(viewLifecycleOwner, patternListUpdateObserver)
        viewModel?.init()

        binding.patternRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        binding.addFab.setOnClickListener {
            val intent = Intent(requireContext(), AddPatternActivity::class.java)
            addPatternLauncher.launch(intent)
        }

        patternAdapter = CrochetPatternRVAdapter(requireContext(), arrayListOf(), this)
        binding.patternRecyclerView.adapter = patternAdapter
    }

    private var patternListUpdateObserver: Observer<ArrayList<CrochetPattern>?> = Observer { patternArrayList ->
        patternAdapter?.updatePatterns(patternArrayList ?: emptyList())
    }

    override fun onItemClick(position: Int) {
        val selectedPattern = patternAdapter?.patterns?.get(position)
        val intent = Intent(requireContext(), InspectPatternActivity::class.java)
        originalPatternId = selectedPattern?.id
        intent.putExtra("selectedPattern", selectedPattern)
        inspectPatternLauncher.launch(intent)
    }
}
