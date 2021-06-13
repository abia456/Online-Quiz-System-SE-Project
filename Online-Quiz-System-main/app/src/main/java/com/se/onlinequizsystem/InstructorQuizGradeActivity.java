package com.se.onlinequizsystem;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class InstructorQuizGradeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_quiz_grade);
    }

    public void InstructorQuizGrade1(View view) {
        setContentView(R.layout.activity_instructor_quiz_grade_student_question_1);
    }

    public void InstructorQuizGradeStudentQuestion1Next(View view) {
        setContentView(R.layout.activity_instructor_quiz_grade_student_question_2);
    }

    public void InstructorQuizGradeStudentQuestion2Next(View view) {
        setContentView(R.layout.activity_instructor_quiz_grade_student_question_3);
    }

    public void InstructorQuizGradeStudentQuestion2Back(View view) {
        setContentView(R.layout.activity_instructor_quiz_grade_student_question_1);
    }

    public void InstructorQuizGradeStudentQuestion3Back(View view) {
        setContentView(R.layout.activity_instructor_quiz_grade_student_question_2);
    }

    public void InstructorQuizGradeStudentQuestion3Submit(View view) {
        setContentView(R.layout.activity_instructor_quiz_grade);
    }

}