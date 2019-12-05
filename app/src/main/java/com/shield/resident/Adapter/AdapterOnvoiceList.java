package com.shield.resident.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterOnvoiceList extends RecyclerView.Adapter<AdapterOnvoiceList.MyViewHolder> {


    private Context context;
    private ArrayList<HashMap<String,String>> hashMapArrayList;
    private int index = -1;
    private GlobalClass globalClass;

    public AdapterOnvoiceList(Context context, ArrayList<HashMap<String,
            String>> cityList, AdapterOnvoiceList.onItemClickListner mListner) {
        this.hashMapArrayList = cityList;
        this.context=context;
        this.mListner=mListner;
        globalClass = ((GlobalClass) context.getApplicationContext());
    }

    @Override
    public AdapterOnvoiceList.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_invoice, parent, false);
        AdapterOnvoiceList.MyViewHolder vh = new AdapterOnvoiceList.MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final AdapterOnvoiceList.MyViewHolder holder, final int position) {

       holder.tv_amout.setText("Billing Amount: "+
               hashMapArrayList.get(position).get("billing_amount"));

       holder.tv_date1.setText(hashMapArrayList.get(position).get("invoice_name"));
       holder.tv_date2.setText("Date: "+hashMapArrayList.get(position).get("date"));

       holder.tv_invoice_no.setText("Invoice No: "
               +hashMapArrayList.get(position).get("invoice_no"));

       // holder.status.setText(hashMapArrayList.get(position).get("invoicestatus"));


        String status = hashMapArrayList.get(position).get("status");
        if (status.equals("paid")){
            holder.tv_pay.setVisibility(View.INVISIBLE);

            holder.status.setText("Paid");

        }else if (status.equals("unpaid")){
            holder.tv_pay.setVisibility(View.VISIBLE);

            holder.status.setText("Unpaid");

        }else if (status.equals("partial_paid")){
            holder.tv_pay.setVisibility(View.VISIBLE);

            holder.status.setText("Partial paid");

        }


        holder.invoice.setOnClickListener(v -> {

            index = position;
            notifyDataSetChanged();
            String invoice_link= hashMapArrayList.get(position).get("invoice_link");

            mListner.onItemClick(invoice_link);
        });

        holder.tv_pay.setOnClickListener(v -> {

            mListner.onClickForPay(hashMapArrayList.get(position));

        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return hashMapArrayList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView tv_amout, tv_date1, tv_date2, tv_number, status, tv_pay, tv_invoice_no;
        ImageView profile_image, iv_delete;
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
            tv_pay =  itemView.findViewById(R.id.tv_pay);
            tv_invoice_no =  itemView.findViewById(R.id.tv_invoice_no);

            profile_image =  itemView.findViewById(R.id.profile_image);



        }
    }
    private AdapterOnvoiceList.onItemClickListner mListner;
    public interface onItemClickListner{
        void onItemClick(String category);
        void onClickForPay(HashMap<String, String> hashMap);
    }




}
