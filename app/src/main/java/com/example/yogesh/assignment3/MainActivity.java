package com.example.yogesh.assignment3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button btnWalk;
    private Button btnRun;
    private Button btnEat;
    private Button btnTrain;
    private Button btnTest;

    public static final String  DATABASE_NAME = "Aman_Abhishek_Yogesh";
    public static String  tableName = "SensorData";
    public SQLiteDatabase db;


    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
            while (!hasPermission)
            {
                hasPermission = (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            }
        }

        String CreateColumns = "", Columns = "" ;

        for(int i=1;i<50;i++){
            CreateColumns = CreateColumns + " Xaxis_" + i + " float" +", Yaxis_" +i+" float " + ", Zaxis_" + i + " float,";
        }
        CreateColumns = CreateColumns + "Xaxis_50 float, Yaxis_50 float, Zaxis_50 float ";

        for (int i =1; i<50;i++){
            Columns = Columns + " Xaxis_" + i + ", Yaxis_" + i + ", Zaxis_" + i ;
        }
        Columns = Columns + "Xaxis_50, Yaxis_50, Zaxis_50";

        try {

            //create the database in external storage of smart phone
            db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + File.separator + DATABASE_NAME, null);
            db.beginTransaction();

            try {

                db.execSQL("create table if not exists "+ tableName +"("
                        + " Id integer PRIMARY KEY autoincrement, "
                        + CreateColumns + " , "
                        + " ActivityLabel text ); ");
                db.setTransactionSuccessful();
            } catch (SQLException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("DATABASE ERROR : ",e.getMessage());
            } finally {
                //Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();
                db.endTransaction();
            }


        }catch (SQLException e){
            Log.i("DATABASE ERROR",e.getMessage());

            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        btnWalk = (Button)findViewById(R.id.btn_walk);
        btnRun = (Button)findViewById(R.id.btn_run);
        btnEat = (Button)findViewById(R.id.btn_eat);
        btnTest = (Button)findViewById(R.id.btn_test);
        btnTrain= (Button)findViewById(R.id.btn_train);

        btnWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, SensorActivity.class);
                in.putExtra("label","Walking");
                startActivity(in);
            }
        });

        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, SensorActivity.class);
                in.putExtra("label","Running");
                startActivity(in);
            }
        });

        btnEat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, SensorActivity.class);
                in.putExtra("label","Eating");
                startActivity(in);
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String strTemp = "";
                    db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + File.separator + DATABASE_NAME, null);
                    db.beginTransaction();
                    try {
                        Cursor c = db.rawQuery("SELECT * FROM "+tableName,null);
                        if(c.moveToFirst())
                        {
                            do
                            {
                                switch (c.getString(151)){
                                    case "Walking": strTemp = strTemp+Integer.toString(1);
                                                    break;
                                    case "Running": strTemp = strTemp+Integer.toString(2);
                                                    break;
                                    case "Eating":  strTemp = strTemp+Integer.toString(3);
                                                    break;
                                }

                                for(int i=1; i<=150; i++)
                                {
                                    strTemp = strTemp+" "+Integer.toString(i)+":"+c.getString(i);
                                }
                                strTemp = strTemp+"\n";
                            }
                            while (c.moveToNext());
                        }
                        c.close();
                        db.setTransactionSuccessful();
                    } catch (SQLException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("DATABASE ERROR : ",e.getMessage());
                    } finally {
                        //Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();
                        db.endTransaction();
                    }
                    WriteToFile(strTemp);

                }catch (SQLException e){
                    Log.i("DATABASE ERROR",e.getMessage());

                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
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
    }

    public void WriteToFile(String str)
    {
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "" );
            File txtFile = new File(root, "training_set.txt");
            FileWriter writer = new FileWriter(txtFile);
            writer.append(str);
            writer.flush();
            writer.close();
            Toast.makeText(getBaseContext(), "File saved successfully",Toast.LENGTH_SHORT).show();

            /*FileOutputStream fileOut = openFileOutput("training_set.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);
            outputWriter.write(str);
            outputWriter.close();

            Toast.makeText(getBaseContext(), "File saved successfully",Toast.LENGTH_LONG).show();*/
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
