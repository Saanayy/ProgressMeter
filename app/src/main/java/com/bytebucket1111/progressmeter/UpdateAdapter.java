package com.bytebucket1111.progressmeter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bytebucket1111.progressmeter.modal.Update;

import java.util.ArrayList;

public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.MyViewHolder2> {
    Context context;
    ArrayList<Update> updates;

    public UpdateAdapter(Context context, ArrayList<Update> updates) {
        this.context = context;
        this.updates = updates;
    }


    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.updateproject, viewGroup, false);
        return new MyViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder2 myViewHolder2, int i) {
        Update update = updates.get(i);
        myViewHolder2.tvName.setText(update.getTitle());
        myViewHolder2.tvDate.setText(update.getDate());
        myViewHolder2.tvDesc.setText(update.getUserweather());
        if(!update.isResolved())
            myViewHolder2.iv1.setImageResource(R.drawable.ic_notconflict);
        else
        {
            if(update.isConflict())
                myViewHolder2.iv1.setImageResource(R.drawable.ic_notresolved);
            else
                myViewHolder2.iv1.setImageResource(R.drawable.ic_resolved);
        }
    }

    @Override
    public int getItemCount() {
        return updates.size();
    }


    class MyViewHolder2 extends RecyclerView.ViewHolder {

        TextView tvName, tvDate, tvDesc;
        ImageView iv1, iv2;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.update_item_name);
            tvDate = itemView.findViewById(R.id.update_item_date);
            tvDesc = itemView.findViewById(R.id.update_item_desc);
            iv1 = itemView.findViewById(R.id.update_item_iv1);

        }
    }
}
