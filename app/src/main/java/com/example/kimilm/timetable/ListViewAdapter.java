package com.example.kimilm.timetable;

import android.content.Context;
import org.bson.Document;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter implements Filterable
{
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList. (원본 데이터 리스트)
    private ArrayList<FriendsItem> listViewItemList = new ArrayList<FriendsItem>();

    // 필터링된 결과 데이터를 저장하기 위한 ArrayList. 최초에는 전체 리스트 저장.
    private ArrayList<FriendsItem> filteredItemList = listViewItemList;

    Filter listFilter;

    public ListViewAdapter() {  }

    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.friends_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조
        CheckBox chkBox = (CheckBox) convertView.findViewById(R.id.chkBox);
        TextView nameView = (TextView) convertView.findViewById(R.id.nameView);
        TextView idView = (TextView) convertView.findViewById(R.id.idView);
        TextView holdTable = (TextView) convertView.findViewById(R.id.friendLessons);

        // Data Set(filteredItemList)에서 position에 위치한 데이터 참조
        final FriendsItem listViewItem = filteredItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        chkBox.setChecked(listViewItem.isChk());
        nameView.setText(listViewItem.getName());
        idView.setText(listViewItem.getId());

        // 버튼이 눌리면 상태 변경
        chkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                listViewItem.setChk(isChecked);
            }
        });

        //리스트뷰에 아이템 리턴
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return filteredItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 아이템 데이터 추가
    public void addItem(boolean chk, String name, String id, ArrayList<Document> lessons)
    {
        FriendsItem item = new FriendsItem();

        item.setChk(chk);
        item.setName(name);
        item.setId(id);
        item.setLessons(lessons);

        listViewItemList.add(item);
    }

   @Override
    public Filter getFilter()
    {
        if (listFilter == null)
        {
            listFilter = new ListFilter();
        }
        return listFilter;
    }

    //리스트 필터 클래스
    private  class ListFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            //필터링 결과를 담을 FilterResults 객체 생성
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0)
            {
                results.values = listViewItemList;
                results.count = listViewItemList.size();
            }
            else
            {
                ArrayList<FriendsItem> itemList = new ArrayList<FriendsItem>();

                for (FriendsItem item : listViewItemList)
                {
                    if (item.getName().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                            item.getId().toUpperCase().contains(constraint.toString().toUpperCase()))
                    {
                        itemList.add(item);
                    }
                }
                results.values = itemList;
                results.count = itemList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            //필터링된 데이터 리스트를 filteredItemList(ArrayList)에 저장
            filteredItemList = (ArrayList<FriendsItem>) results.values;

            //필터링된 데이터가 있을 때
            if (results.count > 0)
            {
                //리스트뷰 갱신(아이템 수가 추가 또는 삭제된경우)
                notifyDataSetChanged();
            }
            else
            {
                //리스트뷰 갱신(아이템 수는 변동은 없지만 데이터에 변화가 있는 경우)
                notifyDataSetInvalidated();
            }
        }
    }
}
