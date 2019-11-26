package com.sketch.securityowner.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sketch.securityowner.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AppHelpAdapter extends RecyclerView.Adapter<AppHelpAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String > arrayList;

    public AppHelpAdapter(Context c, ArrayList<String > arrayList) {
        this.context = c;
        this.arrayList = arrayList;


    }

    @Override
    public AppHelpAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_help_app, parent, false);

        AppHelpAdapter.MyViewHolder vh = new AppHelpAdapter.MyViewHolder(v);
        return vh;

    }


    @Override
    public void onBindViewHolder(AppHelpAdapter.MyViewHolder holder, final int position) {

        holder.tv_msg.setText(arrayList.get(position));

        holder.cardview.setCardBackgroundColor(randomColor());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_msg;
        ImageView iv_bg;
        RelativeLayout rel_bg;
        CardView cardview;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_msg =  itemView.findViewById(R.id.tv_msg);
            iv_bg =  itemView.findViewById(R.id.iv_bg);
            rel_bg =  itemView.findViewById(R.id.rel_bg);
            cardview =  itemView.findViewById(R.id.cardview);

        }
    }


    private int randomColor(){

        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];

       // Random rand = new Random();
       // int randomNum = rand.nextInt((androidColors.length - 0) + 1) + 0;

        return randomAndroidColor;
    }
}