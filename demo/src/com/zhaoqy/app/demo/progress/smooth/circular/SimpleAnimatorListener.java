package com.zhaoqy.app.demo.progress.smooth.circular;

import android.animation.Animator;
import android.annotation.SuppressLint;

@SuppressLint("NewApi")
abstract class SimpleAnimatorListener implements Animator.AnimatorListener{
  private boolean mStarted = false;
  private boolean mCancelled = false;

  @Override
  public void onAnimationStart(Animator animation) {
    mCancelled = false;
    mStarted = true;
  }

  @Override
  public final void onAnimationEnd(Animator animation) {
    onPreAnimationEnd(animation);
    mStarted = false;
  }

  protected void onPreAnimationEnd(Animator animation) {
  }

  @Override
  public void onAnimationCancel(Animator animation) {
    mCancelled = true;
  }

  @Override
  public void onAnimationRepeat(Animator animation) {

  }

  public boolean isStartedAndNotCancelled() {
    return mStarted && !mCancelled;
  }
}
