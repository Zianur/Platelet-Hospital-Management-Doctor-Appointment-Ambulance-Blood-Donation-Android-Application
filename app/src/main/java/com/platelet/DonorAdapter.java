package com.platelet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DonorModuleCLass> donorModuleCLassArrayList;

    OnDonorClickListener onDonorClickListener;

    public DonorAdapter(Context context, ArrayList<DonorModuleCLass> donorModuleCLassArrayList) {
        this.context = context;
        this.donorModuleCLassArrayList = donorModuleCLassArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.donor_listview_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DonorModuleCLass donorModuleCLass = donorModuleCLassArrayList.get(position);


        holder.name.setText(donorModuleCLass.getUserName());
        holder.phone.setText(donorModuleCLass.getPhoneNumber());
        holder.numberofdonation.setText(donorModuleCLass.getNumberofDonation());
        holder.status.setText(donorModuleCLass.getStatus());

    }

    @Override
    public int getItemCount() {
        return donorModuleCLassArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, phone, numberofdonation, status;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameDLLId);
            phone = itemView.findViewById(R.id.phoneDLLId);
            numberofdonation = itemView.findViewById(R.id.donationNumberDLLId);
            status = itemView.findViewById(R.id.statusDLLId);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (onDonorClickListener != null)
            {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION)
                {
                    onDonorClickListener.OnDonorClickListener(position);
                }
            }

        }
    }


    public interface OnDonorClickListener{ //first create this interface

        void OnDonorClickListener(int position);
    }

    public void setOnDonorClickListenerClickListener(OnDonorClickListener listener) //2nd create this method+you have to set this in the main activity
    {
        onDonorClickListener = listener;// declare this hospitalClickListener on the top and go to viewholder method to implement
    }
}
