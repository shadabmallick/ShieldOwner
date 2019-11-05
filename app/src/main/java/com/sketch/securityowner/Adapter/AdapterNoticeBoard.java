package com.sketch.securityowner.Adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.sketch.securityowner.R;
import com.sketch.securityowner.ui.ChatAppActivity;

public class AdapterNoticeBoard  extends RecyclerView.Adapter<TextItemViewHolder> {

    String[] items;
    Context context;

    public AdapterNoticeBoard(Context context,String[] items) {
        this.items = items;
        this.context=context;
    }

    @Override
    public TextItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notice_board, parent, false);
        return new TextItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TextItemViewHolder holder, int position) {
        holder.bind(items[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent placeorder=new Intent(context, ChatAppActivity.class);

                context.startActivity(placeorder);
            }
        });
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
