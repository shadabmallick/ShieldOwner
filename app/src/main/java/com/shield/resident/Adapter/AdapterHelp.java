package com.shield.resident.Adapter;

import android.content.Context;
import android.content.Intent;

import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shield.resident.R;

import com.shield.resident.ui.ChatGroup;

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

        holder.tv_help_id.setText("Help Id: "+help.get("help_id"));

        holder.tv_status.setText(help.get("status"));

        holder.tv_content.setText(help.get("content"));

        holder.tv_view_image.setPaintFlags(holder.tv_view_image.getPaintFlags()
                | Paint.UNDERLINE_TEXT_FLAG);

        if (!help.get("image").isEmpty()){
            holder.tv_view_image.setVisibility(View.VISIBLE);
        }else {
            holder.tv_view_image.setVisibility(View.GONE);
        }

        String date = help.get("date") + " " + help.get("time");
        showActualInTime(holder.tv_post_date, date);



        holder.tv_content.setVisibility(View.GONE);

        try {

            holder.tv_view_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String image = arrayList.get(position).get("image");

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(image));
                    context.startActivity(i);
                }
            });



        }catch (Exception e){
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatGroup.class);
                intent.putExtra("id", arrayList.get(position).get("help_id"));
                intent.putExtra("content", arrayList.get(position).get("content"));
                context.startActivity(intent);

            }
        });



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
        // init the item view's
        private TextView tv_content, tv_post_date, tv_view_image,
                tv_help_id, tv_status;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_post_date = itemView.findViewById(R.id.tv_post_date);
            tv_view_image = itemView.findViewById(R.id.tv_view_image);
            tv_help_id = itemView.findViewById(R.id.tv_help_id);
            tv_status = itemView.findViewById(R.id.tv_status);

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
