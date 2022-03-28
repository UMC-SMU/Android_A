package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.flo.databinding.FragmentAlbumBinding

// Fragment의 기능을 사용할 수 있는 클래스인 Fragment를 상속
class AlbumFragment : Fragment() {
    lateinit var binding : FragmentAlbumBinding// 바인딩 선언

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false) // activity에서 사용한 인플레이터

        super.onCreate(savedInstanceState)
        setFragmentResultListener("requestKey") { requestKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.getString("bundleKey")
            binding.albumMusicTitleTv.text = result
            // Do something with the result
        }

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commitAllowingStateLoss()
        }

        binding.albumAlbumIv.setOnClickListener {
            Toast.makeText(activity, "LILAC", Toast.LENGTH_SHORT).show() // 짧게 띄운다.
        }
        return binding.root
    }
}