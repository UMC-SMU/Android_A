package com.example.flo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var song: Song
    private var gson: Gson = Gson()
    lateinit var timer: Timer
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(), 0, 60, false, "music_proust")

        timer = Timer(song.playTime, song.isPlaying, 0f)
        progressBar = ProgressBar()
        progressBar.start()

        binding.mainPlayerCl.setOnClickListener {
            song.second = (progressBar.getMills()/1000).toInt()

            var intent = Intent(this, SongActivity::class.java)
            // intent라는 택배 상자에 데이터라는 물건을 담아서
            intent.putExtra("title", song.title) // "title"이라는 키로 타이틀 담아주기
            intent.putExtra("singer", song.singer) // "singer"라는 키로 가수 이름 담아주기
            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("music", song.music)
            intent.putExtra("mills", progressBar.getMills())
            Log.d("main play", binding.mainProgressSb.progress.toString())
            Log.d("main->song intent:", song.second.toString() + song.isPlaying.toString())
            getContent.launch(intent)
        // startActivity(intent)
            //startActivity(Intent(this, SongActivity::class.java))
        }

        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
        }


        initBottomNavigation()

        //Log.d("Song", song.title + song.singer) // logcat에 이 태그를 출력하는데 나중에 이걸로 검색할 수 있다.
    }

    private fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = (song.second*100000)/song.playTime
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE) // sharedPreferences의 이름이 "song"
        val songJson = sharedPreferences.getString("song", null) // sharedPreferences 안에 저장된 데이터의 키 이름 "song"
        song = if (songJson == null){
            Song("라일락", "아이유(IU)", 0, 60, false, "music_proust")
        } else {
            gson.fromJson(songJson, Song::class.java)
        }
        setMiniPlayer(song)
    }

    var getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result:ActivityResult ->
        if (result.resultCode == RESULT_OK && result.data!!.hasExtra("returnMillsSecond")){
            var data: Intent? = result.data
            var mills:Float = data!!.getFloatExtra("returnMillsSecond", 0f)
            var playing:Boolean = data!!.getBooleanExtra("returnIsPlaying", false)
            Log.d("main->song get intent:", mills.toString() + playing.toString())
            song.second = (mills/1000).toInt()
            timer.setPlayMills(mills)
            progressBar.setMills(mills)
            progressBar.setMainPlaying(true)
            setPlayerStatus(playing)
        }
    }
    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun setPlayerStatus(isPlaying : Boolean) {
        song.isPlaying = isPlaying
        timer.setIsPlaying(isPlaying)

        if(isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE // 재생버튼 없애기
            binding.mainPauseBtn.visibility = View.VISIBLE // 정지버튼 표시
        }
        else
        {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE // 재생버튼 표시
            binding.mainPauseBtn.visibility = View.GONE // 정지버튼 없애기
        }
    }


    inner class ProgressBar() : Thread() {
        private var mills : Float = timer.getPlayMills()
        private var mainPlaying : Boolean = true

        fun getMills() : Float{
            return mills
        }

        fun setMills(mills : Float) {
            this.mills = mills
        }

        fun getMainPlaying() : Boolean{
            return mainPlaying
        }

        fun setMainPlaying(mainPlaying : Boolean) {
            this.mainPlaying = mainPlaying
        }

        override fun run() {
            super.run()
            try {
                while (true) {
                    sleep(50)
                    if (mainPlaying) {
                        mills = timer.getPlayMills()
                        mainPlaying = false
                    }
                    else {
                        if (timer.getIsPlaying()) {
                            mills += 50
                            timer.millSecPass(50f)
                        }
                        else {
                            mills = timer.getPlayMills()
                            runOnUiThread {
                                setPlayerStatus(false)
                            }
                        }
                        runOnUiThread { // seek bar view rendering
                            binding.mainProgressSb.progress = ((mills * 100) / song.playTime).toInt()
                        }
                    }
                }
            } catch(e: Exception) {
                Log.d("interrupt", "쓰레드가 죽었습니다. ${e.message}")
            }
        }
    } // binding 변수를 사용해야 하므로 inner class로 구현
}