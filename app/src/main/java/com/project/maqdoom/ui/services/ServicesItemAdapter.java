package com.project.maqdoom.ui.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.project.maqdoom.R;

import java.util.ArrayList;
import java.util.List;

public class ServicesItemAdapter extends RecyclerView.Adapter<ServicesItemAdapter.FruitHolder>{

    Context context;
    List<ServicesChecklistItems> servicesChecklistItemsList = new ArrayList<>();

    private ServicesItemAdapter.onItemclickListener mListener;
    public interface onItemclickListener {
        void onItemclickListener(View view,int position,String data);
    }
    public void setOnItemClickListener(ServicesItemAdapter.onItemclickListener mListener){
        this.mListener = mListener;
    }

    public ServicesItemAdapter(Context context, List<ServicesChecklistItems> list) {
        this.context = context;
        this.servicesChecklistItemsList = list;
    }

    @Override
    public FruitHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(context).inflate(R.layout.dialog_services_items,parent,false);

        return new FruitHolder(view);
    }

    @Override
    public void onBindViewHolder(final FruitHolder holder, final int position) {

        final ServicesChecklistItems servicesChecklistItems = servicesChecklistItemsList.get(position);

        holder.tv_name.setText(servicesChecklistItems.getName());

        holder.checkBox.setChecked(servicesChecklistItems.isSelected());
        holder.checkBox.setTag(servicesChecklistItemsList.get(position));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onItemclickListener(view,position,servicesChecklistItemsList.get(position).name);
            }

        });
    }

    @Override
    public int getItemCount() {
        return servicesChecklistItemsList.size();
    }

    public static class FruitHolder extends RecyclerView.ViewHolder{

        TextView tv_name;
        CheckBox checkBox;

        public FruitHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_fruit_price);
            checkBox = itemView.findViewById(R.id.checkBox_select);
        }
    }

    public List<ServicesChecklistItems> getFruitsList(){
        return servicesChecklistItemsList;
    }
}
