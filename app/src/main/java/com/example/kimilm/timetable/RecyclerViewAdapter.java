package com.example.kimilm.timetable;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

//InsertLessonFragment에 사용될 어댑터 클래스
public class RecyclerViewAdapter extends RecyclerView.Adapter<ItemHolder>
{
    private List<Lesson> list;

    public RecyclerViewAdapter(List<Lesson> list) {
        this.list = list;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_item, parent, false);
        return new ItemHolder(root);
    }

    //강의 정보를 변환하여 각 뷰에 세팅
    @Override
    public void onBindViewHolder(final ItemHolder holder, int position)
    {
        Lesson vo = list.get(position);

        holder.code.setText(vo.code);
        holder.title.setText(vo.title);
        holder.classify.setText(vo.classify);
        holder.credit.setText(vo.credit);
        holder.times.setText(vo.times.toString().replace("[", "").replace("]", ""));
        holder.prof.setText(vo.prof);
        holder.classroom.setText(vo.classroom.toString().replace("[", "").replace("]", ""));

        final Lesson lesson = vo;
        final View holderView = holder.view;

        //각 뷰에 온클릭 리스너로 강의를 추가할 수 있게 구현함
        holderView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //강의 추가 여부를 물어보는 스낵바 생성
                Snackbar snackbar = Snackbar.make(v, "강의 추가", Snackbar.LENGTH_INDEFINITE);
                snackbar.getView().setBackgroundColor(v.getResources().getColor(R.color.color8));

                snackbar.setActionTextColor(v.getResources().getColor(R.color.color3))
                        .setAction("강의 추가", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TimeTable.addLesson(lesson))
                        {
                            TimeTable.fragment.showTable(lesson, (byte)1, true);

                            //저장
                            TimeTable.saveTable ();

                            new Thread()
                            {
                                @Override
                                public void run() {
                                    UseDB.uploadTimeTable(MainActivity.isCurAcc, MainActivity.thisFr.getId());
                                }
                            }.start();
                        }
                        else
                        {
                            Toast.makeText(v.getContext(), "강의가 중복됩니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //스낵바가 사라지면 상태 복귀
                snackbar.addCallback(new Snackbar.Callback()
                {
                    @Override
                    public void onShown(Snackbar sb) {
                        holderView.setBackgroundColor(Color.LTGRAY);
                    }

                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        holderView.setBackgroundColor(Color.argb(00, 00, 00, 00));
                    }
                });

                snackbar.show();
            }
        });
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

    View view;

    public ItemHolder(View itemView) {
        super(itemView);

        code = itemView.findViewById(R.id.itemCode);
        title = itemView.findViewById(R.id.itemTitle);
        classify = itemView.findViewById(R.id.itemClassify);
        credit = itemView.findViewById(R.id.itemCredit);
        times = itemView.findViewById(R.id.itemTimes);
        prof = itemView.findViewById(R.id.itemProf);
        classroom = itemView.findViewById(R.id.itemClassroom);

        view = itemView;
    }
}

