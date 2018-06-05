package com.cy.cylibrary.img;

import com.bumptech.glide.load.model.Headers;

import java.util.HashMap;
import java.util.Map;


public class GlideHeader implements Headers{

    String httpHeaderRefererUrl = null; //"http://www.91zhanghu.com"

    public GlideHeader(String httpHeaderRefererUrl) {
        this.httpHeaderRefererUrl = httpHeaderRefererUrl;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String,String> header = new HashMap<>();
        header.put("Referer", httpHeaderRefererUrl);
        return header;
    }
}
