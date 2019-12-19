package com.shield.resident.Adapter;

import android.content.Context;


import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.MyViewHolder> {
    String TAG="Add";
    GlobalClass globalClass;
    Shared_Preference preference;
    Context context;
    ArrayList<HashMap<String,String>> text;

    LayoutInflater inflater;
    ArrayList<Boolean> booleansarr;
    String selected = "";

    public StaffAdapter(Context c,ArrayList<HashMap<String,String>> text
    ) {
        this.context = c;

        this.text = text;

        globalClass = ((GlobalClass) c.getApplicationContext());
        preference = new Shared_Preference(context);

        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public StaffAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_staff, parent, false);

        StaffAdapter.MyViewHolder vh = new StaffAdapter.MyViewHolder(v);
        return vh;

    }


    @Override
    public void onBindViewHolder(final StaffAdapter.MyViewHolder holder, final int position)
    {
        String image=text.get(position).get("staffprofile_pic");
        if(!image.isEmpty()){
            Picasso.with(context)
                    .load(text.get(position).get("staffprofile_pic"))
                    .fit()
                    .placeholder(R.mipmap.profile_image)
                    .into(holder.profile_image);

        }

        String staff_name=text.get(position).get("staff_name");
        String staff_type=text.get(position).get("staff_type");
        holder.name.setText(staff_name);
        holder.tv_name1.setText(staff_type);

        holder.iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(text.get(position).get("staff_mobile"));

            }
        });


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
    private void callPhone(String number){

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));// Initiates the Intent
        context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return text.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name,tv_name1,tv_address_2,tv_address_3;
        ImageView profile_image,iv_call;


        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name =  itemView.findViewById(R.id.tv_name);
            tv_name1 =  itemView.findViewById(R.id.tv_name1);
            iv_call =  itemView.findViewById(R.id.iv_call);
            profile_image =  itemView.findViewById(R.id.profile_image);



        }
    }
}