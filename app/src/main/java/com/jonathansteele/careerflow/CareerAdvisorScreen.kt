package com.jonathansteele.careerflow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jonathansteele.careerflow.ui.theme.CareerFlowTheme

@Composable
fun CareerAdvisorScreen(modifier: Modifier = Modifier, viewModel: CareerFlowViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var prompt by rememberSaveable { mutableStateOf("") }
    val predefinedInterests = listOf(
        "Problem Solving", "Helping People", "Math", "Creative Writing", "Coding", "Biology"
    )

    // Apply the modifier here
    Column(modifier = modifier.fillMaxSize()) {
        TextField(
            value = prompt,
            onValueChange = { prompt = it },
            label = { Text("Enter your interests/skills") },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )

        InterestChipsWrap(
            interests = predefinedInterests,
            selectedInterest = prompt,
            onInterestSelected = { prompt = it }
        )

        Button(
            onClick = { viewModel.getCareerAdvice(prompt) },
            enabled = prompt.isNotEmpty(),
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Get Career Advice")
        }

        // Handle results display
        if (uiState is UiState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState is UiState.Success) {
            val result = (uiState as UiState.Success).outputText
            CareerAdviceTimeline(result = result)
        } else if (uiState is UiState.Error) {
            Text(
                text = (uiState as UiState.Error).errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InterestChipsWrap(
    interests: List<String>,
    selectedInterest: String,
    onInterestSelected: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        interests.forEach { interest ->
            FilterChip(
                selected = selectedInterest == interest,
                onClick = { onInterestSelected(interest) },
                label = { Text(interest) },
                modifier = Modifier.padding(4.dp) // spacing between chips
            )
        }
    }
}

@Composable
fun CareerAdviceTimeline(result: String) {
    val milestones = result.split("\n")  // assuming the AI returns a step-by-step list of career advice

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(milestones) { milestone ->
            Card(
                modifier = Modifier.padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = milestone, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CareerAdvisorScreenPreview() {
    CareerFlowTheme {
        CareerAdvisorScreen(modifier = Modifier.padding(16.dp)) // Example with external modifier
    }
}