package com.blogofyb.oo.util

/**
 * Create by yuanbing
 * on 2019/8/24
 */
object DateBuilder {
    fun time(time: String): String {
        val targetTime = splitTime(time)
        val cTime = splitTime(GlobalMessageManager.parseTimeStamp(System.currentTimeMillis()))
        return if (cTime.year >= targetTime.year && cTime.month >= targetTime.month) {
            when {
                cTime.day - targetTime.day == 1 -> "昨天 ${targetTime.hm}"
                cTime.day == targetTime.day -> "今天 ${targetTime.hm}"
                else -> "${targetTime.month}-${targetTime.day} ${targetTime.hm}"
            }
        } else {
            "${targetTime.month}-${targetTime.day} ${targetTime.hm}"
        }
    }

    private fun splitTime(time: String): SplitTime {
        val year = time.split("-")[0].toInt()
        val month = time.split("-")[1].toInt()
        val day = time.split(" ")[0].split("-")[2].toInt()
        val hm = time.split(" ")[1].split(":")[0] + ":" +
                time.split(" ")[1].split(":")[1]
        return SplitTime(year, month, day, hm)
    }
}

data class SplitTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hm: String
)