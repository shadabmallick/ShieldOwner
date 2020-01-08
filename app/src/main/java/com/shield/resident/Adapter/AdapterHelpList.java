package com.shield.resident.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shield.resident.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterHelpList  extends RecyclerView.Adapter<AdapterHelpList.MyViewHolder> {


    Context context;
    ArrayList<HashMap<String,String>> cityList;

    public AdapterHelpList(Context context, ArrayList<HashMap<String,String>> cityList) {
        this.cityList = cityList;
        this.context=context;
    }

    @Override
    public AdapterHelpList.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_help_list, parent, false);
        AdapterHelpList.MyViewHolder vh = new AdapterHelpList.MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final AdapterHelpList.MyViewHolder holder, final int position) {
         if(!cityList.get(position).get("image").equals("")){
             Picasso.with(context).load(cityList.get(position).get("image")).
                     fit().into(holder.profile_image);
         }

        String staff_name=cityList.get(position).get("name");
        holder.name.setText(staff_name);
        holder.iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               callPhone(cityList.get(position).get("mobile"));
            }
        });

    }
    private void callPhone(String number){

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));// Initiates the Intent
        context.startActivity(intent);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;
        ImageView profile_image,iv_call;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name =  itemView.findViewById(R.id.tv_name);

            profile_image =  itemView.findViewById(R.id.profile_image);
            iv_call =  itemView.findViewById(R.id.iv_call);

        }
    }


}
