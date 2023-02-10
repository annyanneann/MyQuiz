package com.ann_zinoveva.myquiz

import androidx.annotation.StringRes

data class Questions(@StringRes val textResId: Int, val answer : Boolean) {
}