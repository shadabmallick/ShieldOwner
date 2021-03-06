
package com.shield.resident.Adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.R;
import com.shield.resident.model.ActivityChild;
import com.shield.resident.model.ChildItem;
import com.shield.resident.model.HeaderItem;
import com.shield.resident.model.ListItem;

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
        CircleImageView profile_image, vendor_image;
        TextView user_name, visitor_type_name, tv_vendor_name,
                visiting_time, approved_by, tv_lag_code;
        ImageView iv_visitor_type, edit, iv_visiting_time, iv_main_icon;
        LinearLayout ll_buttons, ll_in, ll_out, ll_leaveat_gate, ll_vendors;
        TextView tv_accept, tv_reject, tv_leave_at_gate, tv_gatepass;

        public ChildViewHolder(View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            vendor_image = itemView.findViewById(R.id.vendor_image);
            user_name = itemView.findViewById(R.id.user_name);
            visitor_type_name = itemView.findViewById(R.id.visitor_type_name);
            tv_vendor_name = itemView.findViewById(R.id.tv_vendor_name);
            visiting_time = itemView.findViewById(R.id.visiting_time);
            approved_by = itemView.findViewById(R.id.approved_by);
            tv_lag_code = itemView.findViewById(R.id.tv_lag_code);
            iv_visitor_type = itemView.findViewById(R.id.iv_visitor_type);
            edit = itemView.findViewById(R.id.edit);
            ll_buttons = itemView.findViewById(R.id.ll_buttons);
            ll_in = itemView.findViewById(R.id.ll_in);
            ll_out = itemView.findViewById(R.id.ll_out);
            ll_leaveat_gate = itemView.findViewById(R.id.ll_leaveat_gate);
            tv_accept = itemView.findViewById(R.id.tv_accept);
            tv_reject = itemView.findViewById(R.id.tv_reject);
            tv_leave_at_gate = itemView.findViewById(R.id.tv_leave_at_gate);
            ll_vendors = itemView.findViewById(R.id.ll_vendors);
            tv_gatepass = itemView.findViewById(R.id.tv_gatepass);
            iv_visiting_time = itemView.findViewById(R.id.iv_visiting_time);
            iv_main_icon = itemView.findViewById(R.id.iv_main_icon);


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


    private void setChildData(ChildViewHolder holder, final ActivityChild activityChild){

        try {

            //Log.d(AppConfig.TAG, "setChildData: "+activityChild.getProfile_image());

            holder.tv_gatepass.setVisibility(View.GONE);

                if (activityChild.getType().equals("master")){

                    showTime(holder.visiting_time, activityChild.getVisiting_time());

                    holder.iv_visiting_time.setImageResource(R.drawable.ic_clock);

                    holder.ll_vendors.setVisibility(View.VISIBLE);

                    if (activityChild.getNote().equals("")
                            || activityChild.getNote().equals("0")
                            ||  activityChild.getNote().equals("null")
                            ||  activityChild.getNote().equals(null)
                    ){
                        holder.user_name.setText(activityChild.getName());
                    }else {
                        holder.user_name.setText(activityChild.getName()
                                + " + " +activityChild.getNote());
                    }


                    Glide.with(context)
                            .load(activityChild.getProfile_image())
                            .into(holder.profile_image);

                    Glide.with(context)
                            .load(activityChild.getVendor_image())
                            .centerCrop()
                            .placeholder(R.drawable.ic_user_black)
                            .into(holder.vendor_image);


                    if (activityChild.getVisitor_type().equals(AppConfig.guest)){

                        holder.visitor_type_name.setText("Guest");
                        holder.ll_vendors.setVisibility(View.GONE);

                        holder.iv_visitor_type.setImageResource(R.mipmap.guest_white);

                    }else  if (activityChild.getVisitor_type().equals(AppConfig.delivery)){

                        holder.visitor_type_name.setText("Delivery");

                        holder.tv_vendor_name.setText(activityChild.getVendor_name());

                        holder.iv_visitor_type.setImageResource(R.mipmap.delivery_man_white);

                    }else  if (activityChild.getVisitor_type().equals(AppConfig.staff)){

                        holder.ll_vendors.setVisibility(View.GONE);

                        holder.visitor_type_name.setText("Staff");

                        holder.tv_vendor_name.setText(activityChild.getVendor_name());

                        holder.iv_visitor_type.setImageResource(R.drawable.ic_user);

                    } else  if (activityChild.getVisitor_type().equals(AppConfig.cab)){

                        holder.visitor_type_name.setText("Cab");

                        holder.tv_vendor_name.setText(activityChild.getVendor_name());

                        holder.iv_visitor_type.setImageResource(R.mipmap.cab_white);

                    }else  if (activityChild.getVisitor_type().equals(AppConfig.visiting_help)){

                        holder.visitor_type_name.setText("Visiting Help");

                        holder.tv_vendor_name.setText(activityChild.getVisiting_help_cat());

                        holder.iv_visitor_type.setImageResource(R.mipmap.visiting_help_white);
                    }


                    holder.ll_out.setVisibility(View.GONE);
                    holder.ll_in.setVisibility(View.GONE);

                    if((activityChild.getActual_in_time() == null)
                            ||(activityChild.getActual_in_time().equals("null"))
                            ||(activityChild.getActual_in_time().equals(""))
                    ){

                        holder.ll_in.setVisibility(View.GONE);
                        holder.ll_out.setVisibility(View.GONE);
                        holder.ll_leaveat_gate.setVisibility(View.GONE);


                    }else if (((activityChild.getActual_in_time() != null)
                            || (!activityChild.getActual_in_time().equals("null"))
                            || (!activityChild.getActual_in_time().equals("")))

                            && ((activityChild.getActual_out_time() == null)
                            || (activityChild.getActual_out_time().equals("null"))
                            || (activityChild.getActual_out_time().equals("")))

                    ){

                        if (activityChild.getApprove_status().equals("n")){

                            holder.ll_in.setVisibility(View.GONE);
                            //holder.ll_out.setVisibility(View.VISIBLE);

                        }else {

                            holder.ll_in.setVisibility(View.VISIBLE);
                            //holder.ll_out.setVisibility(View.GONE);
                        }


                        holder.ll_leaveat_gate.setVisibility(View.GONE);

                        holder.iv_visiting_time.setImageResource(R.drawable.ic_log_in);

                        showActualInTime(holder.visiting_time, activityChild.getActual_in_time());


                    }else if(activityChild.getActual_out_time() != null
                            || !activityChild.getActual_out_time().equals("null")
                            || !activityChild.getActual_out_time().equals("")
                    ){

                        holder.ll_in.setVisibility(View.GONE);
                        //holder.ll_out.setVisibility(View.VISIBLE);
                        holder.ll_leaveat_gate.setVisibility(View.GONE);

                        holder.iv_visiting_time.setImageResource(R.drawable.ic_log_out);

                        showActualInTime(holder.visiting_time, activityChild.getActual_out_time());

                    }



                    if (activityChild.getApprove_status().equals("l")){
                        holder.ll_leaveat_gate.setVisibility(View.VISIBLE);
                        holder.tv_lag_code.setText(activityChild.getLeave_at_gate_code());
                    }else {
                        holder.ll_leaveat_gate.setVisibility(View.GONE);
                    }


                    if (activityChild.getApprove_status().equals("w")){

                        holder.ll_in.setVisibility(View.GONE);
                        holder.ll_out.setVisibility(View.GONE);

                        holder.ll_buttons.setVisibility(View.VISIBLE);
                        holder.tv_accept.setVisibility(View.VISIBLE);
                        holder.tv_reject.setVisibility(View.VISIBLE);

                        if (activityChild.getVisitor_type().equals(AppConfig.delivery)){
                            holder.tv_leave_at_gate.setVisibility(View.VISIBLE);
                        }else {
                            holder.tv_leave_at_gate.setVisibility(View.GONE);
                        }

                    }else {

                       holder.ll_buttons.setVisibility(View.GONE);
                    }




                    if (activityChild.getApprove_status().equals("new")){
                        holder.approved_by.setVisibility(View.GONE);
                    }else {
                        holder.approved_by.setVisibility(View.VISIBLE);
                    }


                    if (activityChild.getApprove_status().equals("y")
                     || activityChild.getApprove_status().equals("l")){

                        holder.approved_by.setText("Approved by "+activityChild.getApprove_by());

                    } else if(activityChild.getApprove_status().equals("sr")
                            || activityChild.getApprove_status().equals("n")){

                        holder.approved_by.setText("Rejected by "+activityChild.getApprove_by());

                        holder.ll_in.setVisibility(View.GONE);
                        //holder.ll_out.setVisibility(View.VISIBLE);
                        holder.ll_leaveat_gate.setVisibility(View.GONE);

                        holder.iv_visiting_time.setImageResource(R.drawable.ic_log_out);

                    }else {
                        holder.approved_by.setText("");
                    }


                    if (activityChild.getGetpass().equals("1")){
                        holder.tv_gatepass.setVisibility(View.VISIBLE);
                    }


                    holder.ll_leaveat_gate.setOnClickListener(v -> {


                    });


                    if (!activityChild.getApprove_status().equals("w")){
                        holder.itemView.setOnClickListener(v -> {
                            mClickListenerIn.onItemClickIN(activityChild);
                        });
                    }else {

                        holder.itemView.setOnClickListener(null);
                    }


                    holder.tv_accept.setOnClickListener(v -> {

                        itemClickListenerStatusUpdate.onItemClickStatusUpdate(activityChild, "y");

                    });

                    holder.tv_reject.setOnClickListener(v -> {

                        itemClickListenerStatusUpdate.onItemClickStatusUpdate(activityChild, "n");

                    });

                    holder.tv_leave_at_gate.setOnClickListener(v -> {

                        itemClickListenerStatusUpdate.onItemClickStatusUpdate(activityChild, "l");

                    });



                }else {
                    /// temp

                    holder.ll_in.setVisibility(View.GONE);
                    holder.ll_out.setVisibility(View.GONE);
                    holder.ll_leaveat_gate.setVisibility(View.GONE);
                    holder.ll_buttons.setVisibility(View.GONE);
                    holder.ll_vendors.setVisibility(View.VISIBLE);

                    holder.iv_visiting_time.setImageResource(R.drawable.ic_clock);


                    if (!activityChild.getName().isEmpty()){
                        holder.user_name.setText(activityChild.getName());
                    }else {
                        holder.user_name.setVisibility(View.GONE);
                    }

                    Glide.with(context)
                            .load(activityChild.getProfile_image())
                            .into(holder.profile_image);


                    Glide.with(context)
                            .load(activityChild.getVendor_image())
                            .centerCrop()
                            .placeholder(R.drawable.ic_user_black)
                            .into(holder.vendor_image);

                    if (activityChild.getVisitor_type().equals(AppConfig.guest)){

                        holder.visitor_type_name.setText("Guest");
                        holder.ll_vendors.setVisibility(View.GONE);
                        holder.iv_visitor_type.setImageResource(R.mipmap.guest_white);
                        holder.iv_main_icon.setImageResource(R.mipmap.guest_white);

                    }else  if (activityChild.getVisitor_type().equals(AppConfig.delivery)){

                        holder.visitor_type_name.setText("Delivery");

                        holder.tv_vendor_name.setText(activityChild.getVendor_name());

                        holder.iv_visitor_type.setImageResource(R.mipmap.delivery_man_white);
                        holder.iv_main_icon.setImageResource(R.mipmap.delivery_man_white);

                    }else  if (activityChild.getVisitor_type().equals(AppConfig.staff)){

                        holder.ll_vendors.setVisibility(View.GONE);

                        holder.visitor_type_name.setText("Staff");

                        holder.tv_vendor_name.setText(activityChild.getVendor_name());

                        holder.iv_visitor_type.setImageResource(R.drawable.ic_user);
                        holder.iv_main_icon.setImageResource(R.drawable.ic_user);

                    } else  if (activityChild.getVisitor_type().equals(AppConfig.cab)){

                        holder.visitor_type_name.setText("Cab");

                        holder.tv_vendor_name.setText(activityChild.getVendor_name());

                        holder.iv_visitor_type.setImageResource(R.mipmap.cab_white);
                        holder.iv_main_icon.setImageResource(R.mipmap.cab_white);

                    }else  if (activityChild.getVisitor_type().equals(AppConfig.visiting_help)){

                        holder.visitor_type_name.setText("Visiting Help");

                        holder.tv_vendor_name.setText(activityChild.getVisiting_help_cat());

                        holder.iv_visitor_type.setImageResource(R.mipmap.visiting_help_white);
                        holder.iv_main_icon.setImageResource(R.mipmap.visiting_help_white);

                    }


                    if (activityChild.getApprove_status().equals("new")){
                        holder.approved_by.setVisibility(View.GONE);
                    }else {
                        holder.approved_by.setVisibility(View.VISIBLE);
                    }


                    if (activityChild.getApprove_status() != null
                            && !activityChild.getApprove_status().equals("null")
                            && (activityChild.getApprove_status().equals("y")
                            || activityChild.getApprove_status().equals("l"))){

                        holder.approved_by.setText("Approved by "+activityChild.getApprove_by());

                    } else if(activityChild.getApprove_status().equals("sr")){

                        holder.approved_by.setText("Rejected by "+activityChild.getApprove_by());

                    } else {
                        holder.approved_by.setVisibility(View.GONE);
                    }



                    if (activityChild.getApprove_status().equals("w")){

                        holder.ll_buttons.setVisibility(View.VISIBLE);
                        holder.tv_accept.setVisibility(View.VISIBLE);
                        holder.tv_reject.setVisibility(View.VISIBLE);

                        if (activityChild.getVisitor_type().equals(AppConfig.delivery)){
                            holder.tv_leave_at_gate.setVisibility(View.VISIBLE);
                        }else {
                            holder.tv_leave_at_gate.setVisibility(View.GONE);
                        }

                    }else {

                        holder.ll_buttons.setVisibility(View.GONE);
                    }



                    if (!activityChild.getApprove_status().equals("w")){

                        holder.itemView.setOnClickListener(v -> {
                            mClickListenerIn.onItemClickIN(activityChild);
                        });

                    }else {

                        holder.itemView.setOnClickListener(null);
                    }


                    holder.tv_accept.setOnClickListener(v -> {

                        itemClickListenerStatusUpdate.onItemClickStatusUpdate(activityChild, "y");

                    });

                    holder.tv_reject.setOnClickListener(v -> {

                        itemClickListenerStatusUpdate.onItemClickStatusUpdate(activityChild, "n");

                    });

                    holder.tv_leave_at_gate.setOnClickListener(v -> {

                        itemClickListenerStatusUpdate.onItemClickStatusUpdate(activityChild, "l");

                    });


                    showTime(holder.visiting_time, activityChild.getVisiting_time());
                }



        }catch (Exception e){
            e.printStackTrace();
        }



    }



    private void showTime(TextView textView, String sTime){

        try {

            DateFormat originalFormat =
                    new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat =
                    new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

            Date date = originalFormat.parse(sTime);

            String formattedDate = targetFormat.format(date);

            textView.setText(formattedDate);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void showActualInTime(TextView textView, String sTime){

        try {

            DateFormat originalFormat =
                    new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat =
                    new SimpleDateFormat(" hh:mm a", Locale.ENGLISH);

            Date date = originalFormat.parse(sTime);

            String formattedDate = targetFormat.format(date);

            textView.setText(formattedDate);

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
        void onItemClickIN(ActivityChild activityChild);
    }


/// call mode ...

    private ItemClickListenerStatusUpdate itemClickListenerStatusUpdate;
    public void setClickListenerStatusUpdate(ItemClickListenerStatusUpdate itemClickListener) {
        this.itemClickListenerStatusUpdate = itemClickListener;
    }

    public interface ItemClickListenerStatusUpdate {
        void onItemClickStatusUpdate(ActivityChild activityChild, String status);
    }






}
