package com.platelet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HospitalListAdapter extends RecyclerView.Adapter<HospitalListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HospitalListModule> hospitalListModuleArrayList;

    OnHospitalClickListener hospitalClickListener;



    public HospitalListAdapter(Context context, ArrayList<HospitalListModule> hospitalListModuleArrayList) {
        this.context = context;
        this.hospitalListModuleArrayList = hospitalListModuleArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.hospital_listview_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HospitalListModule hospitalListModule = hospitalListModuleArrayList.get(position);

        holder.hospitalName.setText(hospitalListModule.getUserName());

    }

    @Override
    public int getItemCount() {
        return hospitalListModuleArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView hospitalName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hospitalName = itemView.findViewById(R.id.nameHospitalLLId);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (hospitalClickListener != null)
            {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION)
                {
                    hospitalClickListener.OnHospitalClick(position);
                }
            }

        }
    }




    public interface OnHospitalClickListener{ //first create this interface

        void OnHospitalClick(int position);
    }

public void setOnHospitalClickListener(OnHospitalClickListener listener) //2nd create this method+you have to set this in the main activity
{
    hospitalClickListener = listener;// declare this hospitalClickListener on the top and go to viewholder method to implement
}

}
