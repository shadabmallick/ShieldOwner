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

    public CarAdapter(Context c,ArrayList<HashMap<String,String>> text
    ) {
        this.context = c;

        this.text = text;

        globalClass = ((GlobalClass) c.getApplicationContext());
        preference = new Shared_Preference(context);

        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //setBooleanValue();


    }


    @Override
    public CarAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters

// pass the view to View Holder
        CarAdapter.MyViewHolder vh = new CarAdapter.MyViewHolder(v);
        return vh;

    }


    @Override
    public void onBindViewHolder(final CarAdapter.MyViewHolder holder, final int position)
    {

        String city=text.get(position).get("car_no");
        holder.name.setText(city);
/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                setSelectedData(position,true);

                String address_id = text.get(position).get("id");
                String fname = text.get(position).get("address");
                String lname = text.get(position).get("pin");
                String address= text.get(position).get("city");
                String city =  text.get(position).get("city");
                String country = text.get(position).get("state");
                String mobile = text.get(position).get("phone_number");
                String ship_name = text.get(position).get("ship_name");

                //   String state = address_list.get(position).get("state");
                //   String zip = address_list.get(position).get("zip");

               */
/*  lat= Double.valueOf(address_list.get(position).get("lat"));
                lng= Double.valueOf(address_list.get(position).get("lng"));*//*



                Log.d(TAG, "onClick: "+address_id);

                globalClass.setAddressid(address_id);

                preference.savePrefrence();


            }
        });
*/


/*
        Picasso.with(context).load(listProduct.get(position)).
                fit().into(holder.image);*/



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