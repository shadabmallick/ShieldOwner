package com.sketch.securityowner.Adapter;

import android.app.Dialog;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.sketch.securityowner.R;

import java.util.ArrayList;

public class AdaptorVisitor extends RecyclerView.Adapter<AdaptorVisitor.MyViewHolder> {
    ArrayList<String> aList;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdaptorVisitor(Context context,ArrayList<String> aList) {
        this.aList = aList;
        this.context = context;
    }


    @Override
    public AdaptorVisitor.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_activity, parent, false);

        AdaptorVisitor.MyViewHolder vh = new AdaptorVisitor.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdaptorVisitor.MyViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaffDialog();
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return aList.size();
    }


    public void StaffDialog(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.visitor_dialog);

        dialog.show();

    }
}

