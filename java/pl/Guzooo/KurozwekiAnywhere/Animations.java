package pl.Guzooo.KurozwekiAnywhere;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class Animations {

    public static void ShowCircle(View v, int centerX, int centerY, float radius){
        ViewAnimationUtils.createCircularReveal(v, centerX, centerY, 0, radius).start();
        v.setVisibility(View.VISIBLE);
    }

    public static void HideCircle(final View v, int centerX, int centerY, float radius){
        Animator anim = ViewAnimationUtils.createCircularReveal(v, centerX, centerY, radius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }

    public static ObjectAnimator StartSpinIcon(final View v){
        ObjectAnimator animRotation =  ObjectAnimator.ofFloat(v, "rotation", -360);
        animRotation.setRepeatMode(ValueAnimator.RESTART);
        animRotation.setRepeatCount(ValueAnimator.INFINITE);
        animRotation.setInterpolator(new LinearInterpolator());
        animRotation.setDuration(750);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            animRotation.setAutoCancel(true);
        }
        animRotation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(v.getRotation() == -360){
                    v.setRotation(0);
                }
            }
        });
        animRotation.start();
        return animRotation;
    }

    public static void ErrorSpinIcon(View v){
        ObjectAnimator animRotation = ObjectAnimator.ofFloat(v, "rotation", v.getRotation(), 40, -40, 15, -15, -5, 5, 0);
        animRotation.setDuration(1000);
        animRotation.start();
    }

    public static void ChangeIcon(final ImageView v, final int newIcon){
        ObjectAnimator animOutX = ObjectAnimator.ofFloat(v, "scaleX", 0);
        ObjectAnimator animOutY = ObjectAnimator.ofFloat(v, "scaleY", 0);
        ObjectAnimator animInX = ObjectAnimator.ofFloat(v, "scaleX", 1);
        ObjectAnimator animInY = ObjectAnimator.ofFloat(v, "scaleY", 1);
        AnimatorSet animOut = new AnimatorSet();
        final AnimatorSet animIn = new AnimatorSet();
        animOut.playTogether(animOutX, animOutY);
        animIn.playTogether(animInX, animInY);
        animOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setImageResource(newIcon);
            }
        });
        AnimatorSet animScale = new AnimatorSet();
        animScale.playSequentially(animOut, animIn);
        animScale.start();
    }
}
