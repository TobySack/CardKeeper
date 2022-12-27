package com.example.rewardcards

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform