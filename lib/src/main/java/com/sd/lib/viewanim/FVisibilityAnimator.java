package com.sd.lib.viewanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import com.sd.lib.viewanim.creator.AnimatorCreator;
import com.sd.lib.viewanim.creator.EmptyCreator;

import java.util.Map;
import java.util.WeakHashMap;

public class FVisibilityAnimator
{
    private final View mView;
    private final FVisibilityAnimatorHandler mAnimatorHandler = new FVisibilityAnimatorHandler();
    private final FViewSizeChecker mViewSizeChecker = new FViewSizeChecker();

    private int mHideVisibility = View.INVISIBLE;
    private AnimatorCreator mAnimatorCreator;

    private Map<View, String> mFollowVisibilityViewHolder;

    public FVisibilityAnimator(View view)
    {
        if (view == null)
            throw new NullPointerException("view is null");

        mView = view;
        view.addOnAttachStateChangeListener(mOnAttachStateChangeListener);

        mAnimatorHandler.setShowAnimatorListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                super.onAnimationStart(animation);
                showView();
            }
        });
        mAnimatorHandler.setHideAnimatorListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                hideView();
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
     * 设置隐藏的状态值
     *
     * @param visibility {@link View#INVISIBLE}或者{@link View#GONE}
     */
    public void setHideVisibility(int visibility)
    {
        if (visibility == View.INVISIBLE || visibility == View.GONE)
        {
            mHideVisibility = visibility;
        } else
        {
            throw new IllegalArgumentException("Illegal visibility value. View.INVISIBLE or View.GONE required");
        }
    }

    /**
     * 添加跟随可见状态View
     *
     * @param view
     */
    public void addFollowVisibilityView(View view)
    {
        if (view == null)
            return;

        if (mFollowVisibilityViewHolder == null)
            mFollowVisibilityViewHolder = new WeakHashMap<>();

        mFollowVisibilityViewHolder.put(view, "");
        synchronizeFollowViewVisibility(view);
    }

    /**
     * 移除跟随可见状态View
     *
     * @param view
     */
    public void removeFollowVisibilityView(View view)
    {
        if (view == null)
            return;

        if (mFollowVisibilityViewHolder != null)
        {
            mFollowVisibilityViewHolder.remove(view);
            if (mFollowVisibilityViewHolder.isEmpty())
                mFollowVisibilityViewHolder = null;
        }
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
     * 开始显示
     */
    public void startShow()
    {
        if (isShowAnimatorStarted())
            return;

        final View view = getView();
        if (view.getVisibility() == View.GONE)
            view.setVisibility(View.INVISIBLE);

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
                } else
                {
                    showView();
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
        destroySizeChecker();
    }

    /**
     * 开始隐藏，如果不满足动画执行条件，则直接隐藏
     * <p>
     * 动画执行条件：view被添加到ui上，并且宽高都大于0
     *
     * @return true-动画被成功发起
     */
    public boolean startHide()
    {
        if (isHideAnimatorStarted())
            return true;

        destroySizeChecker();

        final View view = getView();
        if (view.getVisibility() != View.VISIBLE)
        {
            // 如果看不见，不执行动画
            hideView();
            return false;
        }

        if (!mViewSizeChecker.checkReady(view))
        {
            // 如果未准备好，不执行动画
            hideView();
            return false;
        }

        final Animator animator = getAnimatorCreator().createAnimator(false, view);
        if (animator != null)
        {
            cancelShowAnimator();
            mAnimatorHandler.setHideAnimator(animator);
            return mAnimatorHandler.startHideAnimator();
        } else
        {
            hideView();
            return false;
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
    }

    private void destroySizeChecker()
    {
        mViewSizeChecker.destroy();
    }

    private void showView()
    {
        if (mView.getVisibility() != View.VISIBLE)
        {
            mView.setVisibility(View.VISIBLE);
            synchronizeFollowViewVisibility();
        }
    }

    private void hideView()
    {
        if (mView.getVisibility() != mHideVisibility)
        {
            mView.setVisibility(mHideVisibility);
            synchronizeFollowViewVisibility();
        }
    }

    private void synchronizeFollowViewVisibility()
    {
        if (mFollowVisibilityViewHolder == null)
            return;

        for (View item : mFollowVisibilityViewHolder.keySet())
        {
            synchronizeFollowViewVisibility(item);
        }
    }

    private void synchronizeFollowViewVisibility(View view)
    {
        final int visibility = mView.getVisibility();
        if (view.getVisibility() != visibility)
            view.setVisibility(visibility);
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
