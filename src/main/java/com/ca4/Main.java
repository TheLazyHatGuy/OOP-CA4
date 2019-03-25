package com.ca4;

import com.ca4.BusinessObjects.MainApp;
import com.ca4.Utilities.Asker;

public class Main
{
    public static void main(String[] args)
    {
        Asker asker = new Asker(System.in, System.out);

        MainApp.startApp(asker);
    }
}
