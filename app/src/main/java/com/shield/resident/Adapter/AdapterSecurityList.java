package com.shield.resident.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shield.resident.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterSecurityList extends RecyclerView.Adapter<AdapterSecurityList.MyViewHolder> {


    Context context;
    ArrayList<HashMap<String,String>> blockList;

    public AdapterSecurityList(Context context, ArrayList<HashMap<String,String>> blockList,
                               onItemClickListner mListner) {
        this.blockList = blockList;
        this.context=context;
        this.mListner = mListner;
    }

    @Override
    public AdapterSecurityList.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_security, parent, false);
        AdapterSecurityList.MyViewHolder vh = new AdapterSecurityList.MyViewHolder(view, mListner);
        return vh;
    }


    @Override
    public void onBindViewHolder(AdapterSecurityList.MyViewHolder holder, int position) {
        // holder.bind(items[position]);
        holder.tv_name.setText(blockList.get(position).get("name"));
            String image=blockList.get(position).get("image");

        if(!image.isEmpty()){
            Picasso.with(context).load(blockList.get(position).get("image")).
                    placeholder(R.mipmap.profile_image).
                    fit().into(holder.profile_image);
        }


        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListner.onItemClick(blockList.get(position).get("mobile"));

            }
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
        ImageView profile_image,call;
        TextView name,tv_name;

        onItemClickListner listner;

        public MyViewHolder(View itemView, onItemClickListner listner) {
            super(itemView);

            tv_name =  itemView.findViewById(R.id.tv_name);
            profile_image =  itemView.findViewById(R.id.profile_image);
            call =  itemView.findViewById(R.id.call);

            this.listner = listner;
        }

    }


    private onItemClickListner mListner;
    public interface onItemClickListner{
        void onItemClick(String number);
    }


}
