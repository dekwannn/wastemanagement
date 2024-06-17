package com.widi.scan.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.widi.scan.data.ScanRepository
import com.widi.scan.databinding.FragmentHistoryBinding
import com.widi.scan.ui.adapter.HistoryAdapter
import com.widi.scan.ui.main.ViewModelFactory

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModelFactory: ViewModelProvider.Factory by lazy {
        ViewModelFactory(ScanRepository())
    }
    private val historyViewModel: HistoryViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = HistoryAdapter()
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = adapter

        historyViewModel.allHistory.observe(viewLifecycleOwner) { history ->
            if (history.isNullOrEmpty()) {
                binding.apply {
                    btnDelete.visibility = View.GONE
                    noHistory.visibility = View.VISIBLE
                    rvHistory.visibility = View.GONE
                }
            } else {
                binding.apply {
                    noHistory.visibility = View.GONE
                    rvHistory.visibility = View.VISIBLE
                    adapter.submitList(history)
                    btnDelete.setOnClickListener{
                        historyViewModel.deleteAllHistory()
                    }
                }
            }
        }
        historyViewModel.loadAllHistory()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

