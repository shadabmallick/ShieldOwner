
package com.sketch.securityowner.Adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.R;
import com.sketch.securityowner.model.ActivityChild;
import com.sketch.securityowner.model.ChildItem;
import com.sketch.securityowner.model.HeaderItem;
import com.sketch.securityowner.model.ListItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityListAdapterIN extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ListItem> mItems;

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        TextView tv_header;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tv_header = itemView.findViewById(R.id.tv_header);

        }
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profile_image, image_visitor_type;
        TextView tv_name, approved_by, visiting_time, in_premises, visitor_type, tv_allowed_by2;
        ImageView iv_call;
        Switch switch_out;
        RelativeLayout rel_1, rel_2;


        public ChildViewHolder(View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            tv_name = itemView.findViewById(R.id.user_name);
            approved_by = itemView.findViewById(R.id.approved_by);
            visitor_type = itemView.findViewById(R.id.visitor_type);
            visiting_time = itemView.findViewById(R.id.visiting_time);
            in_premises = itemView.findViewById(R.id.in_premises);


        }
    }


    public ActivityListAdapterIN(Context context,
                                 ArrayList<ListItem> mItems){

        this.context = context;
        this.mItems=mItems;

    }


    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == ListItem.TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_layout, parent, false);
            return new HeaderViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_activity, parent, false);
            return new ChildViewHolder(itemView);
        }


    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        int type = getItemViewType(position);
        if (type == ListItem.TYPE_HEADER) {
            HeaderItem header = (HeaderItem) mItems.get(position);
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;

            showDate(holder.tv_header, header.getHeaderName());

        } else {
            ChildItem childItem = (ChildItem) mItems.get(position);
            ChildViewHolder holder = (ChildViewHolder) viewHolder;

            setChildData(holder, childItem.getActivityChild());

        }


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    private void showDate(TextView textView, String s_date){

        try {

            DateFormat originalFormat =
                    new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            DateFormat targetFormat =
                    new SimpleDateFormat("dd MMM", Locale.ENGLISH);

            Date date = originalFormat.parse(s_date);

            String formattedDate = targetFormat.format(date);

            textView.setText(formattedDate);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void setChildData(ChildViewHolder holder,
                              final ActivityChild activityChild){

        try {
            Log.d(AppConfig.TAG, "setChildData: "+activityChild.getProfile_image());
          //  holder.profile_image.setImageResource(R.mipmap.profile_image);
           // holder.image_visitor_type.setImageResource(R.drawable.ic_user);




                if (activityChild.getType().equals("master")){
                    holder.tv_name.setText(activityChild.getName());
                    holder.visitor_type.setText(activityChild.getVisitor_type());
                    holder.visiting_time.setText(activityChild.getVisiting_time());
                    Glide.with(context)
                            .load(activityChild.getProfile_image())
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .into(holder.profile_image);
                    if((activityChild.getActual_in_time()==null)||(activityChild.getActual_in_time().equals(""))){

                        holder.in_premises.setText("In");
                        holder.approved_by.setText("Approved By"+activityChild.getApprove_by());

                    }
                    else {
                        holder.in_premises.setText(activityChild.getVisiting_time()+"-"+activityChild.getActual_out_time());
                        holder.approved_by.setText("Approved By"+activityChild.getApprove_by());


                    }
                }else {
                    holder.tv_name.setText(activityChild.getName()
                           );
                    Glide.with(context)
                            .load(activityChild.getVendor_image())
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .into(holder.profile_image);
                    holder.visitor_type.setText(activityChild.getVisitor_type());
                    holder.visiting_time.setText(activityChild.getVisiting_time());
                    holder.in_premises.setText(activityChild.getVisiting_time());

                }




















/*
            holder.iv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemClickListenerCall.onItemClickCall(activityChild);
                }
            });
*/



        }catch (Exception e){
            e.printStackTrace();
        }



    }





    private void showTime(TextView textView, String sTime){

        try {

            DateFormat originalFormat =
                    new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat =
                    new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

            Date date = originalFormat.parse(sTime);

            String formattedDate = targetFormat.format(date);

            textView.setText("Visiting Time: "+formattedDate);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showActualInTime(TextView textView, String sTime){

        try {

            DateFormat originalFormat =
                    new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat =
                    new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH);

            Date date = originalFormat.parse(sTime);

            String formattedDate = targetFormat.format(date);

            textView.setText("In Time: "+formattedDate);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    ///// in mode ...
    private ItemClickListenerIN mClickListenerIn;
    public void setClickListenerIN(ItemClickListenerIN itemClickListener) {
        this.mClickListenerIn = itemClickListener;
    }

    public interface ItemClickListenerIN {
        void onItemClickIN(ActivityChild activityChild, String in_out);
    }


/// call mode ...

    private ItemClickListenerCall itemClickListenerCall;
    public void setClickListenerCall(ItemClickListenerCall itemClickListener) {
        this.itemClickListenerCall = itemClickListener;
    }

    public interface ItemClickListenerCall {
        void onItemClickCall(ActivityChild activityChild);
    }






}
