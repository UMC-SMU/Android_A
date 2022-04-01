package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentSongBinding

class SongFragment: Fragment() {

    lateinit var binding: FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        binding.songLalacLayout.setOnClickListener {
            Toast.makeText(activity, binding.songMusicTitle01Tv.text, Toast.LENGTH_SHORT).show()
        }
        binding.songFluLayout.setOnClickListener {
            Toast.makeText(activity, binding.songMusicTitle02Tv.text, Toast.LENGTH_SHORT).show()
        }
        binding.songCoinLayout.setOnClickListener {
            Toast.makeText(activity, binding.songMusicTitle03Tv.text, Toast.LENGTH_SHORT).show()
        }
        binding.songSpringLayout.setOnClickListener {
            Toast.makeText(activity, binding.songMusicTitle04Tv.text, Toast.LENGTH_SHORT).show()
        }
        binding.songCelebrityLayout.setOnClickListener {
            Toast.makeText(activity, binding.songMusicTitle05Tv.text, Toast.LENGTH_SHORT).show()
        }
        binding.songSingLayout.setOnClickListener {
            Toast.makeText(activity, binding.songMusicTitle06Tv.text, Toast.LENGTH_SHORT).show()
        }

        binding.songMixonTg.setOnClickListener {
            setMixStatus(false)
        }
        binding.songMixoffTg.setOnClickListener {
            setMixStatus(true)
        }


        return binding.root
    }

    fun setMixStatus(isPlaying : Boolean) {
        if(isPlaying) {
            binding.songMixoffTg.visibility = View.GONE
            binding.songMixonTg.visibility = View.VISIBLE
        }
        else
        {
            binding.songMixoffTg.visibility = View.VISIBLE
            binding.songMixonTg.visibility = View.GONE
        }
    }
}