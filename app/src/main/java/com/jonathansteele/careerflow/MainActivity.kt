package com.jonathansteele.careerflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.jonathansteele.careerflow.ui.theme.CareerFlowTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CareerFlowTheme {
                // Use Scaffold for more structured layout with AppBar
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(text = "Career Advisor") }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    CareerAdvisorScreen(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}
