package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentMusicfilesBinding

class MusicfilesFragment(): Fragment() {

    lateinit var binding: FragmentMusicfilesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicfilesBinding.inflate(inflater, container, false)

        return binding.root
    }
}