package com.shield.resident.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.sdsmdg.tastytoast.TastyToast;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.R;


import java.util.ArrayList;
import java.util.HashMap;

public class CarAdapter  extends RecyclerView.Adapter<CarAdapter.MyViewHolder> {
    String TAG="Add";
    private GlobalClass globalClass;
    private Shared_Preference preference;
    private Context context;
    private ArrayList<HashMap<String,String>> arrayList;
    private LayoutInflater inflater;

    public CarAdapter(Context context,ArrayList<HashMap<String,String>> arrayList) {
        this.context = context;

        this.arrayList = arrayList;
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

        String city=arrayList.get(position).get("car_no");
        String parking_no=arrayList.get(position).get("parking_no");
        holder.name.setText(city);
        holder.tv_parking_no.setText("P. No : "+parking_no);

        holder.iv_delete.setOnClickListener(v -> {
            if (globalClass.getUser_type().equals("1")){
                mViewClickListener.onCarDelete(arrayList.get(position));
            }else {
                TastyToast.makeText(context,
                        "Sorry! Only flat owner can do this.",
                        TastyToast.LENGTH_LONG, TastyToast.WARNING);
            }

        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name,tv_parking_no;
        ImageView profile_image, iv_delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.tv_name);
            tv_parking_no =  itemView.findViewById(R.id.tv_parking_no);
            iv_delete =  itemView.findViewById(R.id.iv_delete);

        }
    }



    ///////
    private ViewClickListener mViewClickListener;

    public interface ViewClickListener {
        void onCarDelete(HashMap<String,String> hashMap);
    }

    public void setViewClickListener (ViewClickListener viewClickListener) {
        mViewClickListener = viewClickListener;
    }


}