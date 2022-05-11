package com.example.project_skripsi.utils.helper

class DisplayHelper {

    companion object {

        fun getDurationDisplay(duration: Float, unit: String) =
            if (unit == "menit") "${duration.toInt()} $unit"
            else "${".1f".format(duration)} $unit"


    }

}