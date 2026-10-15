package com.example.practical13;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity hosting the CustomCanvasView and providing UI controls
 * to toggle drawing styles and reset the interactive canvas.
 */
public class MainActivity extends AppCompatActivity {

    private CustomCanvasView customCanvasView;
    private Button btnToggleMode, btnResetCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customCanvasView = findViewById(R.id.customCanvasView);
        btnToggleMode = findViewById(R.id.btnToggleMode);
        btnResetCanvas = findViewById(R.id.btnResetCanvas);

        btnToggleMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCanvasView.toggleStyle();
                Toast.makeText(MainActivity.this, "Switched Canvas Color Palette!", Toast.LENGTH_SHORT).show();
            }
        });

        btnResetCanvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCanvasView.resetCanvas();
                Toast.makeText(MainActivity.this, "Canvas Reset", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
