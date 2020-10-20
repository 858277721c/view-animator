package com.sd.lib.viewanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import com.sd.lib.viewanim.creator.AnimatorCreator;
import com.sd.lib.viewanim.creator.EmptyCreator;

public class FVisibilityAnimator
{
    private final View mView;
    private final FVisibilityAnimatorHandler mAnimatorHandler = new FVisibilityAnimatorHandler();
    private AnimatorCreator mAnimatorCreator;

    public FVisibilityAnimator(View view)
    {
        if (view == null)
            throw new NullPointerException("view is null");

        mView = view;
        mAnimatorHandler.setShowAnimatorListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                super.onAnimationStart(animation);
                final View animView = getView();
                if (animView != null)
                {
                    if (animView.getVisibility() != View.VISIBLE)
                        animView.setVisibility(View.VISIBLE);
                }
            }
        });
        view.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
    }

    /**
     * 动画View
     *
     * @return
     */
    public View getView()
    {
        return mView;
    }

    private AnimatorCreator getAnimatorCreator()
    {
        if (mAnimatorCreator == null)
            mAnimatorCreator = new EmptyCreator();
        return mAnimatorCreator;
    }

    /**
     * 动画创建者
     *
     * @param creator
     */
    public void setAnimatorCreator(AnimatorCreator creator)
    {
        mAnimatorCreator = creator;
    }

    /**
     * 添加显示动画监听
     *
     * @param listener
     */
    public void addShowAnimatorListener(Animator.AnimatorListener listener)
    {
        mAnimatorHandler.addShowAnimatorListener(listener);
    }

    /**
     * 移除显示动画监听
     *
     * @param listener
     */
    public void removeShowAnimatorListener(Animator.AnimatorListener listener)
    {
        mAnimatorHandler.removeShowAnimatorListener(listener);
    }

    /**
     * 添加隐藏动画监听
     *
     * @param listener
     */
    public void addHideAnimatorListener(Animator.AnimatorListener listener)
    {
        mAnimatorHandler.addHideAnimatorListener(listener);
    }

    /**
     * 移除隐藏动画监听
     *
     * @param listener
     */
    public void removeHideAnimatorListener(Animator.AnimatorListener listener)
    {
        mAnimatorHandler.removeHideAnimatorListener(listener);
    }

    /**
     * 开始显示动画
     *
     * @return true-动画被执行
     */
    public boolean startShowAnimator()
    {
        if (isShowAnimatorStarted())
            return true;

        final Animator animator = getAnimatorCreator().createAnimator(true, getView());
        if (animator == null)
            return false;

        cancelHideAnimator();
        mAnimatorHandler.setShowAnimator(animator);
        return mAnimatorHandler.startShowAnimator();
    }

    /**
     * 显示动画是否已经开始
     *
     * @return
     */
    public boolean isShowAnimatorStarted()
    {
        return mAnimatorHandler.isShowAnimatorStarted();
    }

    /**
     * 取消显示动画
     */
    public void cancelShowAnimator()
    {
        mAnimatorHandler.cancelShowAnimator();
    }

    /**
     * 开始隐藏动画
     *
     * @return true-动画被执行
     */
    public boolean startHideAnimator()
    {
        if (isHideAnimatorStarted())
            return true;

        final Animator animator = getAnimatorCreator().createAnimator(false, getView());
        if (animator == null)
            return false;

        cancelShowAnimator();
        mAnimatorHandler.setHideAnimator(animator);
        return mAnimatorHandler.startHideAnimator();
    }

    /**
     * 隐藏动画是否已经开始执行
     *
     * @return
     */
    public boolean isHideAnimatorStarted()
    {
        return mAnimatorHandler.isHideAnimatorStarted();
    }

    /**
     * 取消隐藏动画
     */
    public void cancelHideAnimator()
    {
        mAnimatorHandler.cancelHideAnimator();
    }

    private final View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener()
    {
        @Override
        public void onViewAttachedToWindow(View v)
        {

        }

        @Override
        public void onViewDetachedFromWindow(View v)
        {
            if (isShowAnimatorStarted())
                cancelShowAnimator();

            if (isHideAnimatorStarted())
                cancelHideAnimator();
        }
    };
}
