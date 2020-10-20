package com.sd.view_animator;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.viewanim.FVisibilityAnimator;
import com.sd.lib.viewanim.creator.obj.SlideTopBottomCreator;
import com.sd.view_animator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
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
    }

    @Override
    public void onClick(View v)
    {
        if (v == mBinding.btnShow)
        {
            mVisibilityAnimator.startShowAnimator();
        } else if (v == mBinding.btnHide)
        {
            mVisibilityAnimator.startHideAnimator();
        }
    }
}