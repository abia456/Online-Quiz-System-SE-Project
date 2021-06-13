package com.se.onlinequizsystem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    private static final String TAG = "=== Question ===";
    public int qId;
    public int qType; // 1 MCQ single, 2 MCQ multiple, 3 TF, 4 Obj, 5 Sbj
    public String qText;
    public int qMarks;
    public boolean attempted;
    public boolean hasbeenInserted;
    public List<String> qAnsPossible = new ArrayList<String>(); // Assuming all these sizes 4, discuss with Murad
    public List<Integer> trueAnswers = new ArrayList<Integer>();
    // public List<int> qAnsSelected = new ArrayList<int>();
    // public String qAnswer; // Subjective or Objective

    public Question() {

    }

    public Question(int questionID) {
        qId = questionID;
    }

    public Question(int qId, int qType, String qText, int qMarks) {
        this.qId = qId;
        this.qType = qType;
        this.qText = qText;
        this.qMarks = qMarks;
        this.attempted = false;
        this.hasbeenInserted=false;
        // Add possible answers incase of an MCQ/TF
    }

    //takes questionID as int, gets and populates object from DB
    public void updateQuestion(Context context) {

        QuizDbHelper dbHelper = new QuizDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            Log.d(TAG, "updateQuestion: questionID: " + this.qId);
            String query = "select * from Question where qID = " + this.qId;
            Cursor cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {// Always one row returned.

                this.qType = Integer.parseInt(cursor.getString(cursor.getColumnIndex("qType")));
                this.qText = cursor.getString(cursor.getColumnIndex("qText"));
                this.qMarks = Integer.parseInt(cursor.getString(cursor.getColumnIndex("qMarks")));
                this.hasbeenInserted= false;

                if (this.qType == 1 || this.qType == 2) {
                    this.qAnsPossible.add(cursor.getString(cursor.getColumnIndex("posAns1")));
                    this.qAnsPossible.add(cursor.getString(cursor.getColumnIndex("posAns2")));
                    this.qAnsPossible.add(cursor.getString(cursor.getColumnIndex("posAns3")));
                    this.qAnsPossible.add(cursor.getString(cursor.getColumnIndex("posAns4")));
                    this.trueAnswers.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns1"))));
                    this.trueAnswers.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns2"))));
                    this.trueAnswers.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns3"))));
                    this.trueAnswers.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns4"))));
                } else if (this.qType == 3) {
                    this.qAnsPossible.add(cursor.getString(cursor.getColumnIndex("posAns1")));
                    this.qAnsPossible.add(cursor.getString(cursor.getColumnIndex("posAns2")));
                    this.trueAnswers.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns1"))));
                    this.trueAnswers.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns2"))));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void AddOptions(List<String> temp) {
        this.qAnsPossible.addAll(temp);
    }

    public void AddAnswers(List<Integer> temp) {
        this.trueAnswers.addAll(temp);
    }
}