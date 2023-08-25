package com.platelet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAppointmentAdapter extends RecyclerView.Adapter<UserAppointmentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<UserAppointmentModule> userAppointmentModuleArrayList;

    OnRecyclerViewClickListener onRecyclerViewClickListener;

    public UserAppointmentAdapter(Context context, ArrayList<UserAppointmentModule> userAppointmentModuleArrayList) {
        this.context = context;
        this.userAppointmentModuleArrayList = userAppointmentModuleArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.user_appointment_listview_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserAppointmentModule userAppointmentModule = userAppointmentModuleArrayList.get(position);

        holder.name.setText(userAppointmentModule.getHospitalName());
        holder.doctorName.setText(userAppointmentModule.getDoctorName());
        holder.department.setText(userAppointmentModule.getDeptName());
        holder.date.setText(userAppointmentModule.getAppointmentDate());
        holder.payment.setText(userAppointmentModule.getPayment());


    }

    @Override
    public int getItemCount() {
        return userAppointmentModuleArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView name, doctorName, department, date, payment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameUALId);
            doctorName = itemView.findViewById(R.id.doctorNameUALId);
            department = itemView.findViewById(R.id.deptUALId);
            date = itemView.findViewById(R.id.dateUALId);
            payment = itemView.findViewById(R.id.paymentUALId);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (onRecyclerViewClickListener != null)
            {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION)
                {
                    onRecyclerViewClickListener.OnRecyclerViewClick(position);
                }
            }

        }
    }

    public interface OnRecyclerViewClickListener{ //first create this interface

        void OnRecyclerViewClick(int position);
    }

    public void setOnRecyclerViewClickListener(OnRecyclerViewClickListener listener) //2nd create this method+you have to set this in the main activity
    {
        onRecyclerViewClickListener = listener;// declare this hospitalClickListener on the top and go to viewholder method to implement
    }
}
