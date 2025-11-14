package ua.nure.bonte.extension

fun String?.firstName() = this?.split(" ")?.getOrNull(0)

fun String?.lastName() = this?.split(" ")?.getOrNull(1)