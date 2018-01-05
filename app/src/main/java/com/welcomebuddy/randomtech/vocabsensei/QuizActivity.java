package com.welcomebuddy.randomtech.vocabsensei;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.welcomebuddy.randomtech.vocabsensei.Database.QuizContract;
import com.welcomebuddy.randomtech.vocabsensei.Database.QuizDbHelper;

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
    private static int position;

    Dialog hintPopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QUIZ_INCREMENT=0;
        SCORE=0;
        setContentView(R.layout.activity_quiz);
        Intent intentFromDetailActivity = getIntent();
        if(intentFromDetailActivity.getExtras()!=null) {
            detail = intentFromDetailActivity.getExtras().getString("detail");
            position = Integer.parseInt(intentFromDetailActivity.getExtras().getString("position"));
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
        hintPopUp = new Dialog(this);
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
        } else {
            Toast.makeText(this, "Quiz up, try the next one!!",
                    Toast.LENGTH_SHORT).show();

            QuizDbHelper mDbHelper = new QuizDbHelper(this);

            // Gets the data repository in write mode
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(QuizContract.QuizEntry.COLUMN_SCORE,SCORE);

            String selector = QuizContract.QuizEntry.COLUMN_KEY + " = ? AND " + QuizContract.QuizEntry.COLUMN_DETAIL + " = ?";
            String[] selectOptions = {Integer.toString(position),detail.toLowerCase()};

            db.update(QuizContract.QuizEntry.TABLE_NAME,values,selector,selectOptions);
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
cd
    protected void fourthChoice(View v) {
        TextView textView = findViewById(v.getId());
        checkAnswer(textView.getText().toString());
    }

    protected void getHint(View v) {
        hintPopUp.setContentView(R.layout.hint_pop);
        TextView hintPopUpText = hintPopUp.findViewById(R.id.quiz_hint_text);
        Button cancelButton = hintPopUp.findViewById(R.id.cancel_button);

        try {
            hintPopUpText.setText(word_list.getJSONObject(QUIZ_INCREMENT).getString("sentence"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hintPopUp.dismiss();
            }
        });

        hintPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        hintPopUp.show();
    }


}
