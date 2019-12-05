package com.shield.resident.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shield.resident.R;

import java.util.ArrayList;
import java.util.Random;

public class AppHelpAdapter extends RecyclerView.Adapter<AppHelpAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String > arrayList;
    private ArrayList<Integer> arrayTopImage;
    private ArrayList<Integer> arrayMidImage;

    public AppHelpAdapter(Context c, ArrayList<String > arrayList,
                          ArrayList<Integer> arrayTopImage,
                          ArrayList<Integer> arrayMidImage) {
        this.context = c;
        this.arrayList = arrayList;
        this.arrayTopImage = arrayTopImage;
        this.arrayMidImage = arrayMidImage;


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

        holder.iv_top.setImageResource(arrayTopImage.get(position));
        holder.iv_center.setImageResource(arrayMidImage.get(position));





    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_msg;
        ImageView iv_top, iv_center;
        RelativeLayout rel_bg;
        CardView cardview;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_msg =  itemView.findViewById(R.id.tv_msg);
            iv_top =  itemView.findViewById(R.id.iv_top);
            iv_center =  itemView.findViewById(R.id.iv_center);
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