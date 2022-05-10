package com.example.flo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson
import java.lang.Exception
import java.util.Collections.addAll

class SongActivity : AppCompatActivity() { // 코틀린에서는 extends 대신 : 사용하고 괄호 쓴다, AppCompatActivity는 안드로이드 기능을 사용할 수 있게 해준다.
    // 뷰 바인딩 사용하는 방법
    // Gradle Scripts-build.gradle(Module)
    // viewBinding {
    //        enabled true
    //    } // 뷰 바인딩을 사용하겠단 뜻, 끝나고 sync now 눌러야 한다.

    // 바인딩은 전역변수에 선언해야한다.

    lateinit var binding : ActivitySongBinding // 레이아웃 xml 파일에 스네이크식 이름으로 매칭되는 거 자동 변환

    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null // ?는 nullable의 의미. 액티비티가 해제될 때 미디어 플레이어를 해제해야하기 때문에
    private var gson: Gson = Gson()

    var songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

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

        songDB = SongDatabase.getInstance(this)!!
        initSong()
        initClickListner()
    }

    //포커스 잃었을 때 중지
    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }

    // 사용자가 포커스를 잃었을 때 음악이 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        songs[nowPos].second = ((binding.songProgressSb.progress * songs[nowPos].playTime)/100)/ 1000

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE) // 내부 저장소에 데이터를 저장
        // 앱이 종료되었다가 다시 실행되어도 이곳에 저장된 값을 사용 가능
        // 설정값 등에 사용
        // 로그인할 때 비밀번호 기억 등에도 사용
        // MODE_PRIVATE: 이 앱에서만 사용 가능
        // sharedPreference는 에디터로만 사용 가능

        val editor = sharedPreferences.edit() // 에디터 만들기, put을 통해 넣는다.
        // editor.putString("title", song.title) // 이런 식으로 song에 있는 데이터 수만큼 할 수도 있지만 번거로우니까 json 형식으로 보낼 것.
        // 자바 객체를 json으로 간편하게 변환시켜주는 라이브러리 추가

        editor.putInt("songId", songs[nowPos].id)

        editor.apply() // 여기까지 해야 실제로 저장작업 깃의 푸시와 같은 것
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.setMills(songs[nowPos].second*1000f)
        timer.start()
    }

    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        songs.addAll(songDB.songDao().getSongs())

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song Id", songs[nowPos].id.toString())
        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun initClickListner(){
        binding.songDownIb.setOnClickListener {
            val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE) // 내부 저장소에 데이터를 저장
            val editor = sharedPreferences.edit()
            editor.putInt("songId", songs[nowPos].id)
            editor.apply() // 여기까지 해야 실제로 저장작업 깃의 푸시와 같은 것
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

        binding.songNextIv.setOnClickListener {
            moveSong(1)
        }

        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }

        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }
    
    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike // DB 값을 업데이트해줘야 동기화됨
        songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id)

        if (!isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else{
            binding.songLikeIv.setImageResource((R.drawable.ic_my_like_off))
        }
    }

    private fun moveSong(direct: Int) {
        if (nowPos + direct < 0){
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size) {
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }
        nowPos += direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제

        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size){
            if (songId == songs[i].id) return i
        }
        return 0
    }

    private fun setPlayer(song : Song) {
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer
        binding.songAlbumIvI.setImageResource(song.coverImg!!)
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second/60, song.second%60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        if (song.isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }

    // 함수 생성, 카멜 케이스
    private fun setPlayerStatus(isPlaying : Boolean) {
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE // 재생버튼 없애기
            binding.songPauseIv.visibility = View.VISIBLE // 정지버튼 표시
            mediaPlayer?.start()
        }
        else
        {
            binding.songMiniplayerIv.visibility = View.VISIBLE // 재생버튼 표시
            binding.songPauseIv.visibility = View.GONE // 정지버튼 없애기
            if(mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var second: Int = songs[nowPos].second
        private var mills: Float = songs[nowPos].second*1000f

        fun setMills(mills : Float) {
            this.mills = mills
        }

        fun getMills() : Float {
            return mills
        }

        fun restart() {
            songs[nowPos].second = 0
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
                    songs[nowPos].second = second
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