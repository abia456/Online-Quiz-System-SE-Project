package com.se.onlinequizsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class InstructorQuizGenerationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_quiz_generation);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Going Back to Main Screen")
                .setMessage("You will lose all your progress. Are you sure?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }

    public void InstructorQuizGenerationNext(View view) {
        RadioButton radioButtonManual = findViewById(R.id.radio_button_quiz_generation_type_manual);
        RadioButton radioButtonAutomatic = findViewById(R.id.radio_button_quiz_generation_type_automatic);
        RadioButton radioButtonAutomaticDifficulty = findViewById(R.id.radio_button_quiz_generation_type_automatic_difficulty);
        if (radioButtonManual.isChecked()) {
            setContentView(R.layout.activity_instructor_quiz_generation_manual);
        }
        if (radioButtonAutomatic.isChecked()) {
            setContentView(R.layout.activity_instructor_quiz_generation_automatic);
        }
        if (radioButtonAutomaticDifficulty.isChecked()) {
            setContentView(R.layout.activity_instructor_quiz_generation_automatic_difficulty);
        }
    }

    public void InstructorQuizGenerationManualGenerate(View view) {
        Intent intent = new Intent(this, InstructorQuizListActivity.class);
        startActivity(intent);
    }

    public void InstructorQuizGenerationManualBack(View view) {
        setContentView(R.layout.activity_instructor_quiz_generation);
        RadioButton radioButtonManual = findViewById(R.id.radio_button_quiz_generation_type_manual);
        radioButtonManual.setChecked(true);
    }

    public void InstructorQuizGenerationAutomaticGenerate(View view) {
        Intent intent = new Intent(this, InstructorQuizListActivity.class);
        startActivity(intent);
    }

    public void InstructorQuizGenerationAutomaticBack(View view) {
        setContentView(R.layout.activity_instructor_quiz_generation);
        RadioButton radioButtonAutomatic = findViewById(R.id.radio_button_quiz_generation_type_automatic);
        radioButtonAutomatic.setChecked(true);
    }

    public void InstructorQuizGenerationAutomaticDifficultyGenerate(View view) {
        Intent intent = new Intent(this, InstructorQuizListActivity.class);
        startActivity(intent);
    }

    public void InstructorAddEditQuestionObjectiveDifficultyBack(View view) {
        setContentView(R.layout.activity_instructor_quiz_generation);
        RadioButton radioButtonAutomaticDifficulty = findViewById(R.id.radio_button_quiz_generation_type_automatic_difficulty);
        radioButtonAutomaticDifficulty.setChecked(true);
    }
}