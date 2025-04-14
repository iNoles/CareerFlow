package com.jonathansteele.careerflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel to handle the career advice logic
class CareerFlowViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash", // Replace with your actual model name
        apiKey = BuildConfig.apiKey // Replace with your actual API key
    )

    fun getCareerAdvice(prompt: String) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Structured prompt to guide the AI
                val fullPrompt = """
                    Act as a career advisor. The user is interested in the following areas: $prompt.
                    Recommend career paths, required skills, and a brief learning roadmap.
                """.trimIndent()

                val response = generativeModel.generateContent(
                    content {
                        text(fullPrompt)
                    }
                )

                response.text?.let { output ->
                    _uiState.value = UiState.Success(output)
                } ?: run {
                    _uiState.value = UiState.Error("No response from AI")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}
