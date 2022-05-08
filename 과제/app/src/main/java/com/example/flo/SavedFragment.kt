package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSavedBinding

class SavedFragment(): Fragment() {

    lateinit var binding: FragmentSavedBinding
    private var albumDatas = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedBinding.inflate(inflater, container, false)
// 리사이클러뷰
        // 데이터 리스트 생성 더미 데이터
        // 실제 어플에선 데이터를 서버에서 받아온다.
        albumDatas.apply {
            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp))
            add(Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp2))
            add(Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp))
            add(Album("Weekden", "태연 (Tae Yeon)", R.drawable.img_album_exp2))
        }

        val albumVerticalRVAdapter = AlbumVerticalRVAdapter(albumDatas)
        binding.savedAlbumRv.adapter = albumVerticalRVAdapter
        binding.savedAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        albumVerticalRVAdapter.setMyItemClickListner(object: AlbumVerticalRVAdapter.CustomItemClickListener{
            override fun onItemClick(album: Album) {
                Toast.makeText(activity, album.title, Toast.LENGTH_SHORT).show()
                Log.d("asd", album.title.toString())
            }
        })
        return binding.root
    }
}