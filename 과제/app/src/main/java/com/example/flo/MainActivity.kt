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

        inputDummySongs()
        inputDummyAlbums()
        song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(), 0, 60, false, "music_proust")

        timer = Timer(song.playTime, song.isPlaying, 0f)
        progressBar = ProgressBar()
        progressBar.start()

        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
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

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!

        song = if (songId == 0) { // sharedPreferences에 저장된 기존에 틀던 노래 없을 때
            songDB.songDao().getSong(1)
        } else{
            songDB.songDao().getSong(songId) // 아이디로 검색해서 가져오기
        }

        Log.d("song ID", song.id.toString())

        setMiniPlayer(song)
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

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!

        // DB 데이터 전부받아오기
        val songs = songDB.songDao().getSongs()

        // 소스가 비어있지 않다면(데이터가 원래 있다면)
        if (songs.isNotEmpty()) return

        // 비어 있는 경우에만 더미 데이터
        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "music_boy",
                0,
                230,
                false,
                "music_proust",
                R.drawable.img_album_exp,
                false
            )
        )
        songDB.songDao().insert(
            Song(
                "Boy with Luv2",
                "music_boy",
                0,
                230,
                false,
                "music_proust",
                R.drawable.img_album_exp2,
                false
            )
        )
        songDB.songDao().insert(
            Song(
                "Boy with Luv3",
                "music_boy",
                0,
                230,
                false,
                "music_proust",
                R.drawable.img_album_exp,
                false
            )
        )

        // DB에 잘 들어갔는지 확인
        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }
    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!

        // DB 데이터 전부받아오기
        val albums = songDB.albumDao().getAlbums()

        // 소스가 비어있지 않다면(데이터가 원래 있다면)
        if (albums.isNotEmpty()) return

        // 비어 있는 경우에만 더미 데이터
        songDB.albumDao().insert(
            Album(
                0,
                "music_boy",
                "asdf",
                R.drawable.img_album_exp
            )
        )
        songDB.albumDao().insert(
            Album(
                1,
                "music_boy2",
                "asdf2",
                R.drawable.img_album_exp2
            )
        )
        songDB.albumDao().insert(
            Album(
                2,
                "music_boy3",
                "asdf3",
                R.drawable.img_album_exp
            )
        )
    }
}