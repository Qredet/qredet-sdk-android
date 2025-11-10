package co.qredet.demo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.qredet.demo.ui.theme.QredetTheme
import co.qredet.sdk.Qredet
import org.json.JSONObject
import java.util.UUID

class MainActivity : ComponentActivity() {

    private val qredet = Qredet("QREDET_API_KEY_HERE",::onTransactionInitiated, ::onTransactionCompleted)
    private val transactionStatus = mutableStateOf("Tap the button to start a transaction")
    
    fun onTransactionCompleted(data: String) {
        Log.i("FINAL RESULT", data)
        updateStatus("Transaction completed:\n$data")
    }
    
    fun onTransactionInitiated(data: Map<String, *>) {
        Log.i("TRANSACTION INITIATED", data.toString())
        updateStatus("Transaction initiated:\n${JSONObject(data as Map<*, *>).toString()}")
    }

    private fun updateStatus(message: String) {
        runOnUiThread {
            transactionStatus.value = message
        }
    }

    private fun startTransactionDemo() {
        updateStatus("Starting transaction…")
        val extras = mapOf("merchantName" to "Demo Merchant")
        val transactionId = "txn-demo-${UUID.randomUUID()}"
        qredet.startTransaction(this, transactionId, 100.0, extras)
    }

    private fun launchReaderDemo() {
        updateStatus("Waiting for NFC tap…")
        qredet.completeTransaction(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QredetTheme {
                val status by transactionStatus
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TransactionDemoScreen(
                        status = status,
                        onStartTransaction = ::startTransactionDemo,
                        onReadNfc = ::launchReaderDemo
                    )
                }
            }
        }
        
        // Example usage - uncomment the line you want to test:
        
        // Start a transaction (merchant side)
        // qredet.startTransaction(this, "txn-123", 100.0, "REF-001", mapOf("merchantName" to "Demo Merchant"))
        
        // Complete a transaction (customer side) - already triggered above for demo
        // qredet.completeTransaction(this)
    }
}

@Composable
fun TransactionDemoScreen(
    status: String,
    onStartTransaction: () -> Unit,
    onReadNfc: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Qredet SDK Demo",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onStartTransaction) {
            Text(text = "Start Transaction")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onReadNfc) {
            Text(text = "Read NFC Data")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = status,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionDemoScreenPreview() {
    QredetTheme {
        TransactionDemoScreen(
            status = "Transaction completed:\n{\"amount\": 100.0}",
            onStartTransaction = {},
            onReadNfc = {}
        )
    }
}
