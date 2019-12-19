package com.shield.resident.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.shield.resident.R;
import com.shield.resident.model.HeaderItem;
import com.shield.resident.model.ListItem;
import com.shield.resident.model.Owner;
import com.shield.resident.model.OwnerItem;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class OwnerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ListItem> mItems;


    public OwnerListAdapter(Context context, ArrayList<ListItem> itemList) {
        this.context = context;
        this.mItems = itemList;

    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        TextView tv_header;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tv_header = itemView.findViewById(R.id.tv_header);

        }
    }

    public class OwnerViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_flat_name;
        private CircleImageView profile_image;
        private ImageView iv_call;


        public OwnerViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_flat_name = view.findViewById(R.id.tv_flat_name);
            profile_image = view.findViewById(R.id.profile_image);
            iv_call = view.findViewById(R.id.iv_call);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@Nullable ViewGroup parent, int viewType) {

        if (viewType == ListItem.TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_item, parent, false);
            return new HeaderViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.owner_listitem, parent, false);
            return new OwnerViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(@Nullable RecyclerView.ViewHolder viewHolder,
                                 final int position) {

        int type = getItemViewType(position);
        if (type == ListItem.TYPE_HEADER) {
            HeaderItem header = (HeaderItem) mItems.get(position);
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;

            holder.tv_header.setText("Floor: "+header.getHeaderName());

        } else {
            OwnerItem childItem = (OwnerItem) mItems.get(position);
            OwnerViewHolder holder = (OwnerViewHolder) viewHolder;


            setOwnerData(holder, childItem.getOwner());
        }


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }


    private void setOwnerData(OwnerViewHolder holder, Owner ownerData){

        holder.tv_name.setText(ownerData.getName());
        holder.tv_flat_name.setText("Flat: "+ownerData.getFlat_no());

        if (ownerData.getPhone_show().equals("1")){
            holder.iv_call.setVisibility(View.VISIBLE);
        }else {
            holder.iv_call.setVisibility(View.GONE);
        }

        Glide.with(context).load(ownerData.getImage())
                .into(holder.profile_image);

        holder.iv_call.setOnClickListener(v -> {

            mViewClickListener.onCallClicked(ownerData);
        });

    }


    /////////////

    private ViewClickListener mViewClickListener;

    public interface ViewClickListener {
        void onCallClicked(Owner owner);
    }

    public void setViewClickListener (ViewClickListener viewClickListener) {
        mViewClickListener = viewClickListener;
    }
}