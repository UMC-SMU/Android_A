package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentLoockerBinding
import com.google.android.material.tabs.TabLayoutMediator

class LoockerFragment : Fragment() {

    lateinit var binding: FragmentLoockerBinding

    private val infomation = arrayListOf("저장한 곡", "음악파일")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoockerBinding.inflate(inflater, container, false)

        val lookerAdapter = LookerVPAdapter(this)
        binding.loockerContentVp.adapter = lookerAdapter
        TabLayoutMediator(binding.loockerContentTb,binding.loockerContentVp){
            tab, position ->
            tab.text = infomation[position]
        }.attach()

        return binding.root
    }

}