package co.qredet.demo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import co.qredet.demo.ui.theme.QredetTheme
import co.qredet.sdk.Qredet

class MainActivity : ComponentActivity() {

    private val qredet = Qredet("QREDET_API_KEY_HERE",::onTransactionInitiated, ::onTransactionCompleted)
    
    fun onTransactionCompleted(data: String) {
        Log.i("FINAL RESULT", data)
        // do whatever you want to do with the data received
    }
    
    fun onTransactionInitiated(data: Map<String, *>) {
        Log.i("TRANSACTION INITIATED", data.toString())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QredetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Qredet SDK")

                }
            }
        }
        
        // Example usage - uncomment the line you want to test:
        
        // Start a transaction (merchant side)
        // qredet.startTransaction(this, "txn-123", 100.0, "REF-001", mapOf("merchantName" to "Demo Merchant"))
        
        // Complete a transaction (customer side)
        // qredet.completeTransaction(this)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QredetTheme {
        Greeting("Qredet SDK")
    }
}
