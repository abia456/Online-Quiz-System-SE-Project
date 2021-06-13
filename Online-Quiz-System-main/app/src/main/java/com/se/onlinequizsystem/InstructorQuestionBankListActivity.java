package com.se.onlinequizsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class InstructorQuestionBankListActivity extends AppCompatActivity {
    private static final String TAG = "=== InstructorQuestionBankListA ===";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: init");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_question_bank_list);

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // Not calling super, disables back button in current screen.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_question_bank, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.quiz_list_menu_button:
                intent = new Intent(this, InstructorQuizListActivity.class);
                startActivity(intent);

                return true;
            case R.id.qb_log_out:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void InstructorQuestionBankAddButton(View view) {
        Intent intent = new Intent(this, InstructorAddEditQuestionActivity.class);
        startActivity(intent);
    }

    public void InstructorQuestionBankEditButton(View view) {
        Intent intent = new Intent(this, InstructorAddEditQuestionActivity.class);
        startActivity(intent);
    }
}