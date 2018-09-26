package com.picker.gallery.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by deepan-5901 on 01/02/18.
 */
class DateUtil {
    fun getTimeString(time: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        val month = when (cal[Calendar.MONTH]) {
            Calendar.JANUARY -> "Jan"
            Calendar.FEBRUARY -> "Feb"
            Calendar.MARCH -> "Mar"
            Calendar.APRIL -> "Apr"
            Calendar.MAY -> "May"
            Calendar.JUNE -> "Jun"
            Calendar.JULY -> "Jul"
            Calendar.AUGUST -> "Aug"
            Calendar.SEPTEMBER -> "Sep"
            Calendar.OCTOBER -> "Oct"
            Calendar.NOVEMBER -> "Nov"
            Calendar.DECEMBER -> "Dec"
            else -> ""
        }
        val year = cal[Calendar.YEAR]
        return "$month ${cal[Calendar.DAY_OF_MONTH]}, $year at ${if (cal[Calendar.HOUR] == 0) "12" else "${cal[Calendar.HOUR]}"}:${if (cal[Calendar.MINUTE] < 10) "0${cal[Calendar.MINUTE]}" else "${cal[Calendar.MINUTE]}"} ${if (cal[Calendar.AM_PM] == 0) "AM" else "PM"}"
    }

    fun getDateString(time: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        val month = when (cal[java.util.Calendar.MONTH]) {
            Calendar.JANUARY -> "January"
            Calendar.FEBRUARY -> "February"
            Calendar.MARCH -> "March"
            Calendar.APRIL -> "April"
            Calendar.MAY -> "May"
            Calendar.JUNE -> "June"
            Calendar.JULY -> "July"
            Calendar.AUGUST -> "August"
            Calendar.SEPTEMBER -> "September"
            Calendar.OCTOBER -> "October"
            Calendar.NOVEMBER -> "November"
            Calendar.DECEMBER -> "December"
            else -> ""
        }
        return "$month ${cal[Calendar.DAY_OF_MONTH]}, ${cal[Calendar.YEAR]}"
    }

    fun getMonthAndYearString(time: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        val month = when (cal[java.util.Calendar.MONTH]) {
            Calendar.JANUARY -> "January"
            Calendar.FEBRUARY -> "February"
            Calendar.MARCH -> "March"
            Calendar.APRIL -> "April"
            Calendar.MAY -> "May"
            Calendar.JUNE -> "June"
            Calendar.JULY -> "July"
            Calendar.AUGUST -> "August"
            Calendar.SEPTEMBER -> "September"
            Calendar.OCTOBER -> "October"
            Calendar.NOVEMBER -> "November"
            Calendar.DECEMBER -> "December"
            else -> ""
        }
        return "$month, ${cal[Calendar.YEAR]}"
    }

    fun getPrettyDateString(time: Long): String {
        var finalString = ""
        try {
            val now = Calendar.getInstance().timeInMillis
            val seconds = TimeUnit.MILLISECONDS.toSeconds(now - time)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(now - time)
            val hours = TimeUnit.MILLISECONDS.toHours(now - time)
            val days = TimeUnit.MILLISECONDS.toDays(now - time)
            val weeks = days / 7

            finalString += when {
                weeks >= 1 -> getTimeString(time)
                seconds < 60 -> "${seconds}s ago"
                minutes < 60 -> "${minutes}m ago"
                hours < 24 -> "${hours}h ago"
                else -> "${days}d ago"
            }
        } catch (e: Exception) {
            finalString = ""
        }
        return finalString
    }

    fun getPrettyTimeDifferenceString(time: Long): String {
        var finalString = ""
        try {
            val now = Calendar.getInstance().timeInMillis
            val seconds = TimeUnit.MILLISECONDS.toSeconds(now - time)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(now - time)
            val hours = TimeUnit.MILLISECONDS.toHours(now - time)
            val days = TimeUnit.MILLISECONDS.toDays(now - time)
            val weeks = days / 7

            finalString += when {
                weeks >= 1 -> getDateString(time)
                seconds < 60 -> "Today"
                minutes < 60 -> "Today"
                hours < 24 -> "Today"
                else -> "${if (days.toInt() == 1) "Yesterday" else "$days days"}"
            }
        } catch (e: Exception) {
            finalString = ""
        }
        return finalString
    }

    fun getTimeStringDetailed(time: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        val month = when (cal[java.util.Calendar.MONTH]) {
            Calendar.JANUARY -> "January"
            Calendar.FEBRUARY -> "February"
            Calendar.MARCH -> "March"
            Calendar.APRIL -> "April"
            Calendar.MAY -> "May"
            Calendar.JUNE -> "June"
            Calendar.JULY -> "July"
            Calendar.AUGUST -> "August"
            Calendar.SEPTEMBER -> "September"
            Calendar.OCTOBER -> "October"
            Calendar.NOVEMBER -> "November"
            Calendar.DECEMBER -> "December"
            else -> ""
        }
        val dayOfWeek = when (cal[Calendar.DAY_OF_WEEK]) {
            Calendar.SUNDAY -> "Sun"
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY -> "Wed"
            Calendar.THURSDAY -> "Thu"
            Calendar.FRIDAY -> "Fri"
            Calendar.SATURDAY -> "Sat"
            else -> ""
        }
        val year = cal[Calendar.YEAR]
        return "$dayOfWeek, ${cal[Calendar.DAY_OF_MONTH]} $month $year at ${if (cal[Calendar.HOUR] == 0) "12" else "${cal[Calendar.HOUR]}"}:${if (cal[Calendar.MINUTE] < 10) "0${cal[Calendar.MINUTE]}" else "${cal[Calendar.MINUTE]}"} ${if (cal[Calendar.AM_PM] == 0) "AM" else "PM"}"
    }

    private fun getDate(sdf: SimpleDateFormat, timeStr: String): Date {
        try {
            return sdf.parse(timeStr)
        } catch (parseExp: ParseException) {
            MLog.e("PARSE ERROR", parseExp.toString())
        }
        return Date()
    }

    fun millisToTime(millis: Long): String {
        var seconds = 0
        var minutes = 0
        var hours = 0
        if (millis > 1000) {
            seconds = (millis / 1000).toInt()
            if (seconds > 60) {
                minutes = (seconds / 60)
                seconds %= 60
                if (minutes > 60) {
                    hours /= 60
                    minutes %= 60
                }
            }
        }
        return if (hours < 0 || minutes < 0 || seconds < 0) ""
        else if (hours > 0) "${if (hours > 9) "$hours" else "0$hours"}:${if (minutes > 9) "$minutes" else "0$minutes"}:${if (seconds > 9) "$seconds" else "0$seconds"}"
        else if (hours == 0 && minutes > 0) "${if (minutes > 9) "$minutes" else "0$minutes"}:${if (seconds > 9) "$seconds" else "0$seconds"}"
        else if (hours == 0 && minutes == 0) "0:${if (seconds > 9) "$seconds" else "0$seconds"}"
        else ""
    }

    fun getTimeZonedDate(time: Long): String {
        try {
            val tz = SimpleTimeZone(0, "GMT")
            val cal = Calendar.getInstance(tz)
            cal.timeInMillis = time
            val day = cal[Calendar.DAY_OF_MONTH]
            val month = when (cal[Calendar.MONTH]) {
                Calendar.JANUARY -> "Jan"
                Calendar.FEBRUARY -> "Feb"
                Calendar.MARCH -> "Mar"
                Calendar.APRIL -> "Apr"
                Calendar.MAY -> "May"
                Calendar.JUNE -> "Jun"
                Calendar.JULY -> "Jul"
                Calendar.AUGUST -> "Aug"
                Calendar.SEPTEMBER -> "Sep"
                Calendar.OCTOBER -> "Oct"
                Calendar.NOVEMBER -> "Nov"
                Calendar.DECEMBER -> "Dec"
                else -> ""
            }
            val year = cal[Calendar.YEAR]
            return "$month $day, $year"
        } catch (e: Exception) {
            return ""
        }
    }
}