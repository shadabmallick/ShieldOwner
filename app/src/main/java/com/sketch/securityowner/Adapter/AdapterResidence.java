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
import com.sketch.securityowner.ui.ChatAppActivity;
import com.sketch.securityowner.ui.OwnerList;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterResidence extends RecyclerView.Adapter<AdapterResidence.MyViewHolder> {

        ArrayList<HashMap<String,String>> blockList;
        Context context;

public AdapterResidence(Context context,ArrayList<HashMap<String,String>> blockList) {
        this.blockList = blockList;
        this.context=context;
        }

@Override
public AdapterResidence.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_residence, parent, false);
        return new AdapterResidence.MyViewHolder(view);
        }

@Override
public void onBindViewHolder(final AdapterResidence.MyViewHolder holder, final int position) {
       // holder.bind(items[position]);
        holder.name.setText("Block"+" "+blockList.get(position).get("block"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                        Intent placeorder=new Intent(context, OwnerList.class);
                                        placeorder.putExtra("block",blockList.get(position).get("block"));


                                        context.startActivity(placeorder);
                                }
                        });

                }
        });

/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {

        Intent placeorder=new Intent(context, ChatAppActivity.class);

        context.startActivity(placeorder);
        }
        });
*/
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
                TextView name;
                ImageView profile_image;


                public MyViewHolder(View itemView) {
                        super(itemView);
                        // get the reference of item view's
                        name =  itemView.findViewById(R.id.list_item);

                       // profile_image =  itemView.findViewById(R.id.profile_image);



                }
        }

}
