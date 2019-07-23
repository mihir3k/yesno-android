package com.mihir3k.yesno

object Model {
    data class Result(
        val answer: String,
        val forced: String,
        val image: String
    )
}
