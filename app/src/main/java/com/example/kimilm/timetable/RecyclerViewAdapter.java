package com.example.kimilm.timetable;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

//어댑터 클래스
public class RecyclerViewAdapter extends RecyclerView.Adapter<ItemHolder>  {
    private List<Lesson> list;

    public RecyclerViewAdapter(List<Lesson> list) {
        this.list = list;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_item, parent, false);
        return new ItemHolder(root);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Lesson vo = list.get(position);

        holder.code.setText(vo.code);
        holder.title.setText(vo.title);
        holder.classify.setText(vo.classify);
        holder.credit.setText(vo.credit);
        holder.times.setText(vo.times.toString().replace("[", "").replace("]", ""));
        holder.prof.setText(vo.prof);
        holder.classroom.setText(vo.classroom.toString().replace("[", "").replace("]", ""));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

//ViewHolder 클래스 선언
class ItemHolder extends RecyclerView.ViewHolder{

    TextView code;
    TextView title;
    TextView classify;
    TextView credit;
    TextView times;
    TextView prof;
    TextView classroom;

    public ItemHolder(View itemView) {
        super(itemView);

        code = itemView.findViewById(R.id.itemCode);
        title = itemView.findViewById(R.id.itemTitle);
        classify = itemView.findViewById(R.id.itemClassify);
        credit = itemView.findViewById(R.id.itemCredit);
        times = itemView.findViewById(R.id.itemTimes);
        prof = itemView.findViewById(R.id.itemProf);
        classroom = itemView.findViewById(R.id.itemClassroom);


    }
}

