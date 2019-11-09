package com.sketch.securityowner.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daasuu.bl.BubbleLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import com.sketch.securityowner.R;
import com.sketch.securityowner.model.ChatData;

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ChatGroupAdapter extends RecyclerView.Adapter<ChatGroupAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<ChatData> arrayList;


    private onItemClickListner mListner;
    public interface onItemClickListner{
        void onItemClick(ChatData chatData);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView textView1, textView2, textView1Date, textView2Date,
                textView1ImgDate, textView2ImgDate, textView2_name, imageView2_name;
        RelativeLayout relMy1, relMy2, relimgView1, relimgView2;
        LinearLayout linMsg1, linMsg2;
        RoundedImageView imageView11, imageView22;
        //ProgressBar progressBar;
        BubbleLayout bubble_msg1, bubble_image1, bubble_msg2, bubble_image2;

        onItemClickListner listner;

        public ItemViewHolder(View itemView, onItemClickListner listner) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView1Date = itemView.findViewById(R.id.textView1Date);
            textView2Date = itemView.findViewById(R.id.textView2Date);
            textView1ImgDate = itemView.findViewById(R.id.textView1ImgDate);
            textView2ImgDate = itemView.findViewById(R.id.textView2ImgDate);
            textView2_name = itemView.findViewById(R.id.textView2_name);
            imageView2_name = itemView.findViewById(R.id.imageView2_name);

            relMy1 = itemView.findViewById(R.id.relMy1);
            relMy2 = itemView.findViewById(R.id.relMy2);
            relimgView1 = itemView.findViewById(R.id.relimgView1);
            relimgView2 = itemView.findViewById(R.id.relimgView2);
            linMsg1 = itemView.findViewById(R.id.linMsg1);
            linMsg2 = itemView.findViewById(R.id.linMsg2);
            imageView11 = itemView.findViewById(R.id.imageView11);
            imageView22 = itemView.findViewById(R.id.imageView22);


            bubble_msg1 = itemView.findViewById(R.id.bubble_msg1);
            bubble_image1 = itemView.findViewById(R.id.bubble_image1);
            bubble_msg2 = itemView.findViewById(R.id.bubble_msg2);
            bubble_image2 = itemView.findViewById(R.id.bubble_image2);


            this.listner = listner;
        }
    }


    public ChatGroupAdapter(Context context, ArrayList<ChatData> itemList,
                            onItemClickListner mListner){
        this.context = context;
        this.arrayList = itemList;
        this.mListner = mListner;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item_group, parent,false);
        ItemViewHolder dvh = new ItemViewHolder(v, mListner);
        return dvh;
    }


    @Override
    public void onBindViewHolder(final ItemViewHolder viewHolder, final int position) {

        final ChatData chatData = arrayList.get(position);

        //Log.d("TAG" ,"msg- " + chatData.getMessage());
        //Log.d("TAG" ,"msg type- " + chatData.getMessage_type());

        //viewHolder.progressBar.setVisibility(View.GONE);

        if (chatData.getContent() != null
                && !chatData.getContent().equals("null")
                && !chatData.getContent().isEmpty()) {

            if (chatData.isIs_me()) {

                viewHolder.relMy1.setVisibility(View.VISIBLE);
                viewHolder.relMy2.setVisibility(View.GONE);

                viewHolder.textView1.setText(chatData.getContent());
                viewHolder.textView1Date.setText(getShowDateTime(chatData.getDate(),
                        chatData.getTime()));
                viewHolder.linMsg1.setVisibility(View.VISIBLE);
                viewHolder.bubble_msg1.setVisibility(View.VISIBLE);

                viewHolder.relimgView1.setVisibility(View.GONE);
                viewHolder.bubble_image1.setVisibility(View.GONE);

            }else{

                if (chatData.getType().equals("admin")){
                    viewHolder.bubble_msg2.setBubbleColor(context.getResources()
                            .getColor(R.color.bubble_2));

                    viewHolder.textView2_name.setText("Admin");

                }else if (chatData.getType().equals("user")){
                    viewHolder.bubble_msg2.setBubbleColor(context.getResources()
                            .getColor(R.color.bubble_3));

                    viewHolder.textView2_name.setText("Resident");

                }else if (chatData.getType().equals("security")){
                    viewHolder.bubble_msg2.setBubbleColor(context.getResources()
                            .getColor(R.color.bubble_1));

                    viewHolder.textView2_name.setText("Security");

                }




                viewHolder.relMy2.setVisibility(View.VISIBLE);
                viewHolder.relMy1.setVisibility(View.GONE);

                viewHolder.textView2.setText(chatData.getContent());
                viewHolder.textView2Date.setText(getShowDateTime(chatData.getDate(),
                        chatData.getTime()));

                viewHolder.linMsg2.setVisibility(View.VISIBLE);
                viewHolder.bubble_msg2.setVisibility(View.VISIBLE);

                viewHolder.relimgView2.setVisibility(View.GONE);
                viewHolder.bubble_image2.setVisibility(View.GONE);

            }

        }else  if (!chatData.getImage().isEmpty()){

            Log.d("TAG" ,"msg from - " + chatData.getImage_from());

            if (chatData.isIs_me()) {

                viewHolder.relMy1.setVisibility(View.VISIBLE);
                viewHolder.relMy2.setVisibility(View.GONE);
                //viewHolder.progressBar.setVisibility(View.VISIBLE);

                //checking if image is just sent or loaded via api
                if (chatData.getImage_from().matches("web")){

                    viewHolder.imageView11.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    Glide.with(context)
                            .load(chatData.getImage())
                            .placeholder(R.mipmap.profile_image)
                            .into(viewHolder.imageView11);


                }else if (chatData.getImage_from().matches("local")){

                    try {

                        //viewHolder.progressBar.setVisibility(View.GONE);

                        InputStream inputStream = context.getContentResolver()
                                .openInputStream(Uri.fromFile(new File(chatData.getImage())));
                        Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                        if (inputStream != null) inputStream.close();


                        viewHolder.imageView11.setImageURI(Uri.parse(chatData.getImage()));
                        viewHolder.imageView11.setImageBitmap(bmp);

                        viewHolder.imageView11.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                viewHolder.relimgView1.setVisibility(View.VISIBLE);
                viewHolder.bubble_image1.setVisibility(View.VISIBLE);

                viewHolder.linMsg1.setVisibility(View.GONE);
                viewHolder.bubble_msg1.setVisibility(View.GONE);

                viewHolder.textView1ImgDate.setText(getShowDateTime(chatData.getDate(),
                        chatData.getTime()));


            }else {

                if (chatData.getType().equals("admin")){
                    viewHolder.bubble_image2.setBubbleColor(context.getResources()
                            .getColor(R.color.bubble_2));
                    viewHolder.imageView22.setBackgroundColor(context.getResources()
                            .getColor(R.color.bubble_3));

                    viewHolder.imageView2_name.setText("Admin");

                }else if (chatData.getType().equals("user")){
                    viewHolder.bubble_image2.setBubbleColor(context.getResources()
                            .getColor(R.color.bubble_3));
                    viewHolder.imageView22.setBackgroundColor(context.getResources()
                            .getColor(R.color.bubble_3));

                    viewHolder.imageView2_name.setText("Resident");

                }else if (chatData.getType().equals("security")){
                    viewHolder.bubble_image2.setBubbleColor(context.getResources()
                            .getColor(R.color.bubble_1));
                    viewHolder.imageView22.setBackgroundColor(context.getResources()
                            .getColor(R.color.bubble_1));

                    viewHolder.imageView2_name.setText("Security");

                }




                viewHolder.relMy2.setVisibility(View.VISIBLE);
                viewHolder.relMy1.setVisibility(View.GONE);
                //viewHolder.progressBar.setVisibility(View.VISIBLE);

                viewHolder.imageView22.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(context)
                        .load(chatData.getImage())
                        .placeholder(R.mipmap.profile_image)
                        .into(viewHolder.imageView22);


                viewHolder.relimgView2.setVisibility(View.VISIBLE);
                viewHolder.bubble_image2.setVisibility(View.VISIBLE);

                viewHolder.linMsg2.setVisibility(View.GONE);
                viewHolder.bubble_msg2.setVisibility(View.GONE);

                viewHolder.textView2ImgDate.setText(getShowDateTime(chatData.getDate(),
                        chatData.getTime()));
               // viewHolder.imageView2_name.setText(chatData.getSender_name());

            }

        }


/*
        viewHolder.imageView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!chatData.getImage().isEmpty()) {

                    Intent intent = new Intent(context, ChatImageFull.class);
                    intent.putExtra("img", chatData.getImage());
                    intent.putExtra("key", chatData.getImage_from());
                    context.startActivity(intent);
                }

            }
        });
*/

/*
        viewHolder.imageView22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!chatData.getImage().isEmpty()) {

                    Intent intent = new Intent(context, ChatImageFull.class);
                    intent.putExtra("img", chatData.getImage());
                    intent.putExtra("key", chatData.getImage_from());
                    context.startActivity(intent);
                }

            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    private String getShowDateTime(String dates, String times){

        String return_date = "";
        try {

            DateFormat originalFormat =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat =
                    new SimpleDateFormat("dd MMM,yy hh:mm a", Locale.ENGLISH);

            Date date = originalFormat.parse(dates+" "+times);

            //String formattedDate = targetFormat.format(date);

            return_date = targetFormat.format(date);

            //textView.setText("Visiting Time: "+formattedDate);

        }catch (Exception e){
            e.printStackTrace();
        }

        return return_date;

    }


}