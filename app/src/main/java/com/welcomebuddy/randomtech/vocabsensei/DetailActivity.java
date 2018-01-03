package com.welcomebuddy.randomtech.vocabsensei;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DetailActivity extends AppCompatActivity {

    private String detail;
    private JSONArray vocabulary;
    private Map<String,JSONArray> word_list = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getData();
        Intent intentFromMainActivity = getIntent();
        if(intentFromMainActivity.hasExtra("detail")) {
            if(intentFromMainActivity.getStringExtra("detail")!=null) {
                detail = intentFromMainActivity.getExtras().getString("detail");
                if(detail.equals("easy")) {
                    setupEasy();
                } else if(detail.equals("medium")) {
                    setupMedium();
                } else if(detail.equals("difficult")) {
                    setupDifficult();
                } else if(detail.equals("random")) {
                    setupRandom();
                }
            }
        }
    }

    protected void getData() {
        String json = null;
        try {
            InputStream is = getAssets().open("vocabulary.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            vocabulary = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
            vocabulary = null;
        }

        if(vocabulary!=null) {
            for (int i = 0; i <=220; i+=110) {
                JSONArray chunksOfEasyWords = new JSONArray();
                int n_words = i+110;
                for(int j=i;j<=n_words;j++) {
                    try {
                        JSONObject word = (JSONObject) vocabulary.get(j);
                        chunksOfEasyWords.put(word);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(n_words==110) {
                    word_list.put("easy",chunksOfEasyWords);
                } else if (n_words==220) {
                    word_list.put("medium",chunksOfEasyWords);
                } else if (n_words==330) {
                    word_list.put("difficult",chunksOfEasyWords);
                }
            }
        }

    }

    protected void setupPage(final String detail, final JSONArray words) {
        int n_buttons = words.length()/11;
        ScrollView scrollView = findViewById(R.id.scrollview_words);
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        llParams.gravity = Gravity.CENTER;

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(llParams);

        for(int i =1;i<n_buttons+1;i++) {
            Button button = new Button(this);
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedbutton));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(60,40,60,40);
            button.setLayoutParams(layoutParams);
            button.setTextColor(getResources().getColor(R.color.white));
            button.setText(detail+" "+i);
            final int position = i;
            final int randomGuess = generateRandom(1,11,position);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent startDetailActivity = new Intent(view.getContext(),QuizActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("detail", detail);
                    JSONArray short_list = new JSONArray();
                    for(int j =(position-1)*11;j<=position*11;j++) {
                        try {
                            short_list.put(words.getJSONObject(j));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    JSONArray guess_list = new JSONArray();
                    for(int j =(randomGuess-1)*11;j<=randomGuess*11;j++) {
                        try {
                            guess_list.put(words.getJSONObject(j));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    extras.putString("words",short_list.toString());
                    extras.putSerializable("guesses",guess_list.toString());
                    startDetailActivity.putExtras(extras);
                    startActivity(startDetailActivity);
                }
            });
            linearLayout.addView(button);
        }
        scrollView.addView(linearLayout);
    }

    protected void setupEasy() {
//        TextView textView = findViewById(R.id.textview_words);
//        textView.setText(word_list.get("easy").toString());
        setupPage("Easy", word_list.get("easy"));
    }

    protected void setupMedium() {
//        TextView textView = findViewById(R.id.textview_words);
//        textView.setText(word_list.get("medium").toString());
        setupPage("Medium", word_list.get("medium"));
    }

    protected void setupDifficult() {
//        TextView textView = findViewById(R.id.textview_words);
//        textView.setText(word_list.get("difficult").toString());
        setupPage("Difficult", word_list.get("difficult"));
    }

    protected void setupRandom() {
//        TextView textView = findViewById(R.id.textview_words);
//        textView.setText(word_list.get("easy").toString());
//        setupPage("Easy", word_list.get("easy"));
    }


    public int generateRandom(int start, int end, int excludeValue) {
        Random rand = new Random();
        int range = end - start + 1;
        int random = 0;

        boolean success = false;
        while(!success) {
            random = rand.nextInt(range) + 1;
                if(excludeValue != random) {
                    break;
                }
            }
        return random;
    }


}
