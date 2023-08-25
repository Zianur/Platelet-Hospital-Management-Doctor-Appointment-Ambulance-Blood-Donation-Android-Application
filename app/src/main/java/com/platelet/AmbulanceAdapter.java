package com.platelet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AmbulanceAdapter extends RecyclerView.Adapter<AmbulanceAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AmbulanceModule> ambulanceModuleArrayList;

    OnAmbulanceClickListener onAmbulanceClickListener;

    public AmbulanceAdapter(Context context, ArrayList<AmbulanceModule> ambulanceModuleArrayList) {
        this.context = context;
        this.ambulanceModuleArrayList = ambulanceModuleArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.ambulance_recyclerview_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AmbulanceModule ambulanceModule = ambulanceModuleArrayList.get(position);

        holder.name.setText(ambulanceModule.getUserName());
        holder.phone.setText(ambulanceModule.getPhoneNumber());

    }

    @Override
    public int getItemCount() {
        return ambulanceModuleArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, phone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameALLId);
            phone = itemView.findViewById(R.id.phoneALLId);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (onAmbulanceClickListener != null)
            {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION)
                {
                    onAmbulanceClickListener.OnAmbulanceClick(position);
                }
            }

        }
    }

    public interface OnAmbulanceClickListener{ //first create this interface

        void OnAmbulanceClick(int position);
    }

    public void setOnAmbulanceClickListener(OnAmbulanceClickListener listener) //2nd create this method+you have to set this in the main activity
    {
        onAmbulanceClickListener = listener;// declare this hospitalClickListener on the top and go to viewholder method to implement
    }
}
