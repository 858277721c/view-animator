package com.sd.lib.viewanim.creator;

import android.animation.Animator;
import android.view.View;

public interface AnimatorCreator
{
    /**
     * 创建动画
     *
     * @param show true-显示，false-隐藏
     * @param view 动画view
     * @return
     */
    Animator createAnimator(boolean show, View view);
}