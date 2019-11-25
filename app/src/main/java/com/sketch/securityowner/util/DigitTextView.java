package com.sketch.securityowner.util;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sketch.securityowner.R;

import java.util.ArrayList;
import java.util.Locale;

public class DigitTextView extends FrameLayout {

    private static int ANIMATION_DURATION = 250;
    TextView currentTextView, nextTextView;

    public DigitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DigitTextView(Context context) {
        super(context);
        init(context);
    }



    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.digit_text_view, this);
        currentTextView = getRootView().findViewById(R.id.currentTextView);
        nextTextView = getRootView().findViewById(R.id.nextTextView);

        nextTextView.setTranslationY(getHeight());

    }



    public void setValue22222222(final int desiredValue) {
        if (currentTextView.getText() == null || currentTextView.getText().length() == 0) {
            currentTextView.setText(String.format(Locale.getDefault(), "%d", desiredValue));
        }

        final int oldValue = Integer.parseInt(currentTextView.getText().toString());

        if (oldValue > desiredValue) {
            nextTextView.setText(String.format(Locale.getDefault(), "%d", oldValue-1));

            currentTextView.animate().translationY(-getHeight()).setDuration(ANIMATION_DURATION).start();
            nextTextView.setTranslationY(nextTextView.getHeight());
            nextTextView.animate().translationY(0).setDuration(ANIMATION_DURATION).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}
                @Override
                public void onAnimationEnd(Animator animation) {
                    currentTextView.setText(String.format(Locale.getDefault(), "%d", oldValue - 1));
                    currentTextView.setTranslationY(0);
                    if (oldValue - 1 != desiredValue) {
                        setValue22222222(desiredValue);
                    }
                }
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            }).start();
        } else if (oldValue < desiredValue) {
            nextTextView.setText(String.format(Locale.getDefault(), "%d", oldValue+1));

            currentTextView.animate().translationY(getHeight()).setDuration(ANIMATION_DURATION).start();
            nextTextView.setTranslationY(-nextTextView.getHeight());
            nextTextView.animate().translationY(0).setDuration(ANIMATION_DURATION).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}
                @Override
                public void onAnimationEnd(Animator animation) {
                    currentTextView.setText(String.format(Locale.getDefault(), "%d", oldValue + 1));
                    currentTextView.setTranslationY(0);
                    if (oldValue + 1 != desiredValue) {
                        setValue22222222(desiredValue);
                    }
                }
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            }).start();
        }
    }


    public void setValue(final String desiredValue, String roll) {
        if (currentTextView.getText() == null || currentTextView.getText().length() == 0) {
            currentTextView.setText(desiredValue);
        }


        if (roll.equals("up")) {

            nextTextView.setText(desiredValue);

            currentTextView.setVisibility(VISIBLE);

            currentTextView.animate().translationY(-getHeight()).setDuration(ANIMATION_DURATION).start();
            nextTextView.setTranslationY(nextTextView.getHeight());
            nextTextView.animate()
                    .translationY(0)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}
                @Override
                public void onAnimationEnd(Animator animation) {
                    nextTextView.setText(desiredValue);
                    currentTextView.setTranslationY(0);

                    currentTextView.setVisibility(GONE);

                  //  Log.d("TAG", "nextTextView = "+nextTextView.getText().toString());
                  //  Log.d("TAG", "currentTextView = "+currentTextView.getText().toString());

                }
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            }).start();

        } else if (roll.equals("down")) {
            nextTextView.setText(desiredValue);

            currentTextView.setVisibility(VISIBLE);

            currentTextView.animate().translationY(getHeight()).setDuration(ANIMATION_DURATION).start();
            nextTextView.setTranslationY(-nextTextView.getHeight());
            nextTextView.animate()
                    .translationY(0)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}
                @Override
                public void onAnimationEnd(Animator animation) {
                    nextTextView.setText(desiredValue);
                    currentTextView.setTranslationY(0);

                    currentTextView.setVisibility(GONE);

                    Log.d("TAG", "nextTextView = "+nextTextView.getText().toString());
                    Log.d("TAG", "currentTextView = "+currentTextView.getText().toString());

                }
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            }).start();
        }
    }
}