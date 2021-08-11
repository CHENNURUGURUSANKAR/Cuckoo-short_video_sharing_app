package com.gurusankar149.cuckoo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirebaseDatabase firebaseDatabase;

    public static int likes = 0, views = 0, shares = 0, downloads = 0;

    public static DatabaseReference databaseReference;
    public static ArrayList<File> allVideoList = new ArrayList<>();
    public static File directory;
    public static String[] allpath;
    public static AllExistingVideosAdapter allExistingVideosAdapter;
    public static List<RecyclerVideoModel> recyclerVideoModelList = new ArrayList<>();

    public static void storeUserData(String email, String name, String UID, final MycompleteListener mycompleteListener) {
        Map<String, Object> user = new HashMap<>();
        user.put(" EMAIL_ID", email);
        user.put("NAME", name);
        user.put("UID", UID);
        DocumentReference userdoc = db.collection("USERS").document(UID);
        userdoc.set(user).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mycompleteListener.OnFailure();

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mycompleteListener.OnSuccess();

            }
        });

    }

    public static void storeVideo(String title, String description, String videouri, final MycompleteListener mycompleteListener) {
        Map<String, Object> videos = new HashMap<>();
        videos.put("title", title);
        videos.put("description", description);
        videos.put("videoUri", videouri);
        videos.put("likes", likes);
        videos.put("shares", shares);
        videos.put("views", views);
        videos.put("downloads", downloads);

        String docId = Database.db.collection("Videos").document().getId();
        videos.put("videoId", docId);
        Task<Void> videoref = db.collection("Videos").document(docId).set(videos).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mycompleteListener.OnSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mycompleteListener.OnFailure();
            }
        });
    }


    public static void loadAllVideos(Context context, final MycompleteListener mycompleteListener) {
        allpath = StorageUtil.getStorageDirectories(context);
        for (String path : allpath) {
            directory = new File(path);
            Method.loadDirectory(directory);
        }
        mycompleteListener.OnSuccess();
    }

    public static void loadDatabaseVideo(final MycompleteListener mycompleteListener) {
        recyclerVideoModelList.clear();
        db.collection("Videos").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                String title, description, videoId, videoUri;
                int likes, shares, downloads, coments;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                    title = doc.getString("title");
                    description = doc.getString("description");
                    videoId = doc.getString("videoId");
                    videoUri = doc.getString("videoUri");
                    likes=doc.getLong("likes").intValue();
                    downloads=doc.getLong("downloads").intValue();
                    shares=doc.getLong("shares").intValue();
                    coments=doc.getLong("views").intValue();
                    recyclerVideoModelList.add(new RecyclerVideoModel(videoId,videoUri,title,description,likes,shares,downloads,coments));
                }

                mycompleteListener.OnSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mycompleteListener.OnFailure();
            }
        });
    }
}
