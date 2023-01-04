package com.example.rewardcards.android

import com.example.rewardcards.domain.card.Card

// This exists only for demonstration! Doesn't have to be pretty
sealed class Company(val logo: Int, val color: Long) {
    object Unknown: Company(R.drawable.question_mark, Card.generateRandomColor())
    object Costco: Company(R.drawable.costco_logo_1, 0xFFE31837)
    object Walmart: Company(R.drawable.walmart_spark_svg, 0xFF0071ce)
    object SamsClub: Company(R.drawable.sams_club_svg, 0xFF0081c6)
    object HomeDepot: Company(R.drawable.home_depot_logo, 0xFFF96302)
    object BestBuy: Company(R.drawable.best_buy_logo, 0xFF0A4ABF)

    companion object {
        fun getCompanyLogo(company: String): Company {
            val logos = mapOf(
                "costco" to Costco,
                "walmart" to Walmart,
                "sam's club" to SamsClub,
                "home depot" to HomeDepot,
                "best buy" to BestBuy
            )
            val filteredLogo = logos.filterKeys { it == company }.values

            return if (filteredLogo.isEmpty()) Unknown else filteredLogo.first()
        }
    }
}
