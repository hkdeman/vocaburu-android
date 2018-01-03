package com.welcomebuddy.randomtech.vocabsensei;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button easyButton = findViewById(R.id.action_button_easy);
        final Button mediumButton = findViewById(R.id.action_button_medium);
        final Button difficultButton = findViewById(R.id.action_button_difficult);
        final Button randomButton = findViewById(R.id.action_button_random);

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startDetailActivity = new Intent(view.getContext(),DetailActivity.class);
                startDetailActivity.putExtra("detail","easy");
                startActivity(startDetailActivity);
            }
        });

        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startDetailActivity = new Intent(view.getContext(),DetailActivity.class);
                startDetailActivity.putExtra("detail","medium");
                startActivity(startDetailActivity);
            }
        });

        difficultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startDetailActivity = new Intent(view.getContext(),DetailActivity.class);
                startDetailActivity.putExtra("detail","difficult");
                startActivity(startDetailActivity);
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startDetailActivity = new Intent(view.getContext(),DetailActivity.class);
                startDetailActivity.putExtra("detail","random");
                startActivity(startDetailActivity);
            }
        });


    }

}
