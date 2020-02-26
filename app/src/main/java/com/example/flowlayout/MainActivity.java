package com.example.flowlayout;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MyFlowLayout first_flowlayout;
    List<String> sousuolist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        first_flowlayout = findViewById(R.id.first_flowlayout);
        first_flowlayout.setLisetenr(new OnFLowclicklistener() {
            @Override
            public void onclicklistener(int positon, View view) {
                first_flowlayout.setSelectPosition(positon); //设置选中的item
                first_flowlayout.requestLayout();
                Toast.makeText(MainActivity.this, "点击" + ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoneclicklistener(int positon, View view) {

                Toast.makeText(MainActivity.this, "长按" + ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });
        initdata();
    }

    public void initdata(){
        sousuolist = new ArrayList<>();

        sousuolist.add("水果味孕妇奶粉水果味");
        sousuolist.add("儿童洗衣机");
        sousuolist.add("洗衣机全自动");
        sousuolist.add("小度");
        sousuolist.add("儿童汽车可坐人");
        sousuolist.add("抽真空收纳袋");
        sousuolist.add("儿童滑板车");
        sousuolist.add("稳压器 电容");
        sousuolist.add("羊奶粉");
        sousuolist.add("奶粉1段");
        sousuolist.add("图书勋章日");

        for(int i=0;i<sousuolist.size();i++){
            TextView textView = new TextView(MainActivity.this);
            textView.setText(sousuolist.get(i));
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setBackgroundResource(R.drawable.shape_button_circular);
            first_flowlayout.addView(textView);
        }
    }
}
