package com.platelet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HospitalAppointmentAdapter extends RecyclerView.Adapter<HospitalAppointmentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HospitalAppointmentModule> hospitalAppointmentModuleArrayList;

    OnRecyclerViewClickListener onRecyclerViewClickListener;

    public HospitalAppointmentAdapter(Context context, ArrayList<HospitalAppointmentModule> hospitalAppointmentModuleArrayList) {
        this.context = context;
        this.hospitalAppointmentModuleArrayList = hospitalAppointmentModuleArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.hospital_appointment_listview_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HospitalAppointmentModule hospitalAppointmentModule = hospitalAppointmentModuleArrayList.get(position);

        holder.name.setText(hospitalAppointmentModule.getPatientName());
        holder.email.setText(hospitalAppointmentModule.getEmail());
        holder.doctorName.setText(hospitalAppointmentModule.getDoctorName());
        holder.department.setText(hospitalAppointmentModule.getDeptName());
        holder.date.setText(hospitalAppointmentModule.getAppointmentDate());
        holder.payment.setText(hospitalAppointmentModule.getPayment());
        holder.phone.setText(hospitalAppointmentModule.getPatientPhoneNumber());

    }

    @Override
    public int getItemCount() {
        return hospitalAppointmentModuleArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, email, doctorName, department, date, payment, phone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameUALId);
            email = itemView.findViewById(R.id.emailUALId);
            doctorName = itemView.findViewById(R.id.doctorNameUALId);
            department = itemView.findViewById(R.id.deptUALId);
            date = itemView.findViewById(R.id.dateUALId);
            payment = itemView.findViewById(R.id.paymentUALId);
            phone = itemView.findViewById(R.id.phoneUALId);

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
