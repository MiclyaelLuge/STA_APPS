package com.example.sta_apps.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sta_apps.R
import com.example.sta_apps.databinding.FragmentGithubBinding


class GithubFragment : Fragment() {
    private var _binding : FragmentGithubBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentGithubBinding.inflate(inflater,container,false)

        return binding.root
    }

    companion object {

    }
}