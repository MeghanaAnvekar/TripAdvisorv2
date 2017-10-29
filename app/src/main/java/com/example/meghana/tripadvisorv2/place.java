package com.example.meghana.tripadvisorv2;

/**
 * Created by meghana on 28/10/17.
 */


public class place {
    private  String title;
    private  String description;
    private  String img_src;
    private  String link;

    place(String title, String description, String img_src, String link )
    {
        this.description = description;
        this.title = title;
        this.img_src = img_src;
        this.link = link;
    }

    public String getTitleValue()
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


