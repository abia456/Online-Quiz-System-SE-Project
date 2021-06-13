package com.se.onlinequizsystem;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//class AttemptRow {
//    String course;
//    String totaltime;
//    String status;
//    String marks;
//    String remaining_time;
//    String ButtonTxt;
//
//
//    public AttemptRow(String x, String y, String z, String a, String b) {
//
//        course = x;
//        totaltime = y;
//        status = z;
//        marks = a;
//        remaining_time = b;
//    }
//}

public class StudentQuizListAdapter extends RecyclerView.Adapter<StudentQuizListAdapter.QuizViewHolder> {
    //    protected ArrayList<AttemptRow> quizList;
    private static final String TAG = "=== StudentQuizListAdapter ===";
    protected ArrayList<Quiz> quizList;
    Context context;
    private QuizViewHolder holder;
    private int position;

    public StudentQuizListAdapter(Context context) {
//        this.quizList = new ArrayList<AttemptRow>();
        this.quizList = Quiz.getAllQuiz(context);
        this.context = context;

//        Resources res = c.getResources();
//        String[] coursess = res.getStringArray(R.array.subjects);
//        String[] totaltimes = res.getStringArray(R.array.durations);
//        String[] statuss = res.getStringArray(R.array.Status);
//        String[] markss = res.getStringArray(R.array.marks);
//        String[] Remaining_time = res.getStringArray(R.array.timeleft);
//
//        for (int i = 0; i < coursess.length; i++) {
//            quizList.add(new AttemptRow(coursess[i], totaltimes[i], statuss[i], markss[i], Remaining_time[i]));
//        }
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.student_quiz_list_item, parent, false);
        return new QuizViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder quizViewHolder, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm a");

        quizViewHolder.quizName.setText(quizList.get(position).quizName);
        quizViewHolder.openTime.setText(String.valueOf(sdf.format(
                new Date((quizList.get(position).openTime) * 1000L))));
        quizViewHolder.closeTime.setText(String.valueOf(sdf.format(
                new Date((quizList.get(position).closeTime) * 1000L))));
        quizViewHolder.marks.setText(String.valueOf(quizList.get(position).totalMarks));
        quizViewHolder.duration.setText(String.valueOf(quizList.get(position).totalTime -
                Quiz.getQuizAttemptTime(context, 1, quizList.get(position).quizID)));

        // Quiz status conditions
        boolean submitted = Quiz.checkQuizSubmitted(
                context, 1, quizList.get(position).quizID);

        Calendar currentCalender = Calendar.getInstance();
        long currentTime = currentCalender.getTimeInMillis();
        if (submitted) {
            quizViewHolder.status.setText("Submitted");
        }
        if (currentTime < (quizList.get(position).openTime) * 1000L) {
            quizViewHolder.status.setText("Not Started");
        }
        if (!submitted && (currentTime >= (quizList.get(position).openTime) * 1000L)) {
            quizViewHolder.status.setText("Open");
        }
        if (!submitted && currentTime >= (quizList.get(position).closeTime) * 1000L) {
            quizViewHolder.status.setText("Missed");
        }
        if (quizViewHolder.status.getText() != "Open") {
            quizViewHolder.attemptQuizButton.setEnabled(false);
        }
        quizViewHolder.attemptQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StudentAttemptQuizActivity.class);
                intent.putExtra("quizViewIntent", quizList.get(position));
                context.startActivity(intent);
            }
        });
        if ((!submitted || (currentTime < (quizList.get(position).closeTime) * 1000L))) {
            quizViewHolder.viewQuizButton.setEnabled(false);
        }
        quizViewHolder.viewQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StudentViewQuizActivity.class);
                intent.putExtra("quizViewIntent", quizList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView quizName;
        TextView openTime;
        TextView closeTime;
        TextView status;
        TextView marks;
        TextView duration;
        Button attemptQuizButton;
        Button viewQuizButton;


        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quizName = itemView.findViewById(R.id.subject);
            openTime = itemView.findViewById(R.id.open_time);
            closeTime = itemView.findViewById(R.id.close_time);
            status = itemView.findViewById(R.id.status);
            marks = itemView.findViewById(R.id.marks);
            duration = itemView.findViewById(R.id.duration);
            attemptQuizButton = itemView.findViewById(R.id.student_attempt_quiz_button);
            viewQuizButton = itemView.findViewById(R.id.student_view_quiz_button);
        }
    }
}

