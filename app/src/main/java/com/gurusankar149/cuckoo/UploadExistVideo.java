package com.gurusankar149.cuckoo;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import static com.gurusankar149.cuckoo.MainActivity.EXTRA_VIDEO_PATH;


public class UploadExistVideo extends AppCompatActivity {
    private static final int PICk_VIDEO = 1;
    VideoView videoView;
    Button pick_existVideo, next;
    ImageView ic_play;
    Uri videoUri;
    CardView trim_video;

    private Dialog progess_dailog;
    private TextView dailog_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_exist_video);
        videoView = findViewById(R.id.video_view_localac);
        ic_play = findViewById(R.id.ic_play_uplac);
        trim_video = findViewById(R.id.ic_trim_video_UEAc);
        trim_video.setVisibility(View.INVISIBLE);
        //set progress dialoge
        progess_dailog = new Dialog(getApplicationContext());
        progess_dailog.setCancelable(false);
        progess_dailog.setContentView(R.layout.progress_dailog);
        progess_dailog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dailog_text = progess_dailog.findViewById(R.id.progess_text);
        dailog_text.setText("Loading videos Please wait...");


        pick_existVideo = findViewById(R.id.pick_exist_video_uplac);
        next = findViewById(R.id.next_exist_video_uplac);
        next.setVisibility(View.INVISIBLE);
        ic_play.setVisibility(View.INVISIBLE);
        Intent intent=getIntent();
        String videouriString=intent.getStringExtra(EXTRA_VIDEO_PATH);
         videoUri=Uri.parse(videouriString);

        pick_existVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progess_dailog.show();

                        Intent intent =new Intent(getApplicationContext(),LoadAllExistingVideos.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
            }
        });
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    ic_play.setVisibility(View.VISIBLE);
                } else {
                    videoView.start();
                    ic_play.setVisibility(View.INVISIBLE);

                }
            }
        });
        // moving all to on creat activity
        // start from here
        videoView.setVideoURI(videoUri);
        videoView.start();
        ic_play.setVisibility(View.INVISIBLE);
        next.setVisibility(View.VISIBLE);
        trim_video.setVisibility(View.VISIBLE);
        MediaPlayer mediaPlayer = MediaPlayer.create(this, videoUri);
        int duration = mediaPlayer.getDuration();
        Log.d("Duration", String.valueOf(duration));

        // tyrim activity for video belo  1min
        trim_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Tag","Trime clicked clicked");
                Intent intent = new Intent(UploadExistVideo.this, TrimExtraActivity.class);
                intent.putExtra(EXTRA_VIDEO_PATH, videouriString);
               overridePendingTransition(0,0);
                startActivity(intent);
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (duration > 60000) {
                    Log.d("Tag", "next clicked");
                    Intent intent = new Intent(getApplicationContext(), TrimExtraActivity.class);
                    intent.putExtra(EXTRA_VIDEO_PATH, videoUri.toString());
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    finish();
//                startActivity(new Intent(getApplicationContext(), TrimActivity.class));
                }
                else {
                    Log.d("Tag", "next clicked");
                    Intent intent = new Intent(getApplicationContext(), UploadToFireStore.class);
                    intent.putExtra(EXTRA_VIDEO_PATH, videoUri.toString());
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICk_VIDEO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri videoUri = data.getData();



        }
    }*/
}