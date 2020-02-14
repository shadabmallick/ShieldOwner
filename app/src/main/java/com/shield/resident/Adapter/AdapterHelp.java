package com.shield.resident.Adapter;

import android.content.Context;
import android.content.Intent;

import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shield.resident.R;

import com.shield.resident.ui.ChatGroup;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AdapterHelp extends RecyclerView.Adapter<AdapterHelp.MyViewHolder> {


    Context context;
    ArrayList<HashMap<String,String>> arrayList;

    public AdapterHelp(Context context, ArrayList<HashMap<String,String>> blockList) {
        this.arrayList = blockList;
        this.context=context;
    }

    @Override
    public AdapterHelp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.help_single, parent, false);
        AdapterHelp.MyViewHolder vh = new AdapterHelp.MyViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        HashMap<String, String> help = arrayList.get(position);

        holder.tv_help_id.setText("Ticket No : "+help.get("help_id"));

        holder.tv_content.setText(help.get("content"));

        String date = help.get("date") + " " + help.get("time");
        showActualInTime(holder.tv_post_date, date);

        holder.tv_status.setText(help.get("status"));
        if (help.get("status").equalsIgnoreCase("Pending")){
            holder.tv_status.setBackgroundResource(R.drawable.curved_orange);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ChatGroup.class);
                    intent.putExtra("id", arrayList.get(position).get("help_id"));
                    intent.putExtra("content", arrayList.get(position).get("content"));
                    intent.putExtra("image", arrayList.get(position).get("image"));
                    context.startActivity(intent);

                }
            });

        }else {
            holder.tv_status.setBackgroundResource(R.drawable.curved_green2);
            holder.itemView.setOnClickListener(null);
        }


        try {
            String image = arrayList.get(position).get("image");
            if(!image.isEmpty()){
                Picasso.with(context)
                        .load(image)
                        .fit()
                        .into(holder.iv_pic);
            }

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_help_id, tv_content, tv_post_date, tv_status;
        private ImageView iv_pic;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_help_id = itemView.findViewById(R.id.tv_help_id);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_post_date = itemView.findViewById(R.id.tv_post_date);
            tv_status = itemView.findViewById(R.id.tv_status);
            iv_pic = itemView.findViewById(R.id.iv_pic);

        }
    }



    private void showActualInTime(TextView textView, String sTime){

        try {

            DateFormat originalFormat =
                    new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat =
                    new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH);

            Date date = originalFormat.parse(sTime);

            String formattedDate = targetFormat.format(date);

            textView.setText(formattedDate);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
