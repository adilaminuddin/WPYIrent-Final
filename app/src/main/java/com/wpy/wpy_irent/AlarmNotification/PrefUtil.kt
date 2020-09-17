package com.wpy.wpy_irent.AlarmNotification

import android.content.Context
import androidx.preference.PreferenceManager
import com.wpy.wpy_irent.PlayActivity

class PrefUtil {
    companion object{

        private const val TIMER_LENGTH_ID="com.wpy.wpy_irent.timer_length"
        private const val PREVIOUS_TIME_LENGTH_SECONDS_ID= "com.wpy.wpy_irent.previoustimerlength"
        private const val TIMER_STATE_ID= "com.wpy.wpy_irent.timerstate"
        private const val SECONDS_REMAINING_ID= "com.wpy.wpy_irent.secondsremaining"
        private const val ALARM_SET_TIME_ID= "com.wpy.wpy_irent.backgroundedtime"

        fun getTimerLength(context: Context): Int{
            val preferences=PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(TIMER_LENGTH_ID, 10)
        }
        fun setTimerLength(minute: Int, context: Context){
            val editor=PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putInt(TIMER_LENGTH_ID, minute)
            editor.apply()
        }


        fun getPreviousTimerLengthSeconds(context: Context): Long{
            val preferences=PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIME_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context){
            val editor=PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIME_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        fun getTimerState(context: Context): PlayActivity.TimerState{
            val preferences=PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal= preferences.getInt(TIMER_STATE_ID, 0)
            return PlayActivity.TimerState.values()[ordinal]
        }

        fun setTimerState(state: PlayActivity.TimerState, context: Context){
            val editor= PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal= state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }



        fun getTimerSecondsRemaining(context: Context): Long{
            val preferences=PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setTimerSecondsRemaining(seconds: Long, context: Context){
            val editor=PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }


        fun getAlarmSetTime(context: Context): Long{
            val preferences=PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(ALARM_SET_TIME_ID, 0)
        }

        fun setAlarmSetTime(time: Long, context: Context){
            val editor=PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }
    }
}