package com.example.myapplication

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.StrictMode
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlin.system.exitProcess


class MainClass : android.app.Activity() {

    lateinit var txtCoordinates: TextView
    lateinit var sc: SocketClient
    private var parameters = Parameters(this) //nella activity, this è il contesto
    var coordinates = Coordinates()
    lateinit var testo : String
    lateinit var viewMousePad: View
    lateinit var KeyboardButtons: MutableList<Button>   //tasti dalla A alla Z che cambiano in base allo shift se premuto o meno
    lateinit var KeyboardOtherButtons: List<Button>     //altri tasti tipo ? . , : ecc. ecc.
    lateinit var KeyboardSpecialButtons: List<Button>   //shift e spazio
    lateinit var KeyboardSpecialImageButtons: List<ImageButton> //Tasti che hanno una immagine di background (shift, backspace, enter)
    private var widthViewMousePad: Int = 0
    private var heightViewMousePad: Int = 0
    var tempo : Int = 0

    fun main() {
        print(R.string.app_name)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean
    {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            closeApp()
        }
        return true
    }

    override fun onBackPressed() {
        closeApp()
    }

    private fun closeApp() {
        sc.close()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonLeft = findViewById<Button>(R.id.buttonLeft)
        buttonLeft.setOnClickListener(clickListener)
        val buttonRight = findViewById<Button>(R.id.buttonRight)
        buttonRight.setOnClickListener(clickListener)
        val btnSettings = findViewById<ImageButton>(R.id.btnSettings)
        btnSettings.setOnClickListener(clickListener)

        viewMousePad = findViewById<View>(R.id.viewMousePad)
        txtCoordinates = findViewById<View>(R.id.txtCoordinates) as TextView
        showHideCoordinates()

        KeyboardSpecialButtons = listOf(
            findViewById<Button>(R.id.keyboardSpace)
        )

        KeyboardSpecialImageButtons = listOf(
            findViewById<ImageButton>(R.id.keyboardShift),
            findViewById<ImageButton>(R.id.keyboardBackspace),
            findViewById<ImageButton>(R.id.keyboardEnter)
        )

        for (specialBtn : Button in KeyboardSpecialButtons)
        {
            specialBtn.setOnClickListener(clickListener);
        }

        for (imageSpecialBtn : ImageButton in KeyboardSpecialImageButtons)
        {
            imageSpecialBtn.setOnClickListener(clickListener);
        }

        KeyboardOtherButtons = listOf(
            findViewById<Button>(R.id.keyboard1)
            , findViewById<Button>(R.id.keyboard2)
            , findViewById<Button>(R.id.keyboard3)
            , findViewById<Button>(R.id.keyboard4)
            , findViewById<Button>(R.id.keyboard5)
            , findViewById<Button>(R.id.keyboard6)
            , findViewById<Button>(R.id.keyboard7)
            , findViewById<Button>(R.id.keyboard8)
            , findViewById<Button>(R.id.keyboard9)
            , findViewById<Button>(R.id.keyboard0)
            , findViewById<Button>(R.id.keyboardMinore)
            , findViewById<Button>(R.id.keyboardMaggiore)
            , findViewById<Button>(R.id.keyboardQuestion)
            , findViewById<Button>(R.id.keyboardPoint)
            , findViewById<Button>(R.id.keyboardColon)
            , findViewById<Button>(R.id.keyboardComma)
            , findViewById<Button>(R.id.keyboardSemicolon)
            , findViewById<Button>(R.id.keyboardExclamation)
        )

        for (otherBtn : Button in KeyboardOtherButtons)
        {
            otherBtn.setOnClickListener(clickListener);
        }

        //mutable perché devono poter essere convertiti in maiuscolo e/o viceversa
        KeyboardButtons = mutableListOf(
            findViewById<Button>(R.id.keyboardA)
            , findViewById<Button>(R.id.keyboardB)
            , findViewById<Button>(R.id.keyboardC)
            , findViewById<Button>(R.id.keyboardD)
            , findViewById<Button>(R.id.keyboardE)
            , findViewById<Button>(R.id.keyboardF)
            , findViewById<Button>(R.id.keyboardG)
            , findViewById<Button>(R.id.keyboardH)
            , findViewById<Button>(R.id.keyboardI)
            , findViewById<Button>(R.id.keyboardJ)
            , findViewById<Button>(R.id.keyboardK)
            , findViewById<Button>(R.id.keyboardL)
            , findViewById<Button>(R.id.keyboardM)
            , findViewById<Button>(R.id.keyboardN)
            , findViewById<Button>(R.id.keyboardO)
            , findViewById<Button>(R.id.keyboardP)
            , findViewById<Button>(R.id.keyboardQ)
            , findViewById<Button>(R.id.keyboardR)
            , findViewById<Button>(R.id.keyboardS)
            , findViewById<Button>(R.id.keyboardT)
            , findViewById<Button>(R.id.keyboardU)
            , findViewById<Button>(R.id.keyboardV)
            , findViewById<Button>(R.id.keyboardW)
            , findViewById<Button>(R.id.keyboardX)
            , findViewById<Button>(R.id.keyboardY)
            , findViewById<Button>(R.id.keyboardZ)
            )

        for (btn : Button in KeyboardButtons)
        {
            btn.setOnClickListener(clickListener);
        }

        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build() //questa policy è ancora necessaria?
        StrictMode.setThreadPolicy(policy)

        parameters.readFromMemory()

        sc = SocketClient(parameters.getServerIP(), parameters.getServerPort())
        sc.start() //sc.run()

        viewMousePad.setOnTouchListener(touchListener)
    }
    //funzione che a seconda dell'evento relativo al touchpad, aggiornerà le coordinate correnti
    private fun touchGesture()
    {
        var msg: String? = null

        if (coordinates.getOldX() == null) { //inizializzazione coordinate dell'ultima posizione rilevata
            coordinates.updateOld()
        }
        testo = "X: ${coordinates.getX()} Y: ${coordinates.getY()} OldX: ${coordinates.getOldX()} OldY: ${coordinates.getOldY()}" //assemblaggio messaggio per il server
        txtCoordinates.text = testo

        if (coordinates.areNotNull()) { //controlla se sono state catturate nuove coordinate, e cioè se è stato rilevato un movimento
            if (parameters.getMode() == ModeClass.DIRECT.getMode()) {
                    if (widthViewMousePad == 0) {
                        widthViewMousePad = viewMousePad.measuredWidth
                    }
                    if (heightViewMousePad == 0) {
                        heightViewMousePad = viewMousePad.measuredHeight
                    }

                    msg = coordinates.computeDifference(
                        parameters.getResolutionWidth(),
                        parameters.getResolutionHeight(),
                        widthViewMousePad,
                        heightViewMousePad
                    )
                }
                else
                {
                    if (coordinates.areChanged()) {
                        msg = coordinates.computeDifference(
                            //velocità movimento cursore = speed
                            parameters.getSpeed()
                        )
                    }
                }
                coordinates.updateOld()
            }
            if (msg != null) {
                writeSocket(msg)
            }
    }

    private val touchListener = View.OnTouchListener { v, event ->
        v.performClick()
        val action = event.action
        //0 click
        //1 rilascio
        //2 pressed

        if (action == 0)
        {
            coordinates.setXY(event.x.toInt(), event.y.toInt())
            coordinates.updateOld()
            touchGesture()
        }
        else if (action == 1)
        {
            coordinates.setXY(event.x.toInt(), event.y.toInt()) //invia ultima posizione e poi setta x e y a null dal momento che non c'è più nessun input
            touchGesture()

            coordinates.setXY(null, null)
            coordinates.updateOld()
        }
        coordinates.setXY(event.x.toInt(), event.y.toInt())
        touchGesture()
        //debug
        //txtCoordinates.text = "coordinate -> x==${coordinates.getX()}, y==${coordinates.getY()}, action==$action"

        true
    }


    private fun writeSocket(command: String)
    {
        var res = sc.write(command)
        if (!res.first)
        {
            when (res.second) {
                "" -> {
                    //niente
                }
                "CANNOT_WRITE" -> {
                    //ancora deve arrivare una risposta da parte del server alla precedente write
                }
                "NOT_CONNECTED" -> {
                    showDialogReconnectSocket()
                }
                else -> {
                    showMessage(res.second)
                }
            }
        }
    }

    private fun showDialogReconnectSocket()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.app_name)
        builder.setMessage("Si è verificato un errore durante l'invio della richiesta al pc")

        builder.setPositiveButton("CHIUDI") { dialog, which ->
            exitProcess(0)
        }

        builder.setNegativeButton("TENTA RICONNESSIONE") { dialog, which ->
            sc.connect(parameters.getServerIP(), parameters.getServerPort())
        }

        builder.setNeutralButton("IMPOSTAZIONI") { dialog, which ->
            showSettingsActivity()
        }

        builder.show()
    }

    //di seguito viene creato un listener riusato da più risorse
    private val clickListener = View.OnClickListener { v ->
        var tmpString: String = ""
        val id: Int = v.getId()

        when (id) {
            R.id.buttonLeft -> {
                //sc.write("MOUSE_SX")
                //sc.write("MOUSE_SX_200")
                writeSocket("MCL")
            }
            R.id.buttonRight -> {
                //sc.write("MOUSE_DX")
                writeSocket("MCR")
            }
            in KeyboardButtons.map { it.id } -> {
                var buttonPressed = (KeyboardButtons.filter { it.id == id })[0]
                tmpString = buttonPressed.text.toString()
                writeSocket("CHR_" + tmpString)
            }
            in KeyboardOtherButtons.map { it.id } -> {
                var buttonPressed = (KeyboardOtherButtons.filter { it.id == id })[0]
                tmpString = buttonPressed.text.toString()
                writeSocket("CHR_" + tmpString)
            }
            in KeyboardSpecialButtons.map { it.id } -> {
                when (v.getId()) {
                    R.id.keyboardSpace -> {
                        tmpString = "CHR_ "
                    }
                }
                if (tmpString != "")
                {
                    writeSocket(tmpString)
                }
            }
            in KeyboardSpecialImageButtons.map { it.id } -> {
                when (v.getId()) {
                    R.id.keyboardBackspace -> {
                        tmpString = "CHR_BKSP"
                    }
                    R.id.keyboardEnter -> {
                        tmpString = "CHR_ENT"
                    }
                    R.id.keyboardShift -> {
                        var color = (v.background as ColorDrawable).color

                        if (color == getResources().getColor(R.color.keyboardShiftNormal))
                        {
                            v.setBackgroundResource(R.color.keyboardShiftPressed)

                            for (KeyboardButton in KeyboardButtons) {
                                KeyboardButton.text = KeyboardButton.text.toString().uppercase()
                            }
                        }
                        else
                        {
                            v.setBackgroundResource(R.color.keyboardShiftNormal)
                            for (KeyboardButton in KeyboardButtons) {
                                KeyboardButton.text = KeyboardButton.text.toString().lowercase()
                            }
                        }
                    }
                }
                if (tmpString != "")
                {
                    writeSocket(tmpString)
                }
            }
            R.id.btnSettings -> {
                showSettingsActivity()
            }

        }
    }

    //intent esplicita per il passaggio di parametri
    private fun showSettingsActivity()
    {
        val intentInput = Intent(this, SettingsClass::class.java)
        intentInput.putExtra("parameters_ip", parameters.getServerIP())
        intentInput.putExtra("parameters_port", parameters.getServerPort())
        intentInput.putExtra("parameters_mode", parameters.getMode())
        intentInput.putExtra("parameters_speed", parameters.getSpeed())
        intentInput.putExtra("parameters_resolution_w", parameters.getResolutionWidth())
        intentInput.putExtra("parameters_resolution_h", parameters.getResolutionHeight())
        intentInput.putExtra("parameters_show_coordinates", parameters.getShowCoordinates())
        //startActivity(intentInput)
        startActivityForResult(intentInput, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val saveParameters = data.getBooleanExtra("save_parameters", false)
                if (saveParameters)
                {
                    var result = false
                    var changedConnectionParameters = false

                    val ip = data.getStringExtra("parameters_ip")!!
                    val port = data.getIntExtra("parameters_port", 0)
                    val mode = data.getIntExtra("parameters_mode", ModeClass.TOUCHPAD.getMode())
                    val speed = data.getIntExtra("parameters_speed", 0)
                    val resolutionWidth = data.getIntExtra("parameters_resolution_w", 0)
                    val resolutionHeight = data.getIntExtra("parameters_resolution_h", 0)
                    val showCoordinates = data.getBooleanExtra("parameters_show_coordinates", false)

                    if (ip != parameters.getServerIP() || port != parameters.getServerPort())
                    {
                        changedConnectionParameters = true
                    }

                    parameters.setServerIP(ip)
                    parameters.setServerPort(port)
                    if (ModeClass.TOUCHPAD.getMode() == mode)
                        parameters.setMode(ModeClass.TOUCHPAD)
                    else if (ModeClass.DIRECT.getMode() == mode)
                        parameters.setMode(ModeClass.DIRECT)
                    parameters.setSpeed(speed)
                    parameters.setResolutionWidth(resolutionWidth)
                    parameters.setResolutionHeight(resolutionHeight)
                    parameters.setShowCoordinates(showCoordinates)
                    if (parameters.writeInMemory())
                    {
                        showMessage("Parametri memorizzati con successo !")
                        result = true;
                    }
                    else
                    {
                        showMessage("Errore durante la memorizzazione dei parametri !")
                    }

                    if (result && changedConnectionParameters)
                    {
                        sc.connect(ip, port)
                    }

                    showHideCoordinates()

                    //TODO
                    //getione caduta di connessione soket

                    //TODO piu dopo
                    //gestione del touch come DIRECT
                }
            }
        }
    }

    private fun showHideCoordinates()
    {
        if (parameters.getShowCoordinates())
        {
            txtCoordinates.visibility = View.VISIBLE
        }
        else
        {
            txtCoordinates.visibility = View.INVISIBLE
        }
    }

    fun showMessage(msg : String)
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.app_name)
        builder.setMessage(msg)

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            Toast.makeText(applicationContext,
                android.R.string.yes, Toast.LENGTH_SHORT).show()
        }

        builder.show()
    }

}