package com.brkc.traffic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brkc.common.img.AsynImageLoader;
import com.brkc.traffic.R;

import java.util.List;

/**
 * Created by Administrator on 16-4-22.
 */
public class VehicleResultAdapter extends ArrayAdapter<VehicleResult> {

    private int item_layout_id;
    private Context mContext;
    private AsynImageLoader imageLoader;

    public VehicleResultAdapter(Context context, int resource,List<VehicleResult> objects, AsynImageLoader imageLoader) {
        super(context, resource, objects);
        item_layout_id = resource;
        mContext = context;
        this.imageLoader = imageLoader;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VehicleResult item = getItem(position);
        View view;
        ViewHolder viewHolder;

        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(item_layout_id, null);
            viewHolder = new ViewHolder();
            viewHolder.thumbImage = (ImageView) view.findViewById(R.id.image_thumb);
            viewHolder.textPlateNo = (TextView)view.findViewById(R.id.plate_no);
            viewHolder.textPassTime = (TextView)view.findViewById(R.id.pass_time);
            viewHolder.textCrossName = (TextView)view.findViewById(R.id.cross_name);
            viewHolder.plate = (LinearLayout)view.findViewById(R.id.plate);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }

        int color = 0;
        if(item.getPlateColor().equals("1") || item.getPlateColor().indexOf("蓝")>=0) {
            color = mContext.getResources().getColor(R.color.blue);
        }else{
            color = mContext.getResources().getColor(R.color.dark_orange);
        }
        String plate = item.getPlateNo() + "," + item.getPlateColor();

        if(item.getThumb().length()>0)
            imageLoader.showImageAsyn(viewHolder.thumbImage, item.getThumb(), R.drawable.ajax_loader);
        else
            viewHolder.thumbImage.setImageResource(R.drawable.no_image);

        viewHolder.textPlateNo.setText(item.getPlateNo());
        viewHolder.textPlateNo.setTextColor(color);
        viewHolder.textPassTime.setText(item.getPassTime());
        viewHolder.textCrossName.setText(item.getCrossName());
        viewHolder.plate.setTag(plate);
        return view;
    }
    class ViewHolder{
        ImageView thumbImage;
        TextView textPlateNo;
        TextView textPassTime;
        TextView textCrossName;
        LinearLayout plate;
    }
}
