package com.cy.cylibrary.img;


public class ImageReSize {
    int reWidth=0;
    int reHeight=0;
    public ImageReSize(int reWidth,int reHeight){
        if (reHeight<=0){
            reHeight=0;
        }
        if (reWidth<=0) {
            reWidth=0;
        }
        this.reHeight=reHeight;
        this.reWidth=reWidth;
    }
}
