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


public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.MyViewHolder> {

    GlobalClass globalClass;
    Shared_Preference preference;
    Context context;
    ArrayList<HashMap<String,String>> text;
    LayoutInflater inflater;


    public FamilyAdapter(Context context ,ArrayList<HashMap<String,String>> text
    ) {
        this.context = context;

        this.text = text;

        globalClass = ((GlobalClass) context.getApplicationContext());
        preference = new Shared_Preference(context);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public FamilyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.family_adapter, parent, false);
        FamilyAdapter.MyViewHolder vh = new FamilyAdapter.MyViewHolder(v);
        return vh;

    }


    @Override
    public void onBindViewHolder(final FamilyAdapter.MyViewHolder holder, final int position) {

        String profile_pic_family=text.get(position).get("profile_pic_family");
        if(!profile_pic_family.isEmpty()){
            Picasso.with(context).load(text.get(position).get("profile_pic_family")).
                    fit().into(holder.profile_image);
        }


        String staff_name=text.get(position).get("family_member_name");
        String staff_type=text.get(position).get("family_member_type");
        holder.name.setText(staff_name);
        holder.iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callPhone(text.get(position).get("family_member_mobile"));

            }
        });

        holder.iv_share.setOnClickListener(v -> {

            shareToMember();

        });




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
        TextView name;
        ImageView profile_image,iv_call, iv_share;
        public MyViewHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.tv_name);
            profile_image =  itemView.findViewById(R.id.profile_image);
            iv_call =  itemView.findViewById(R.id.iv_call);
            iv_share =  itemView.findViewById(R.id.iv_share);

        }
    }


    private void shareToMember(){

        String url = "https://play.google.com/store/apps/details?id="
                + context.getPackageName();

        String message = globalClass.getName() + " has added you as a member in "
                + "Complex-" + globalClass.getComplex_name()
                + ", block-"+globalClass.getBlock()
                + ", flat-"+globalClass.getFlat_name()
                + ".\n\nDownload from this link:\n"
                + url;


        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share Via ");
        context.startActivity(shareIntent);
    }
}