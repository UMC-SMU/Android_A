package com.example.flo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() { // 코틀린에서는 extends 대신 : 사용하고 괄호 쓴다, AppCompatActivity는 안드로이드 기능을 사용할 수 있게 해준다.
    // 뷰 바인딩 사용하는 방법
    // Gradle Scripts-build.gradle(Module)
    // viewBinding {
    //        enabled true
    //    } // 뷰 바인딩을 사용하겠단 뜻, 끝나고 sync now 눌러야 한다.

    // 바인딩은 전역변수에 선언해야한다.

    lateinit var binding : ActivitySongBinding // 레이아웃 xml 파일에 스네이크식 이름으로 매칭되는 거 자동 변환
    // lateinit은 전방 선언, 선언은 지금하고 초기화는 나중
    // 코틀린에서 변수를 선언하는 방법
    // var은 나중에 값 변경 가능, val은 나중에 값 변경 불가
    // var test1 : String = 'dd'

    // Activity에서 처음으로 실행되는 함수
    // fun은 함수를 뜻한다.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater) // inflate란 xml을 메모리에 객체화시키는 것
        setContentView(binding.root) // xml의 것들을 가져와서 마음대로 쓸 거다. root는 해당 activity의 최상단뷰를 가리킨다.

        binding.songDownIb.setOnClickListener {
            // 현재 activity 끄기
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            // 함수를 만듦으로써 직관성을 높임
            setPlayerStatus(false)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(true)
        }

        // intent로 받아오는 부분
        // 데이터가 담겨올 수도 있고 아닐 수도 있다.
        if (intent.hasExtra("title") && intent.hasExtra("singer")) { // "title"과 "singer"라는 키값을 갖는 것이 intent 안에 있는가?
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }
    }

    // 함수 생성, 카멜 케이스
    fun setPlayerStatus(isPlaying : Boolean) {
        if(isPlaying) {
            binding.songMiniplayerIv.visibility = View.VISIBLE // 재생버튼 표시
            binding.songPauseIv.visibility = View.GONE // 정지버튼 없애기
        }
        else
        {
            binding.songMiniplayerIv.visibility = View.GONE // 재생버튼 없애기
            binding.songPauseIv.visibility = View.VISIBLE // 정지버튼 표시
        }
    }

    // menifests 파일에서 activity를 등록해줘야 한다.

}