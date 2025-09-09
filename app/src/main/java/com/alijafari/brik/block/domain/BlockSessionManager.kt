package com.alijafari.brik.block.domain


interface BlockSessionManager {

    fun startSession()
    fun stopSession()

    fun extend(extraMillis : Long)
}