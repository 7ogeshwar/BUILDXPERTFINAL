package com.example.buidxpert;

import android.os.Environment;
import android.util.Log;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class GenerateInvoice {

    private static final String TAG = "GenerateInvoice";
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    public static void generate() {


        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/invoice.pdf";
        File file = new File(filePath);

        try {
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            List<CartItem> items = Cart.getInstance().getItems();
            for (CartItem item : items) {
                document.add(new Paragraph(item.getName() + " - Rs. " + item.getPrice()));
            }

            document.close();
            Log.d(TAG, "Invoice generated successfully. File path: " + file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Failed to generate invoice. File not found.", e);
        } catch (Exception e) {
            Log.e(TAG, "Failed to generate invoice.", e);
        }
    }
}
