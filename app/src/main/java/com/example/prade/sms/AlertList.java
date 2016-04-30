package com.example.prade.sms;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class AlertList extends RecyclerView.Adapter<AlertList.MyViewHolder>{

    List<Person> personData = Collections.emptyList();
    private LayoutInflater inflater;
    public AlertList(Context context, List<Person> personData){
        this.personData = personData;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.person_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Person current = personData.get(position);
        holder.contactName.setText(current.getName());
        holder.contentNmber.setText(current.getnumber());
    }



    @Override
    public int getItemCount() {
        return personData.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView contactName, contentNmber;

        public MyViewHolder(View itemView) {
            super(itemView);

            contactName = (TextView) itemView.findViewById(R.id.contactName);
            contentNmber = (TextView) itemView.findViewById(R.id.contactNumber);

        }
    }

}
