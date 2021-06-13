package com.se.onlinequizsystem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Login {
    private static final String TAG = "=== Login ===";

    static int loginUser(Context context, String id, String password) {
        int type = -1;

        QuizDbHelper dbHelper = new QuizDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            String query = "select * from users where userName=? and uPassword=?";
            Cursor cursor = db.rawQuery(query, new String[]{id, password});

            if (cursor != null && cursor.moveToFirst()) {                   // Always one row returned.
                type = Integer.parseInt(cursor.getString(cursor.getColumnIndex("uType")));
                Log.d(TAG, "loginUser: login successful");
            } else {
                Log.d(TAG, "loginUser: login unsuccessful   nothing returned");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "loginUser: login unsuccessful");
        }
        db.close();

        return type;
    }

    static Question fetchMCQ(Context context, int id) {
        Question q = new Question();

        QuizDbHelper dbHelper = new QuizDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            String query = "select * from Question where qID = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

            if (cursor != null && cursor.moveToFirst()) {                   // Always one row returned.
                q.qId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("qID")));
                q.qType = Integer.parseInt(cursor.getString(cursor.getColumnIndex("qType")));
                q.qText = cursor.getString(cursor.getColumnIndex("qText"));
                q.qMarks = Integer.parseInt(cursor.getString(cursor.getColumnIndex("qMarks")));
                q.qAnsPossible.add(cursor.getString(cursor.getColumnIndex("posAns1")));
                q.qAnsPossible.add(cursor.getString(cursor.getColumnIndex("posAns2")));
                q.qAnsPossible.add(cursor.getString(cursor.getColumnIndex("posAns3")));
                q.qAnsPossible.add(cursor.getString(cursor.getColumnIndex("posAns4")));
                q.trueAnswers.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns1"))));
                q.trueAnswers.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns2"))));
                q.trueAnswers.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns3"))));
                q.trueAnswers.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns4"))));
            } else {
                Log.d(TAG, "fetchMCQ: 0 rows read");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();

        return q;
    }

    static boolean checkSingularMCQ(Context context, int qId, int choice) // options 1, 2, 3, 4 selected corresond to 0, 1, 2, 3
    {
        boolean check = false;

        QuizDbHelper dbHelper = new QuizDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            String query = "select * from Question where qID = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(qId)});

            if (cursor != null && cursor.moveToFirst()) {                   // Always one row returned.
                if (choice==1 && Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns1"))) == 1) {
                    check = true;
                }
                if (choice==2 && Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns2"))) == 1) {
                    check = true;
                }
                if (choice==3 && Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns3"))) == 1) {
                    check = true;
                }
                if (choice==4 && Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns4"))) == 1) {
                    check = true;
                }
            } else {
                Log.d(TAG, "checkSingularMCQ: 0 rows read");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();

        return check;
    }

    static boolean checkObjective(Context context, int qId, int[] choices) // options 1, 2, 3, 4 selected corresond to 0, 1, 2, 3
    {
        boolean check = true;

        QuizDbHelper dbHelper = new QuizDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            String query = "select * from Question where qID = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(qId)});

            if (cursor != null && cursor.moveToFirst()) {                   // Always one row returnd
                int totalAns = choices.length;
                for (int i = 0; i < totalAns; i++) {
                    if (choices[i] == 1 && Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns1"))) != 1) {
                        check = false;
                    } else if (choices[i] == 2 && Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns2"))) != 1) {
                        check = false;
                    } else if (choices[i] == 3 && Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns3"))) != 1) {
                        check = false;
                    } else if (choices[i] == 4 && Integer.parseInt(cursor.getString(cursor.getColumnIndex("valAns4"))) != 1) {
                        check = false;
                    }
                }
            } else {
                Log.d(TAG, "checkObjective: 0 rows read");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();

        return check;
    }


}