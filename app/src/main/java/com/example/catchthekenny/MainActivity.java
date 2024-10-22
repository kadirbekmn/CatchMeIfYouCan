package com.example.catchthekenny;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    boolean isPaused = false;
    ImageView pauseIcon;
    TextView scoreText;
    TextView timeText;
    int score;
    ImageView[] imageArray;
    Handler handler;
    Runnable runnable;
    long timeLeftInMillis;
    long imageSpeed;
    CountDownTimer countDownTimer;
    SharedPreferences sharedPreferences;
    int personalBest;
    TextView personalBestText;
    Random random;
    ConstraintLayout constraintLayoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeText = findViewById(R.id.timeText);
        scoreText = findViewById(R.id.scoreText);
        pauseIcon = findViewById(R.id.pauseIcon);
        constraintLayoutMain = findViewById(R.id.constraintLayoutMain);  // Ana layoutu tanımlıyoruz
        random = new Random();

        sharedPreferences = getSharedPreferences("com.example.catchthekenny", MODE_PRIVATE);
        personalBest = sharedPreferences.getInt("personalBest", 0);
        personalBestText = findViewById(R.id.personalBestText);
        personalBestText.setText("Personal Best: " + personalBest);

        long gameDuration = sharedPreferences.getInt("gameDuration", 10) * 1000;
        imageSpeed = sharedPreferences.getInt("imageSpeed", 1000);
        timeLeftInMillis = gameDuration;
        constraintLayoutMain.setBackgroundColor(Color.BLACK);

        pauseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseGame();
            }
        });

        imageArray = new ImageView[]{findViewById(R.id.imageView1), findViewById(R.id.imageView2), findViewById(R.id.imageView3), findViewById(R.id.imageView4), findViewById(R.id.imageView5), findViewById(R.id.imageView6), findViewById(R.id.imageView7), findViewById(R.id.imageView8), findViewById(R.id.imageView9)};

        hideImages(imageSpeed);
        score = 0;

        startTimer(timeLeftInMillis);
    }

    public void startTimer(long duration) {
        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                timeText.setText("Time : " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                timeText.setText("Time Off");
                handler.removeCallbacks(runnable);
                for (ImageView image : imageArray) {
                    image.setVisibility(View.INVISIBLE);
                }

                if (score > personalBest) {
                    personalBest = score;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("personalBest", personalBest);
                    editor.apply();
                    Toast.makeText(MainActivity.this, "New Personal Best: " + personalBest, Toast.LENGTH_SHORT).show();
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Restart?");
                alert.setMessage("Are you sure to restart the game?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                alert.show();
            }
        }.start();
    }

    public void increaseScore(View view) {
        score++;
        scoreText.setText("Score : " + score);
    }

    public void decreaseScore(View view) {
        score -= 3;
        scoreText.setText("Score : " + score);
        constraintLayoutMain.setBackgroundColor(Color.RED);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                constraintLayoutMain.setBackgroundColor(Color.BLACK);
            }
        }, 500);
    }

    public void pauseGame() {
        isPaused = true;
        countDownTimer.cancel();
        handler.removeCallbacks(runnable);

        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Oyun Duraklatıldı");
        alert.setMessage("Oyuna devam etmek istiyor musunuz?");

        alert.setPositiveButton("Oyuna Devam Et", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isPaused = false;
                resumeGame();
            }
        });

        alert.setNegativeButton("Menüye Geri Dön", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alert.setCancelable(false);
        alert.show();
    }

    public void resumeGame() {
        startTimer(timeLeftInMillis);
        handler.post(runnable);
    }

    public void hideImages(long imageSpeed) {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                for (ImageView image : imageArray) {
                    image.setVisibility(View.INVISIBLE);
                }

                int i = random.nextInt(9);
                ImageView selectedImage = imageArray[i];
                selectedImage.setVisibility(View.VISIBLE);

                if (random.nextInt(5) == 0) {
                    selectedImage.setImageResource(R.drawable.bomba);
                    selectedImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            decreaseScore(v);
                        }
                    });
                } else {
                    selectedImage.setImageResource(R.drawable.adam);
                    selectedImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            increaseScore(v);
                        }
                    });
                }

                if (!isPaused) {
                    handler.postDelayed(this, imageSpeed);
                }
            }
        };
        handler.post(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}