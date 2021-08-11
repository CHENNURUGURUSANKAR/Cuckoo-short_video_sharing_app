package com.gurusankar149.cuckoo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UploadVideoFrag extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentusr = mAuth.getCurrentUser();
    //    String uid=currentusr.getUid();

    private Dialog progess_dailog;
    private TextView dailog_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_video, container, false);
        progess_dailog = new Dialog(getContext());
        progess_dailog.setCancelable(false);
        progess_dailog.setContentView(R.layout.progress_dailog);
        progess_dailog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dailog_text = progess_dailog.findViewById(R.id.progess_text);
        dailog_text.setText("Loading videos Please wait");
        progess_dailog.show();
        checkAuthentication();

        return view;
    }

    private void checkAuthentication() {
        if (currentusr != null) {
            startActivity(new Intent(getContext(), LoadAllExistingVideos.class));
            getActivity().finish();

        } else {
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }
    }

}