package com.kobbi.project.coronamask.model

enum class RemainState(private val stat: String, val level: IntRange) {
    BREAK("break", Int.MIN_VALUE until 0),
    EMPTY("empty", 0 until 1),
    FEW("few", 1 until 30),
    SOME("some", 30 until 100),
    PLENTY("plenty", 100..Int.MAX_VALUE),
    UNKNOWN("unknown", Int.MIN_VALUE..Int.MIN_VALUE);

    companion object {
        @JvmStatic
        fun getState(stat: String?): RemainState {
            return values().firstOrNull {
                it.stat == stat
            } ?: UNKNOWN
        }
    }
}