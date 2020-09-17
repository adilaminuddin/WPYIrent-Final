package com.wpy.wpy_irent.AlarmNotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.wpy.wpy_irent.PlayActivity
import com.wpy.wpy_irent.SelesaiActivity

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.showTimerExpired(context)
        PrefUtil.setTimerState(PlayActivity.TimerState.FINISH, context)
    }
}