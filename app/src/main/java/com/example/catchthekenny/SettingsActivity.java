package com.example.catchthekenny;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    SeekBar volumeControl;
    MediaPlayer mediaPlayer;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Toolbar'ı ayarla
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Geri butonunu göster
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back); // Geri butonu için simge (drawable altında ic_back simgesi olmalı)
        }

        volumeControl = findViewById(R.id.volumeSeekBar);
        sharedPreferences = getSharedPreferences("com.example.catchthekenny", MODE_PRIVATE);

        // Medya oynatıcıyı al
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);

        // Önceden ayarlanmış ses seviyesini yükle
        int savedVolume = sharedPreferences.getInt("volume", 50);
        volumeControl.setProgress(savedVolume);
        mediaPlayer.setVolume(savedVolume / 100f, savedVolume / 100f);

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.setVolume(progress / 100f, progress / 100f);
                // Ses seviyesini kaydet
                sharedPreferences.edit().putInt("volume", progress).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Geri butonuna basıldığında menüye dön
            Intent intent = new Intent(SettingsActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
