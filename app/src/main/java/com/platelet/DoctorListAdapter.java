package com.platelet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.Viewholder> { //ViewHolder dile valo but it works with anyname

    private Context context;
    private ArrayList<DoctorListModule> doctorListModuleArrayList;

    OnRecyclerViewClickListener onRecyclerViewClickListener;


    public DoctorListAdapter(Context context, ArrayList<DoctorListModule> doctorListModuleArrayList) {
        this.context = context;
        this.doctorListModuleArrayList = doctorListModuleArrayList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.doctor_listview_layout,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        DoctorListModule doctorListModule = doctorListModuleArrayList.get(position);

        holder.userName.setText(doctorListModule.getUserName());
        holder.information.setText(doctorListModule.getInformation());
        holder.payment.setText(doctorListModule.getPayment());

    }

    @Override
    public int getItemCount() {
        return doctorListModuleArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView userName, information, payment;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.nameDocId);
            information = itemView.findViewById(R.id.informationDocId);
            payment = itemView.findViewById(R.id.paymentDocId);

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
