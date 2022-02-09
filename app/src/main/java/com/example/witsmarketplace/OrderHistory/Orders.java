package com.example.witsmarketplace.OrderHistory;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.witsmarketplace.Account;
import com.example.witsmarketplace.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Orders extends AppCompatActivity {
    private static String date_str, street_name, surburb_name, city_name, country_name, items, order_no_str, total_str, email_str;
    private static String[] names, prices;
    private Bitmap bitmap, scaled_bmp;
    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;
    //width and height for our PDF file.
    int pageHeight = 1336, pagewidth = 720;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        TextView street = findViewById(R.id.addressStreet);
        TextView surburb = findViewById(R.id.addressSurburb);
        TextView city = findViewById(R.id.addressCity);
        TextView country = findViewById(R.id.addressCountry);
        TextView date = findViewById(R.id.date_id);
        TextView order_no = findViewById(R.id.orderNo);
        TextView total = findViewById(R.id.totalPrice);
        TextView email = findViewById(R.id.email);
        LinearLayout pdf = findViewById(R.id.pdf);
        ImageView downloadPDF = findViewById(R.id.downloadPDF);

        Intent intent = getIntent();
        street_name = intent.getStringExtra("street");
        surburb_name = intent.getStringExtra("surburb");
        city_name = intent.getStringExtra("city");
        country_name = intent.getStringExtra("country");
        date_str = intent.getStringExtra("date");
        order_no_str = intent.getStringExtra("order_no");
        total_str = intent.getStringExtra("total");
        email_str = intent.getStringExtra("user");
        names = intent.getStringArrayExtra("names");
        prices = intent.getStringArrayExtra("prices");

        street.setText(street_name);
        surburb.setText(surburb_name);
        city.setText(city_name);
        country.setText(country_name);
        date.setText(date_str);
        order_no.setText(order_no_str);
        total.setText(total_str);
        email.setText(email_str);

        renderer();

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        scaled_bmp = Bitmap.createScaledBitmap(bitmap, 240, 240, false);

//        pagewidth = pdf.getWidth();
//        pageHeight = pdf.getHeight();
        // checking our permissions.
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        downloadPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Invoice Downloaded", Toast.LENGTH_SHORT).show();
                System.out.println("W: " + pdf.getWidth() + " H: " + pdf.getHeight());
                generatePDF();
//                int width = pdf.getWidth(), height = pdf.getHeight();
//                bitmap = loadBitmap(pdf, width, height);
//                createPDF();
            }
        });
        
    }

    private void generatePDF() {
        // creating an object variable for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used for drawing shapes and we will use "title" for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file in which we will be passing our pageWidth, pageHeight and number of pages and after that we are calling it to create our PDF.
        System.out.println(pagewidth + " ///////////////// " + pageHeight);
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // setting start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // draw our image on our PDF file.
        // the first parameter is our bitmap || second parameter is position from left || third parameter is position from top and last one is our variable for paint.
        canvas.drawBitmap(scaled_bmp, 56, 40, paint);

        // adding typeface for our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // setting text size which we will be displaying in our PDF file.
        title.setTextSize(25);

        // setting color of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(this, R.color.blue));
        title.setTextAlign(Paint.Align.RIGHT);

        // draw text in our PDF file.
        // the first parameter is our text, second parameter is position from start, third parameter is position from top and then we are passing our variable of paint which is title.
        canvas.drawText(street_name, 680, 80, title);
        canvas.drawText(surburb_name, 680, 120, title);
        canvas.drawText(city_name, 680, 160, title);
        canvas.drawText(country_name, 680, 200, title);
        canvas.drawText(date_str, 680, 250, title);
        title.setColor(ContextCompat.getColor(this, R.color.lightGrey));
        canvas.drawText(email_str, 680, 280, title);

        title.setColor(ContextCompat.getColor(this, R.color.blue));
        title.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Order Number: #" + order_no_str, 60, 340, title);
        title.setTextSize(30);

        canvas.drawLine(60, 370, 680, 370, paint);
        int k = 0;
        for (int i = 0; i < prices.length; i++){
            title.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(names[i], 60, 420+k, title);
            title.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(prices[i], 680, 420+k, title);
            k += 60;
        }
        canvas.drawLine(60, 400 + k, 680, 400 + k, paint);

        title.setTextAlign(Paint.Align.RIGHT);
        title.setTextSize(45);
        canvas.drawText("Total: " + total_str, 680, 470+k, title);

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // set the name of our PDF file and its path.
        File file = new File(Environment.getExternalStorageDirectory(), "order" + order_no_str + ".pdf");

        try {
            // after creating a file name we will write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // print toast message on completion of PDF generation.
            Toast.makeText(Orders.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // after storing our pdf to that location we are closing our PDF file.
        pdfDocument.close();
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void renderer(){
        //display the data in a recyclerview which allows us to scroll through
        RecyclerView recyclerView = findViewById(R.id.rv_orderList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new OrdersAdapter(names, prices);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}