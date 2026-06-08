package com.example.kabaddikounter.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.kabaddikounter.MyApplication
import com.example.kabaddikounter.databinding.FragmentHomeBinding
import com.example.kabaddikounter.viewModels.SharedViewModel
import com.example.kabaddikounter.viewModels.SharedViewModelFactory
import kotlin.getValue

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory(
            (requireActivity().application as MyApplication).scoreRepository
        )
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels{
        HomeViewModelFactory(
            (requireActivity().application as MyApplication).scoreRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        val homeViewModel =
//            ViewModelProvider(
//                requireActivity(),
//                HomeViewModelFactory(
//                    (requireActivity().application as MyApplication).scoreRepository)
//            ).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewModel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner


        homeViewModel.toastMessage.observe(viewLifecycleOwner) {
            message -> message?.let{
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            homeViewModel.onToastShown()
        }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel._score.observe(viewLifecycleOwner) {
           score -> homeViewModel.loadData(score)
        }
    }

    override fun onPause() {
        super.onPause()
//        sharedViewModel.setScore(homeViewModel.getScore())
        sharedViewModel.setScore(homeViewModel.getScore())
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}