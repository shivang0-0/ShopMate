package com.sparkathon.shopmate.main.screens

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
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.sparkathon.shopmate.ui.theme.ShopMateTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import com.sparkathon.shopmate.R

class InStoreMapActivity : ComponentActivity() {

    private var nfcAdapter: NfcAdapter? = null
    private val nfcTagData = mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopMateTheme {
                InStoreMapScreen(nfcTagData.value)
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
            Log.d("NFC", "Tag: $tag")
            tag?.let { nfcTag ->
                val ndef = Ndef.get(nfcTag)
                Log.d("NFC", "NDEF: $ndef")
                ndef?.connect()
                val ndefMessage = ndef?.ndefMessage
                val payload = ndefMessage?.records?.get(0)?.payload
                val locatorId = String(payload ?: ByteArray(0)).trim()
                Log.d("NFC", "Locator ID: $locatorId")
                nfcTagData.value = locatorId  // Handling map-related NFC
                Toast.makeText(this, "You are here", Toast.LENGTH_SHORT).show() // Display the toast
                ndef?.close()
            }
        }
    }
}

@Composable
fun InStoreMapScreen(nfcTagData: String?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (nfcTagData != null) {
            // Display the image corresponding to the nfcTagData
            val imageRes = when (nfcTagData) {
                "locator1" -> R.drawable.locator1 // Replace with your actual image resources
//                "locator2" -> R.drawable.locator2 // Replace with your actual image resources
                else -> R.drawable.default_locator // Fallback image
            }
            Image(painter = painterResource(id = imageRes), contentDescription = null)
        } else {
            // Display default text when no NFC tag has been read
            Text(text = "Tap on the nearest Locator")
        }
    }
}
