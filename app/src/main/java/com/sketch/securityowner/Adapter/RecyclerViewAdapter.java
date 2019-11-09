package com.sketch.securityowner.Adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.sdsmdg.tastytoast.TastyToast;
import com.sketch.securityowner.R;

import java.util.ConcurrentModificationException;

/**
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<TextItemViewHolder> {

    String[] items;
    Context context;

    public RecyclerViewAdapter(Context context,String[] items) {
        this.items = items;
        this.context=context;
    }

    @Override
    public TextItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_alert, parent, false);
        return new TextItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TextItemViewHolder holder, int position) {
        holder.bind(items[position]);
/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent placeorder=new Intent(context, ChatAppActivity.class);

                        context.startActivity(placeorder);
                    }
                });

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
        return items.length;
    }
}
