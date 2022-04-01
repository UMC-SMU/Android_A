package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentSavesongBinding

class SavesongFragment : Fragment() {
    lateinit var binding : FragmentSavesongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavesongBinding.inflate(inflater,container,false)
        return binding.root
    }
}