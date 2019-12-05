package com.shield.resident.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shield.resident.R;
import com.shield.resident.ui.OwnerList;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterResidence extends RecyclerView.Adapter<AdapterResidence.MyViewHolder> {

        private ArrayList<HashMap<String,String>> blockList;
        private Context context;

public AdapterResidence(Context context,ArrayList<HashMap<String,String>> blockList) {
        this.blockList = blockList;
        this.context=context;
        }

@Override
public AdapterResidence.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_residence, parent, false);
        return new AdapterResidence.MyViewHolder(view);
        }

@Override
public void onBindViewHolder(final AdapterResidence.MyViewHolder holder, final int position) {
        holder.name.setText("Block"+" "+blockList.get(position).get("block"));

        holder.itemView.setOnClickListener(v -> {

                Intent placeorder=new Intent(context, OwnerList.class);
                placeorder.putExtra("block",blockList.get(position).get("block"));

                context.startActivity(placeorder);
        });

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

                public MyViewHolder(View itemView) {
                        super(itemView);
                        name =  itemView.findViewById(R.id.list_item);

                }
        }

}
