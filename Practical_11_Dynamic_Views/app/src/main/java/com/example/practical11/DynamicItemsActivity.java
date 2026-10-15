package com.example.practical11;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * Activity that dynamically creates and renders the requested number of UI items in a ScrollView container.
 */
public class DynamicItemsActivity extends AppCompatActivity {

    private TextView tvSummaryHeader;
    private Button btnBack;
    private LinearLayout containerDynamicViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_items);

        tvSummaryHeader = findViewById(R.id.tvSummaryHeader);
        btnBack = findViewById(R.id.btnBack);
        containerDynamicViews = findViewById(R.id.containerDynamicViews);

        int count = getIntent().getIntExtra(MainActivity.EXTRA_ITEM_COUNT, 0);

        tvSummaryHeader.setText("Successfully generated " + count + " items dynamically:");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Generate items programmatically
        generateDynamicCards(count);
    }

    /**
     * Programmatically constructs view hierarchies for the given count.
     */
    private void generateDynamicCards(int count) {
        containerDynamicViews.removeAllViews();

        int dp10 = dpToPx(10);
        int dp14 = dpToPx(14);
        int dp8 = dpToPx(8);

        for (int i = 1; i <= count; i++) {
            final int itemIndex = i;

            // 1. Root Card Layout for each item
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.HORIZONTAL);
            card.setGravity(Gravity.CENTER_VERTICAL);
            card.setBackground(ContextCompat.getDrawable(this, R.drawable.card_bg));
            card.setPadding(dp14, dp14, dp14, dp14);

            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, dp10);
            card.setLayoutParams(cardParams);

            // 2. Circular Number Badge
            TextView tvBadge = new TextView(this);
            tvBadge.setText("#" + itemIndex);
            tvBadge.setTextColor(Color.WHITE);
            tvBadge.setTextSize(13);
            tvBadge.setTypeface(null, Typeface.BOLD);
            tvBadge.setGravity(Gravity.CENTER);
            tvBadge.setBackground(ContextCompat.getDrawable(this, R.drawable.item_badge));
            LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(dpToPx(38), dpToPx(38));
            tvBadge.setLayoutParams(badgeParams);

            // 3. Middle Content Container (Title + Subtitle)
            LinearLayout textContainer = new LinearLayout(this);
            textContainer.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            textParams.setMargins(dp14, 0, dp8, 0);
            textContainer.setLayoutParams(textParams);

            TextView tvTitle = new TextView(this);
            tvTitle.setText("Dynamically Generated Item " + itemIndex);
            tvTitle.setTextSize(15);
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.purple_700));
            tvTitle.setTypeface(null, Typeface.BOLD);

            TextView tvDesc = new TextView(this);
            tvDesc.setText("Created via Java code without XML templates.");
            tvDesc.setTextSize(12);
            tvDesc.setTextColor(ContextCompat.getColor(this, R.color.gray_700));

            textContainer.addView(tvTitle);
            textContainer.addView(tvDesc);

            // 4. Action Button inside the card
            Button btnAction = new Button(this, null, androidx.appcompat.R.attr.borderlessButtonStyle);
            btnAction.setText("Tap");
            btnAction.setTextColor(ContextCompat.getColor(this, R.color.purple_700));
            btnAction.setTextSize(13);
            btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(DynamicItemsActivity.this, "Clicked Item #" + itemIndex, Toast.LENGTH_SHORT).show();
                }
            });

            // Assemble the card
            card.addView(tvBadge);
            card.addView(textContainer);
            card.addView(btnAction);

            // Add the card to the container inside ScrollView
            containerDynamicViews.addView(card);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
