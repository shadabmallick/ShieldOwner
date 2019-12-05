package com.shield.resident.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shield.resident.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileFlatAdapter extends RecyclerView.Adapter<ProfileFlatAdapter.MyViewHolder> {
    ArrayList<HashMap<String, String>> aList;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_flat_name;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_flat_name = itemView.findViewById(R.id.tv_flat_name);

        }
    }

    public ProfileFlatAdapter(Context context, ArrayList<HashMap<String, String>> aList) {
        this.aList = aList;
        this.context = context;
    }

    @Override
    public ProfileFlatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_flat_item, parent, false);

        ProfileFlatAdapter.MyViewHolder vh = new ProfileFlatAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ProfileFlatAdapter.MyViewHolder holder, int position) {

        HashMap<String, String> hashMap = aList.get(position);

        holder.tv_flat_name.setText(hashMap.get("flat_no")
                +" "+hashMap.get("block")+" "+"block");

        holder.tv_flat_name.setOnClickListener(v -> {

            mViewClickListener.onClicked(hashMap);
        });

    }

    @Override
    public int getItemCount() {
        return aList.size();
    }


    private ViewClickListener mViewClickListener;

    public interface ViewClickListener {
        void onClicked(HashMap<String, String> hashMap);
    }

    public void setViewClickListener (ViewClickListener viewClickListener) {
        mViewClickListener = viewClickListener;
    }
}

