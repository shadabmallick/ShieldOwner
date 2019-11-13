package com.sketch.securityowner.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sketch.securityowner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AdapterSecurityList extends RecyclerView.Adapter<AdapterSecurityList.MyViewHolder> {


    Context context;
    ArrayList<HashMap<String,String>> blockList;

    public AdapterSecurityList(Context context, ArrayList<HashMap<String,String>> blockList) {
        this.blockList = blockList;
        this.context=context;
    }

    @Override
    public AdapterSecurityList.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_security, parent, false);
        AdapterSecurityList.MyViewHolder vh = new AdapterSecurityList.MyViewHolder(view);
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


/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent placeorder=new Intent(context, ChatAppActivity.class);

                        context.startActivity(placeorder);
                    }
                });

            }
        });
*/
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                String number="+"+blockList.get(position).get("mobile");
                Log.d(TAG, "mobile: "+number);
                String p = "tel:" + number;
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
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
        return blockList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        ImageView profile_image,call;
        TextView name,tv_name,tv_status,tv_help_id;


        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            tv_name =  itemView.findViewById(R.id.tv_name);


            profile_image =  itemView.findViewById(R.id.profile_image);
            call =  itemView.findViewById(R.id.call);




        }

    }

}
