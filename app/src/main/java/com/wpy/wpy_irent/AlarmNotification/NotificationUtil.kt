package com.wpy.wpy_irent.AlarmNotification

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.wpy.wpy_irent.MainActivity
import com.wpy.wpy_irent.PlayActivity
import com.wpy.wpy_irent.R
import com.wpy.wpy_irent.SelesaiActivity
import java.text.SimpleDateFormat
import java.util.*

class NotificationUtil {
    companion object{
        private const val CHANNEL_ID_TIMER="menu-timer"
        private const val CHANNEL_NAME_TIMER="Timer App Timer"
        private const val TIMER_ID=0
        private const val REQ_CODE=0

        fun showTimerExpired(context: Context){
            val startIntent= Intent(context, TimerNotificationActionReceiver::class.java)
            startIntent.action=AppConstants.ACTION_FINISH
            val startPendingIntent=PendingIntent.getBroadcast(context, REQ_CODE, startIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val pattern = longArrayOf(500, 500, 500, 500, 500, 500, 500, 500, 500)
            val nBuilder=getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle("Waktu Habis")
                .setContentText("Waktu peminjaman anda sudah habis, mohon hubungi admin jika ingin tambah waktu..")
                .setContentIntent(getPendingIntentWithStack(context, SelesaiActivity::class.java))
                .addAction(R.drawable.ic_play, "Oke", startPendingIntent)
                .setVibrate(pattern)

            val nManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            context.startService(Intent(context, AlarmSoundService::class.java))
            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true)
            nManager.notify(TIMER_ID, nBuilder.build())
        }

        fun showTimerPaused(context: Context){
//            val resumeIntent= Intent(context, TimerNotificationActionReceiver::class.java)
//            resumeIntent.action=AppConstants.ACTION_RESUME
//            val resumePendingIntent=PendingIntent.getBroadcast(context, REQ_CODE, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val nBuilder=getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle("Paket Dibatalkan")
                .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
                .setOngoing(true)
//                .addAction(R.drawable.ic_play, "Resume", resumePendingIntent)
            val nManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true)
            nManager.notify(TIMER_ID, nBuilder.build())
        }

        @SuppressLint("RestrictedApi")
        fun showTimerRunning(context: Context, wakeUpTime: Long){
            val dateFormat=SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)

            val nBuilder=getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle("Waktu Berjalan")
                .setContentText("Selesai : ${dateFormat.format(Date(wakeUpTime))}")
                .setContentIntent(getPendingIntentWithStack(context, PlayActivity::class.java))
                .setOngoing(true).priority

//                .addAction(R.drawable.ic_stop, "Lihat", resumePendingIntent)
//                .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent)


            val nManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, false)
            nManager.notify(TIMER_ID, nBuilder.build())
        }

        fun hideTimerNotification(context: Context){
            val nManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.cancel(TIMER_ID)
        }

        private fun getBasicNotificationBuilder(context: Context, channelId: String, playSound: Boolean): NotificationCompat.Builder{
            val notificationSound: Uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL)
            val  nBuilder=NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_timer)
                .setAutoCancel(false)
                .setDefaults(0)

            if(playSound){
                nBuilder.setSound(notificationSound)
            }
            return nBuilder
        }

        private fun <T> getPendingIntentWithStack(context: Context, javaClass: Class<T>): PendingIntent{
            val resultIntent=Intent(context, javaClass)
            resultIntent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_FORWARD_RESULT

            val stackBuilder=TaskStackBuilder.create(context)
            stackBuilder.addParentStack(javaClass)
            stackBuilder.addNextIntent(resultIntent)

            return stackBuilder.getPendingIntent(REQ_CODE, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        @TargetApi(26)
        private fun NotificationManager.createNotificationChannel(channelId: String, channelName: String, playSound: Boolean){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                val channelImportance=if(playSound) {NotificationManager.IMPORTANCE_DEFAULT}
                else {NotificationManager.IMPORTANCE_DEFAULT}

                val nChannel=NotificationChannel(channelId, channelName, channelImportance)
                nChannel.enableLights(true)
                nChannel.lightColor=Color.BLUE
                this.createNotificationChannel(nChannel)
            }
        }
    }
}