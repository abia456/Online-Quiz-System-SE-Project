package com.se.onlinequizsystem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "=== QuizDbHelper ===";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "QuizDB.db";

    public QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        sql = "create table users(userID integer not null," + "userName varchar(100) not null,"
                + "uPassword varchar(100) not null," + "uType integer not null," + "primary key(userID));";
        db.execSQL(sql);

        sql = "create table quiz(quizID integer not null," + "quizName varchar(100)," + "difficulty integer,"
                + "openTime integer," + "closeTime integer," + "totalTimeInSeconds integer," + "totalQuestions integer,"
                + "totalMarks integer," + "instructions varchar(1000)," + "primary key(quizID));";
        db.execSQL(sql);

        sql = "create table question(qID integer primary key," + "qType integer," + "qText varchar(1000),"
                + "qMarks integer," + "qAnswer varchar(1000)," + "posAns1 varchar(1000)," + "posAns2 varchar(1000),"
                + "posAns3 varchar(1000)," + "posAns4 varchar(1000)," + "valAns1 integer," + "valAns2 integer,"
                + "valAns3 integer," + "valAns4 integer," + "sel1 integer," + "sel2 integer," + "sel3 integer,"
                + "sel4 integer);";
        db.execSQL(sql);

        sql = "create table quizQuestions(quizID integer," + "questionID integer,"
                + "foreign key (quizID) references Quiz(quizID),"
                + "foreign key (QuestionID) references Questions(questionID)," + "primary key(quizID, questionID));";
        db.execSQL(sql);

        sql = "create table studentAttempt(" + "userID integer," + "QuizID integer," + "QuestionID integer," + "enteredAns varchar(1000),"
                + "timeTaken integer," + "marksScored integer," + "foreign key (userID) references users(userID),"
                + "foreign key (quizID) references Quiz(quizID),"
                + "foreign key (QuestionID) references Questions(questionID),"
                + "primary key (userID, quizID, questionID)" + ")";
        db.execSQL(sql);

        sql = "create table studentSubmission(" +
                "userID integer, quizID integer, submitted integer," +
                "foreign key (userID) references users(userID)," +
                "foreign key (quizID) references Quiz(quizID)," +
                "primary key (userID, quizID))";
        db.execSQL(sql);

        Log.i(TAG, "onCreate: created tables");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS quiz");
        db.execSQL("DROP TABLE IF EXISTS question");
        db.execSQL("DROP TABLE IF EXISTS quizQuestions");
        db.execSQL("DROP TABLE IF EXISTS studentAttempt");
        onCreate(db);
    }

}
