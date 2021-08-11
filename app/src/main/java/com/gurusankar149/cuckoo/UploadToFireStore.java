package com.gurusankar149.cuckoo;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import static com.gurusankar149.cuckoo.MainActivity.EXTRA_VIDEO_PATH;

public class UploadToFireStore extends AppCompatActivity {
    VideoView videoView;
    ImageView ic_play;
    Button upload_btn;
    TextInputEditText title, description;
    String stitle, sdescription;
    private Dialog progess_dailog;
    private TextView dailog_text;
    DatabaseReference databaseReference;



    StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trim);
        Intent intent = getIntent();
        String vuripath = intent.getStringExtra(EXTRA_VIDEO_PATH);
        Uri videoUri = Uri.parse(vuripath);
        //realtime databse
        databaseReference=FirebaseDatabase.getInstance().getReference();

        videoView = findViewById(R.id.video_view_trimac);
        ic_play = findViewById(R.id.ic_play_trimac);
        ic_play.setVisibility(View.INVISIBLE);
        upload_btn = findViewById(R.id.next_trimac);
        videoView.setVideoURI(videoUri);
        videoView.start();
        progess_dailog = new Dialog(this);
        progess_dailog.setCancelable(false);
        progess_dailog.setContentView(R.layout.progress_dailog);
        progess_dailog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dailog_text = progess_dailog.findViewById(R.id.progess_text);
        dailog_text.setText("Uploading video please wait");
        File file = new File(Environment.getExternalStorageDirectory() + "Cuckoo");
       /* String destpath = file.getPath().toString();
        MediaPlayer mediaPlayer = MediaPlayer.create(this, videoUri);
        Log.d("tag", String.valueOf(mediaPlayer.getDuration()));
      */
        title = findViewById(R.id.title_trim_text);
        description = findViewById(R.id.Description_trim_text);

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
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stitle = title.getText().toString().trim();
                sdescription = description.getText().toString().trim();
                if (TextUtils.isEmpty(stitle)) {
                    title.setError("Please enter Title");
                } else if (TextUtils.isEmpty(sdescription)) {
                    description.setError("Please enter Descriptiom");
                } else {
                    progess_dailog.show();
                    uploadVideo(videoUri, stitle, sdescription);
                }
            }
        });
    }

    public String getExtension(Uri videoUri) {
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(videoUri));
    }

    public void uploadVideo(Uri videoUri, String stitle, String sdescription) {

        final StorageReference uploader = storageReference.child("Videos/" + System.currentTimeMillis() + ".mp4");
        uploader.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UploadToFireStore.this, "Video Upload", Toast.LENGTH_SHORT).show();
                uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Database.storeVideo(stitle, sdescription, uri.toString(), new MycompleteListener() {
                            @Override
                            public void OnSuccess() {
                                progess_dailog.dismiss();
                                Toast.makeText(getApplicationContext(), "Videos saved", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void OnFailure() {
                                progess_dailog.dismiss();
                                Toast.makeText(getApplicationContext(), "Videos not saved", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progess_dailog.dismiss();
                Toast.makeText(UploadToFireStore.this, "Video Upload fail", Toast.LENGTH_SHORT).show();
            }
        });
    }



}