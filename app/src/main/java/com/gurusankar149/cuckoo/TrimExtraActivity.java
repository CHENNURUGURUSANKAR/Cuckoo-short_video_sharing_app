package com.gurusankar149.cuckoo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import life.knowledge4.videotrimmer.K4LVideoTrimmer;
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;

import static com.gurusankar149.cuckoo.MainActivity.EXTRA_VIDEO_PATH;

public class TrimExtraActivity extends AppCompatActivity implements OnTrimVideoListener, OnK4LVideoListener {

    private K4LVideoTrimmer mVideoTrimmer;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trim_extra);

        Intent extraIntent = getIntent();
       String  path = extraIntent.getStringExtra(EXTRA_VIDEO_PATH);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Trim video");
        builder.setMessage("You can trim video below 60 seconds (1 minute)");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(getApplicationContext(),LoadAllExistingVideos.class);
                startActivity(intent);
                dialog.dismiss();
            }
        }).create().show();


        //setting progressbar
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("please wait");

        mVideoTrimmer = ((K4LVideoTrimmer) findViewById(R.id.timeLine));
        if (mVideoTrimmer != null) {
            mVideoTrimmer.setMaxDuration(60);
            mVideoTrimmer.setOnTrimVideoListener(this);
            mVideoTrimmer.setDestinationPath("/storage/emulated/0/Cuckoo/");
            mVideoTrimmer.setVideoURI(Uri.parse(path));
        }
    }

    @Override
    public void onTrimStarted() {
        mProgressDialog.show();
    }

    @Override
    public void getResult(final Uri uri) {
        mProgressDialog.cancel();
        Log.d("Tag", "OnRESULt");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TrimExtraActivity.this, String.valueOf(uri.getPath()), Toast.LENGTH_SHORT).show();
            }
        });
        /*Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setDataAndType(uri, "video/mp4");*/
        Intent intent = new Intent(getApplicationContext(), UploadToFireStore.class);
        intent.putExtra(EXTRA_VIDEO_PATH, uri.toString());
        overridePendingTransition(0,0);
        startActivity(intent);
        finish();
    }

    @Override
    public void cancelAction() {
        mProgressDialog.cancel();
        mVideoTrimmer.destroy();
        Intent intent=new Intent(getApplicationContext(),LoadAllExistingVideos.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onError(final String message) {
        mProgressDialog.cancel();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TrimExtraActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onVideoPrepared() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TrimExtraActivity.this, "onVideoPrepared", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
