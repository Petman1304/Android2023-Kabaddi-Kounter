package com.example.kabaddikounter.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.window.application
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kabaddikounter.MyApplication
import com.example.kabaddikounter.databinding.FragmentHistoryBinding
import com.example.kabaddikounter.repository.ScoreRepository
import com.example.kabaddikounter.ui.ScoreAdapter

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val historyViewModel =
//            ViewModelProvider(this).get(HistoryViewModel::class.java)

        val historyViewModel = ViewModelProvider(requireActivity(), HistoryViewModelFactory((requireActivity().application  as MyApplication).scoreRepository)).get(HistoryViewModel::class.java)


        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val scoreAdapter = ScoreAdapter(requireContext())

        binding.viewModel = historyViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.scoreRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scoreAdapter
        }

        historyViewModel.allScore.observe(viewLifecycleOwner){
            scores -> scoreAdapter.submitList(scores)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}