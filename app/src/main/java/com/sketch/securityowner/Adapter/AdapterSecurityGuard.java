package com.sketch.securityowner.Adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sdsmdg.tastytoast.TastyToast;
import com.sketch.securityowner.R;

import com.sketch.securityowner.ui.ChatGroup;
import com.sketch.securityowner.ui.OwnerList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterSecurityGuard  extends RecyclerView.Adapter<AdapterSecurityGuard.MyViewHolder> {


    Context context;
    ArrayList<HashMap<String,String>> blockList;

    public AdapterSecurityGuard(Context context, ArrayList<HashMap<String,String>> blockList) {
        this.blockList = blockList;
        this.context=context;
    }

    @Override
    public AdapterSecurityGuard.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_desk_single, parent, false);
        AdapterSecurityGuard.MyViewHolder vh = new AdapterSecurityGuard.MyViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       // holder.bind(items[position]);
       holder.tv_content.setText(blockList.get(position).get("content"));
       holder.tv_help_id.setText("Help Id :"+blockList.get(position).get("help_id"));
       holder.tv_status.setText(blockList.get(position).get("status"));
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent placeorder=new Intent(context, ChatGroup.class);
               placeorder.putExtra("id",blockList.get(position).get("help_id"));


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
        return blockList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name,tv_content,tv_status,tv_help_id;


        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            tv_content =  itemView.findViewById(R.id.tv_content);
            tv_status =  itemView.findViewById(R.id.tv_status);
            tv_help_id =  itemView.findViewById(R.id.tv_help_id);




        }
    }

}
