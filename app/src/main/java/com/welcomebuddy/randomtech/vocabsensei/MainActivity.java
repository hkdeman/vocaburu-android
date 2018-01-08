package com.welcomebuddy.randomtech.vocabsensei;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.welcomebuddy.randomtech.vocabsensei.Database.QuizContract;
import com.welcomebuddy.randomtech.vocabsensei.Database.QuizDbHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Cursor data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final Button randomButton = findViewById(R.id.action_button_random);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("db_setup",false)) {
            getScores();
        } else {
            setupDB(prefs);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getScores();
    }

    protected void getScores() {

        TextView easyScore = findViewById(R.id.dashboard_score_easy);
        TextView mediumScore = findViewById(R.id.dashboard_score_medium);
        TextView difficultScore = findViewById(R.id.dashboard_score_difficult);

        double easyAverageScore = getAverageScore("easy");
        double mediumAverageScore = getAverageScore("medium");
        double difficultAverageScore = getAverageScore("difficult");

        final Button easyButton = findViewById(R.id.action_button_easy);
        final Button mediumButton = findViewById(R.id.action_button_medium);
        final Button difficultButton = findViewById(R.id.action_button_difficult);

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startDetailActivity = new Intent(view.getContext(),DetailActivity.class);
                startDetailActivity.putExtra("detail","easy");
                startActivity(startDetailActivity);
            }
        });

        if(easyAverageScore>10) {
            mediumButton.setClickable(true);
            mediumButton.setText("MEDIUM");
            mediumButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent startDetailActivity = new Intent(view.getContext(),DetailActivity.class);
                    startDetailActivity.putExtra("detail","medium");
                    startActivity(startDetailActivity);
                }
            });
        } else {
            mediumButton.setClickable(false);
            mediumButton.setText("MEDIUM (Locked)");
        }

        if (mediumAverageScore>10) {
            difficultButton.setClickable(true);
            difficultButton.setText("DIFFICULT");
                difficultButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent startDetailActivity = new Intent(view.getContext(),DetailActivity.class);
                        startDetailActivity.putExtra("detail","difficult");
                        startActivity(startDetailActivity);
                    }
                });

        } else {
            difficultButton.setClickable(false);
            difficultButton.setText("DIFFICULT (Locked)");
        }

        easyScore.setText("Score : "+String.valueOf(easyAverageScore));
        mediumScore.setText("Score : "+String.valueOf(mediumAverageScore));
        difficultScore.setText("Score : "+String.valueOf(difficultAverageScore));

    }

    protected double getAverageScore(String detail) {
        QuizDbHelper dbHelper = new QuizDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                QuizContract.QuizEntry._ID,
                QuizContract.QuizEntry.COLUMN_KEY,
                QuizContract.QuizEntry.COLUMN_DETAIL,
                QuizContract.QuizEntry.COLUMN_SCORE
        };

        String selection = QuizContract.QuizEntry.COLUMN_DETAIL + " = ?";
        String[] selectionArgs = { detail };

        Cursor data = db.query(
                QuizContract.QuizEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                        // The sort order
        );

       List<Integer> scores = new ArrayList<>();

        while(data.moveToNext()) {
            int itemId = data.getInt(
                    data.getColumnIndexOrThrow(QuizContract.QuizEntry.COLUMN_SCORE));
            scores.add(itemId);
        }
        data.close();

        return calculateAverage(scores);

    }


    private double calculateAverage(List <Integer> scores) {
        Integer sum = 0;
        if(!scores.isEmpty()) {
            for (Integer mark : scores) {
                sum += mark;
            }
            return sum.doubleValue() / scores.size();
        }
        return sum;
    }


    protected void setupDB(SharedPreferences prefs) {
        QuizDbHelper mDbHelper = new QuizDbHelper(this);
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        List<String> details = new ArrayList<>();
        details.add("easy");
        details.add("medium");
        details.add("difficult");
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        for(int i=0;i<3;i++) {
            for(int j=1;j<=10;j++) {
                values.put(QuizContract.QuizEntry.COLUMN_KEY, j);
                values.put(QuizContract.QuizEntry.COLUMN_DETAIL, details.get(i));
                values.put(QuizContract.QuizEntry.COLUMN_SCORE,0);
                db.insert(QuizContract.QuizEntry.TABLE_NAME, null, values);
            }
        }

        // Insert the new row, returning the primary key value of the new row
        prefs.edit().putBoolean("db_setup",true).apply();
        getScores();
    }


}
