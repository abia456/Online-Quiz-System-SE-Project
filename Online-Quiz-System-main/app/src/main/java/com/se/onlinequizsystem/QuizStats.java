package com.se.onlinequizsystem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class QuizStats {
    private static final String TAG = "=== QuizStats ===";
    int totalStudents;
    int quizAttempted;
    Double averageTime;
    ArrayList<Double> averageQuestionTime;

    public QuizStats(Context context, int quizID) {
        int idx = 0;
        this.averageTime = 0.0;
        this.averageQuestionTime = new ArrayList<Double>();


        QuizDbHelper dbHelper = new QuizDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            Log.d(TAG, "QuizStats: read quiz stats");

            String query = "select quizID, questionID, " +
                    "AVG(timeTaken) as avgTimeQuestion, " +
                    "AVG(marksScored) as avgMarks " +
                    "from studentAttempt " +
                    "where quizID = " + quizID +
                    " GROUP by quizID, questionID;";

            Cursor cursor = db.rawQuery(query, null);
//            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(quizID)});

            while (cursor.moveToNext()) {
                Log.d(TAG, "QuizStats: inside time sql reader");
                
//                idx += 1;
                Double tempAverageTime = Double.parseDouble(cursor.getString(cursor.getColumnIndex("avgTimeQuestion")));
                Log.d(TAG, "QuizStats: average time: " + tempAverageTime);

                this.averageTime += tempAverageTime;
                Log.d(TAG, "QuizStats: quiz average time: " + String.valueOf(this.averageTime));

                this.averageQuestionTime.add(tempAverageTime);
                Log.d(TAG, "QuizStats: average question time: " + String.valueOf(this.averageQuestionTime));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            Log.d(TAG, "QuizStats: read quiz stats");

            String query = "select COUNT(DISTINCT userID) as cnt from studentAttempt where quizID = " + quizID;

            Cursor cursor = db.rawQuery(query, null);
//            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(quizID)});

            if (cursor != null && cursor.moveToFirst()) {
                this.quizAttempted = Integer.parseInt(cursor.getString(cursor.getColumnIndex("cnt")));

                Log.d(TAG, "QuizStats: total quizzes attempted count: " + this.quizAttempted);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Log.d(TAG, "QuizStats: read total students");
            String query = "select COUNT(userID) as cnt from users where uType = 0;";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                this.totalStudents = Integer.parseInt(cursor.getString(cursor.getColumnIndex("cnt")));

                Log.d(TAG, "QuizStats: total student count: " + this.totalStudents);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "QuizStats: stats read");
        db.close();

//        this.quizAttempted = idx;
    }
}
