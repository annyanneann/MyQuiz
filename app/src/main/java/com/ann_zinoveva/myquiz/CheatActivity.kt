package com.ann_zinoveva.myquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import com.ann_zinoveva.myquiz.databinding.ActivityCheatBinding
private const val EXTRA_ANSWER_IS_TRUE = "com.ann_zinoveva.myquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.ann_zinoveva.myquiz.answer_shown"

class CheatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheatBinding

    private val resultIntent: Intent
    get() = Intent().apply {
        putExtra(EXTRA_ANSWER_SHOWN, true)
    }

    private var answerToTheQuestion: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater).also { setContentView(it.root) }

        answerToTheQuestion = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

            val answerToTheQuestion = when {
                answerToTheQuestion -> R.string.buttonTrue
                else -> R.string.buttonFalse
            }
            binding.cheatWarning.setText(answerToTheQuestion)
            setAnswerShownResult()

        }




    //Передача данных(дополнений) MainActivity.Создаем интент, помещаем в него данные и
    //вызываем метод setResult для передачи данных MainActivity.
    private fun setAnswerShownResult() {
        setResult(Activity.RESULT_OK, resultIntent)
    }



    class Contract : ActivityResultContract<Boolean, Boolean>() {

        override fun createIntent(context: Context, answerToTheQuestion: Boolean): Intent {
            val intent = Intent(context, CheatActivity::class.java)
            intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerToTheQuestion)
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            if (intent == null) return false
            return intent.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
        }
    }

}