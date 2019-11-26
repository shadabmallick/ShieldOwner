package com.sketch.securityowner.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sketch.securityowner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AlertAdapter  extends RecyclerView.Adapter<AlertAdapter.MyViewHolder> {


    Context context;
    ArrayList<HashMap<String,String>> blockList;

    public AlertAdapter(Context context, ArrayList<HashMap<String,String>> blockList) {
        this.blockList = blockList;
        this.context=context;
    }

    @Override
    public AlertAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_panic, parent, false);
        AlertAdapter.MyViewHolder vh = new AlertAdapter.MyViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(AlertAdapter.MyViewHolder holder, int position) {

        String category=blockList.get(position).get("category");

        if(category.equals("Fire")){
            Picasso.with(context)
                    .load(R.mipmap.fire)
                    .resize(200, 200)
                    .into(holder.profile_image);

        }
        if(category.equals("Medical Emergency")){
            Picasso.with(context)
                    .load(R.mipmap.doctor)
                    .resize(200, 200)
                    .into(holder.profile_image);

        }
        if(category.equals("Lift Stuck")){
            Picasso.with(context)
                    .load(R.mipmap.lift_stuck)
                    .resize(200, 200)
                    .into(holder.profile_image);

        }
        if(category.equals("Animal Attack")){
            Picasso.with(context)
                    .load(R.mipmap.animal_attack)
                    .resize(200, 200)
                    .into(holder.profile_image);

        }
        if(category.equals("Threat")){
            Picasso.with(context)
                    .load(R.mipmap.threat)
                    .resize(200, 200)
                    .into(holder.profile_image);

        }
        if(category.equals("Burglary")){
            Picasso.with(context)
                    .load(R.mipmap.thief)
                    .resize(200, 200)
                    .into(holder.profile_image);

        }
        holder.tv_category.setText(blockList.get(position).get("category"));
        holder.tv_data.setText(blockList.get(position).get("date"));



    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return blockList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name,tv_category,tv_data,tv_help_id;
         ImageView profile_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            tv_category =  itemView.findViewById(R.id.tv_category);
            tv_data =  itemView.findViewById(R.id.tv_data);
            tv_help_id =  itemView.findViewById(R.id.tv_help_id);
            profile_image =  itemView.findViewById(R.id.profile_image);




        }
    }

}
