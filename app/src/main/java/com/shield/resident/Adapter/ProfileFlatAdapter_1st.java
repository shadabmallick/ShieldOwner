package com.shield.resident.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shield.resident.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileFlatAdapter_1st extends
        RecyclerView.Adapter<ProfileFlatAdapter_1st.MyViewHolder> {

    private ArrayList<HashMap<String, String>> aList;
    private ArrayList<Boolean> booleanArrayList;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_flat_name;
        public RelativeLayout rl_main;
        public RadioButton radio;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_flat_name = itemView.findViewById(R.id.tv_flat_name);
            rl_main = itemView.findViewById(R.id.rl_main);
            radio = itemView.findViewById(R.id.radio);

        }
    }

    public ProfileFlatAdapter_1st(Context context, ArrayList<HashMap<String, String>> aList) {
        this.aList = aList;
        this.context = context;

        booleanArrayList = new ArrayList<>();

        setBooleanData();

    }


    private void setBooleanData(){
        booleanArrayList = new ArrayList<>();
        for (int i = 0; i < aList.size(); i++){
            booleanArrayList.add(false);
        }
    }

    @Override
    public ProfileFlatAdapter_1st.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_flat_item_1st, parent, false);

        ProfileFlatAdapter_1st.MyViewHolder vh = new ProfileFlatAdapter_1st.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ProfileFlatAdapter_1st.MyViewHolder holder, int position) {

        HashMap<String, String> hashMap = aList.get(position);

        holder.tv_flat_name.setText(hashMap.get("flat_no")
                +" "+hashMap.get("block")+" "+"block");

        holder.rl_main.setOnClickListener(v -> {

            mViewClickListener.onClicked(hashMap);

            setBooleanData();

            booleanArrayList.set(position, true);

            notifyDataSetChanged();

        });


        /*holder.radio.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){

                mViewClickListener.onClicked(hashMap);

                setBooleanData();

                booleanArrayList.set(position, true);

                notifyDataSetChanged();

            }

        });*/


        holder.radio.setChecked(booleanArrayList.get(position));


    }

    @Override
    public int getItemCount() {
        return aList.size();
    }


    private ViewClickListener mViewClickListener;

    public interface ViewClickListener {
        void onClicked(HashMap<String, String> hashMap);
    }

    public void setViewClickListener (ViewClickListener viewClickListener) {
        mViewClickListener = viewClickListener;
    }
}

