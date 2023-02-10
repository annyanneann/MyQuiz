package com.ann_zinoveva.myquiz


import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.parcel.Parcelize


class QuizViewModel : ViewModel() {

    val state: LiveData<State> get() = stateLiveData
    private val stateLiveData = MutableLiveData<State>()

    fun initState(state: State) {
        stateLiveData.value = state
    }

    fun updateQuestion() {
            val oldState = stateLiveData.value
            stateLiveData.value = oldState?.copy(
                currentIndex = oldState.currentIndex + 1,
                isCheater = false,
                cheaterAndTrue = false
            )
    }

    fun checkAnswer(userAnswer : Boolean) {
        val oldState = stateLiveData.value
            val correctAnswer = questionsBank[oldState?.currentIndex!!].answer
            if (correctAnswer == userAnswer && !oldState.isCheater) {stateLiveData.value = oldState.copy(
                currentAnswer = oldState.currentAnswer + 1
            ) } else if (correctAnswer == userAnswer && oldState.isCheater) {stateLiveData.value = oldState.copy(
                cheaterAndTrue = true
            ) }
        }


    fun comparisonIndexWithSize() : Boolean {
        val oldState = stateLiveData.value
        return oldState?.currentIndex!! < questionsBank.size
    }

    fun percent() : Boolean {
        return stateLiveData.value?.currentAnswer in ((questionsBank.size.toDouble() * 80) / 100).toInt()..questionsBank.size
    }

    fun zeroing() {
        stateLiveData.value?.currentIndex = 0
        stateLiveData.value?.currentAnswer = 0
        stateLiveData.value?.isCheater = false
        stateLiveData.value?.cheaterAndTrue = false
        stateLiveData.value?.hints = 3
    }

    fun hints() {
        val oldState = stateLiveData.value
        stateLiveData.value = oldState?.copy(
            hints = oldState.hints - 1
        )
    }

    fun checkHints() : Boolean {
        val oldState = stateLiveData.value
        return oldState?.hints!! == 0
    }



    //Инициализация вопросов и переменной для смены индекса(создали в вью модельке):
    val questionsBank = listOf(
        Questions(R.string.question1,true),
        Questions(R.string.question2,false),
        Questions(R.string.question3,false),
        Questions(R.string.question4,true),
        Questions(R.string.question5,false))

    @Parcelize
    data class State(
        var currentIndex: Int,
        var currentAnswer: Int,
        var isCheater: Boolean,
        var cheaterAndTrue: Boolean,
        var hints : Int
        ) : Parcelable
}

