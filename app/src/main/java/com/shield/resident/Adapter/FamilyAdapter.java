package com.shield.resident.Adapter;

import android.content.Context;


import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.sdsmdg.tastytoast.TastyToast;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.MyViewHolder> {

    private GlobalClass globalClass;
    private Shared_Preference preference;
    private Context context;
    private ArrayList<HashMap<String,String>> arrayList;
    private LayoutInflater inflater;

    public FamilyAdapter(Context context ,ArrayList<HashMap<String,String>> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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

        HashMap<String,String> hashMap = arrayList.get(position);

        String profile_pic_family = hashMap.get("profile_pic_family");
        if(!profile_pic_family.isEmpty()){
            Picasso.with(context).load(profile_pic_family)
                    .fit()
                    .centerCrop()
                    .into(holder.profile_image);
        }


        String staff_name=hashMap.get("family_member_name");
        String staff_type=hashMap.get("family_member_type");
        holder.name.setText(staff_name);
        holder.iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callPhone(hashMap.get("family_member_mobile"));

            }
        });



        holder.iv_menu.setOnClickListener(v -> {

            showPopUp(holder.iv_menu, hashMap);
        });

    }
    private void callPhone(String number){

        Intent intent = new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + number));// Initiates the Intent
        context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView profile_image,iv_call, iv_menu;
        public MyViewHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.tv_name);
            profile_image =  itemView.findViewById(R.id.profile_image);
            iv_call =  itemView.findViewById(R.id.iv_call);
            iv_menu =  itemView.findViewById(R.id.iv_menu);

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



    private void showPopUp(View view, HashMap<String,String> hashMap){

        PopupMenu popup = new PopupMenu(context, view);
        popup.inflate(R.menu.menu_member);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:

                        if (globalClass.getUser_type().equals("1")){
                            mViewClickListener.onMemberDelete(hashMap);
                        }else {
                            TastyToast.makeText(context,
                                    "Sorry! Only flat owner can do this.",
                                    TastyToast.LENGTH_LONG, TastyToast.WARNING);
                        }

                        return true;
                    case R.id.share:

                        shareToMember();

                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }






    ///////
    private ViewClickListener mViewClickListener;

    public interface ViewClickListener {
        void onMemberDelete(HashMap<String,String> hashMap);
    }

    public void setViewClickListener (ViewClickListener viewClickListener) {
        mViewClickListener = viewClickListener;
    }
}