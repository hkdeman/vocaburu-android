package com.welcomebuddy.randomtech.vocabsensei;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    private static int QUIZ_INCREMENT=0;
    private static JSONArray words;
    private static JSONArray guesses;
    private static String detail;
    private static JSONArray word_list;
    private static int SCORE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QUIZ_INCREMENT=0;
        SCORE=0;
        setContentView(R.layout.activity_quiz);
        Intent intentFromDetailActivity = getIntent();
        if(intentFromDetailActivity.getExtras()!=null) {
            detail = intentFromDetailActivity.getExtras().getString("detail");
            try {
                words = new JSONArray(intentFromDetailActivity.getExtras().getString("words"));
                guesses = new JSONArray(intentFromDetailActivity.getExtras().getString("guesses"));
                word_list = getWords(words);
                layoutTheQuiz();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        TextView score = findViewById(R.id.textview_score);
        score.setText("Score: "+SCORE);
    }

    protected void layoutTheQuiz(){
        TextView status = findViewById(R.id.textview_status);
        TextView chosenWord = findViewById(R.id.textview_word);
        TextView firstChoice = findViewById(R.id.textview_first_choice);
        TextView secondChoice = findViewById(R.id.textview_second_choice);
        TextView thirdChoice = findViewById(R.id.textview_third_choice);
        TextView fourthChoice = findViewById(R.id.textview_fourth_choice);
        status.setText(QUIZ_INCREMENT+1+"/11");
        try {
            JSONArray guess_list = getGuesses(guesses);
            guess_list.put(word_list.getJSONObject(QUIZ_INCREMENT));
            JSONArray choices = shuffle(guess_list);
//            chosenWord.setText(guesses.toString());
            chosenWord.setText(word_list.getJSONObject(QUIZ_INCREMENT).getString("word"));
            firstChoice.setText(choices.getJSONObject(0).getString("definition"));
            secondChoice.setText(choices.getJSONObject(1).getString("definition"));
            thirdChoice.setText(choices.getJSONObject(2).getString("definition"));
            fourthChoice.setText(choices.getJSONObject(3).getString("definition"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        textView.setText(detail + Integer.toString(position) + words.toString());
    }

    protected JSONArray getWords(JSONArray words) {
        List<Integer> numbers = new ArrayList<>();
        for(int i=0;i<11;i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        JSONArray word_choices = new JSONArray();
        for(int i =0;i<11;i++) {
            try {
                JSONObject word = words.getJSONObject(numbers.get(i));
                word_choices.put(word);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return word_choices;
    }


    protected JSONArray getGuesses(JSONArray guesses) {
        List<Integer> numbers = new ArrayList<>();
        for(int i=0;i<11;i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        JSONArray guess_choices = new JSONArray();
        for(int i =0;i<3;i++) {
            try {
                JSONObject guess = guesses.getJSONObject(numbers.get(i));
                guess_choices.put(guess);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return guess_choices;
    }

    protected JSONArray shuffle(JSONArray array) {
        List<Integer> numbers = new ArrayList<>();
        for(int i=0;i<4;i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        JSONArray shuffled_array = new JSONArray();
        for(int i=0;i<4;i++) {
            try {
                shuffled_array.put(array.getJSONObject(numbers.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return shuffled_array;
    }

    protected void checkAnswer(String answer) {
        if(QUIZ_INCREMENT<10) {
            try {
                if (answer.equals(word_list.getJSONObject(QUIZ_INCREMENT).getString("definition"))) {
                    SCORE++;
                    TextView score = findViewById(R.id.textview_score);
                    score.setText("Score: " + SCORE);
                    Toast.makeText(this, "Correct Answer!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Wrong Answer!",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            QUIZ_INCREMENT++;
            layoutTheQuiz();
        }
    }

    protected void firstChoice(View v) {
        TextView textView = findViewById(v.getId());
        checkAnswer(textView.getText().toString());
    }

    protected void secondChoice(View v) {
        TextView textView = findViewById(v.getId());
        checkAnswer(textView.getText().toString());
    }

    protected void thirdChoice(View v) {
        TextView textView = findViewById(v.getId());
        checkAnswer(textView.getText().toString());
    }

    protected void fourthChoice(View v) {
        TextView textView = findViewById(v.getId());
        checkAnswer(textView.getText().toString());
    }


}
