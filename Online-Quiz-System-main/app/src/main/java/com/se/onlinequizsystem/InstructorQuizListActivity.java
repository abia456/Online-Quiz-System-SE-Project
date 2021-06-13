package com.se.onlinequizsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class InstructorQuizListActivity extends AppCompatActivity {

    private static final String TAG = "=== InstructorQuizListActivity ===";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: init");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_quiz_list);
    }


    public void InstructorQuizItem1Button(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Quiz1")
                .setMessage("SE-E\n07/12/2020 5:00PM")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Show Feedback", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = getBaseContext();
                        Intent intent = new Intent(context, InstructorQuizFeedbackActivity.class);
                        startActivity(intent);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Grade Quiz", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = getBaseContext();
                        Intent intent = new Intent(context, InstructorQuizGradeActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // Not calling super, disables back button in current screen.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_quiz_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.question_bank_menu_button:
                intent = new Intent(this, InstructorQuestionBankListActivity.class);
                startActivity(intent);

                return true;
            case R.id.ql_log_out:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void InstructorQuizListAddButton(View view) {
        Intent intent = new Intent(this, InstructorQuizGenerationActivity.class);
        startActivity(intent);
    }
}