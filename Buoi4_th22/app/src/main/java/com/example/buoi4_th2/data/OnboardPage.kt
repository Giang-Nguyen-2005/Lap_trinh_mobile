package com.example.buoi4_th2.data

import androidx.annotation.DrawableRes
import com.example.buoi4_th2.R

data class OnboardPage(
    val title: String,
    val description: String,
    @DrawableRes val imageRes: Int
)

val onboardPages = listOf(
    OnboardPage(
        "Easy Time Management",
        "With management based on priority and daily tasks, it will give you convenience in managing and determining the tasks that must be done first.",
        R.drawable.onboard1
    ),
    OnboardPage(
        "Increase Work Effectiveness",
        "Time management and determination of more important tasks will give your job statistics better and always improve.",
        R.drawable.onboard2
    ),
    OnboardPage(
        "Reminder Notification",
        "The advantage of this application is that it provides reminders for you so you don’t forget to finish your assignments on time.",
        R.drawable.onboard3
    )
)
