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
import com.shield.resident.ui.Webview;
import com.skyhope.showmoretextview.ShowMoreTextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterNotice extends RecyclerView.Adapter<AdapterNotice.MyViewHolder> {


    Context context;
    ArrayList<HashMap<String,String>> arrayList;

    public AdapterNotice(Context context, ArrayList<HashMap<String,String>> blockList) {
        this.arrayList = blockList;
        this.context=context;
    }

    @Override
    public AdapterNotice.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notice_single, parent, false);
        AdapterNotice.MyViewHolder vh = new AdapterNotice.MyViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       // holder.bind(items[position]);

       holder.tv_content.setText(arrayList.get(position).get("content"));
       holder.tv_content.setShowingLine(4);

       holder.tv_subject.setText(arrayList.get(position).get("subject"));
       holder.view_file.setPaintFlags(holder.view_file.getPaintFlags()
                | Paint.UNDERLINE_TEXT_FLAG);


        try {

            JSONArray files = new JSONArray(arrayList.get(position).get("files"));

            if (files.length() > 0){

                String url = files.getString(0);

                holder.view_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       Intent intent = new Intent(context, Webview.class);
                       intent.putExtra("url", url);
                       context.startActivity(intent);

                    }
                });


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
        // init the item view's
        TextView view_file, tv_subject;
        ShowMoreTextView tv_content;


        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            tv_subject =  itemView.findViewById(R.id.tv_subject);
            tv_content =  itemView.findViewById(R.id.tv_content);
            view_file =  itemView.findViewById(R.id.view_file);

        }
    }

}
