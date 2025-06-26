package com.example.gradconnect;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splash1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find Views
        TextView textView2 = findViewById(R.id.textView2);
        TextView textView3 = findViewById(R.id.textView3);
        TextView textView4 = findViewById(R.id.textView4);
        Button button = findViewById(R.id.button);

        // Set the initial visibility of text views and button to invisible (alpha 0)
        textView2.setAlpha(0f);
        textView3.setAlpha(0f);
        textView4.setAlpha(0f);
        button.setAlpha(0f);

        // Slide and fade-in animations
        ObjectAnimator textView2Animator = ObjectAnimator.ofFloat(textView2, "translationX", -1000f, 0f);
        textView2Animator.setDuration(1000);
        textView2Animator.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator textView2FadeIn = ObjectAnimator.ofFloat(textView2, "alpha", 0f, 1f);
        textView2FadeIn.setDuration(1000);

        ObjectAnimator textView3Animator = ObjectAnimator.ofFloat(textView3, "translationX", 1000f, 0f);
        textView3Animator.setDuration(1000);
        textView3Animator.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator textView3FadeIn = ObjectAnimator.ofFloat(textView3, "alpha", 0f, 1f);
        textView3FadeIn.setDuration(1000);

        ObjectAnimator textView4Animator = ObjectAnimator.ofFloat(textView4, "translationX", -1000f, 0f);
        textView4Animator.setDuration(1000);
        textView4Animator.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator textView4FadeIn = ObjectAnimator.ofFloat(textView4, "alpha", 0f, 1f);
        textView4FadeIn.setDuration(1000);

        // Button fade-in animation after text views have slid in
        ObjectAnimator buttonFadeIn = ObjectAnimator.ofFloat(button, "alpha", 0f, 1f);
        buttonFadeIn.setDuration(500);

        // Chain the animations
        AnimatorSet textView2Set = new AnimatorSet();
        textView2Set.playTogether(textView2Animator, textView2FadeIn);

        AnimatorSet textView3Set = new AnimatorSet();
        textView3Set.playTogether(textView3Animator, textView3FadeIn);

        AnimatorSet textView4Set = new AnimatorSet();
        textView4Set.playTogether(textView4Animator, textView4FadeIn);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(textView2Set, textView3Set, textView4Set, buttonFadeIn);
        animatorSet.start();

        // Set button click listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Splash1Activity.this, RollSelectionActivity.class);
                startActivity(intent);
            }
        });
    }
}
