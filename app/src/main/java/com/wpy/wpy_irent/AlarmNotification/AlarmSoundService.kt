package com.wpy.wpy_irent.AlarmNotification

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import com.wpy.wpy_irent.R

class AlarmSoundService : Service() {
    var mediaPlayer  : MediaPlayer? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound)
        mediaPlayer?.setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build())
        mediaPlayer?.start()
        mediaPlayer?.isLooping = true
    }

    override fun onDestroy() {
        super.onDestroy()

        //On destroy stop and release the media player
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                it.reset()
                it.release()
            }
        }
    }
}