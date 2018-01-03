package com.welcomebuddy.randomtech.vocabsensei;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Intent intentFromDetailActivity = getIntent();
        if(intentFromDetailActivity.getExtras()!=null) {
            String detail = intentFromDetailActivity.getExtras().getString("detail");
            int position = Integer.parseInt(intentFromDetailActivity.getExtras().getString("position"));
            try {
                JSONArray words = new JSONArray(intentFromDetailActivity.getExtras().getString("words"));
                layoutTheQuiz(detail,position,words);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void layoutTheQuiz(String detail,int position, JSONArray words){
        TextView textView = findViewById(R.id.hello);
        textView.setText(detail + Integer.toString(position) + words.toString());
    }
}
