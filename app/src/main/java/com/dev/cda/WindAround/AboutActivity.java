package com.dev.cda.WindAround;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
        TableLayout ll = (TableLayout) findViewById(R.id.displayAboutTable);
        int i = 0;

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        ll.addView(createTableRow(createTextView("version :"), lp),i++);
        ll.addView(createTableRow(createTextView("1.0.0", 15), lp),i++);
        ll.addView(createTableRow(createTextView("developper :"), lp),i++);
        ll.addView(createTableRow(createTextView("Clément Daviller", 15), lp),i++);
        ll.addView(createTableRow(createTextView("mail :"), lp),i++);
        ll.addView(createTableRow(createTextView("clement.daviller@gmail.com", 15), lp),i++);
        ll.addView(createTableRow(createTextView("wind data collected from:"), lp),i++);
        ll.addView(createTableRow(createTextView("https://pioupiou.fr", 15), lp),i++);
        ll.addView(createTableRow(createTextView("source code:"), lp),i++);
        ll.addView(createTableRow(createTextView("https://github.com/tnemelc/WindAround", 15), lp),i++);
    }


    protected TextView createTextView(String Text){
        TextView tv = new TextView(this);
        tv.setTextSize(25);
        tv.setText(Text);
        tv.setTextColor(Color.DKGRAY);
        return tv;
    }
    protected TextView createTextView(String Text, int textSize){
        TextView tv = new TextView(this);
        tv.setTextSize(textSize);
        tv.setText(Text);
        tv.setTextColor(Color.DKGRAY);
        return tv;
    }

    protected TableRow createTableRow(TextView tv, TableRow.LayoutParams lp){
        TableRow tR = new TableRow(this);
        tR.setLayoutParams(lp);
        tR.addView(tv);
        return tR;
    }
}
