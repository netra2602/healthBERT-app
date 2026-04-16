package com.healthbert.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var model: HealthBERTModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = HealthBERTModel()

        val editTextReview = findViewById<EditText>(R.id.editTextReview)
        val buttonAnalyze = findViewById<Button>(R.id.buttonAnalyze)
        val textViewSentiment = findViewById<TextView>(R.id.textViewSentiment)
        val textViewAspect = findViewById<TextView>(R.id.textViewAspect)
        val textViewConfidence = findViewById<TextView>(R.id.textViewConfidence)

        buttonAnalyze.setOnClickListener {
            val review = editTextReview.text.toString().trim()

            if (review.isNotEmpty()) {
                val result = model.predict(review)

                textViewSentiment.text = "Sentiment: ${result.sentiment}"
                textViewAspect.text = "Aspect: ${result.aspect}"
                textViewConfidence.text = "Confidence: ${result.confidence}"
            } else {
                textViewSentiment.text = "Sentiment: Please enter a review"
                textViewAspect.text = "Aspect: -"
                textViewConfidence.text = "Confidence: -"
            }
        }
    }
}