package com.widi.scan.ui.articles

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.widi.scan.data.ScanRepository
import com.widi.scan.databinding.FragmentArticleBinding
import com.widi.scan.ui.main.ViewModelFactory

class ArticleFragment : Fragment() {
    private lateinit var binding: FragmentArticleBinding
    private lateinit var articleAdapter: ArticleAdapter
    private val viewModelFactory: ViewModelProvider.Factory by lazy {
        ViewModelFactory(ScanRepository())
    }
    private val articleViewModel: ArticleViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articleAdapter = ArticleAdapter(emptyList())

        binding.rvArticles.layoutManager = LinearLayoutManager(context)
        binding.rvArticles.adapter = articleAdapter

        articleViewModel.getArticles().observe(viewLifecycleOwner) { articles ->
            if (articles != null) {
                Log.d("ArticleFragment", "Articles received: ${articles.size}")
                articleAdapter.articles = articles
                articleAdapter.notifyDataSetChanged()
            } else {
                Log.d("ArticleFragment", "No articles received")
            }
        }

        articleViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}

