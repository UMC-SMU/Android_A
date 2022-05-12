package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()

    private lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        val bannerAdapter = BannerVPAdapter(this)// 새 어댑터 클래스 만들어야
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp)) // 괄호 안에는 추가할 프래그먼트
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2)) // 여러 이미지 추가

        binding.homeBannerVp.adapter = bannerAdapter // 어댑터와 뷰페이저 연결
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 좌우로 스크롤될 수 있게

        val panelAdapter = PanelVPAdapter(this)// 새 어댑터 클래스 만들어야
        panelAdapter.addFragment(PanelFragment(R.drawable.img_first_album_default, "포근하게 덮어주는 꿈의\n목소리")) // 괄호 안에는 추가할 프래그먼트
        panelAdapter.addFragment(PanelFragment(R.drawable.img_album_exp, "달밤의 감성 산책")) // 여러 이미지 추가
        panelAdapter.addFragment(PanelFragment(R.drawable.img_album_exp2, "아무 타이틀")) // 여러 이미지 추가

        binding.homePanelVp.adapter = panelAdapter // 어댑터와 뷰페이저 연결
        binding.homePanelVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 좌우로 스크롤될 수 있게

        val autoBanner = AutoBanner(panelAdapter)
        autoBanner.start()

        songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbums())

        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setMyItemClickListner(object: AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                // album fagment로 전환
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
        })

        return binding.root
    }

    // 코드 영역 선택-Refactor-Function
    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    // 번들에 데이터를 담아서 넘긴다.
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }

    inner class AutoBanner(var panelAdapter: PanelVPAdapter) : Thread() {
        var idx = 1

        override fun run() {
            while (true) {
                sleep(1000L)
                binding.homePanelVp.setCurrentItem(idx % panelAdapter.itemCount)
                idx++
            }
        }
    }

}