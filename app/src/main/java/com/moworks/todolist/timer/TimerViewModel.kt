package com.moworks.todolist.timer

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moworks.todolist.notifications.showTimerNotification

class TimerViewModel(application: Application) : AndroidViewModel (application){

    companion object{
        const val SECOND = 1000L
        const val INTERVAL_25M :Long = 25 * 60 * 1000
        const val BREAK_INTERVAL :Long = 5 * 60 * 1000

        private var INTERVAL :Long = INTERVAL_25M
    }


    private  var countDownTimer:CountDownTimer? = null

    private val _isPlaying : MutableLiveData<Boolean> = MutableLiveData()
    val isPlaying : LiveData<Boolean>
        get() = _isPlaying

    private val toResumeTimer : MutableLiveData<Long> = MutableLiveData()



    private val _timer : MutableLiveData<Long> = MutableLiveData()
    val timer : LiveData<Long>
        get() = _timer


    private val _progress : MutableLiveData<Int> = MutableLiveData()
    val progress : LiveData<Int>
        get() = _progress


    init {
        _isPlaying.value = false
    }


     fun startBreak(){
         resetTimer()
         INTERVAL = BREAK_INTERVAL
         triggerTimer()
     }


    fun start25M(){
        resetTimer()
        INTERVAL = INTERVAL_25M
        triggerTimer()
    }

    fun panTimer() {
        if(countDownTimer != null) {
            pauseTimer()
        }
    }


    fun triggerTimer() {
        if (countDownTimer == null && toResumeTimer.value == null)
        {
            startTimer()
        }

        else if (countDownTimer != null && toResumeTimer.value != null)
        {
            pauseTimer()
        }

        else if(countDownTimer == null && toResumeTimer.value != null)
        {
            resumeTimer()

        }
    }



    private fun resumeTimer(){
        val resumeInterval = toResumeTimer.value
        countDownTimer = object : CountDownTimer(resumeInterval!!, SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _timer.value = millisUntilFinished
                _progress.value  = (((INTERVAL.toFloat() - millisUntilFinished.toFloat()) / INTERVAL.toFloat()) * 100).toInt()

                toResumeTimer.value = millisUntilFinished

            }

            override fun onFinish() {
                resetTimer()
                // notification
                showTimerNotification(getApplication())
            }

        }.start()
        _isPlaying.value = true

    }

    private fun startTimer(){
        countDownTimer = object : CountDownTimer(INTERVAL, SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _timer.value = millisUntilFinished

                _progress.value = (((INTERVAL.toFloat() - millisUntilFinished.toFloat()) / INTERVAL.toFloat()) * 100).toInt()
                toResumeTimer.value = millisUntilFinished
//                    toResumeProgress.value = _progress.value
            }

            override fun onFinish() {
                resetTimer()
                // notification
                showTimerNotification(getApplication())
            }

        }.start()
        _isPlaying.value = true
    }


    private  fun pauseTimer(){
        countDownTimer?.cancel()
        countDownTimer = null
        _isPlaying.value = false
        _timer.value = toResumeTimer.value
    }

    fun resetTimer(){
        countDownTimer?.cancel()
        countDownTimer = null
        toResumeTimer.value = null
        _isPlaying.value = false
        _progress.value =null
        _timer.value =null

    }



    override fun onCleared() {
        super.onCleared()
        resetTimer()
    }

}