package com.wpy.wpy_irent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wpy.wpy_irent.AlarmNotification.AlarmSoundService
import com.wpy.wpy_irent.AlarmNotification.NotificationUtil
import com.wpy.wpy_irent.AlarmNotification.PrefUtil
import com.wpy.wpy_irent.Auth.HistoriPref
import kotlinx.android.synthetic.main.activity_selesai.*

class SelesaiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selesai)
        btnSelesaiS.setOnClickListener {
            PrefUtil.setAlarmSetTime(0,this)
            PrefUtil.setTimerState(PlayActivity.TimerState.STOPPED,this)
            PrefUtil.setTimerLength(0,this)
            PrefUtil.setPreviousTimerLengthSeconds(0,this)
            PrefUtil.setTimerSecondsRemaining(0,this)
            HistoriPref.getInstance(this).clear()
            NotificationUtil.hideTimerNotification(applicationContext)
            val intent = Intent(this@SelesaiActivity, MainActivity::class.java)
            PlayActivity.removeAlarm(this)
            applicationContext.stopService(Intent(this, AlarmSoundService::class.java))
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}