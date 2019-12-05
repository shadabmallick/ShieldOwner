package com.shield.resident.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.shield.resident.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CityAdapter  extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {


    Context context;
    ArrayList<HashMap<String,String>> list_claim;
    ArrayList<String > text;
    LayoutInflater inflater;


    public CityAdapter(Context c, ArrayList<HashMap<String,String>> list_claim
    ) {
        this.context = c;
        // this.listProduct = listProduct;
        this.list_claim = list_claim;

        //   globalClass = ((GlobalClass) c.getApplicationContext());

        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



    }
    @Override
    public CityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list, parent, false);
        // set the view's size, margins, paddings and layout parameters

        CityAdapter.MyViewHolder vh = new CityAdapter.MyViewHolder(v);
        return vh;

    }


    @Override
    public void onBindViewHolder(CityAdapter.MyViewHolder holder, final int position) {

        holder.name.setText(list_claim.get(position).get("feature"));

    }

    @Override
    public int getItemCount() {
        return list_claim.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;
        //  ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name =  itemView.findViewById(R.id.city_id);
            // image = (ImageView) itemView.findViewById(R.id.image_view);

        }
    }
}