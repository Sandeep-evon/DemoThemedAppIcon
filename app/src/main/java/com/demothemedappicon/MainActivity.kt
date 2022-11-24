package com.demothemedappicon

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.demothemedappicon.databinding.ActivityMainBinding
import com.demothemedappicon.receiver.ConnectivityReceiver
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions



class MainActivity : AppCompatActivity(),OnItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private lateinit var binding: ActivityMainBinding
    private  var countryFrom= arrayListOf<String>("English")
    private lateinit var countryName: Array<String>
    private lateinit var countryCode: Array<String>
    private lateinit var from:String
    private lateinit var to:String
    private var translator: Translator? = null
    private var flag:Boolean = false
    private var snackBar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        countryName = resources.getStringArray(R.array.languageName)
        countryCode = resources.getStringArray(R.array.languageCode)
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countryName)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerLanguageFrom.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countryFrom)
        binding.spinnerLanguageTo.adapter = adapter
        binding.spinnerLanguageFrom.onItemSelectedListener = this
        binding.spinnerLanguageTo.onItemSelectedListener = this
        binding.buttonTranslate.setOnClickListener {
            if(binding.editText.text.isNotEmpty()){
                binding.progressBar.visibility = View.VISIBLE
                val translatorOptions= TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(to)
                    .build()
                translator = Translation.getClient(translatorOptions);
                val downloadConditions = DownloadConditions.Builder()
                    .requireWifi()
                    .build()
                translator!!.downloadModelIfNeeded(downloadConditions)
                    .addOnSuccessListener {
                        flag = true
                        binding.progressBar.visibility = View.GONE
                        translator!!.translate(binding.editText.getText().toString())
                            .addOnSuccessListener { s ->
                                binding.textView.text = s
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
                                binding.textView.text = e.toString()
                            }
                    }
                    .addOnFailureListener {
                        flag = false
                        binding.progressBar.visibility = View.GONE
                    }
            }else{
                Toast.makeText(this,"Please enter the text",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onPause() {
        super.onPause()
        ConnectivityReceiver.connectivityReceiverListener = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(parent?.id==binding.spinnerLanguageFrom.id){
            from = countryCode[position]
        }else{
            to = countryCode[position]
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (!isConnected) {
            snackBar = Snackbar.make(findViewById(R.id.rootLayout), "You are offline", Snackbar.LENGTH_LONG) //Assume "rootLayout" as the root layout of every activity.
            snackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackBar?.show()
        } else {
            snackBar?.dismiss()
        }
    }

}