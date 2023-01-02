package com.example.rewardcards.android

sealed class Screen(val route: String) {
    object CardList: Screen("cardList")
    object CardAdd: Screen("cardAdd")
    object CardEdit: Screen("cardEdit")
    object CardDetail: Screen("cardDetail")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    fun withArgs(vararg args: Long): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
