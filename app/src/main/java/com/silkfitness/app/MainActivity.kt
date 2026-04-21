package com.silkfitness.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.silkfitness.app.ui.navigation.NavGraph
import com.silkfitness.app.ui.theme.SilkFitnessTheme
import com.silkfitness.app.ui.theme.SurfaceBackground
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilkFitnessTheme {
                NavGraph(modifier = Modifier.fillMaxSize().background(SurfaceBackground))
            }
        }
    }
}
