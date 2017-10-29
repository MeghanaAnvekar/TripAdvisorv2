package com.example.meghana.tripadvisorv2;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.bitmap;
import static com.example.meghana.tripadvisorv2.R.id.image;
import static com.example.meghana.tripadvisorv2.R.id.img;
import static com.example.meghana.tripadvisorv2.R.id.poi;

public class MainActivity extends AppCompatActivity {
    RecyclerView poi;

    ArrayAdapter<place> adapter;
    ArrayList<place> place_list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        poi = (RecyclerView) findViewById(R.id.poi);
        poi.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        poi.setLayoutManager(llm);

        place_list = new ArrayList<place>();
        GetData task = new GetData();
        task.execute();
        poi.setAdapter(new placeAdapter(this,place_list));

       /* adapter = new ArrayAdapter<place>(
                MainActivity.this,
                android.R.layout.simple_gallery_item,
                place_list
        );

       // poi.setAdapter(adapter);*/

    }


    @SuppressLint("NewApi")
    public class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // poi = new RecyclerView(MainActivity.this);

        }

        @Override
        protected Void doInBackground(Void... params) {
            getWebsite();
            return null;
        }

        void getListings(String url) {

            try {
                Document main_page = null;
                main_page = Jsoup.connect(url).get();
                Elements links = main_page.select("div.attraction_clarity_cell");

                for (Element link : links) {
                    //System.out.println(link);

                    Element listing = link.select("div.listing_title > a").first();
                    String title = listing.text();
                    String listing_link = "https://www.tripadvisor.in" + listing.attr("href");

                    Element e1 = link.select("div.listing").first();
                    Element e2 = e1.select("div.listing_details").first();
                    Element e3 = e2.select("div.listing_info").first();
                    Element e4 = e3.select("div.listing_rating").first();
                    Element e5 = e4.select("div.wrap >div.rs rating >span").first();
                    //Element e6 = e5.select("div.rs rating >span").first();
                    System.out.println(e5.attr("alt"));
                    //if(e6 == null)
                    //System.out.println("yes");


                    String img_src = link.select("img[src]").first().attr("src");

                    Element d1 = link.select("div.listing").first();
                    Element d2 = d1.select("div.listing_details").first();
                    Element d3 = d2.select("div.listing_info").first();
                    Element d4 = d3.select("div.tag_line").first();
                    String description = d4.text();

                    // System.out.println(title +"\n"+description+"\n"+listing_link+"\n"+img_src+"\n\n");


                   place_list.add(new place(title,description,img_src,listing_link));

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        private void getWebsite() {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    Document doc = null, main_page;
                    String google_url = "https://www.google.co.in/search?q=things+to+do+in+london";
                    try {
                        doc = Jsoup.connect(google_url).get();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String url = doc.select("a[href^=https://www.tripadvisor.in/Attractions] ").attr("href");

                    getListings(url);

                    try {
                        main_page = Jsoup.connect(url).get();


                        Elements pages = main_page.select("div.pageNumbers>a");


                        for (int i = 0; i < pages.size() - 1; ++i) {
                            Element page = pages.get(i);
                            //System.out.println(page.text());
                            //System.out.println("Yes");
                            //System.out.println(page.attr("href"));
                            url = "https://www.tripadvisor.in" + page.attr("href");
                            //System.out.println(url);
                            getListings(url);

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

               /* runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(builder.toString());
                    }
                });*/
                }
            }).start();
        }
    }

    public class placeAdapter extends RecyclerView.Adapter<placeAdapter.placeViewHolder> {

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
            d.title.setText(ci.getTitle());
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




}
