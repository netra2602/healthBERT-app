package com.healthbert.app

data class PredictionResult(
    val sentiment: String,
    val aspect: String,
    val confidence: Float
)

class HealthBERTModel {

    fun predict(review: String): PredictionResult {
        val sentiment = when {
            review.contains("good", ignoreCase = true) ||
                    review.contains("great", ignoreCase = true) ||
                    review.contains("excellent", ignoreCase = true) -> "Positive"

            review.contains("bad", ignoreCase = true) ||
                    review.contains("poor", ignoreCase = true) ||
                    review.contains("worst", ignoreCase = true) -> "Negative"

            else -> "Neutral"
        }

        val aspect = when {
            review.contains("doctor", ignoreCase = true) -> "Doctor Interaction"
            review.contains("staff", ignoreCase = true) -> "Staff Behavior"
            review.contains("hospital", ignoreCase = true) ||
                    review.contains("facility", ignoreCase = true) -> "Hospital Facilities"
            review.contains("treatment", ignoreCase = true) ||
                    review.contains("medicine", ignoreCase = true) -> "Treatment Effectiveness"
            else -> "General"
        }

        val confidence = 0.85f

        return PredictionResult(sentiment, aspect, confidence)
    }
}