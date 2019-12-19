package com.shield.resident.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shield.resident.R;
import com.shield.resident.model.Contact;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    private Context context;
    private ArrayList<Contact> contactList;
    private ArrayList<Contact> contactListFiltered;


    public ContactListAdapter(Context context, ArrayList<Contact> itemList) {
        this.context = context;
        this.contactListFiltered = itemList;
        this.contactList = itemList;

    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_number;
        private CircleImageView profile_image;


        public ContactHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_number = view.findViewById(R.id.tv_number);
            profile_image = view.findViewById(R.id.profile_image);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@Nullable ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item, parent, false);
        return new ContactHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@Nullable RecyclerView.ViewHolder viewHolder, int position) {
        ContactHolder holder = (ContactHolder) viewHolder;
        Contact contact = contactListFiltered.get(position);



        holder.tv_name.setText(contact.getName());
        holder.tv_number.setText(contact.getMobile());

        Glide.with(context)
                .load(contact.getImage())
                .into(holder.profile_image);


        holder.itemView.setOnClickListener(v -> {

            mViewClickListener.onItemClicked(contact);
        });

    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    ArrayList<Contact> filteredList = new ArrayList<>();
                    for (Contact row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                || row.getMobile().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Contact contact);
    }

    /////////////

    private ViewClickListener mViewClickListener;

    public interface ViewClickListener {
        void onItemClicked(Contact contact);
    }

    public void setViewClickListener (ViewClickListener viewClickListener) {
        mViewClickListener = viewClickListener;
    }
}