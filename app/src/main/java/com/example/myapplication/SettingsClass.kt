package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatToggleButton


class SettingsClass : android.app.Activity() {

    lateinit var layoutResolutionWidth: View
    lateinit var layoutResolutionHeight: View

    lateinit var txtServerIP : TextView
    lateinit var txtServerPort : TextView
    lateinit var spnMode : Spinner
    lateinit var sliderSpeed : SeekBar
    lateinit var txtResolutionWidth : TextView
    lateinit var txtResolutionHeight : TextView
    lateinit var tbShowCoordinates : AppCompatToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnSettingsSave = findViewById<Button>(R.id.buttonSettingsSave)
        btnSettingsSave.setOnClickListener(clickListener)
        val btnSettingsExit = findViewById<Button>(R.id.buttonSettingsExit)
        btnSettingsExit.setOnClickListener(clickListener)

        layoutResolutionWidth = findViewById<TextView>(R.id.layoutResolutionWidth)
        layoutResolutionHeight = findViewById<TextView>(R.id.layoutResolutionHeight)

        txtServerIP = findViewById<TextView>(R.id.txtIPAddress)
        txtServerPort = findViewById<TextView>(R.id.txtServerPort)
        spnMode = findViewById<Spinner>(R.id.spnMode)
        sliderSpeed = findViewById<SeekBar>(R.id.sliderSpeed)
        txtResolutionWidth = findViewById<TextView>(R.id.txtResolutionWidth)
        txtResolutionHeight = findViewById<TextView>(R.id.txtResolutionHeight)
        tbShowCoordinates = findViewById<AppCompatToggleButton>(R.id.tbShowCoordinates)

        //spnMode.setOnItemClickListener(itemSelectedListener)
        spnMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { //metodo richiamato quando un oggetto viene selezionato, position è il parametro interessato
                if (position == 0)
                {
                    txtResolutionWidth.isFocusable = false
                    txtResolutionWidth.isEnabled = false

                    txtResolutionHeight.isFocusable = false
                    txtResolutionHeight.isEnabled = false

                    sliderSpeed.isFocusable = true
                    sliderSpeed.isEnabled = true
                }
                else
                {
                    txtResolutionWidth.isFocusable = true
                    txtResolutionWidth.isEnabled = true

                    txtResolutionHeight.isFocusable = true
                    txtResolutionHeight.isEnabled = true

                    sliderSpeed.isFocusable = false
                    sliderSpeed.isEnabled = false
                }
            }

        }

        val extras = intent.extras

        if (extras != null) {
            txtServerIP.setText(extras!!.getString("parameters_ip")) // retrieve the data using keyName
            txtServerPort.setText(extras!!.getInt("parameters_port").toString()) // retrieve the data using keyName
            spnMode.setSelection(extras!!.getInt("parameters_mode")) // retrieve the data using keyName
            sliderSpeed.progress = extras!!.getInt("parameters_speed")
            txtResolutionWidth.setText(extras!!.getInt("parameters_resolution_w").toString()) // retrieve the data using keyName
            txtResolutionHeight.setText(extras!!.getInt("parameters_resolution_h").toString()) // retrieve the data using keyName
            tbShowCoordinates.isChecked = extras!!.getBoolean("parameters_show_coordinates") // retrieve the data using keyName
        }
    }

    private val clickListener = View.OnClickListener { v ->
        when (v.getId()) {
            R.id.buttonSettingsSave -> {
                val data = Intent()
                data.putExtra("save_parameters", true)
                data.putExtra("parameters_ip", txtServerIP.text.toString())
                data.putExtra("parameters_port", txtServerPort.text.toString().toInt())
                data.putExtra("parameters_mode", spnMode.selectedItemPosition)
                data.putExtra("parameters_speed", sliderSpeed.progress)
                data.putExtra("parameters_resolution_w", txtResolutionWidth.text.toString().toInt())
                data.putExtra("parameters_resolution_h", txtResolutionHeight.text.toString().toInt())
                data.putExtra("parameters_show_coordinates", tbShowCoordinates.isChecked)
                setResult(RESULT_OK, data)
                finish()
            }
            R.id.buttonSettingsExit -> {
                val data = Intent()
                data.putExtra("save_parameters", false)
                setResult(RESULT_OK, data)
                finish()
            }
        }
    }
}