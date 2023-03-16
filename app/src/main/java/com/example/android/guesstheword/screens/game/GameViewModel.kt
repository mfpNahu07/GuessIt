package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel


class GameViewModel : ViewModel() {

    companion object {
        // These represent different important times

        // This is when the game is over
        const val DONE = 0L

        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L

        // This is the total time of the game
        const val COUNTDOWN_TIME = 10000L

    }

    private val timer: CountDownTimer

    private val _word = MutableLiveData<String>() // internal use

    val word: LiveData<String>       // external use.  You can read LiveData but cannot set a value on.
        get() = _word               // backing property allows to return something from a getter other than the exact object


    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score


    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    //Encapsulated LiveData for the current time
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long> //This would be LiveData "A" for Transformations map.
        get() = _currentTime

    //This would be LiveData "B" that is emitting a String
    val currentTimeString = Transformations.map(currentTime, { time ->
        DateUtils.formatElapsedTime(time)
    })


    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>


    init {
        Log.i("GameViewModel", "GameViewModel created!!!")
        //_eventGameFinish.value = false
        resetList()
        nextWord()
        _score.value = 0 // Initialize score.value to 0.


        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                // TODO implement what should happen each tick of the timer

                //show the current number of second is going down
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                // TODO implement what should happen when the timer finishes

                //the game finish event to be triggered
                _currentTime.value = DONE
                _eventGameFinish.value = true
            }
        }

        timer.start()
    }


    override fun onCleared() {
        super.onCleared()
        timer.cancel() //avoid memory leaks, always cancel a CountDownTimer if you no longer need it
        Log.i("GameViewModel", "GameViewModel destroyed!!!")
    }


    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }


    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)

    }

    fun onSkip() {
        _score.value = score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = score.value?.plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }
}