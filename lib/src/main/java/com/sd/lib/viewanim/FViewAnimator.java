package com.sd.lib.viewanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import com.sd.lib.viewanim.creator.AnimatorCreator;
import com.sd.lib.viewanim.creator.EmptyCreator;
import com.sd.lib.viewanim.utils.FVisibilityAnimatorHandler;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

public class FViewAnimator
{
    private final WeakReference<View> mView;
    private final FVisibilityAnimatorHandler mAnimatorHandler = new FVisibilityAnimatorHandler();
    private AnimatorCreator mAnimatorCreator;

    private FViewAnimator(View view)
    {
        mView = new WeakReference<>(view);
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
    }

    /**
     * 动画View
     *
     * @return
     */
    public View getView()
    {
        return mView.get();
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

        cancelHideAnimator();
        final Animator animator = getAnimatorCreator().createAnimator(true, getView());
        if (animator == null)
            return false;

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

        cancelShowAnimator();
        final Animator animator = getAnimatorCreator().createAnimator(false, getView());
        if (animator == null)
            return false;

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

    //---------- static ----------

    private static final Map<View, FViewAnimator> MAP_INSTANCE = new WeakHashMap<>();

    public static synchronized FViewAnimator of(View view)
    {
        if (view == null)
            return null;

        FViewAnimator animator = MAP_INSTANCE.get(view);
        if (animator == null)
        {
            animator = new FViewAnimator(view);
            MAP_INSTANCE.put(view, animator);
        }
        return animator;
    }
}
