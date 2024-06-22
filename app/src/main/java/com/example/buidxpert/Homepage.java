package com.example.buidxpert;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Homepage extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView userDetailsTextView; // Corrected variable name

    private ImageView cpuButton, gpuButton, coolerButton, motherboardButton;
    private ViewPager viewPager;
    private BottomAppBar bottomAppBar;
    private boolean bottomAppBarVisible = true;
    private float startY;
    private ImageButton accountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_homepage);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize Views
        userDetailsTextView = findViewById(R.id.userdetails); // Corrected variable assignment
        cpuButton = findViewById(R.id.cpu);
        gpuButton = findViewById(R.id.gpu);
        coolerButton = findViewById(R.id.cooler);
        motherboardButton = findViewById(R.id.motherboard);
        viewPager = findViewById(R.id.viewPager);
        bottomAppBar = findViewById(R.id.bottomappbar);
        accountButton = findViewById(R.id.account_button);

        // Initialize ViewPager with images
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.one);
        imageList.add(R.drawable.three);
        imageList.add(R.drawable.four);
        imageList.add(R.drawable.five);
        imageList.add(R.drawable.six);
        imageList.add(R.drawable.seven);
        imageList.add(R.drawable.eight);

        MyAdapter myAdapter = new MyAdapter(imageList);
        viewPager.setAdapter(myAdapter);

        // Check if user is logged in, otherwise redirect to login page
        user = auth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(Homepage.this, Loginpage.class));
            finish();
        } else {
            String email = user.getEmail();
            if (userDetailsTextView != null) {
                userDetailsTextView.setText("Welcome, " + (email != null ? email : "User"));
            } else {
                Log.e("Homepage", "TextView userdetails is null");
            }
        }

        // Set up PopupMenu for account button
        accountButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(Homepage.this, v);
            popupMenu.getMenuInflater().inflate(R.menu.nav_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Toast.makeText(Homepage.this, "Home selected", Toast.LENGTH_SHORT).show();
                    // Navigate to Home activity if needed
                } else if (itemId == R.id.nav_settings) {
                    Toast.makeText(Homepage.this, "Settings selected", Toast.LENGTH_SHORT).show();
                    // Navigate to Settings activity if needed
                } else if (itemId == R.id.nav_share) {
                    Toast.makeText(Homepage.this, "Share selected", Toast.LENGTH_SHORT).show();
                    // Implement share functionality
                } else if (itemId == R.id.nav_about) {
                    Toast.makeText(Homepage.this, "About Us selected", Toast.LENGTH_SHORT).show();
                    // Navigate to About Us activity if needed
                } else if (itemId == R.id.nav_logout) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Homepage.this, Loginpage.class));
                    finish();
                }
                return false;
            });
            popupMenu.show();
        });
    }

    // Method for CPU button click
    public void selectCPU(View view) {
        Toast.makeText(this, "CPU selected", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Homepage.this, cpu.class);
        startActivity(intent);
    }

    // Method for GPU button click
    public void selectGPU(View view) {
        Toast.makeText(this, "GPU selected", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Homepage.this, gpu.class);
        startActivity(intent);
    }

    // Method for Motherboard button click
    public void selectMotherboard(View view) {
        Toast.makeText(this, "Motherboard selected", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Homepage.this, motherboard.class);
        startActivity(intent);
    }

    // Method for Cooler button click
    public void selectCooler(View view) {
        Toast.makeText(this, "Cooler selected", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Homepage.this, cooler.class);
        startActivity(intent);
    }
}
