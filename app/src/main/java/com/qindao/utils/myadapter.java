package com.qindao.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qindao.coalfield.R;
import com.qindao.model.Coalbytruckbean;

import java.util.List;


public class myadapter<T> extends BaseAdapter  {
    private List<Coalbytruckbean> coalfieldzoneList;
    private int resource;   //item的布局
    private Context context;
    private LayoutInflater inflator;
    private int selectedItem = -1;
    /**
     *
     * @param context mainActivity
     * @param coalfieldzoneList   显示的数据
     * @param resource  一个Item的布局
     */
    public myadapter(Context context, List<Coalbytruckbean> coalfieldzoneList, int resource){
        this.context = context;
        this.coalfieldzoneList = coalfieldzoneList;
        this.resource = resource;
    }
    /*
     * 获得数据总数
     * */
    @Override
    public int getCount() {
        return coalfieldzoneList.size();
    }
    /*
     * 根据索引为position的数据
     * */
    @Override
    public Object getItem(int position) {
        return coalfieldzoneList.get(position);
    }
    /*
     * 根据索引值获得Item的Id
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }
    /*
     *通过索引值position将数据映射到视图
     *convertView具有缓存功能，在第一页时为null，在第二第三....页时不为null
     * */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(resource, null);
            viewHolder = new ViewHolder();
            viewHolder.nameTextView = (TextView)convertView.findViewById(R.id.txt_xs);   //为了减少开销，则只在第一页时调用findViewById
            viewHolder.aearTextView =(TextView) convertView.findViewById(R.id.txt_msg);
            convertView.setTag(viewHolder);
        }else{
            if (position == selectedItem) {
                convertView.setBackgroundColor(Color.BLUE);
            }else {
                convertView.setBackgroundColor(Color.WHITE);
            }
            viewHolder = (ViewHolder)convertView.getTag();

        }
        Coalbytruckbean cya = coalfieldzoneList.get(position);
        viewHolder.nameTextView.setText(cya.getVehicleno());
        viewHolder.aearTextView.setText(cya.getCoalfieldid());
        return convertView;
    }
    public void setSelectedItem(int selectedItem)
    {
        this.selectedItem = selectedItem;
    }



    class ViewHolder{
        private TextView nameTextView;
        private TextView aearTextView;
    }
    /**
     * 局部刷新
     * @param view
     * @param itemIndex
     */
    public void updateView(View view, int itemIndex) {
        if (view == null) {
            return;
        }else if(itemIndex == selectedItem){
            view.setBackgroundColor(Color.BLUE);
            return;
        }
    }
}
