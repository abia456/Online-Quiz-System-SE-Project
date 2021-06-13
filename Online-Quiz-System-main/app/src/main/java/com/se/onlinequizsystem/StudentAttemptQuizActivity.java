
package com.se.onlinequizsystem;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class StudentAttemptQuizActivity extends AppCompatActivity {
    private static final String TAG = "=== StudentAttemptQuizActivity ===";
    int Qno;
    Stopwatch[] sw;
    Boolean Attempted = false; //false is equal to not attempted
    int count = 0;
    long[] Time_perQuestion; //time for each question stored here
    CountDownTimer timeLeftTimer;
    private Quiz quiz;
    private Menu menu;
    private long timeLeft;
    private ArrayList<Question> questionsList;
    View v;
    Spinner spinner;
    Boolean FromSpinner= false;
    int FromSpinnerQ=0;
    int nullifyFIrstSelction=0;

    int opt1Checked=0;
    int opt2Checked=0;
    int opt3Checked=0;
    Context c;

    String Answer="";
    String Answer2="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c=getApplicationContext();

        Intent intent = getIntent();
        quiz = (Quiz) intent.getSerializableExtra("quizViewIntent");
        questionsList = quiz.listOfQuestions;
        Qno = -1;

        sw = new Stopwatch[questionsList.size()];
        int i;
        for (i = 0; i < questionsList.size(); i++) {
            sw[i] = new Stopwatch();
        }

        Time_perQuestion = new long[questionsList.size()];
        for (i = 0; i < questionsList.size(); i++) {
            Time_perQuestion[i] = 0;
        }

        // Time Left Functionality
        Calendar currentCalender = Calendar.getInstance();
        long currentTimeEpoch = currentCalender.getTimeInMillis();
        long timeLeftToClose = (quiz.closeTime * 1000L) - currentTimeEpoch;
        timeLeft = quiz.totalTime * 1000L; // convert quiz duration from seconds to milliseconds
        timeLeft -= Quiz.getQuizAttemptTime(this, 1, quiz.quizID);
        if (timeLeft > timeLeftToClose) {
            timeLeft = timeLeftToClose;
        }

        timeLeftTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                MenuItem timeLeftItem = menu.findItem(R.id.quiz_time_left);
                timeLeftItem.setTitle("Time Left: " + hms);
            }

            @Override
            public void onFinish() {
                Quiz.submitQuiz(getApplicationContext(), 1, quiz.quizID);
                Intent intent = new Intent(getApplicationContext(), StudentQuizListActivity.class);
                startActivity(intent);
            }
        };
        setContentView(R.layout.activity_student_attempt_quiz);
    }

    public void StudentAttemptNextQuestion(View view) {
        Qno++; //next question
        Intent intent = getIntent();
        Quiz quiz = (Quiz) intent.getSerializableExtra("quizViewIntent");

        // Context context = getApplicationContext();
        //CharSequence text = "Lenght !"+ String.valueOf(questionsList.size());
        //int duration = Toast.LENGTH_SHORT;
        //Toast toast = Toast.makeText(context, text, duration);
        //toast.show();

        if (Qno >= questionsList.size()) {
            HideMenuItems();
            setContentView(R.layout.attempt_submit);

            ////////////////////////////set all paused timers to stop before submitting////////////////
            int i;
            for (i = 0; i < sw.length; i++) {
                if (sw[i].paused) {
                    sw[i].stop();
                    Time_perQuestion[i] = 0; //not attempted
                }
            }

            ////////////////////////////////////////////////////////////////////////////

        } else {
            Question q = questionsList.get(Qno);
            TextView QuestionTxt;
            TextView QuesNo;


            ///////////////////////////// time the question (try: next, next back)///////////////////////
            if (Time_perQuestion[Qno] == 0) {  //if time has already not been calculated
                if (count % 2 == 0) // meaning question started
                {

                    if (!sw[Qno].paused) {

                        sw[Qno].start();
                    } else {
                        sw[Qno].resume();
                    }
                }
                if (count >= 1) {


                    if (sw[Qno - 1].running) {

                        ArrayList<Integer> Attempted_Questions = Quiz.getQuestionAttemptList(getApplicationContext(), 1, quiz.quizID);

                        int i = 0;
                        for (i = 0; i < Attempted_Questions.size(); i++) {
                            if (Attempted_Questions.get(i) == (Qno - 1)) {
                                Attempted = true;
                            } else {
                                Attempted = false;
                            }
                        }
                        if (Attempted == true) {
                            long timetaken = sw[Qno - 1].getElapsedTimeSecs();
                            Time_perQuestion[Qno - 1] = timetaken;
                            sw[Qno - 1].stop();
                        } else {
                            sw[Qno - 1].pause(); // pause prev question if not attempted
                        }
                    }
                }
                else //meaning question ended wiht next pressed
                {
                    if (!sw[Qno].paused) {
                        sw[Qno].start();
                    } else {
                        sw[Qno].resume();
                    }

                    if ((Qno - 1) >= 0){
                        if (sw[Qno - 1].running) {
                            ArrayList<Integer> Attempted_Questions = Quiz.getQuestionAttemptList(getApplicationContext(), 1, quiz.quizID);
                            int i = 0;
                            for (i = 0; i < Attempted_Questions.size(); i++) {
                                if (Attempted_Questions.get(i) == (Qno - 1)) {
                                    Attempted = true;
                                } else {
                                    Attempted = false;
                                }
                            }
                            if (Attempted) {
                                long timetaken = sw[Qno - 1].getElapsedTimeSecs();
                                Time_perQuestion[Qno - 1] = timetaken;
                                sw[Qno - 1].stop();
                            } else {
                                sw[Qno - 1].pause(); // pause prev question
                            }
                        }
                    }

                }
                count++;

            }

            /////update in DB//////////



            if (q.qType == 1) // MCQ single
            {
                setContentView(R.layout.attempt_quiz_mcq_single_ans);

                // add question
                QuestionTxt = findViewById(R.id.MCQ_single_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizMCQ_single_qno);
                QuesNo.setText(String.valueOf(Qno + 1));

                //add options
                RadioButton RB1;
                RadioButton RB2;
                RadioButton RB3;

                RB1 = findViewById(R.id.MCQ_single_sound);
                RB1.setText(q.qAnsPossible.get(0));

                RB2 = findViewById(R.id.MCQ_single_vibration);
                RB2.setText(q.qAnsPossible.get(1));

                RB3 = findViewById(R.id.MCQ_single_silent);
                RB3.setText(q.qAnsPossible.get(2));

//////////////////////////////////////////////////////////////////////////////////////////////////////

                if(CHeckifAttempted(Qno+1))
                {

                    String choices= quiz.getQuestionAttemptChoices(c,1,quiz.quizID,Qno+1);
                    ArrayList<Integer> choicesInt=quiz.getMCQArrayfromSubmissionString(choices);

                    int k=0;
                    for(k=0; k<choicesInt.size();k++)
                    {
                        if(choicesInt.get(k)==1)
                        {
                            RB1.setChecked(true);
                        }
                        else if(choicesInt.get(k)==2)
                        {
                            RB2.setChecked(true);
                        }
                        else{
                            RB3.setChecked(true);
                        }

                    }

                }
                //////////////////////////////////////////////////////////

            } else if (q.qType == 2) //MCQ multiple
            {
                setContentView(R.layout.attempt_quiz_mcq_multiple_ans);

                // add question
                QuestionTxt = findViewById(R.id.MCQ_mul_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizMCQ_mul_qno);
                QuesNo.setText(String.valueOf(Qno + 1));

                //add options
                CheckBox RB1;
                CheckBox RB2;
                CheckBox RB3;

                RB1 = findViewById(R.id.MCQ_mul_checkBox);
                RB1.setText(q.qAnsPossible.get(0));

                RB2 = findViewById(R.id.MCQ_mul_checkBox2);
                RB2.setText(q.qAnsPossible.get(1));

                RB3 = findViewById(R.id.MCQ_mul_checkBox3);
                RB3.setText(q.qAnsPossible.get(2));
                if(CHeckifAttempted(Qno+1))
                {

                    String choices= quiz.getQuestionAttemptChoices(c,1,quiz.quizID,Qno+1);
                    ArrayList<Integer> choicesInt=quiz.getMCQArrayfromSubmissionString(choices);

                    int k=0;
                    for(k=0; k<choicesInt.size();k++)
                    {
                        if(choicesInt.get(k)==1)
                        {
                            RB1.setChecked(true);
                        }
                        else if(choicesInt.get(k)==2)
                        {
                            RB2.setChecked(true);
                        }
                        else{
                            RB3.setChecked(true);
                        }

                    }

                }
                //////////////////////////////////////////////////////////



            } else if (q.qType == 3) //true false
            {
                setContentView(R.layout.attempt_quiz_truefalse);



                // add question
                QuestionTxt = findViewById(R.id.mcq_tf_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizMCQ_tf_qno);
                QuesNo.setText(String.valueOf(Qno + 1));

                RadioButton RB1;
                RadioButton RB2;

                RB1= findViewById(R.id.mcq_tf_sound);
                RB2= findViewById(R.id.mcq_tf_vibration);
                RB1.setChecked(false);
                RB2.setChecked(false);

                if(CHeckifAttempted(Qno+1))
                {
                    String choices= quiz.getQuestionAttemptChoices(c,1,quiz.quizID,Qno+1);
                    ArrayList<Integer> choicesInt=quiz.getMCQArrayfromSubmissionString(choices);

                    int k=0;
                    for(k=0; k<choicesInt.size();k++)
                    {
                        if(choicesInt.get(k)==1)
                        {
                            RB1.setChecked(true);
                        }
                        else
                        {
                            RB2.setChecked(true);
                        }


                    }

                }
////////////////////////////////////
            }

            ///////////////////////////////////////////////////////////////

            else if (q.qType == 4) //ow question
            {
                //sub_ow_question
                setContentView(R.layout.attempt_quiz_subjective_ow);
                //get the spinner from the xml.

                // add question
                QuestionTxt = findViewById(R.id.sub_ow_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizSubjective_ow_qno);
                QuesNo.setText(String.valueOf(Qno + 1));

                EditText OW= findViewById(R.id.sub_ow_editTextTextPersonName);
                saveAnswerObjective();
                if(CHeckifAttempted(Qno+1))
                {
                    OW.setText(Answer);
                }



            } else if (q.qType == 5) //mw question
            {
                setContentView(R.layout.attempt_quiz_subejective_multple_words);

                // add question
                QuestionTxt = findViewById(R.id.sub_words_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizsubjective_mul_qno);
                QuesNo.setText(String.valueOf(Qno + 1));

                EditText OW= findViewById(R.id.editTextTextPersonName);
                saveAnswerSubjective();
                if(CHeckifAttempted1(Qno+1))
                {
                    OW.setText(Answer2);
                }


            } else {
                //setContentView(R.layout.attempt_quiz_mcq_multiple_ans);
                //get the spinner from the xml.
                //Spinner dropdown = findViewById(R.id.spinner1);
                //create a list of items for the spinner.
                //String[] items = new String[]{"1", "2", "3"};
                //create an adapter to describe how the items are displayed, adapters are used in several places in android.
                //There are multiple variations of this, but this is the basic variant.
                //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
                //set the spinners adapter to the previously created one.
                //dropdown.setAdapter(adapter);
            }

            if(q.qType==1 || q.qType==2|| q.qType==3 || q.qType==4) {
                if (opt1Checked == 1 || opt2Checked == 1 || opt3Checked == 1) {
                    String choices = quiz.generateMCQsubmissionString(opt1Checked, opt2Checked, opt3Checked, 0);

                    if (questionsList.get(Qno).hasbeenInserted == false) {
                        Time_perQuestion[0]= 10;
                        Time_perQuestion[1]= 15;
                        Time_perQuestion[2]= 35;
                        Time_perQuestion[3]= 10;
                        Time_perQuestion[4]= 10;


                        quiz.insertQuestionAttempt(c, 1, quiz.quizID, Qno, Integer.parseInt(String.valueOf(Time_perQuestion[Qno])), choices);
                        RefreshAdapter();
                        questionsList.get(Qno).hasbeenInserted = true;
                    } else {
                        quiz.updateQuestionAttempt(c, 1, quiz.quizID, Qno, Integer.parseInt(String.valueOf(Time_perQuestion[Qno])), choices);
                    }
                }
                opt1Checked = 0;
                opt2Checked = 0;
                opt3Checked = 0;
            }
            else if(q.qType==4|| q.qType==5)
            {

                if(Answer.matches(""))
                {
                    //Do nothing
                }
                else{
                    String choices=Answer;
                    if (questionsList.get(Qno).hasbeenInserted == false) {

                        quiz.insertQuestionAttempt(c, 1, quiz.quizID, Qno, Integer.parseInt(String.valueOf(Time_perQuestion[Qno])), choices);
                        RefreshAdapter();
                        questionsList.get(Qno).hasbeenInserted = true;
                    } else {
                        quiz.updateQuestionAttempt(c, 1, quiz.quizID, Qno, Integer.parseInt(String.valueOf(Time_perQuestion[Qno])), choices);
                    }
                }

            }
            //////////////////////////////////////////////////////////////////
        }


    }

    public void StudentAttemptPrevQuestion(View view) {

        Qno--; //previous question


        Context c = getApplicationContext();
        if (Qno < 0) {
            Qno++;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Close Quiz?");
            builder.setMessage("");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            timeLeftTimer.cancel();
                            Intent intent = new Intent(c, StudentQuizListActivity.class);
                            startActivity(intent);
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        } else {


            Question q = questionsList.get(Qno);
            TextView QuestionTxt;
            TextView QuesNo;

            Intent intent = getIntent();
            Quiz quiz = (Quiz) intent.getSerializableExtra("quizViewIntent");


            /////////////////////////// timer per question ///////////////////////////////////////
            // TODO: 04/01/2021 ABIA
            // COMMENTED THIS CODE BECAUSE IT WAS CAUSING OUT OF BOUND EXCEPTION ON LINE 396 for sw[Qno + 1]
            if((Qno+1)<sw.length) {
                if (sw[Qno + 1] != null) {
                    if (sw[Qno + 1].running == true) //question from which back button was pressed-> Qno+1
                    {
                        sw[Qno + 1].pause();
                        //TODO: if question is attempted,then store time
                        ArrayList<Integer> Attempted_Questions = Quiz.getQuestionAttemptList(getApplicationContext(), 1, quiz.quizID);
                        int i = 0;
                        for (i = 0; i < Attempted_Questions.size(); i++) {
                            if (Attempted_Questions.get(i) == (Qno + 1)) {
                                Attempted = true;
                            } else {
                                Attempted = false;
                            }
                        }
                        if (Attempted == true) {
                            long timetaken = sw[Qno + 1].getElapsedTimeSecs();
                            Time_perQuestion[Qno + 1] = timetaken;
                            sw[Qno + 1].stop();
                        }
                    }
                    if (sw[Qno].paused == true) //question to which back button took->Qno
                    {
                        sw[Qno].resume();
                    }
                }
            }
            //////////////////////////////////////////////////////////////////////////////////////
            if (q.qType == 1)// MCQ single
            {
                setContentView(R.layout.attempt_quiz_mcq_single_ans);

                // add question
                QuestionTxt = findViewById(R.id.MCQ_single_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizMCQ_single_qno);
                QuesNo.setText(String.valueOf(Qno + 1));

                //add options
                RadioButton RB1;
                RadioButton RB2;
                RadioButton RB3;

                RB1 = findViewById(R.id.MCQ_single_sound);
                RB1.setText(q.qAnsPossible.get(0));

                RB2 = findViewById(R.id.MCQ_single_vibration);
                RB2.setText(q.qAnsPossible.get(1));

                RB3 = findViewById(R.id.MCQ_single_silent);
                RB3.setText(q.qAnsPossible.get(2));


                if(CHeckifAttempted(Qno+1))
                {
                    String choices= quiz.getQuestionAttemptChoices(c,1,quiz.quizID,Qno+1);
                    ArrayList<Integer> choicesInt=quiz.getMCQArrayfromSubmissionString(choices);

                    int k=0;
                    for(k=0; k<choicesInt.size();k++)
                    {
                        if(choicesInt.get(k)==1)
                        {
                            RB1.setChecked(true);
                        }
                        else if(choicesInt.get(k)==2)
                        {
                            RB2.setChecked(true);
                        }
                        else{
                            RB3.setChecked(true);
                        }

                    }

                }


            } else if (q.qType == 2) //MCQ multiple
            {
                setContentView(R.layout.attempt_quiz_mcq_multiple_ans);



                // add question
                QuestionTxt = findViewById(R.id.MCQ_mul_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizMCQ_mul_qno);
                QuesNo.setText(String.valueOf(Qno + 1));

                //add options
                CheckBox RB1;
                CheckBox RB2;
                CheckBox RB3;

                RB1 = findViewById(R.id.MCQ_mul_checkBox);
                RB1.setText(q.qAnsPossible.get(0));

                RB2 = findViewById(R.id.MCQ_mul_checkBox2);
                RB2.setText(q.qAnsPossible.get(1));

                RB3 = findViewById(R.id.MCQ_mul_checkBox3);
                RB3.setText(q.qAnsPossible.get(2));

                if(CHeckifAttempted(Qno+1))
                {
                    String choices= quiz.getQuestionAttemptChoices(c,1,quiz.quizID,Qno+1);
                    ArrayList<Integer> choicesInt=quiz.getMCQArrayfromSubmissionString(choices);

                    int k=0;
                    for(k=0; k<choicesInt.size();k++)
                    {
                        if(choicesInt.get(k)==1)
                        {
                            RB1.setChecked(true);
                        }
                        else if(choicesInt.get(k)==2)
                        {
                            RB2.setChecked(true);
                        }
                        else{
                            RB3.setChecked(true);
                        }

                    }

                }


            } else if (q.qType == 3) //true false
            {
                setContentView(R.layout.attempt_quiz_truefalse);


                // add question
                QuestionTxt = findViewById(R.id.mcq_tf_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizMCQ_tf_qno);
                QuesNo.setText(String.valueOf(Qno + 1));

                RadioButton RB1;
                RadioButton RB2;

                RB1= findViewById(R.id.mcq_tf_sound);
                RB2= findViewById(R.id.mcq_tf_vibration);
                RB1.setChecked(false);
                RB2.setChecked(false);

                if(CHeckifAttempted(Qno+1))
                {

                    String choices= quiz.getQuestionAttemptChoices(c,1,quiz.quizID,Qno+1);
                    ArrayList<Integer> choicesInt=quiz.getMCQArrayfromSubmissionString(choices);

                    int k=0;
                    for(k=0; k<choicesInt.size();k++)
                    {
                        if(choicesInt.get(k)==1)
                        {
                            RB1.setChecked(true);
                        }
                        else
                        {
                            RB2.setChecked(true);
                        }


                    }

                }


            }

            ///////////////////////////////////////////////////////////////
            else if (q.qType == 4) //ow question
            {
                //sub_ow_question
                setContentView(R.layout.attempt_quiz_subjective_ow);

                // add question
                QuestionTxt = findViewById(R.id.sub_ow_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizSubjective_ow_qno);
                QuesNo.setText(String.valueOf(Qno + 1));

                EditText OW= findViewById(R.id.sub_ow_editTextTextPersonName);
                saveAnswerObjective();
                if(CHeckifAttempted(Qno+1))
                {
                    OW.setText(Answer);
                }

            } else if (q.qType == 5) //mw question
            {
                setContentView(R.layout.attempt_quiz_subejective_multple_words);


                // add question
                QuestionTxt = findViewById(R.id.sub_words_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizsubjective_mul_qno);
                QuesNo.setText(String.valueOf(Qno + 1));

                EditText OW= findViewById(R.id.editTextTextPersonName);
                saveAnswerSubjective();
                if(CHeckifAttempted1(Qno+1))
                {
                    OW.setText(Answer2);
                }




            } else {
                //setContentView(R.layout.attempt_quiz_mcq_multiple_ans);
                //get the spinner from the xml.
                //Spinner dropdown = findViewById(R.id.spinner1);
                //create a list of items for the spinner.
                //String[] items = new String[]{"1", "2", "3"};
                //create an adapter to describe how the items are displayed, adapters are used in several places in android.
                //There are multiple variations of this, but this is the basic variant.
                //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
                //set the spinners adapter to the previously created one.
                //dropdown.setAdapter(adapter);
            } if(q.qType==1 || q.qType==2|| q.qType==3 || q.qType==4) {
                if (opt1Checked == 1 || opt2Checked == 1 || opt3Checked == 1) {
                    String choices = quiz.generateMCQsubmissionString(opt1Checked, opt2Checked, opt3Checked, 0);

                    if (questionsList.get(Qno).hasbeenInserted == false) {
                        Time_perQuestion[0]= 10;
                        Time_perQuestion[1]= 15;
                        Time_perQuestion[2]= 35;
                        Time_perQuestion[3]= 10;
                        Time_perQuestion[4]= 10;


                        quiz.insertQuestionAttempt(c, 1, quiz.quizID, Qno, Integer.parseInt(String.valueOf(Time_perQuestion[Qno])), choices);
                        RefreshAdapter();
                        questionsList.get(Qno).hasbeenInserted = true;
                    } else {
                        quiz.updateQuestionAttempt(c, 1, quiz.quizID, Qno, Integer.parseInt(String.valueOf(Time_perQuestion[Qno])), choices);
                    }
                }
                opt1Checked = 0;
                opt2Checked = 0;
                opt3Checked = 0;
            }
            else if(q.qType==4|| q.qType==5)
            {

                if(Answer.matches(""))
                {
                    //Do nothing
                }
                else{
                    String choices=Answer;
                    if (questionsList.get(Qno).hasbeenInserted == false) {

                        quiz.insertQuestionAttempt(c, 1, quiz.quizID, Qno, Integer.parseInt(String.valueOf(Time_perQuestion[Qno])), choices);
                        RefreshAdapter();
                        questionsList.get(Qno).hasbeenInserted = true;
                    } else {
                        quiz.updateQuestionAttempt(c, 1, quiz.quizID, Qno, Integer.parseInt(String.valueOf(Time_perQuestion[Qno])), choices);
                    }
                }

            }
            //////////////////////////////////////////////////////////////////
        }
    }

    public void StudentAttemptGotToFirstQues(View view) {

        v=view;
        Qno = 0; //question no 1
        Context c = getApplicationContext();
        if (Qno < 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Close Quiz?");
            builder.setMessage("");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(c, StudentQuizListActivity.class);
                            startActivity(intent);

                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        } else {

            if(FromSpinner== true)
            {

                if(FromSpinnerQ>=0 && FromSpinnerQ<=questionsList.size())
                {
                    Qno= FromSpinnerQ-1;
                }

            }

            Question q = questionsList.get(Qno);
            TextView QuestionTxt;
            TextView QuesNo;

            Intent intent = getIntent();
            Quiz quiz = (Quiz) intent.getSerializableExtra("quizViewIntent");

            //timer per question part
            ////////////////
            int j;
            ArrayList<Integer> AttemptedQuestions=quiz.getQuestionAttemptList(getApplicationContext(),1,quiz.quizID);
            for(j=1; j<AttemptedQuestions.size();j++)
            {
                if(AttemptedQuestions.get(j)==j) {
                    Attempted = true;
                    if (sw[j].running) {
                        if (Attempted) {
                            Time_perQuestion[j] = sw[j].getElapsedTimeSecs();
                            sw[j].stop();
                        }
                    }
                }
                else{Attempted=false;}
            }
            if(sw[Qno].paused) //if current question was paused, resume it
            {
                sw[Qno].resume();
            }

            if (q.qType == 1)// MCQ single
            {
                setContentView(R.layout.attempt_quiz_mcq_single_ans);


                // add question
                QuestionTxt = findViewById(R.id.MCQ_single_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizMCQ_single_qno);
                QuesNo.setText(String.valueOf(Qno + 1));

                //add options
                RadioButton RB1;
                RadioButton RB2;
                RadioButton RB3;

                RB1 = findViewById(R.id.MCQ_single_sound);
                RB1.setText(q.qAnsPossible.get(0));

                RB2 = findViewById(R.id.MCQ_single_vibration);
                RB2.setText(q.qAnsPossible.get(1));

                RB3 = findViewById(R.id.MCQ_single_silent);
                RB3.setText(q.qAnsPossible.get(2));
                if(CHeckifAttempted(Qno+1))
                {
                    String choices= quiz.getQuestionAttemptChoices(c,1,quiz.quizID,Qno+1);
                    ArrayList<Integer> choicesInt=quiz.getMCQArrayfromSubmissionString(choices);

                    int k=0;
                    for(k=0; k<choicesInt.size();k++)
                    {
                        if(choicesInt.get(k)==1)
                        {
                            RB1.setChecked(true);
                        }
                        else if(choicesInt.get(k)==2)
                        {
                            RB2.setChecked(true);
                        }
                        else{
                            RB3.setChecked(true);
                        }

                    }

                }

            } else if (q.qType == 2) //MCQ multiple
            {
                setContentView(R.layout.attempt_quiz_mcq_multiple_ans);

                // add question
                QuestionTxt = findViewById(R.id.MCQ_mul_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizMCQ_mul_qno);
                QuesNo.setText(String.valueOf(Qno + 1));

                //add options
                CheckBox RB1;
                CheckBox RB2;
                CheckBox RB3;

                RB1 = findViewById(R.id.MCQ_mul_checkBox);
                RB1.setText(q.qAnsPossible.get(0));

                RB2 = findViewById(R.id.MCQ_mul_checkBox2);
                RB2.setText(q.qAnsPossible.get(1));

                RB3 = findViewById(R.id.MCQ_mul_checkBox3);
                RB3.setText(q.qAnsPossible.get(2));

                if(CHeckifAttempted(Qno+1))
                {
                    String choices= quiz.getQuestionAttemptChoices(c,1,quiz.quizID,Qno+1);
                    ArrayList<Integer> choicesInt=quiz.getMCQArrayfromSubmissionString(choices);

                    int k=0;
                    for(k=0; k<choicesInt.size();k++)
                    {
                        if(choicesInt.get(k)==1)
                        {
                            RB1.setChecked(true);
                        }
                        else if(choicesInt.get(k)==2)
                        {
                            RB2.setChecked(true);
                        }
                        else{
                            RB3.setChecked(true);
                        }

                    }

                }

            } else if (q.qType == 3) //true false
            {
                setContentView(R.layout.attempt_quiz_truefalse);


                // add question
                QuestionTxt = findViewById(R.id.mcq_tf_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizMCQ_tf_qno);
                QuesNo.setText(String.valueOf(Qno + 1));


            }

            ///////////////////////////////////////////////////////////////
            else if (q.qType == 4) //ow question
            {
                //sub_ow_question
                setContentView(R.layout.attempt_quiz_subjective_ow);


                // add question
                QuestionTxt = findViewById(R.id.sub_ow_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizSubjective_ow_qno);
                QuesNo.setText(String.valueOf(Qno + 1));

            } else if (q.qType == 5) //mw question
            {
                setContentView(R.layout.attempt_quiz_subejective_multple_words);


                // add question
                QuestionTxt = findViewById(R.id.sub_words_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizsubjective_mul_qno);
                QuesNo.setText(String.valueOf(Qno + 1));


            } else {
                //setContentView(R.layout.attempt_quiz_mcq_multiple_ans);
                //get the spinner from the xml.
                //Spinner dropdown = findViewById(R.id.spinner1);
                //create a list of items for the spinner.
                //String[] items = new String[]{"1", "2", "3"};
                //create an adapter to describe how the items are displayed, adapters are used in several places in android.
                //There are multiple variations of this, but this is the basic variant.
                //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
                //set the spinners adapter to the previously created one.
                //dropdown.setAdapter(adapter);
            }
        }

        if(FromSpinner==false) {
            Question q = questionsList.get(Qno);
            if (q.qType == 1 || q.qType == 2 || q.qType == 3 || q.qType == 4) {
                if (opt1Checked == 1 || opt2Checked == 1 || opt3Checked == 1) {
                    String choices = quiz.generateMCQsubmissionString(opt1Checked, opt2Checked, opt3Checked, 0);

                    if (questionsList.get(Qno).hasbeenInserted == false) {
                        Time_perQuestion[0] = 10;
                        Time_perQuestion[1] = 15;
                        Time_perQuestion[2] = 35;
                        Time_perQuestion[3] = 10;
                        Time_perQuestion[4] = 10;

                        quiz.insertQuestionAttempt(c, 1, quiz.quizID, Qno, Integer.parseInt(String.valueOf(Time_perQuestion[Qno])), choices);
                        RefreshAdapter();
                        questionsList.get(Qno).hasbeenInserted = true;
                    } else {
                        quiz.updateQuestionAttempt(c, 1, quiz.quizID, Qno, Integer.parseInt(String.valueOf(Time_perQuestion[Qno])), choices);
                    }
                }
                opt1Checked = 0;
                opt2Checked = 0;
                opt3Checked = 0;
            } else if (q.qType == 4 || q.qType == 5) {

                if (Answer.matches("")) {
                    //Do nothing
                } else {
                    String choices = Answer;
                    if (questionsList.get(Qno).hasbeenInserted == false) {

                        quiz.insertQuestionAttempt(c, 1, quiz.quizID, Qno, Integer.parseInt(String.valueOf(Time_perQuestion[Qno])), choices);
                        RefreshAdapter();
                        questionsList.get(Qno).hasbeenInserted = true;
                    } else {
                        quiz.updateQuestionAttempt(c, 1, quiz.quizID, Qno, Integer.parseInt(String.valueOf(Time_perQuestion[Qno])), choices);
                    }
                }

            }
        }
        FromSpinner=false;
        //////////////////////////////////////////////////////////////////
    }

    public void StudentAttemptGotToLastQues(View view) {

        Qno = questionsList.size() - 1; //last question question
        Context c = getApplicationContext();
        if (Qno < 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Close Quiz?");
            builder.setMessage("");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(c, StudentQuizListActivity.class);
                            startActivity(intent);

                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        } else {


            Question q = questionsList.get(Qno);
            TextView QuestionTxt;
            TextView QuesNo;
            Intent intent = getIntent();
            Quiz quiz = (Quiz) intent.getSerializableExtra("quizViewIntent");

            //timer per question part
            int j;
            ArrayList<Integer> AttemptedQuestions=quiz.getQuestionAttemptList(getApplicationContext(),1,quiz.quizID);
            for(j=0; j<AttemptedQuestions.size()-1;j++)
            {
                if(AttemptedQuestions.get(j)==j) {
                    Attempted = true;
                    if (sw[j].running) {
                        if (Attempted) {
                            Time_perQuestion[j] = sw[j].getElapsedTimeSecs();
                            sw[j].stop();
                        }
                    }
                }
                else{Attempted=false;}
            }
            if(sw[Qno].paused) //if current question was paused, resume it
            {
                sw[Qno].resume();
            }

            //////////////////////////////////////////////////////////////////

            if (q.qType == 1)// MCQ single
            {
                setContentView(R.layout.attempt_quiz_mcq_single_ans);

                // add question
                QuestionTxt = findViewById(R.id.MCQ_single_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizMCQ_single_qno);
                QuesNo.setText(String.valueOf(Qno + 1));

                //add options
                RadioButton RB1;
                RadioButton RB2;
                RadioButton RB3;

                RB1 = findViewById(R.id.MCQ_single_sound);
                RB1.setText(q.qAnsPossible.get(0));

                RB2 = findViewById(R.id.MCQ_single_vibration);
                RB2.setText(q.qAnsPossible.get(1));

                RB3 = findViewById(R.id.MCQ_single_silent);
                RB3.setText(q.qAnsPossible.get(2));



            } else if (q.qType == 2) //MCQ multiple
            {
                setContentView(R.layout.attempt_quiz_mcq_multiple_ans);


                // add question
                QuestionTxt = findViewById(R.id.MCQ_mul_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizMCQ_mul_qno);
                QuesNo.setText(String.valueOf(Qno + 1));

                //add options
                CheckBox RB1;
                CheckBox RB2;
                CheckBox RB3;

                RB1 = findViewById(R.id.MCQ_mul_checkBox);
                RB1.setText(q.qAnsPossible.get(0));

                RB2 = findViewById(R.id.MCQ_mul_checkBox2);
                RB2.setText(q.qAnsPossible.get(1));

                RB3 = findViewById(R.id.MCQ_mul_checkBox3);
                RB3.setText(q.qAnsPossible.get(2));


            } else if (q.qType == 3) //true false
            {
                setContentView(R.layout.attempt_quiz_truefalse);

                // add question
                QuestionTxt = findViewById(R.id.mcq_tf_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizMCQ_tf_qno);
                QuesNo.setText(String.valueOf(Qno + 1));


            }

            ///////////////////////////////////////////////////////////////
            else if (q.qType == 4) //ow question
            {
                //sub_ow_question
                setContentView(R.layout.attempt_quiz_subjective_ow);

                // add question
                QuestionTxt = findViewById(R.id.sub_ow_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizSubjective_ow_qno);
                QuesNo.setText(String.valueOf(Qno + 1));


                EditText OW= findViewById(R.id.sub_ow_editTextTextPersonName);
                saveAnswerObjective();
                if(CHeckifAttempted(Qno+1))
                {
                    OW.setText(Answer);
                }

            } else if (q.qType == 5) //mw question
            {
                setContentView(R.layout.attempt_quiz_subejective_multple_words);

                // add question
                QuestionTxt = findViewById(R.id.sub_words_question);
                QuestionTxt.setText(q.qText);

                //add qno
                QuesNo = findViewById(R.id.AttemptQuizsubjective_mul_qno);
                QuesNo.setText(String.valueOf(Qno + 1));

                EditText OW= findViewById(R.id.editTextTextPersonName);
                saveAnswerSubjective();
                if(CHeckifAttempted1(Qno+1))
                {
                    OW.setText(Answer2);
                }

            } else {
                //setContentView(R.layout.attempt_quiz_mcq_multiple_ans);
                //get the spinner from the xml.
                //Spinner dropdown = findViewById(R.id.spinner1);
                //create a list of items for the spinner.
                //String[] items = new String[]{"1", "2", "3"};
                //create an adapter to describe how the items are displayed, adapters are used in several places in android.
                //There are multiple variations of this, but this is the basic variant.
                //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
                //set the spinners adapter to the previously created one.
                //dropdown.setAdapter(adapter);
            }
        }

        Question q = questionsList.get(Qno);
        if (q.qType == 1 || q.qType == 2 || q.qType == 3 || q.qType == 4) {
            if (opt1Checked == 1 || opt2Checked == 1 || opt3Checked == 1) {
                String choices = quiz.generateMCQsubmissionString(opt1Checked, opt2Checked, opt3Checked, 0);

                if (questionsList.get(Qno).hasbeenInserted == false) {
                    Time_perQuestion[0] = 10;
                    Time_perQuestion[1] = 15;
                    Time_perQuestion[2] = 35;
                    Time_perQuestion[3] = 10;
                    Time_perQuestion[4] = 10;

                    quiz.insertQuestionAttempt(c, 1, quiz.quizID, Qno, Integer.parseInt(String.valueOf(Time_perQuestion[Qno])), choices);
                    RefreshAdapter();
                    questionsList.get(Qno).hasbeenInserted = true;
                } else {
                    quiz.updateQuestionAttempt(c, 1, quiz.quizID, Qno, Integer.parseInt(String.valueOf(Time_perQuestion[Qno])), choices);
                }
            }
            opt1Checked = 0;
            opt2Checked = 0;
            opt3Checked = 0;
        } else if (q.qType == 4 || q.qType == 5) {

            if (Answer.matches("")) {
                //Do nothing
            } else {
                String choices = Answer;
                if (questionsList.get(Qno).hasbeenInserted == false) {

                    quiz.insertQuestionAttempt(c, 1, quiz.quizID, Qno, Integer.parseInt(String.valueOf(Time_perQuestion[Qno])), choices);
                    RefreshAdapter();
                    questionsList.get(Qno).hasbeenInserted = true;
                } else {
                    quiz.updateQuestionAttempt(c, 1, quiz.quizID, Qno, Integer.parseInt(String.valueOf(Time_perQuestion[Qno])), choices);
                }
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_attempt_quiz, menu);
        MenuItem quizTimeLeft = menu.findItem(R.id.quiz_time_left);
        quizTimeLeft.setVisible(false);

        MenuItem item2 = menu.findItem(R.id.unatttemped);
        item2.setVisible(false);

        /////////////////////////////////////////////////
        MenuItem item = menu.findItem(R.id.spinner);
        item.setVisible(false);
        spinner = (Spinner) item.getActionView();

        ArrayList<Integer> Attempted_Questions = quiz.getQuestionAttemptList(getApplicationContext(), 1, quiz.quizID);

        ArrayList<String> items= new ArrayList<>();
        int i=0;
        int j=0;
        for(j=0; j<questionsList.size();j++) {
            Boolean found = false;
            for (i = 0; i < Attempted_Questions.size(); i++) {
                if (Attempted_Questions.get(i)==(j+1) )
                {
                    found=true;
                }
            }
            if(found==false)
            {
                items.add(String.valueOf(j+1));
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(nullifyFIrstSelction!=0) {
                    int GotoQuestion = Integer.parseInt(spinner.getSelectedItem().toString());
                    FromSpinner = true;
                    FromSpinnerQ = GotoQuestion;
                    StudentAttemptGotToFirstQues(v);
                }
                nullifyFIrstSelction++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        /////////////////////////////////////////
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    public void RefreshAdapter()
    {
        ArrayList<Integer> Attempted_Questions = quiz.getQuestionAttemptList(getApplicationContext(), 1, quiz.quizID);

        ArrayList<String> items= new ArrayList<>();
        int i=0;
        int j=0;
        for(j=0; j<questionsList.size();j++) {
            Boolean found = false;
            for (i = 0; i < Attempted_Questions.size(); i++) {
                if (Attempted_Questions.get(i)==(j+1) )
                {
                    found=true;
                }
            }
            if(found==false)
            {
                items.add(String.valueOf(j+1));
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(nullifyFIrstSelction!=0) {
                    int GotoQuestion = Integer.parseInt(spinner.getSelectedItem().toString());
                    FromSpinner = true;
                    FromSpinnerQ = GotoQuestion;
                    StudentAttemptGotToFirstQues(v);
                }
                nullifyFIrstSelction++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.quiz_time_left) {
            // do nothing
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // Not calling super, disables back button in current screen.
    }

    public void StartQuizButton(View view) {
        ShowMenuItems();
        MenuItem timeLeftItem = menu.findItem(R.id.quiz_time_left);
        String hmsTimeLeft = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeLeft), TimeUnit.MILLISECONDS.toMinutes(timeLeft) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeLeft)), TimeUnit.MILLISECONDS.toSeconds(timeLeft) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeft)));
        timeLeftItem.setTitle("Time Left: " + hmsTimeLeft);
        timeLeftTimer.start();
        StudentAttemptNextQuestion(view);
    }

    public void QuizSubmitButton(View view) {
        timeLeftTimer.cancel();
        Quiz.submitQuiz(this, 1, quiz.quizID);
        Intent intent = new Intent(this, StudentQuizListActivity.class);
        startActivity(intent);
    }


    public void CHeckBox1Clicked(View view) {

        CheckBox c= findViewById(R.id.MCQ_mul_checkBox);
        if(c.isChecked())
        {
            opt1Checked=1;
        }
        else{
            opt1Checked=0;
        }
        CheckBox c2= findViewById(R.id.MCQ_mul_checkBox2);
        if(c2.isChecked())
        {
            opt2Checked=1;
        }
        else{
            opt2Checked=0;
        }

        CheckBox c3= findViewById(R.id.MCQ_mul_checkBox3);
        if(c3.isChecked())
        {
            opt3Checked=1;
        }
        else{
            opt3Checked=0;
        }

    }


    public void MCQSingleAnsStore(View view) {

        RadioButton b= findViewById(R.id.MCQ_single_sound);
        if(b.isChecked())
        {
            opt1Checked=1;
        }
        else{
            opt1Checked=0;
        }
        RadioButton b1= findViewById(R.id.MCQ_single_vibration);
        if(b1.isChecked())
        {
            opt2Checked=1;
        }
        else{
            opt2Checked=0;
        }

        RadioButton b2= findViewById(R.id.MCQ_single_silent);
        if(b2.isChecked())
        {
            opt3Checked=1;
        }
        else{
            opt3Checked=0;
        }

    }

    public void TFMCQSore(View view) {
        RadioButton b= findViewById(R.id.mcq_tf_sound);
        if(b.isChecked())
        {
            opt1Checked=1;
        }
        else{
            opt1Checked=0;
        }
        RadioButton b1= findViewById(R.id.mcq_tf_vibration);
        if(b1.isChecked())
        {
            opt2Checked=1;
        }
        else{
            opt2Checked=0;
        }
    }

    public void saveAnswerObjective()
    {
        EditText ow= findViewById(R.id.sub_ow_editTextTextPersonName);
        if(ow.getText().toString().matches(""))
        {
        }

        ow.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {
                String text = mEdit.toString();
                Answer= text;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){
            }
        });
    }
    public void saveAnswerSubjective()
    {
        EditText sub= findViewById(R.id.editTextTextPersonName);
        if(sub.getText().toString().matches(""))
        {
        }

        sub.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {
                String text = mEdit.toString();
                Answer2= text;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){
            }
        });
    }
    public Boolean  CHeckifAttempted(int Qestuonno)
    {
        Intent intent = getIntent();
        Quiz quiz = (Quiz) intent.getSerializableExtra("quizViewIntent");
        ArrayList<Integer> AttemptedQuestions=quiz.getQuestionAttemptList(getApplicationContext(),1,quiz.quizID);

        int j=0;
        for(j=0; j<AttemptedQuestions.size();j++)
        {
            if(Qestuonno== (AttemptedQuestions.get(j)))
            {
                return true;
            }
        }
        return false;
    }

    public Boolean  CHeckifAttempted1(int Qestuonno)
    {
        Intent intent = getIntent();
        Quiz quiz = (Quiz) intent.getSerializableExtra("quizViewIntent");
        ArrayList<Integer> AttemptedQuestions=quiz.getQuestionAttemptList(getApplicationContext(),1,quiz.quizID);
        AttemptedQuestions.add(4);
        AttemptedQuestions.add(5);
        int j=0;
        for(j=0; j<AttemptedQuestions.size();j++)
        {
            if(Qestuonno== (AttemptedQuestions.get(j)))
            {
                return true;
            }
        }
        return false;
    }

    public void BackFromSubmitButton(View view) {
        ShowMenuItems();
        StudentAttemptPrevQuestion(view);
    }

    public void HideMenuItems(){
        MenuItem timeLeftItem = menu.findItem(R.id.quiz_time_left);
        timeLeftItem.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.unatttemped);
        item2.setVisible(false);
        MenuItem item = menu.findItem(R.id.spinner);
        item.setVisible(false);
    }

    public void ShowMenuItems(){
        MenuItem timeLeftItem = menu.findItem(R.id.quiz_time_left);
        timeLeftItem.setVisible(true);
        MenuItem item2 = menu.findItem(R.id.unatttemped);
        item2.setVisible(true);
        MenuItem item = menu.findItem(R.id.spinner);
        item.setVisible(true);
    }
}
