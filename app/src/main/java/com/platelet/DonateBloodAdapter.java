package com.platelet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DonateBloodAdapter extends RecyclerView.Adapter<DonateBloodAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DonateBloodModule> donateBloodModuleArrayList;

    OnDonateBloodClickListener donateBloodClickListener;

    public DonateBloodAdapter(Context context, ArrayList<DonateBloodModule> donateBloodModuleArrayList) {
        this.context = context;
        this.donateBloodModuleArrayList = donateBloodModuleArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.donateblood_listviewlayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DonateBloodModule donateBloodModule = donateBloodModuleArrayList.get(position);

        holder.name.setText(donateBloodModule.getUserName());
        holder.email.setText(donateBloodModule.getEmail());
        holder.bag.setText(donateBloodModule.getBagNumber());
        holder.date.setText(donateBloodModule.getDate());
        holder.phone.setText(donateBloodModule.getPhoneNumber());
        holder.location.setText(donateBloodModule.getAddress());
        holder.reason.setText(donateBloodModule.getDescription());

    }

    @Override
    public int getItemCount() {
        return donateBloodModuleArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, email, bag, date, phone, location, reason;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameDBId);
            email = itemView.findViewById(R.id.emailDBId);
            bag = itemView.findViewById(R.id.numberofBBagsDBId);
            date = itemView.findViewById(R.id.dateDBId);
            phone = itemView.findViewById(R.id.phoneDBId);
            location = itemView.findViewById(R.id.locationDBId);
            reason = itemView.findViewById(R.id.reasonDBId);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (donateBloodClickListener != null)
            {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION)
                {
                    donateBloodClickListener.OnDonateBloodClick(position);
                }
            }

        }
    }

    public interface OnDonateBloodClickListener{ //first create this interface

        void OnDonateBloodClick(int position);
    }

    public void setOnDonateBloodClickListener(OnDonateBloodClickListener listener) //2nd create this method+you have to set this in the main activity
    {
        donateBloodClickListener = listener;// declare this hospitalClickListener on the top and go to viewholder method to implement
    }

}
