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
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
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

    // Create new views (invoked by the layout manager)
    @Override
    public AdaptorVisitor.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_activity, parent, false);

        AdaptorVisitor.MyViewHolder vh = new AdaptorVisitor.MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AdaptorVisitor.MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
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


        // set the custom dialog components - text, image and button

        //  LinearLayout ll_save=dialog.findViewById(R.id.ll_save);

        // if button is clicked, close the custom dialog

        dialog.show();

    }
}

