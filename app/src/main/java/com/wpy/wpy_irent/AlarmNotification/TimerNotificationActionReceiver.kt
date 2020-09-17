package com.wpy.wpy_irent.AlarmNotification


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.wpy.wpy_irent.PlayActivity

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        when(intent.action){
            AppConstants.ACTION_STOP->{
                PlayActivity.removeAlarm(context)
                PrefUtil.setTimerState(PlayActivity.TimerState.FINISH, context)
                NotificationUtil.hideTimerNotification(context)
            }
            AppConstants.ACTION_FINISH->{
                var secondsRemaining=PrefUtil.getTimerSecondsRemaining(context)
                val alarmSetTime=PrefUtil.getAlarmSetTime(context)
                val nowSeconds=PlayActivity.nowSeconds

                secondsRemaining-=nowSeconds-alarmSetTime
                PrefUtil.setTimerSecondsRemaining(secondsRemaining, context)
                PlayActivity.removeAlarm(context)

                PrefUtil.setTimerState(PlayActivity.TimerState.FINISH, context)
                NotificationUtil.showTimerExpired(context)
            }
            AppConstants.ACTION_RESUME->{
                var secondsRemaining=PrefUtil.getTimerSecondsRemaining(context)
                val wakeUpTime=PlayActivity.setAlarm(context, PlayActivity.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(PlayActivity.TimerState.RUNNING, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
        }
    }
}