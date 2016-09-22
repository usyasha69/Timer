package com.example.pk.timer;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * Keys for saved instance state.
     */
    private static final String SECONDS_KEY = "seconds";
    private static final String RUNNING_KEY = "running";
    private static final String WAS_RUNNING_KEY = "was running";

    private int seconds;
    private boolean running;
    private boolean wasRunning;

    private EditText editText;
    private TextView textView;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.secondsEditText);
        textView = (TextView) findViewById(R.id.secondsTextView);

        final Button startButton = (Button) findViewById(R.id.startButton);
        final Button stopButton = (Button) findViewById(R.id.stopButton);
        final Button resetButton = (Button) findViewById(R.id.resetButton);

        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

        handler = new Handler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SECONDS_KEY, seconds);
        outState.putBoolean(RUNNING_KEY, running);
        outState.putBoolean(WAS_RUNNING_KEY, wasRunning);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        seconds = savedInstanceState.getInt(SECONDS_KEY);
        running = savedInstanceState.getBoolean(RUNNING_KEY);
        wasRunning = savedInstanceState.getBoolean(WAS_RUNNING_KEY);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startButton:
                if (!editText.getText().toString().isEmpty()) {
                    running = true;
                    seconds = Integer.parseInt(editText.getText().toString());

                    editText.setVisibility(View.INVISIBLE);

                    runTimer();
                } else {
                    Toast.makeText(this, "Please, enter the seconds of timer!"
                            , Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.stopButton:
                editText.setVisibility(View.VISIBLE);

                running = false;

                handler.removeMessages(0);
                break;
            case R.id.resetButton:
                editText.setVisibility(View.VISIBLE);

                textView.setText("");

                seconds = 0;
                running = false;

                handler.removeMessages(0);
        }
    }

    /**
     * This method start the timer with help of handler.
     */
    private void runTimer() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(String.valueOf(seconds));

                if (seconds == 0) {
                    endsTimer();
                    editText.setVisibility(View.VISIBLE);
                    return;
                }

                if (running) {
                    seconds--;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void endsTimer() {
        Toast.makeText(this, "Time!", Toast.LENGTH_SHORT).show();
    }
}