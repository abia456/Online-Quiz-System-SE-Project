package com.se.onlinequizsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StudentQuizListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_quiz_list);
        RecyclerView list = findViewById(R.id.recyclerViewActiveQuizzes);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new StudentQuizListAdapter(this));
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // Not calling super, disables back button in current screen.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_student_quiz_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.student_ql_log_out) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}