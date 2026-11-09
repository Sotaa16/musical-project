package com.example.mucialproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mucialproject.ui.theme.MucialProjectTheme

class MainActivity : ComponentActivity() {
    private lateinit var sounds: DrumSounds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        sounds = DrumSounds(this)
        setContent {
            MucialProjectTheme {
                DrumScreen(onHit = {sounds.play(it)})
            }
        }
    }

    override fun onDestroy() {
        sounds.release()
        super.onDestroy()
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
    MucialProjectTheme {
        Greeting("Android")
    }
}