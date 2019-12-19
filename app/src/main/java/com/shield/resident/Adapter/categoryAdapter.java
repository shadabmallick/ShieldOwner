package com.shield.resident.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shield.resident.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class categoryAdapter  extends RecyclerView.Adapter<categoryAdapter.MyViewHolder> {
    private boolean flagFirstItemSelected = false;

    Context context;
    ArrayList<HashMap<String,String>> blockList;
    int index = -1;
    public categoryAdapter(Context context, ArrayList<HashMap<String,String>> blockList,
                           onItemClickListner mListner) {
        this.blockList = blockList;
        this.context=context;
        this.mListner=mListner;
    }

    @Override
    public categoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_company, parent, false);
        categoryAdapter.MyViewHolder vh = new categoryAdapter.MyViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(categoryAdapter.MyViewHolder holder, int position) {

        Picasso.with(context).load(blockList.get(position).get("icon")).
                fit().into(holder.cab_image);


      // holder.cab_text.setText(blockList.get(position).get("company"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                notifyDataSetChanged();

                String company=blockList.get(position).get("company");

                mListner.onItemClick(company);

            }
        });

        if(index==position){
            //holder.img_check.setVisibility(View.VISIBLE);

            holder.linear_selection.setBackground(context.getResources().getDrawable(R.drawable.curve_green));
        }else{
            //holder.img_check.setVisibility(View.GONE);
            holder.linear_selection.setBackground(null);

        }


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return blockList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView cab_text;
        ImageView cab_image,img_check;
        LinearLayout linear_selection;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
         //   cab_text =  itemView.findViewById(R.id.cab_text);
            cab_image =  itemView.findViewById(R.id.cab_image);
            img_check =  itemView.findViewById(R.id.img_check);
            linear_selection =  itemView.findViewById(R.id.linear_selection);


        }
    }
    private onItemClickListner mListner;
    public interface onItemClickListner{
        void onItemClick(String category);
    }

}
