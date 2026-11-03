package com.utez.citytour

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var btnPlay: Button
    private lateinit var btnPause: Button
    private lateinit var btnStop: Button
    private lateinit var tvEstadoAudio: TextView

    private var mediaPlayer: MediaPlayer? = null
    private var isPaused: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Vinculación de vistas
        viewPager = findViewById(R.id.viewPagerCarousel)
        btnPlay = findViewById(R.id.btnPlay)
        btnPause = findViewById(R.id.btnPause)
        btnStop = findViewById(R.id.btnStop)
        tvEstadoAudio = findViewById(R.id.tvEstadoAudio)

        // ========================================================
        // AQUÍ ESTÁN TUS IMÁGENES (.webp):
        // ========================================================
        val imagenesMonumento = listOf(
            R.drawable.palaciocortez,
            R.drawable.interiorpalacio,
            R.drawable.interiorpalacio_2
        )
        val adapter = ImageCarouselAdapter(imagenesMonumento)
        viewPager.adapter = adapter

        // 3. Configuración de controles
        configurarControlesAudio()
    }

    private fun configurarControlesAudio() {
        btnPlay.setOnClickListener {
            reproducirAudio()
        }

        btnPause.setOnClickListener {
            pausarAudio()
        }

        btnStop.setOnClickListener {
            detenerAudio()
        }
    }

    private fun reproducirAudio() {
        if (mediaPlayer == null) {
            // ========================================================
            // AQUÍ ESTÁ TU AUDIO (.mp3):
            // ========================================================
            mediaPlayer = MediaPlayer.create(this, R.raw.palaciocortez)

            mediaPlayer?.setOnCompletionListener {
                detenerAudio()
                Toast.makeText(this, "Explicación finalizada", Toast.LENGTH_SHORT).show()
            }
        }

        if (isPaused) {
            mediaPlayer?.start()
            isPaused = false
            tvEstadoAudio.text = "Estado: Reproduciendo (reanudado)"
        } else if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
            tvEstadoAudio.text = "Estado: Reproduciendo"
        }
    }

    private fun pausarAudio() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer?.pause()
            isPaused = true
            tvEstadoAudio.text = "Estado: En pausa"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun detenerAudio() {
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            isPaused = false
            tvEstadoAudio.text = "Estado: Detenido"
        }
    }

    override fun onStop() {
        super.onStop()
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            pausarAudio()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detenerAudio()
    }
}
