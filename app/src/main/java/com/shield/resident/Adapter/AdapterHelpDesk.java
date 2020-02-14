package com.shield.resident.Adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shield.resident.R;
import com.shield.resident.ui.HelpListDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterHelpDesk  extends RecyclerView.Adapter<AdapterHelpDesk.MyViewHolder> {


    Context context;
    ArrayList<HashMap<String,String>> arrayList;

    public AdapterHelpDesk(Context context, ArrayList<HashMap<String,String>> cityList) {
        this.arrayList = cityList;
        this.context = context;
    }

    @Override
    public AdapterHelpDesk.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.help_adapter, parent, false);
        AdapterHelpDesk.MyViewHolder vh = new AdapterHelpDesk.MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final AdapterHelpDesk.MyViewHolder holder, final int position) {

        if (arrayList.get(position).get("image") != null
                && !arrayList.get(position).get("image").equals("null")
                && !arrayList.get(position).get("image").isEmpty()
        ){
            Picasso.with(context).load(arrayList.get(position).get("image")).
                    fit().into(holder.profile_image);
        }



        String name=arrayList.get(position).get("name");
        holder.name.setText(name);

        holder.rl_count.setVisibility(View.GONE);

        if (!arrayList.get(position).get("total").equals("0")){
            holder.rl_count.setVisibility(View.VISIBLE);
            holder.tv_count.setText(arrayList.get(position).get("total"));
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent placeorder=new Intent(context, HelpListDetails.class);
                placeorder.putExtra("category",arrayList.get(position).get("name"));

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
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name, tv_count;
        ImageView profile_image;
        RelativeLayout rl_count;


        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name =  itemView.findViewById(R.id.tv_name);

            profile_image =  itemView.findViewById(R.id.profile_image);
            tv_count =  itemView.findViewById(R.id.tv_count);
            rl_count =  itemView.findViewById(R.id.rl_count);



        }
    }


}
