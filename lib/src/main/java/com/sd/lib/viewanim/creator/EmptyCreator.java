package com.sd.lib.viewanim.creator;

import android.animation.Animator;
import android.view.View;

public final class EmptyCreator implements AnimatorCreator
{
    @Override
    public Animator createAnimator(boolean show, View view)
    {
        return null;
    }
}
