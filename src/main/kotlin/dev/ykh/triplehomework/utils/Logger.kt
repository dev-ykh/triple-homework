package dev.ykh.triplehomework.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> loggerFor(): Logger {
    if (T::class.isCompanion) {
        return LoggerFactory.getLogger(T::class.java.enclosingClass)
    }
    return LoggerFactory.getLogger(T::class.java)
}