package com.gurusankar149.cuckoo;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class TrendingRecyclerAdapter extends RecyclerView.Adapter<TrendingRecyclerAdapter.ViewHolder> {

    List<RecyclerVideoModel> recyclerVideoModelList;
    Context context;
    public TrendingRecyclerAdapter() {
    }

    public TrendingRecyclerAdapter(List<RecyclerVideoModel> recyclerVideoModelList) {
        this.recyclerVideoModelList = recyclerVideoModelList;

    }

    @NonNull
    @Override
    public TrendingRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_videos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingRecyclerAdapter.ViewHolder holder, int position) {

        String uristring = recyclerVideoModelList.get(position).getVideoUri();
        Uri videoUri = Uri.parse(uristring);
        holder.videoView.setVideoURI(videoUri);
        holder.videoView.start();
    }

    @Override
    public int getItemCount() {
        return recyclerVideoModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.video_recycler_view);
        }
    }


}
