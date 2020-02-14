package com.shield.resident.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shield.resident.R;
import com.shield.resident.ui.ActivityMyFlatTenants;
import com.shield.resident.ui.OwnerList;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterMyFlat extends RecyclerView.Adapter<AdapterMyFlat.MyViewHolder> {

        private ArrayList<HashMap<String,String>> flatList;
        private Context context;

        public AdapterMyFlat(Context context, ArrayList<HashMap<String,String>> flatList) {
                this.flatList = flatList;
                this.context=context;
        }

        @Override
        public AdapterMyFlat.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.my_complex_item, parent, false);
                return new AdapterMyFlat.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final AdapterMyFlat.MyViewHolder holder, final int position) {

                HashMap<String, String> hashMap = flatList.get(position);

                String ss = hashMap.get("flat_no") + ", "
                        + hashMap.get("block") + ", "
                        + hashMap.get("complex_name");

                if (hashMap.get("user_type").equals("1")){
                        holder.tv_position.setText("Owner");
                }else if (hashMap.get("user_type").equals("4")){
                        holder.tv_position.setText("Member");
                }else if (hashMap.get("user_type").equals("6")){
                        holder.tv_position.setText("Tenant");
                }


                holder.tv_flat_block_name.setText(ss);

                holder.itemView.setOnClickListener(v -> {

                        if (!hashMap.get("user_type").equals("6")){
                                Intent intent = new Intent(context, ActivityMyFlatTenants.class);
                                intent.putExtra("datas", hashMap);
                                intent.putExtra("name", ss);
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
                return flatList.size();
                }

        public class MyViewHolder extends RecyclerView.ViewHolder {
                // init the item view's
                TextView tv_flat_block_name, tv_position;

                public MyViewHolder(View itemView) {
                        super(itemView);
                        tv_flat_block_name =  itemView.findViewById(R.id.tv_flat_block_name);
                        tv_position =  itemView.findViewById(R.id.tv_position);

                }
        }

}
