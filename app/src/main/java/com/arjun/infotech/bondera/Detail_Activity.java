package com.arjun.infotech.bondera;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.arjun.infotech.bondera.MyPreference.MyPref;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Detail_Activity extends AppCompatActivity {
    TextView txt_title_from_data, txt_title_to_data, txt_dis_pr_person, txt_dis_ttl, txt_total_distance_data, txt_num_of_people_data, txt_consumption_rate_data, txt_type_of_car_data, txt_type_of_gas_data, txt_price_per_litrer_data, txt_currency_data;
    TextView txt_copy, txt_screenShot, txt_service, txt_consumption_rate, txt_when_data;
    ImageButton img_copy, img_screen_shot;
    RelativeLayout rl_screen, rl_last;
    ScrollView scrl;
    String ttl_rs;
    View view_divider_number_of;
    ImageButton imgbtn_back;
    String servicefee;
    MyPref myPref;
    RequestPermissionHandler mRequestPermissionHandler;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_);
        myPref = new MyPref(getApplicationContext());
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue));
        txt_when_data = findViewById(R.id.txt_when_data);
        txt_service = findViewById(R.id.txt_service);
        txt_title_from_data = findViewById(R.id.txt_title_from_data);
        txt_title_to_data = findViewById(R.id.txt_title_to_data);
        txt_dis_pr_person = findViewById(R.id.txt_dis_pr_person);
        txt_dis_ttl = findViewById(R.id.txt_dis_ttl);
        txt_total_distance_data = findViewById(R.id.txt_total_distance_data);
        txt_num_of_people_data = findViewById(R.id.txt_num_of_people_data);
        txt_consumption_rate_data = findViewById(R.id.txt_consumption_rate_data);
        txt_type_of_car_data = findViewById(R.id.txt_type_of_car_data);
        txt_type_of_gas_data = findViewById(R.id.txt_type_of_gas_data);
        txt_price_per_litrer_data = findViewById(R.id.txt_price_per_litrer_data);
        img_copy = findViewById(R.id.img_copy);
        img_screen_shot = findViewById(R.id.img_screen_shot);
        rl_screen = findViewById(R.id.rl_screen);
        rl_last = findViewById(R.id.rl_last);
        txt_copy = findViewById(R.id.txt_copy);
        txt_screenShot = findViewById(R.id.txt_screenShot);
        scrl = findViewById(R.id.scrl);
        view_divider_number_of = findViewById(R.id.view_divider_number_of);
        Intent i = getIntent();
        servicefee = i.getStringExtra("servicefee");

        txt_when_data.setText(myPref.getPref(MyPref.journy_date, ""));
        txt_title_from_data.setText(i.getStringExtra("from"));
        txt_title_to_data.setText(i.getStringExtra("to"));
        txt_dis_pr_person.setText(i.getStringExtra("costPerPerson"));
        txt_dis_ttl.setText(i.getStringExtra("total"));
        txt_total_distance_data.setText(i.getStringExtra("total_distance") + " KM");
        txt_num_of_people_data.setText(i.getStringExtra("Number_of_people"));
        //8-8-2018 changes for Float value Convert to String
        //final String s = i.getStringExtra("Consumption_Rate");
        float crate = Float.parseFloat(i.getStringExtra("total_distance")) * Float.parseFloat(i.getStringExtra("crate")) / 100;
        txt_consumption_rate_data.setText(String.format("%.1f", crate) + " " + i.getStringExtra("crate_unit"));
        txt_type_of_car_data.setText(i.getStringExtra("type_of_car"));
        txt_type_of_gas_data.setText(i.getStringExtra("type_of_gas"));
        txt_price_per_litrer_data.setText(i.getStringExtra("price_per"));
        if (servicefee.equals("0") || servicefee.equals("0.0") || servicefee.equals("")) {
            txt_service.setVisibility(View.INVISIBLE);
        } else {
            txt_service.setText("*incl. service fee " + servicefee + " " + myPref.getPref(MyPref.Currencys, "") + "/km");
        }

        ttl_rs = i.getStringExtra("total");
        img_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copy();
            }
        });
        img_screen_shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRequestPermissionHandler = new RequestPermissionHandler();
                mRequestPermissionHandler.requestPermission(Detail_Activity.this, new String[]{
                        Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 123, new RequestPermissionHandler.RequestPermissionListener() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFailed() {
                    }
                });

                img_copy.setVisibility(View.INVISIBLE);
                img_screen_shot.setVisibility(View.INVISIBLE);
                txt_copy.setVisibility(View.INVISIBLE);
                txt_screenShot.setVisibility(View.INVISIBLE);
                view_divider_number_of.setVisibility(View.INVISIBLE);

                View u = findViewById(R.id.scrl);
                u.setDrawingCacheEnabled(true);
                ScrollView z = (ScrollView) findViewById(R.id.scrl);
                int totalHeight = z.getChildAt(0).getHeight();
                int totalWidth = z.getChildAt(0).getWidth();
                u.layout(0, 0, totalWidth, totalHeight);
                u.buildDrawingCache(true);
                Bitmap b = null;
//                if (Build.VERSION.SDK_INT <= 26) {
                b = loadLargeBitmapFromView(scrl, scrl.getChildAt(0).getHeight(), scrl.getChildAt(0).getWidth());

//                } else {
//                    b = Bitmap.createBitmap(u.getDrawingCache());
//                }
                String extr = Environment.getExternalStorageDirectory().toString();

                Date currentTime = Calendar.getInstance().getTime();

                // Date 02/08/2018 solution for image name
                // old code  File myPath = new File(extr, getString(R.string.app_name) + ".jpg");
                File myPath = new File(extr, currentTime + ".jpg");// new code
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(myPath);
                    b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    String test = MediaStore.Images.Media.insertImage(getContentResolver(), b, "Screen", "screen");
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    Uri imageUri = Uri.parse(test);
                    //Date 02/08/2018 solution for share image facebook and instagram

                    // Date 02/08/2018 old code before solution sharingIntent.setType("/png");
                    sharingIntent.setType("image/*"); // new code after solution
                    sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // new code after solution
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    // old code before solution startActivity(Intent.createChooser(sharingIntent, "Share Image via..."));
                    startActivity(sharingIntent); // new code after solution
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(Detail_Activity.this, "File Not Found", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (Exception e) {
                    Toast.makeText(Detail_Activity.this, "Exception", Toast.LENGTH_SHORT).show();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                img_copy.setVisibility(View.VISIBLE);
                img_screen_shot.setVisibility(View.VISIBLE);
                txt_copy.setVisibility(View.VISIBLE);
                txt_screenShot.setVisibility(View.VISIBLE);
                rl_last.setVisibility(View.VISIBLE);
                view_divider_number_of.setVisibility(View.VISIBLE);


                //last

            }
        });


        imgbtn_back = findViewById(R.id.imgbtn_back);
        imgbtn_back.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                finish();
            }
        });
    }

    public static Bitmap loadLargeBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    private void copy() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String str = "";
        ClipData clip = null;
        if (txt_service.getVisibility() == View.INVISIBLE) {
            clip = ClipData.newPlainText("label", "Bondera Fare\n" +
                    "From:" + txt_title_from_data.getText().toString().trim() + "\n" +
                    "To:" + txt_title_to_data.getText().toString().trim() + "\n" +
                    "When:" + txt_when_data.getText().toString() + "\n" +
                    "Total Distance:" + txt_total_distance_data.getText().toString().trim() + "\n" +
                    "Number of people:" + txt_num_of_people_data.getText().toString().trim() + "\n" +
                    "Type of gas:" + txt_type_of_gas_data.getText().toString().trim() + "\n" +
                    "Total per person: " + txt_dis_pr_person.getText().toString().trim() + "\n" +
                    "Total: " + ttl_rs);
        } else {
            clip = ClipData.newPlainText("label", "Bondera Fare\n" +
                    "From:" + txt_title_from_data.getText().toString().trim() + "\n" +
                    "To:" + txt_title_to_data.getText().toString().trim() + "\n" +
                    "When:" + txt_when_data.getText().toString() + "\n" +
                    "Total Distance:" + txt_total_distance_data.getText().toString().trim() + "\n" +
                    "Number of people:" + txt_num_of_people_data.getText().toString().trim() + "\n" +
                    "Type of gas:" + txt_type_of_gas_data.getText().toString().trim() + "\n" +
                    "Total per person: " + txt_dis_pr_person.getText().toString().trim() + " (incl. service fee of " + servicefee + " " + myPref.getPref(MyPref.Currencys, "") + "/km)\n" +
                    "Total: " + ttl_rs);
        }
        clipboard.setPrimaryClip(clip);
        Toast.makeText(Detail_Activity.this, "Trip details copied :)", Toast.LENGTH_SHORT).show();
    }
}
