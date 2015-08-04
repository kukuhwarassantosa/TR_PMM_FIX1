package com.example.jefri.tr_pmm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by jefri on 03/08/2015.
 */
public class CoverActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Button btn1 =(Button)findViewById(R.id.btn_math);
        Button btn2 =(Button)findViewById(R.id.btn_physics);
        Button btn3 =(Button)findViewById(R.id.btn_biology);
        Button btn4 =(Button)findViewById(R.id.btn_Chemistry);


        btn1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                Intent a = new Intent(CoverActivity.this,MainActivity.class);
                startActivity(a);

            }
        });

        btn2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                Intent a = new Intent(CoverActivity.this,MainActivityPhysics.class);
                startActivity(a);

            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                Intent a = new Intent(CoverActivity.this,MainActivityBiology.class);
                startActivity(a);

            }
        });
        btn4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                Intent a = new Intent(CoverActivity.this,MainActivityChemistry.class);
                startActivity(a);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }}
