package ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.smartshoppinglist.R;

public class AddMenuActivity extends AppCompatActivity {

    Button newButton;
    Button productButton;
    Button scanButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        setTitle("Add Menu");

        initBtns();
    }

    private void initBtns() {
        newButton = findViewById(R.id.newButton);
        productButton = findViewById(R.id.productButton);
        scanButton = findViewById(R.id.scanButton);
        cancelButton = findViewById(R.id.cancelButton);

        newButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EntryActivity.class);
            intent.putExtra("direct", true);
            startActivity(intent);
        });

        productButton.setOnClickListener(v1 -> {
            Intent intent = new Intent(this, ProductActivity.class);
            intent.putExtra("add", true);
            startActivity(intent);
        });

        scanButton.setOnClickListener(v1 -> {
            Intent intent = new Intent(this, ScannerActivity.class);
            startActivity(intent);
        });

        cancelButton.setOnClickListener(v1 -> {
            onBackPressed();
        });
    }
}