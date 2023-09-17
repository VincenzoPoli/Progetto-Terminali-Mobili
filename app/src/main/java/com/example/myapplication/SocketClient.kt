package com.example.myapplication

import android.os.Handler
import android.os.Looper
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.Executors

class SocketClient(address: String, port: Int): Thread() {
    var hostAddress: String = address
    var hostPort: Int = port
    private var canWrite: Boolean = true
    private var inputStream: InputStream? = null
    private lateinit var outputStream: OutputStream
    private lateinit var socket: Socket
    var buffer = ByteArray(1024)

    fun close() {
        socket.close();
    }

    fun write(message: String) : Pair<Boolean, String> {
        var result: Boolean = false
        var descResult: String = ""

        try
        {
            if (canWrite)
            {
                if (socket.isConnected)
                {
                    val byteArray = message.toByteArray(Charsets.UTF_8)
                    canWrite = false
                    outputStream.write(byteArray)
                    result = true
                }
                else
                {
                    descResult = "NOT_CONNECTED"
                }
            }
            else
            {
                descResult = "CANNOT_WRITE"
            }
        }
        catch (ex: IOException)
        {
            ex.printStackTrace()
            descResult = ex.toString()
            canWrite = true
        }

        return Pair(result, descResult)
    }

    fun connect(address: String, port: Int) :Boolean {
        var result: Boolean = false

        try {
            hostAddress = address
            hostPort = port
            socket = Socket()
            socket.connect(InetSocketAddress(hostAddress, hostPort), 3000)
            inputStream = socket.getInputStream()
            outputStream = socket.getOutputStream()
            result = true
        }catch (ex:IOException){
            inputStream = null
            ex.printStackTrace()
        }

        return result
    }

    override fun run() {
        connect(hostAddress, hostPort)

        val executor = Executors.newSingleThreadExecutor()
        var handler = Handler(Looper.getMainLooper())

        executor.execute(kotlinx.coroutines.Runnable { //verifica la risposta del server (che è la stringa "OK", e imposta canWrite a vera per poter continuare a mandare messaggi al server stesso
            kotlin.run {
                while (true) {
                    try{
                        if (inputStream != null) {
                            if (inputStream!!.read(buffer) > 0) {
                                handler.post( {
                                    kotlin.run {
                                        canWrite = true;
                                    }
                                })
                            }
                        }
                    }catch (ex:IOException){
                        ex.printStackTrace()
                    }
                }
            }
        })
    }

}