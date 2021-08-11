package com.gurusankar149.cuckoo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LoadAllExistingVideos extends AppCompatActivity {
    RecyclerView recyclerView;
    public static AllExistingVideosAdapter allExistingVideosAdapter;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_all_existing_videos);

        toolbar = findViewById(R.id.toolbar_load_all);
        toolbar.setTitle("Cuckoo");
        setSupportActionBar(toolbar);
        allExistingVideosAdapter = new AllExistingVideosAdapter(this);
        recyclerView = findViewById(R.id.recyclerView_load_all_existing);
        Database.loadAllVideos(this, new MycompleteListener() {
            @Override
            public void OnSuccess() {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
//        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(allExistingVideosAdapter);
            }

            @Override
            public void OnFailure() {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
}