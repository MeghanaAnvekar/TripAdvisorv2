package com.example.meghana.tripadvisorv2;

import android.app.Activity;

import com.example.meghana.tripadvisorv2;

import org.jsoup.nodes.Element;

/**
 * Created by meghana on 28/10/17.
 */

public class place extends Activity implements tripadvisorv2 {
    public String title;
    public String description;
    public  String img_src;
    public  String link;

    public place(String title, String description, String img_src, String link)
    {
        this.description = description;
        this.title = title;
        this.img_src = img_src;
        this.link = link;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return  description;
    }

    public String getImg_src()
    {
        return img_src;
    }
    public String getLink()
    {
        return link;
    }
}

public class place {

}