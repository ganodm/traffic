/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brkc.traffic.ui.image;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brkc.common.image.ImageCache;
import com.brkc.common.image.ImageFetcher;
import com.brkc.common.image.Utils;
import com.brkc.traffic.R;
import com.brkc.traffic.adapter.VehicleResult;
import com.brkc.traffic.ui.VehicleQueryActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * The main fragment that powers the ImageGridActivity screen. Fairly straight forward GridView
 * implementation with the key addition being the ImageWorker class w/ImageCache to load children
 * asynchronously, keeping the UI nice and smooth and caching thumbnails for quick retrieval. The
 * cache is retained over configuration changes like orientation change so the images are populated
 * quickly if, for example, the user rotates the device.
 */
public class ImageListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "ImageListFragment";
    private static final String IMAGE_CACHE_DIR = "thumbs";

    private ImageAdapter mAdapter;
    private ImageFetcher mImageFetcher;

    private static VehicleResult[] result;
    /**
     * Empty constructor as per the Fragment documentation
     */
    public ImageListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        String extraStr = getActivity().getIntent().getStringExtra(VehicleQueryActivity.EXTRA_RESULT_NAME);
        initList(extraStr);

        mAdapter = new ImageAdapter(getActivity());

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(getActivity(),0);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.image_list_fragment, container, false);
        final ListView mListView = (ListView) v.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // Pause fetcher to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Before Honeycomb pause image loading on scroll to help with performance
                    if (!Utils.hasHoneycomb()) {
                        mImageFetcher.setPauseWork(true);
                    }
                } else {
                    mImageFetcher.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN)
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Log.d(TAG, "v=" + v.getId() + ",id=" + id + ",position=" + position);
        final Intent i = new Intent(getActivity(), ImageDetailActivity.class);
        i.putExtra(ImageDetailActivity.EXTRA_IMAGE, (int) id);
        if (Utils.hasJellyBean()) {
            // makeThumbnailScaleUpAnimation() looks kind of ugly here as the loading spinner may
            // show plus the thumbnail image in GridView is cropped. so using
            // makeScaleUpAnimation() instead.
            ActivityOptions options =
                    ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight());
            getActivity().startActivity(i, options.toBundle());
        } else {
            startActivity(i);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_cache:
                mImageFetcher.clearCache();
                Toast.makeText(getActivity(), R.string.clear_cache_complete_toast, Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initList(String resultStr) {
        result = new VehicleResult[0];
        try {
            JSONObject jsonObject = new JSONObject(resultStr);
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
            if(rows.length()>total){
                total = rows.length();
            }

            result = new VehicleResult[total];
            for (int i = 0; i < total; i++) {
                JSONObject row = rows.getJSONObject(i);
                String plateNo = row.getString(noKey);
                String plateColor = row.getString(colorKey);
                String cross = row.getString(crossKey);
                String time = row.getString(timeKey);
                String thumb = row.getString(thumbKey);
                String image = row.getString(imageKey);
                VehicleResult item = new VehicleResult(i,plateNo,plateColor,cross,time,thumb,image);
                result[i] = item;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"查询结果记录条数：" + result.length);
    }

    public static String getImageUrl(int position) {
        return result[position].getImage();
    }

    public static int getImageCount(){
        return result.length;
    }

    /**
     * The main adapter that backs the GridView. This is fairly standard except the number of
     * columns in the GridView is used to create a fake top row of empty views as we use a
     * transparent ActionBar and don't want the real top row of images to start off covered by it.
     */
    private class ImageAdapter extends BaseAdapter {

        private final Context mContext;

        public ImageAdapter(Context context) {
            super();
            mContext = context;
        }

        @Override
        public int getCount() {
            return result.length;
        }

        @Override
        public Object getItem(int position) {
            return result[position];// getThumbUrl(position);
        }

        @Override
        public long getItemId(int position) {
            return ((VehicleResult)getItem(position)).getXh();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            // Now handle the main ImageView thumbnails
            VehicleResult item = (VehicleResult)getItem(position);
            View view;
            ViewHolder viewHolder;
            if (convertView == null) { // if it's not recycled, instantiate and initialize
                view = LayoutInflater.from(getContext()).inflate(R.layout.image_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.thumbImage = (RecyclingImageView) view.findViewById(R.id.image_thumb);
                viewHolder.textPlateNo = (TextView)view.findViewById(R.id.plate_no);
                viewHolder.textPassTime = (TextView)view.findViewById(R.id.pass_time);
                viewHolder.textCrossName = (TextView)view.findViewById(R.id.cross_name);
                viewHolder.plate = (LinearLayout)view.findViewById(R.id.plate);
                view.setTag(viewHolder);
            } else { // Otherwise re-use the converted view
                view = convertView;
                viewHolder = (ViewHolder)view.getTag();
            }

            int color = 0;
            if(item.getPlateColor().equals("1") || item.getPlateColor().contains("蓝")) {
                color = mContext.getResources().getColor(R.color.blue);
            }else{
                color = mContext.getResources().getColor(R.color.dark_orange);
            }
            String plate = item.getPlateNo() + "," + item.getPlateColor();

            if(item.getThumb().length()>0) {
                Log.d(TAG, "url=" + item.getThumb());
                // Finally load the image asynchronously into the ImageView, this also takes care of
                // setting a placeholder image while the background thread runs
                mImageFetcher.loadImage(item.getThumb(), viewHolder.thumbImage);
            }
            else {
                viewHolder.thumbImage.setImageResource(R.drawable.no_image);
            }

            viewHolder.textPlateNo.setText(item.getPlateNo());
            viewHolder.textPlateNo.setTextColor(color);
            viewHolder.textPassTime.setText(item.getPassTime());
            viewHolder.textCrossName.setText(item.getCrossName());
            viewHolder.plate.setTag(plate);

            return view;
        }

    }
    class ViewHolder{
        RecyclingImageView thumbImage;
        TextView textPlateNo;
        TextView textPassTime;
        TextView textCrossName;
        LinearLayout plate;
    }
}
