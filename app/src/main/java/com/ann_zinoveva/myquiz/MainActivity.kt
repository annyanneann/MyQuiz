package com.ann_zinoveva.myquiz


import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.DialogInterface
import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.ann_zinoveva.myquiz.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    //Объявляем переменную для интерфейса.
    private lateinit var binding: ActivityMainBinding

    //Наследуем переменную вью модели от класса ВьюМоделей и объявляем ее.
    private val quizViewModel : QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    private val launcherCheat = registerForActivityResult(CheatActivity.Contract()) {
        quizViewModel.state.value?.isCheater = it
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        //Проверка на сохранения состояния, инициализируем если равно null или вытягиваем правильные данные если не равно.
        quizViewModel.initState(savedInstanceState?.getParcelable(KEY_STATE) ?: QuizViewModel.State(
            currentAnswer = 0,
            currentIndex = 0,
            isCheater = false,
            cheaterAndTrue = false,
            hints = 3
        ))

        //Кнопка Правда. Проверка на кол-во вопросов, проверка ответа, обновление значения и смена вопроса. Иначе завершение.
        binding.True.setOnClickListener {
            if (quizViewModel.comparisonIndexWithSize()) {
                quizViewModel.checkAnswer(true)
                toasts()
                quizViewModel.updateQuestion()
                changeQuestion()
            } else {
                theEnd()
            }
        }

        //Кнопка Неправда. Проверка на кол-во вопросов, проверка ответа, обновление значения и смена вопроса. Иначе завершение.
        binding.False.setOnClickListener {
            if (quizViewModel.comparisonIndexWithSize()) {
                quizViewModel.checkAnswer(false)
                toasts()
                quizViewModel.updateQuestion()
                changeQuestion()
            } else {
                theEnd()
            }
        }

        //Кнопка опять
        binding.again.setOnClickListener {
            again()
        }

        //переход на новую активити через интент(намерение). Создаем диалоговое окно и чепрез него входим в новое активити.
        binding.cheatButton.setOnClickListener { view ->
           if  (!quizViewModel.checkHints()) {
            val listener = DialogInterface.OnClickListener {dialog, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    val answerToTheQuestion = quizViewModel.questionsBank[quizViewModel.state.value?.currentIndex!!].answer
                    quizViewModel.hints()
                    launcherCheat.launch(answerToTheQuestion)
                    }
                }

            val dialog = AlertDialog.Builder(this)
                .setTitle(R.string.wait)
                .setMessage(getString(R.string.Cheat, quizViewModel.state.value?.hints.toString()))
                .setPositiveButton("да я лох", listener)
                .setNegativeButton("отминеет", listener)
                .create()
            dialog.show()
           } else binding.cheatButton.isEnabled = false
        }

        val version = VERSION.SDK_INT.toString()

        binding.tvVersion.text = getString(R.string.api_level, version)


//Начальное значение:
        changeQuestion()
    }


    //Сохраняем состояние в бандл.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_STATE, quizViewModel.state.value)
    }


    //Смена вопроса.
        private fun changeQuestion() {
            if (quizViewModel.comparisonIndexWithSize()) {
                binding.questionText.setText(quizViewModel.questionsBank[quizViewModel.state.value?.currentIndex!!].textResId) }
            else theEnd()
        }

    private fun toasts() {
        if (quizViewModel.state.value?.isCheater == true && quizViewModel.state.value?.cheaterAndTrue == true) {
            val messageResId = R.string.Cheater
            Toast.makeText(this, messageResId,Toast.LENGTH_SHORT)
                .show()
        }
    }


        //Функция для результатов
        private fun theEnd() = with(binding) {
            LinLayHor.visibility = View.GONE
            questionText.setText(R.string.end)
            scoresText.visibility = View.VISIBLE
            scoresText.text = getString(R.string.scores, quizViewModel.state.value?.currentAnswer)
            winOrLossText.visibility = View.VISIBLE
            again.visibility = View.VISIBLE
            cheatButton.visibility = View.GONE
            if (quizViewModel.percent()) {
                winOrLossText.setText(R.string.win)
            } else {
                winOrLossText.setText(R.string.loss)
            }
        }

        //Кнопка заново
        private fun again() = with(binding) {
            LinLayHor.visibility = View.VISIBLE
            scoresText.visibility = View.GONE
            winOrLossText.visibility = View.GONE
            again.visibility = View.GONE
            cheatButton.visibility = View.VISIBLE

            quizViewModel.zeroing()
            changeQuestion()
        }
}




//Ключ для функции состояния
private const val KEY_STATE = "index"
//Ключ для возвращения лополнения(экстра)
private const val REQUEST_CODE_CHEAT = 0






