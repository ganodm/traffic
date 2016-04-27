package com.brkc.traffic.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.brkc.common.img.AsynImageLoader;
import com.brkc.traffic.R;
import com.brkc.traffic.adapter.VehicleResult;
import com.brkc.traffic.adapter.VehicleResultAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VehicleQueryResultActivity extends Activity {

    private List<VehicleResult> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_query_result);

        String result = getIntent().getStringExtra(VehicleQueryActivity.EXTRA_RESULT_NAME);
        initList(result);
        initUI();
    }

    private void initUI() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        int resId = R.layout.item_vehicle_result;

        VehicleResultAdapter adapter = new VehicleResultAdapter(getActivity(),resId,
                resultList, imageLoader);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VehicleResult item = resultList.get(position);
                Toast.makeText(getActivity(), item.getCrossName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initList(String result) {
        resultList = new ArrayList<VehicleResult>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            Resources res = getResources();
            String totalKey = res.getString(R.string.vehicle_query_result_total);
            String rowsKey = res.getString(R.string.vehicle_query_result_rows);
            String noKey = res.getString(R.string.vehicle_query_result_plate_no);
            String colorKey = res.getString(R.string.vehicle_query_result_plate_color);
            String timeKey = res.getString(R.string.vehicle_query_result_pass_time);
            String crossKey = res.getString(R.string.vehicle_query_result_cross);
            String thumbKey = res.getString(R.string.vehicle_query_result_thumb);
            String imageKey = res.getString(R.string.vehicle_query_result_image);

            int total = jsonObject.getInt(totalKey);
            JSONArray rows = jsonObject.getJSONArray(rowsKey);
            for (int i = 0; i < total; i++) {
                JSONObject row = rows.getJSONObject(i);
                String plateNo = row.getString(noKey);
                String plateColor = row.getString(colorKey);
                String cross = row.getString(crossKey);
                String time = row.getString(timeKey);
                String thumb = row.getString(thumbKey);
                String image = row.getString(imageKey);
                VehicleResult item = new VehicleResult(i,plateNo,plateColor,cross,time,thumb,image);
                resultList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private AsynImageLoader imageLoader = new AsynImageLoader();


    private Activity getActivity(){
        return VehicleQueryResultActivity.this;
    }
}
