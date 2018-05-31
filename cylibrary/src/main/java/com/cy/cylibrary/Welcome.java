package com.cy.cylibrary;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by cy on 2018/5/31.
 */
public class Welcome {

    public static void sayHello(Context context){
        Toast.makeText(context , "Hello , this is my third library , created by cy." , Toast.LENGTH_SHORT).show();
    }
}
