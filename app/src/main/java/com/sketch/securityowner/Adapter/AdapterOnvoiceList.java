package com.sketch.securityowner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterOnvoiceList extends RecyclerView.Adapter<AdapterOnvoiceList.MyViewHolder> {


    Context context;
    ArrayList<HashMap<String,String>> cityList;
    int index = -1;
    GlobalClass globalClass;
    public AdapterOnvoiceList(Context context, ArrayList<HashMap<String,String>> cityList, AdapterOnvoiceList.onItemClickListner mListner) {
        this.cityList = cityList;
        this.context=context;
        this.mListner=mListner;
        globalClass = ((GlobalClass) context.getApplicationContext());
    }

    @Override
    public AdapterOnvoiceList.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_invoice, parent, false);
        AdapterOnvoiceList.MyViewHolder vh = new AdapterOnvoiceList.MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final AdapterOnvoiceList.MyViewHolder holder, final int position) {

       holder.tv_amout.setText(cityList.get(position).get("billing_amount"));
       holder.tv_date1.setText(cityList.get(position).get("invoice_name"));
       holder.tv_date2.setText(cityList.get(position).get("date"));
       holder.status.setText(cityList.get(position).get("invoicestatus"));
        holder.invoice.setOnClickListener(v -> {

            index = position;
            notifyDataSetChanged();
            String invoice_link=cityList.get(position).get("invoice_link");

            mListner.onItemClick(invoice_link);
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return cityList.size();



    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView tv_amout,tv_date1,tv_date2,tv_number,status;
        ImageView profile_image,iv_delete;
        LinearLayout invoice;


        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            tv_amout =  itemView.findViewById(R.id.tv_amout);
            tv_number =  itemView.findViewById(R.id.tv_number);
            tv_date1 =  itemView.findViewById(R.id.tv_date1);
            tv_date2 =  itemView.findViewById(R.id.tv_date2);
            status =  itemView.findViewById(R.id.status);
            invoice =  itemView.findViewById(R.id.invoice);

            profile_image =  itemView.findViewById(R.id.profile_image);



        }
    }
    private AdapterOnvoiceList.onItemClickListner mListner;
    public interface onItemClickListner{
        void onItemClick(String category);
    }

}
