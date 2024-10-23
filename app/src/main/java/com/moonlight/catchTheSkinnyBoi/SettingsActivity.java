package com.moonlight.catchTheSkinnyBoi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    SeekBar timeControl;
    SeekBar speedControl;
    SharedPreferences sharedPreferences;
    TextView timeText;
    TextView speedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Menüye Dön");
        }

        timeControl = findViewById(R.id.timeSeekBar);
        speedControl = findViewById(R.id.speedSeekBar);
        timeText = findViewById(R.id.timeText);
        speedText = findViewById(R.id.speedText);
        sharedPreferences = getSharedPreferences("com.example.catchthekenny", MODE_PRIVATE);
        int savedTime = sharedPreferences.getInt("gameDuration", 10);
        timeControl.setProgress(savedTime - 10);
        timeText.setText("Game Duration: " + savedTime + " seconds");

        timeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int selectedTime = progress + 10;
                timeText.setText("Game Duration: " + selectedTime + " seconds");
                sharedPreferences.edit().putInt("gameDuration", selectedTime).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        int savedSpeed = sharedPreferences.getInt("imageSpeed", 1000);
        speedControl.setProgress((savedSpeed / 100) - 1);
        speedText.setText("Image Speed: " + savedSpeed + " ms");

        speedControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int selectedSpeed = (progress + 1) * 100;
                speedText.setText("Image Speed: " + selectedSpeed + " ms");
                sharedPreferences.edit().putInt("imageSpeed", selectedSpeed).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(SettingsActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
