package com.sketch.securityowner.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sketch.securityowner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.MyViewHolder> {


    Context context;
    ArrayList<HashMap<String,String>> arrayList;

    public EmergencyAdapter(Context context, ArrayList<HashMap<String,String>> blockList,
                            onItemClickListner mListner) {

        this.arrayList = blockList;
        this.context=context;
        this.mListner = mListner;
    }

    @Override
    public EmergencyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_emergency, parent, false);
        EmergencyAdapter.MyViewHolder vh =
                new EmergencyAdapter.MyViewHolder(view, mListner);
        return vh;
    }


    @Override
    public void onBindViewHolder(EmergencyAdapter.MyViewHolder holder, int position) {
        // holder.bind(items[position]);
        String name = arrayList.get(position).get("name");
        String contact = arrayList.get(position).get("contact");

        holder.tv_category.setText(name);
        holder.tv_data.setText(contact);

        holder.iv_call.setOnClickListener(v -> {

           mListner.onItemClick(contact);

        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView tv_category, tv_data;
         ImageView iv_call;

        onItemClickListner listner;

        public MyViewHolder(View itemView, onItemClickListner listner) {
            super(itemView);
            // get the reference of item view's
            tv_category =  itemView.findViewById(R.id.tv_category);
            tv_data =  itemView.findViewById(R.id.tv_data);
            iv_call =  itemView.findViewById(R.id.iv_call);

            this.listner = listner;

        }
    }


    private onItemClickListner mListner;
    public interface onItemClickListner{
        void onItemClick(String number);
    }


}
