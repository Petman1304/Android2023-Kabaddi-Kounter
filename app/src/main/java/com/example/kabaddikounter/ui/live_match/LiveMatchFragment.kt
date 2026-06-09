package com.example.kabaddikounter.ui.live_match

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kabaddikounter.ApiInterface
import com.example.kabaddikounter.MyApplication
import com.example.kabaddikounter.RetrofitInst
import com.example.kabaddikounter.databinding.FragmentLiveMatchBinding
import com.example.kabaddikounter.service.FCMService
import com.example.kabaddikounter.ui.LiveMatchAdapter
import com.example.kabaddikounter.ui.ScoreAdapter
import com.example.kabaddikounter.viewModels.SharedViewModel
import com.example.kabaddikounter.viewModels.SharedViewModelFactory
import com.google.firebase.messaging.FirebaseMessaging

class LiveMatchFragment : Fragment() {

    private var _binding: FragmentLiveMatchBinding? = null
    private lateinit var apiInterface: ApiInterface
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory(
            (requireActivity().application as MyApplication).scoreRepository
        )
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val liveMatchViewModel =
            ViewModelProvider(this).get(LiveMatchViewModel::class.java)

        _binding = FragmentLiveMatchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val liveMatchAdapter = LiveMatchAdapter(requireContext()) {
            match -> val action = LiveMatchFragmentDirections.actionLiveMatchFragmentToHomeFragment()
            sharedViewModel.setScore(match)
            sharedViewModel.subscribeTopic(requireContext().applicationContext, "match_${match.id}")
            findNavController().navigate(action)
        }

        binding.viewModel = liveMatchViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.scoreRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = liveMatchAdapter
        }

        liveMatchViewModel.matches.observe(viewLifecycleOwner) {
            match -> liveMatchAdapter.submitList(match)
        }
        liveMatchViewModel.loadMatches()


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}