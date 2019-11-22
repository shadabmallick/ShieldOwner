package com.sketch.securityowner.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.Shared_Preference;
import com.sketch.securityowner.R;


import java.util.ArrayList;
import java.util.HashMap;

public class CarAdapter  extends RecyclerView.Adapter<CarAdapter.MyViewHolder> {
    String TAG="Add";
    GlobalClass globalClass;
    Shared_Preference preference;
    Context context;
    ArrayList<HashMap<String,String>> text;

    LayoutInflater inflater;
    ArrayList<Boolean> booleansarr;
    String selected = "";

    public CarAdapter(Context context,ArrayList<HashMap<String,String>> text
    ) {
        this.context = context;

        this.text = text;

        globalClass = ((GlobalClass) context.getApplicationContext());
        preference = new Shared_Preference(context);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public CarAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_adapter, parent, false);

        CarAdapter.MyViewHolder vh = new CarAdapter.MyViewHolder(v);
        return vh;

    }


    @Override
    public void onBindViewHolder(final CarAdapter.MyViewHolder holder, final int position) {

        String city=text.get(position).get("car_no");
        holder.name.setText(city);

    }

    @Override
    public int getItemCount() {
        return text.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name,tv_address_1,tv_address_2,tv_address_3;
        ImageView profile_image,crud;


        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name =  itemView.findViewById(R.id.tv_name);
         //   profile_image =  itemView.findViewById(R.id.profile_image);



        }
    }
}