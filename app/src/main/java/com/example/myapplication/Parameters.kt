package com.example.myapplication

import android.content.Context

class Parameters(private val context: Context) {
    private val parametersName = "myapplication_parameters"

    private val pnServerIP = "server_ip"
    private val pnServerPort = "server_port"
    private val pnMode = "mode"
    private val pnSpeed = "speed"
    private val pnResolutionWidth = "resolution_w"
    private val pnResolutionHeight = "resolution_h"
    private val pnShowCoordinates = "show_coordinates"

    private val defServerIP: String = "192.168.1.2"
    private val defServerPort: Int = 9194
    private val defMode = ModeClass.TOUCHPAD
    private val defSpeed: Int = 6
    private val defResolutionWidth: Int = 1920
    private val defResolutionHeight: Int = 1080
    private val defShowCoordinates: Boolean = true

    private var serverIP: String = defServerIP
    private var serverPort: Int = defServerPort
    private var mode = defMode //default = touchpad
    private var speed: Int = defSpeed
    private var resolutionWidth: Int = defResolutionWidth
    private var resolutionHeight: Int = defResolutionHeight
    private var showCoordinates: Boolean = defShowCoordinates

    fun readFromMemory() : Boolean
    {
        var result = true
        try
        {
            val sharedPreference = context.getSharedPreferences(parametersName, Context.MODE_PRIVATE)
            val tmpServerIP = sharedPreference.getString(pnServerIP, null)
            val tmpServerPort = sharedPreference.getInt(pnServerPort, -1)
            val tmpMode = sharedPreference.getInt(pnMode, defMode.getMode())
            val tmpSpeed = sharedPreference.getInt(pnSpeed, 0)
            val tmpResolutionWidth = sharedPreference.getInt(pnResolutionWidth, 0)
            val tmpResolutionHeight = sharedPreference.getInt(pnResolutionHeight, 0)
            val tmpShowCoordinates = sharedPreference.getBoolean(pnShowCoordinates, true)

            if (tmpServerIP != null)
            {
                serverIP = tmpServerIP
            }
            else
            {
                result = false
            }

            if (tmpServerPort != -1)
            {
                serverPort = tmpServerPort
            }
            else
            {
                result = false
            }

            if (tmpMode != defMode.getMode())
            {
                mode = ModeClass.DIRECT
            }
            else
            {
                result = false
            }

            if (tmpSpeed != 0)
            {
                speed = tmpSpeed
            }
            else
            {
                result = false
            }

            if (tmpResolutionWidth != 0)
            {
                resolutionWidth = tmpResolutionWidth
            }
            else
            {
                result = false
            }

            if (tmpResolutionHeight != 0)
            {
                resolutionHeight = tmpResolutionHeight
            }
            else
            {
                result = false
            }

            showCoordinates = tmpShowCoordinates
        }
        catch (e: java.lang.Exception)
        {
            result = false
        }
        return result
    }

    fun writeInMemory() : Boolean
    {
        try {
            val sharedPreference = context.getSharedPreferences(parametersName, Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString(pnServerIP, serverIP)
            editor.putInt(pnServerPort, serverPort)
            editor.putInt(pnMode, mode.getMode())
            editor.putInt(pnSpeed, speed)
            editor.putInt(pnResolutionWidth, resolutionWidth)
            editor.putInt(pnResolutionHeight, resolutionHeight)
            editor.putBoolean(pnShowCoordinates, showCoordinates)
            editor.apply()

            return true
        }
        catch (e: Exception)
        {
            return false
        }
    }

    fun getServerIP() : String {
        return serverIP
    }

    fun getServerPort() : Int {
        return serverPort
    }

    fun getMode() : Int {
        return mode.getMode()
    }

    fun getSpeed() : Int {
        return speed
    }

    fun getResolutionWidth() : Int {
        return resolutionWidth
    }

    fun getResolutionHeight() : Int {
        return resolutionHeight
    }

    fun getShowCoordinates() : Boolean {
        return showCoordinates
    }

    fun setServerIP(serverIP: String) {
        this.serverIP = serverIP
    }

    fun setServerPort(serverPort: Int) {
        this.serverPort = serverPort
    }

    fun setMode(mode: ModeClass) {
        this.mode = mode
    }

    fun setSpeed(speed: Int) {
        this.speed = speed
    }

    fun setResolutionWidth(resolutionWidth: Int) {
        this.resolutionWidth = resolutionWidth
    }

    fun setResolutionHeight(resolutionHeight: Int) {
        this.resolutionHeight = resolutionHeight
    }

    fun setShowCoordinates(showCoordinates: Boolean) {
        this.showCoordinates = showCoordinates
    }


}