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

public class AdapterTenant extends RecyclerView.Adapter<AdapterTenant.MyViewHolder> {

    Context context;
    ArrayList<HashMap<String,String>> arrayList;
    int index = -1;
    public AdapterTenant(Context context, ArrayList<HashMap<String,String>> arrayList,
                         AdapterTenant.onItemClickListner mListner) {
        this.arrayList = arrayList;
        this.context=context;
        this.mListner=mListner;
    }

    @Override
    public AdapterTenant.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_tenants, parent, false);
        AdapterTenant.MyViewHolder vh = new AdapterTenant.MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final AdapterTenant.MyViewHolder holder, final int position) {

        String imgage=arrayList.get(position).get("profile_pic");
        if(!imgage.isEmpty()){
            Picasso.with(context)
                    .load(arrayList.get(position).get("profile_pic")).
                    fit()
                    .into(holder.profile_image);
        }

        String staff_name=arrayList.get(position).get("tenant_name");
        String staff_number=arrayList.get(position).get("tenant_mobile");
        holder.name.setText(staff_name);
        holder.tv_number.setText(staff_number);
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                index = position;
                notifyDataSetChanged();
                String tenant_id = arrayList.get(position).get("tenant_id");

                mListner.onItemClick(tenant_id);
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
        TextView name,tv_number;
        ImageView profile_image,iv_delete;


        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name =  itemView.findViewById(R.id.tv_name);
            tv_number =  itemView.findViewById(R.id.tv_number);
            iv_delete =  itemView.findViewById(R.id.iv_delete);

            profile_image =  itemView.findViewById(R.id.profile_image);



        }
    }
    private AdapterTenant.onItemClickListner mListner;
    public interface onItemClickListner{
        void onItemClick(String category);
    }

}
