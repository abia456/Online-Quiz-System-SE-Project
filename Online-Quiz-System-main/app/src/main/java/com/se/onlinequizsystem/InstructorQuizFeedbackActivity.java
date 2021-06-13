package com.se.onlinequizsystem;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class InstructorQuizFeedbackActivity extends AppCompatActivity {

    private static final String TAG = "=== InstructorQuizFeedback ===";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_quiz_feedback);

        TextView averageTimeTakenTextView = findViewById(R.id.instructor_quiz_feedback_average_time_taken);
        TextView quizzesAttemptedTextView = findViewById(R.id.instructor_quiz_feedback_quizzes_attempted);
        TextView totalStudentsTextView = findViewById(R.id.instructor_quiz_feedback_total_students);

        QuizStats quizStats = new QuizStats(this, 1);

        averageTimeTakenTextView.setText(String.valueOf((quizStats.averageTime)/3));
        quizzesAttemptedTextView.setText(String.valueOf(quizStats.quizAttempted));
        totalStudentsTextView.setText(String.valueOf(quizStats.totalStudents));

        // TODO: 02-Jan-21 check if correct data

        // convert question time double to string
        ArrayList<String> averageQuestionTime = new ArrayList<>();
        int i = 1;
        for (double val : quizStats.averageQuestionTime) {
            averageQuestionTime.add("Question " + i + " :\t" + String.valueOf(val));
            i++;
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.instructor_quiz_feedback_question_time_listview, averageQuestionTime);
        ListView listView = (ListView) findViewById(R.id.instructor_quiz_feedback_question_time_list);
        listView.setAdapter(adapter);

    }
}