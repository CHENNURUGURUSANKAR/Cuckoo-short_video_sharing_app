package com.gurusankar149.cuckoo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class UploadActivity extends AppCompatActivity {
    static final int REQUEST_VIDEO_CAPTURE = 1;
    VideoView videoView;
    ProgressBar mProgressBar;
    CountDownTimer mCountDownTimer;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        dispatchTakeVideoIntent();

    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.kathari);


            new Thread() {

                @Override
                public void run() {
                    try {
                        sleep(mediaPlayer.getDuration());
                        mediaPlayer.start();
                        startActivityForResult(takeVideoIntent, RESULT_OK);
                        Log.d("TAG", "duration" + String.valueOf(mediaPlayer.getDuration()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.d("TAG", "catch working");
                    }
                    Log.d("TAG", "run working");
                }
            }.start();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            videoView = findViewById(R.id.video_view_upload_activity);
            videoView.setVideoURI(videoUri);
            double count = videoView.getDuration();
            mProgressBar = (ProgressBar) findViewById(R.id.progressbar_upac);
            mProgressBar.setProgress(i);
            Log.d("tag", "video duration" + String.valueOf(count));
            mCountDownTimer = new CountDownTimer((long) count, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    Log.d("Log_tag", "Tick of Progress" + i + millisUntilFinished);
                    Log.d("tag", "video duration" + String.valueOf(count));
                    i++;
                    mProgressBar.setProgress((int) ((int) i * 100 / (count / 1000)));

                }

                @Override
                public void onFinish() {
                    //Do what you want
                    i++;
                    mProgressBar.setProgress(100);
                }
            };
            mCountDownTimer.start();
            videoView.start();

        }
    }

}