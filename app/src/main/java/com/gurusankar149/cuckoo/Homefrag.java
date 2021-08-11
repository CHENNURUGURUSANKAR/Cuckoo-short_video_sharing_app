package com.gurusankar149.cuckoo;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class Homefrag extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;

    // ViewPagerAdater viewPagerAdater = new ViewPagerAdater(getFragmentManager());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homefrag, container, false);
        viewPager = view.findViewById(R.id.viewpager_hone_frag);
        tabLayout = view.findViewById(R.id.tablayout_home_frag);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdater viewPagerAdater = new ViewPagerAdater(getChildFragmentManager());
        TrendingFrag fragment = new TrendingFrag();
        viewPagerAdater.addfragment(new TrendingFrag(), "Trending");
        viewPagerAdater.addfragment(new ForyouFrag(), "For You");
        viewPager.setAdapter(viewPagerAdater);
        Log.d("Tag","viewpager working");

    }


    class ViewPagerAdater extends FragmentPagerAdapter {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();

        public void addfragment(Fragment fragment, String title) {
            arrayList.add(title);
            fragmentList.add(fragment);
        }

        public ViewPagerAdater(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return arrayList.get(position);
        }
    }

}