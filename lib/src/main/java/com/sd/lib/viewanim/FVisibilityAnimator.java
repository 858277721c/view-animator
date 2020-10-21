package com.sd.lib.viewanim;

import android.animation.Animator;
import android.view.View;

import com.sd.lib.viewanim.creator.AnimatorCreator;
import com.sd.lib.viewanim.creator.EmptyCreator;

public class FVisibilityAnimator
{
    private final View mView;
    private final FVisibilityAnimatorHandler mAnimatorHandler = new FVisibilityAnimatorHandler();
    private final FViewSizeChecker mViewSizeChecker = new FViewSizeChecker();

    private int mHideVisibility = View.INVISIBLE;
    private AnimatorCreator mAnimatorCreator;

    public FVisibilityAnimator(View view)
    {
        if (view == null)
            throw new NullPointerException("view is null");

        mView = view;
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
     * 设置隐藏的可见状态
     *
     * @param visibility
     */
    public void setHideVisibility(int visibility)
    {
        if (visibility == View.INVISIBLE || visibility == View.GONE)
            mHideVisibility = visibility;
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
     */
    public void startShowAnimator()
    {
        if (isShowAnimatorStarted())
            return;

        final View view = getView();
        if (view.getVisibility() != View.VISIBLE)
            view.setVisibility(View.VISIBLE);

        mViewSizeChecker.check(view, new FViewSizeChecker.Callback()
        {
            @Override
            public void onSizeReady()
            {
                final Animator animator = getAnimatorCreator().createAnimator(true, view);
                if (animator != null)
                {
                    cancelHideAnimator();
                    mAnimatorHandler.setShowAnimator(animator);
                    mAnimatorHandler.startShowAnimator();
                }
            }
        });
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
        mViewSizeChecker.destroy();
    }

    /**
     * 开始隐藏动画
     */
    public void startHideAnimator()
    {
        if (isHideAnimatorStarted())
            return;

        final View view = getView();
        if (!mViewSizeChecker.checkReady(view))
        {
            if (view.getVisibility() != mHideVisibility)
                view.setVisibility(mHideVisibility);
            return;
        }

        final Animator animator = getAnimatorCreator().createAnimator(false, getView());
        if (animator != null)
        {
            cancelShowAnimator();
            mAnimatorHandler.setHideAnimator(animator);
            mAnimatorHandler.startHideAnimator();
        }
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
        mViewSizeChecker.destroy();
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
            cancelShowAnimator();
            cancelHideAnimator();
        }
    };
}
