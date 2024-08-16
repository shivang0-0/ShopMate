package com.sparkathon.shopmate.main

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sparkathon.shopmate.ui.theme.ShopMateTheme
import com.sparkathon.shopmate.preferences.addToCart
import com.sparkathon.shopmate.main.screens.InStoreMapScreen
import com.sparkathon.shopmate.main.MainScreen

class MainActivity : ComponentActivity() {

    private var nfcAdapter: NfcAdapter? = null
    private var nfcTagData: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopMateTheme {
                MainScreen(nfcTagData = nfcTagData) // Pass nfcTagData to MainScreen
            }
        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available on this device.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.let {
            val intent = Intent(this, javaClass).apply {
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_MUTABLE
            )
            it.enableForegroundDispatch(this, pendingIntent, null, null)
        }
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNfcIntent(intent)
    }

    private fun handleNfcIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action ||
            NfcAdapter.ACTION_TECH_DISCOVERED == action ||
            NfcAdapter.ACTION_TAG_DISCOVERED == action) {

            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            tag?.let { nfcTag ->
                val ndef = Ndef.get(nfcTag)
                ndef?.connect()
                val ndefMessage = ndef?.ndefMessage
                val payload = ndefMessage?.records?.get(0)?.payload
                var data = String(payload ?: ByteArray(0)).trim()
                data = data.substring(3) // Remove any unnecessary prefix
                Log.d("NFC", "Data: $data")

                if (data.startsWith("locator")) {
                    // If the data contains "locator", open the map
                    nfcTagData = data
                    setContent {
                        ShopMateTheme {
                            InStoreMapScreen(nfcTagData = nfcTagData) // Pass the NFC data to the map screen
                        }
                    }
                } else if (data.all { it.isDigit() }) {
                    // If the data is a number, perform the cart functionality
                    addToCart(this, data)
                }

                ndef?.close()
            }
        }
    }
}
