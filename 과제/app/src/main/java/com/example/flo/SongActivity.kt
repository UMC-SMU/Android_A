package com.example.flo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import java.lang.Exception

class SongActivity : AppCompatActivity() { // 코틀린에서는 extends 대신 : 사용하고 괄호 쓴다, AppCompatActivity는 안드로이드 기능을 사용할 수 있게 해준다.
    // 뷰 바인딩 사용하는 방법
    // Gradle Scripts-build.gradle(Module)
    // viewBinding {
    //        enabled true
    //    } // 뷰 바인딩을 사용하겠단 뜻, 끝나고 sync now 눌러야 한다.

    // 바인딩은 전역변수에 선언해야한다.

    lateinit var binding : ActivitySongBinding // 레이아웃 xml 파일에 스네이크식 이름으로 매칭되는 거 자동 변환

    lateinit var song : Song
    lateinit var timer: Timer

    private var isCircular:Boolean = false
    private var isRandom:Boolean = false
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

        initSong() // intent 데이터를 이용해서 노래 초기화
        setPlayer(song)

        binding.songDownIb.setOnClickListener {
            // 현재 activity 끄기
            var returnIntent: Intent = intent
            intent.putExtra("returnMillsSecond", timer.getMills())
            intent.putExtra("returnIsPlaying", song.isPlaying)
            setResult(Activity.RESULT_OK, returnIntent)
            Log.d("song->main intent:", timer.getMills().toString() + song.isPlaying.toString())
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            // 함수를 만듦으로써 직관성을 높임
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songRepeatIv.setOnClickListener {
            setPlayerStatus(timer.isPlaying)
            timer.restart()
            // 함수를 만듦으로써 직관성을 높임
            if (isCircular == true) {
                setCircularStatus(false)
                isCircular = false
            }
            else {
                setCircularStatus(true)
                isCircular = true
            }
        }

        binding.songRandomIv.setOnClickListener {
            // 함수를 만듦으로써 직관성을 높임
            if (isRandom == true) {
                setRandomStatus(false)
                isRandom = false
            }
            else {
                setRandomStatus(true)
                isRandom = true
            }
        }

    }

//    override fun onDestroy() {
//        super.onDestroy()
//        timer.interrupt()
//    }
    private fun startTimer(){
        timer = Timer(song.playTime, song.isPlaying)
        timer.setMills(intent.getFloatExtra("mills", 0f))
        timer.start()
    }

    private fun initSong(){
        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra("title")!!, // !!(not-null assertion operator)는 Null 값이 들어오면 exception을 발생시키는 연산자
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false)
            )
        }
        startTimer()
    }

    private fun setPlayer(song : Song) {
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second/60, song.second%60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)
        setPlayerStatus(song.isPlaying)
    }

    // 함수 생성, 카멜 케이스
    private fun setPlayerStatus(isPlaying : Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE // 재생버튼 없애기
            binding.songPauseIv.visibility = View.VISIBLE // 정지버튼 표시
        }
        else
        {
            binding.songMiniplayerIv.visibility = View.VISIBLE // 재생버튼 표시
            binding.songPauseIv.visibility = View.GONE // 정지버튼 없애기
        }
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var second: Int = song.second
        private var mills: Float = song.second*1000f

        fun setMills(mills : Float) {
            this.mills = mills
        }

        fun getMills() : Float {
            return mills
        }

        fun restart() {
            song.second = 0
            second = 0
            mills = 0f
            binding.songProgressSb.progress = 0
            binding.songStartTimeTv.text = String.format("%02d:%02d", second/60, second%60)
        }

        override fun run() {
            super.run()
            runOnUiThread { // seek bar view rendering
                binding.songProgressSb.progress = ((mills * 100) / playTime).toInt()
            }
            try {
                while(true){
                    if (second >= playTime){
                        runOnUiThread {
                            setPlayerStatus(false)
                            restart()
                        }
                    }
                    if (isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread { // seek bar view rendering
                            binding.songProgressSb.progress = ((mills * 100) / playTime).toInt()
                            Log.d("song play", binding.songProgressSb.progress.toString())
                        }
                        if (mills%1000 == 0f) {
                            second += 1
                            runOnUiThread{
                                binding.songStartTimeTv.text = String.format("%02d:%02d", second/60, second%60)
                            }
                        }
                    }
                    song.second = second
                }
            }catch(e: Exception) {
                Log.d("Song", "쓰레드가 죽었습니다. ${e.message}")
            }
        }
    } // binding 변수를 사용해야 하므로 inner class로 구현

    private fun setCircularStatus(isCircular : Boolean) {
        if(isCircular) {
            binding.songRepeatIv.setColorFilter(Color.parseColor("#3f3fff"), PorterDuff.Mode.SRC_ATOP) // 재생버튼 표시
        }
        else
        {
            binding.songRepeatIv.setColorFilter(null)
        }
    }

    private fun setRandomStatus(isRandom : Boolean) {
        if(isRandom) {
            binding.songRandomIv.setColorFilter(Color.parseColor("#3f3fff"), PorterDuff.Mode.SRC_ATOP) // 재생버튼 표시
        }
        else
        {
            binding.songRandomIv.setColorFilter(null)
        }
    }

    // menifests 파일에서 activity를 등록해줘야 한다.

}