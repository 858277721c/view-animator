package com.sd.view_animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.viewanim.FVisibilityAnimator;
import com.sd.lib.viewanim.creator.obj.SlideTopBottomCreator;
import com.sd.view_animator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding mBinding;
    private FVisibilityAnimator mVisibilityAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mVisibilityAnimator = new FVisibilityAnimator(mBinding.viewAnimator);
        mVisibilityAnimator.setAnimatorCreator(new SlideTopBottomCreator());
        mVisibilityAnimator.addShowAnimatorListener(mShowListener);
        mVisibilityAnimator.addHideAnimatorListener(mHideListener);

        mVisibilityAnimator.addFollowVisibilityView(mBinding.viewFollowVisibility);
    }

    @Override
    public void onClick(View v)
    {
        if (v == mBinding.btnShow)
        {
            mVisibilityAnimator.startShow();
        } else if (v == mBinding.btnHide)
        {
            mVisibilityAnimator.startHide();
        }
    }

    /**
     * 显示监听
     */
    private final AnimatorListenerAdapter mShowListener = new AnimatorListenerAdapter()
    {
        @Override
        public void onAnimationStart(Animator animation)
        {
            super.onAnimationStart(animation);
            Log.i(TAG, "show onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            super.onAnimationEnd(animation);
            Log.i(TAG, "show onAnimationEnd");
        }
    };

    /**
     * 隐藏监听
     */
    private final AnimatorListenerAdapter mHideListener = new AnimatorListenerAdapter()
    {
        @Override
        public void onAnimationStart(Animator animation)
        {
            super.onAnimationStart(animation);
            Log.i(TAG, "hide onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            super.onAnimationEnd(animation);
            Log.i(TAG, "hide onAnimationEnd");
        }
    };
}