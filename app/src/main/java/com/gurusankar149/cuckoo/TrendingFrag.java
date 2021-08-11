package com.gurusankar149.cuckoo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class TrendingFrag extends Fragment {
   ViewPager2 viewPager2;
    TrendingRecyclerAdapter trendingRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trending, container, false);
     viewPager2=view.findViewById(R.id.view_pager2_trending);
     Database.loadDatabaseVideo(new MycompleteListener() {
         @Override
         public void OnSuccess() {

             trendingRecyclerAdapter=new TrendingRecyclerAdapter(Database.recyclerVideoModelList);

             viewPager2.setAdapter(trendingRecyclerAdapter);
         }

         @Override
         public void OnFailure() {

         }
     });

        return view;

    }


}