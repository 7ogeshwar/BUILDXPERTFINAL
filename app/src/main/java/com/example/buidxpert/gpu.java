package com.example.buidxpert;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class gpu extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpu);

        // Check and request permission for WRITE_EXTERNAL_STORAGE if not granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
            }
        }

        // Set up button click listener for Generate Invoice button
        Button generateInvoiceButton = findViewById(R.id.button_generate_invoice);
        generateInvoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateInvoice();
                // Navigate to CartActivity after generating invoice
                Intent intent = new Intent(gpu.this, CartActivity.class);
                startActivity(intent);
            }
        });

        // Set up button click listener for Add to Cart button
        Button addToCartButton1 = findViewById(R.id.button_add_to_cart_1);
        addToCartButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart("Nvidia GeForce RTX 4070 Super", 57000);
            }
        });

        // Repeat similar setup for other Add to Cart buttons (2 to 15)
        // Button 2
        Button addToCartButton2 = findViewById(R.id.button_add_to_cart_2);
        addToCartButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart("GeForce RTX 3080", 98000);
            }
        });

        // Button 3
        Button addToCartButton3 = findViewById(R.id.button_add_to_cart_3);
        addToCartButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart("ASRock RX 7800 XT", 52800);
            }
        });

        // Button 15 (last item)
        Button addToCartButton15 = findViewById(R.id.button_add_to_cart_15);
        addToCartButton15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart("GPU Model 15", 0); // Replace with actual details
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                Toast.makeText(this, "Permission granted. You can generate invoice now.", Toast.LENGTH_SHORT).show();
            } else {
                // Permission Denied
                Toast.makeText(this, "Permission denied. Cannot generate invoice.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void generateInvoice() {
        // Check if permission is granted
        if (!checkPermission()) {
            requestPermission();
            return;
        }
        File directory;
        String filename = "productinvoice.pdf";

        // Get the directory for storing files
//        File directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
//        if (directory == null) {
//            Toast.makeText(this, "Failed to get external storage directory.", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (isExternalStorageWritable()) {
            // Get the public Documents directory (use DIRECTORY_DOWNLOADS if targeting API 29+)
            directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        } else {
            // If external storage is not available, use internal storage
            directory = getFilesDir();
        }
//        String filePath = directory.getAbsolutePath() + File.separator + "invoice.pdf";
        File file = new File(directory,filename);
        // Create the directory if it doesn't exist
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);


            document.add(new Paragraph("***************************BUILDXPERTInvoice******************************8"));
            document.add(new Paragraph("***************************BUILDXPERT PVT LTD**************************** "));
            document.add(new Paragraph("ANNA UNIVERSITY___chennai___india"));
            document.add(new Paragraph("Invoice Date: " + getCurrentDate()));

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.backlogo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            ImageData imageData = ImageDataFactory.create(byteArray);

            com.itextpdf.layout.element.Image logoImage = new com.itextpdf.layout.element.Image(imageData);

            // Set position and size of the image on the page
            logoImage.setFixedPosition(450, 700); // Example position, adjust as needed
            logoImage.setWidth(100); // Example width, adjust as needed

            // Add the image to the document
            document.add(logoImage);

            List<CartItem> items = Cart.getInstance().getItems();
            for (CartItem item : items) {
                document.add(new Paragraph(item.getName() + " - Rs. " + item.getPrice()));
            }

            document.close();
            Toast.makeText(this, "Invoice generated successfully.", Toast.LENGTH_SHORT).show();
            Log.d("InvoiceGeneration", "Invoice generated successfully. File path: " + file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate invoice. File not found.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate invoice. Unknown error.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat dateformat= new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault());
        return dateformat.format(new Date());
    }


    private void addToCart(String name, double price) {
        Cart.getInstance().addItem(new CartItem(name, price));
        Toast.makeText(this, "Added to Cart: " + name, Toast.LENGTH_SHORT).show();
    }
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}

