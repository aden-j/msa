package com.example.userservice

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface LazyLogging {

    val logger: Logger
        get() = LoggerFactory.getLogger(this.javaClass)
}