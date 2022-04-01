package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 바인딩을 이용해 이미지의 id 값을 가져오고 리스너 사용
        // home_album_img_iv1
        //home_daily_music_album_img_01_iv
        binding.homeTodayreleaseAlbum1Iv.setOnClickListener {
            // 액티비티 파일을 새로 만들었던 것처럼 프래그먼트 파일도 새로 만들어야

            val result = binding.homeTodayreleaseAlbum1TitleTv.text.toString()
            // Use the Kotlin extension in the fragment-ktx artifact
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))

            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, AlbumFragment()).commitAllowingStateLoss() // 메인 액티비티에는 프레임 레이아웃이 있고 홈 프래그먼트는 그 안의 하나의 조각, 조각을 어디서 변경하는지 써준다고 생각하면 될 것 같다.
            // 메인 액티비티의 프레임 레이아웃을 앨범 프래그먼트로 대체
            //commitAllowingStateLoss()은 내부적으로 동작하는 거라 신경 쓸 필요 없음
        }

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

        return binding.root
    }
}