package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    private ArrayList<String> arrayList = new ArrayList<String>();


    public NoticeAdapter(ArrayList<String> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.notice_list_item,parent,false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        String title = arrayList.get(position);
        holder.txtData.setText(title);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder {
        TextView txtData;
        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtData = (TextView) itemView.findViewById(R.id.noticeDatatoshow);
        }
    }
}
