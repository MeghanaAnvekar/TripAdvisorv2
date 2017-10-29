package com.example.meghana.tripadvisorv2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class placeAdapter extends RecyclerView.Adapter<placeAdapter.placeViewHolder>
{

    private List<place> placeList;

    public placeAdapter(MainActivity mainActivity, List<place> list) {
        this.placeList = list;
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    @Override
    public void onBindViewHolder(placeViewHolder d, int i) {
        place ci = placeList.get(i);
        d.title.setText(ci.getTitleValue());
        d.description.setText(ci.getDescription());
        d.img_src = ci.getImg_src();
        d.link = ci.getLink();

        URL url = null;
        try {
            url = new URL(d.img_src);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoInput(true);
        try {
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream input = null;
        try {
            input = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap myBitmap = BitmapFactory.decodeStream(input);

        d.img.setImageBitmap(myBitmap);



    }

    @Override
    public placeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_item_places, viewGroup, false);

        return new placeViewHolder(itemView);
    }

    public  class placeViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView description;
        protected ImageView img;
        public String img_src;
        public String link;


        public placeViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            description = (TextView) v.findViewById(R.id.description);
            img = (ImageView) v.findViewById(R.id.img);

        }

    }

}
