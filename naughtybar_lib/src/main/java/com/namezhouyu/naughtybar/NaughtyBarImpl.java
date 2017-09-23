package com.namezhouyu.naughtybar;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.namezhouyu.naughtybar.listener.OnCloseListener;
import com.namezhouyu.naughtybar.listener.OnPushListener;
import com.namezhouyu.naughtybar.model.BarModel;
import com.namezhouyu.naughtybar_lib.R;

/**
 * FileName：NaughtyBarImpl
 *
 * @Description： naughty bars impl
 * v1.0 https://github.com/namezhouyu 2017/9/22 Create
 */
public class NaughtyBarImpl {
  private View view;
  private Animation animationIn;
  private Animation animationOut;
  private OnCloseListener onCloseListener;
  private OnPushListener onPushListener;
  private Holder holder;

  /**
   * set the bar close listener
   *
   * @param onCloseListener close listener
   */
  public void setOnCloseListener(OnCloseListener onCloseListener) {
    this.onCloseListener = onCloseListener;
  }

  /**
   * set the bar push listener
   *
   * @param onPushListener push listener
   */
  public void setOnPushListener(OnPushListener onPushListener) {
    this.onPushListener = onPushListener;
  }

  /**
   * set the bar view
   *
   * @param view the bar view
   */
  public void setView(View view) {
    this.view = view;
    holder = new Holder();
    holder.txt1 = (TextView) view.findViewById(R.id.txt_tips1);
    holder.txt2 = (TextView) view.findViewById(R.id.txt_tips2);
    holder.txtCount = (TextView) view.findViewById(R.id.txt_count);
  }

  /**
   * set pop in animation
   *
   * @param animationIn pop in animation
   */
  public void setAnimationIn(Animation animationIn) {
    this.animationIn = animationIn;
  }

  /**
   * set pop out animation
   *
   * @param animationOut pop out animation
   */
  public void setAnimationOut(Animation animationOut) {
    this.animationOut = animationOut;
  }

  /**
   * set the bar`s data
   *
   * @param data bar`s data
   */
  public void setData(BarModel data) {
    holder.txt1.setText(data.getName());
    holder.txt2.setText(data.getTips());
    holder.txtCount.setText("X" + data.getCount());
  }

  /**
   * the pop in animation start
   */
  public void animIn() {
    if (null != animationIn) {
      animationIn.setAnimationListener(new Animation.AnimationListener() {
        @Override public void onAnimationStart(Animation animation) {
          if (null != onPushListener) {
            onPushListener.start();
          }
          view.setVisibility(View.VISIBLE);
        }

        @Override public void onAnimationEnd(Animation animation) {

        }

        @Override public void onAnimationRepeat(Animation animation) {

        }
      });
      view.startAnimation(animationIn);
    }
  }

  /**
   * the pop out animation start
   */
  public void animOut() {
    if (null != animationOut) {
      animationOut.setAnimationListener(new Animation.AnimationListener() {
        @Override public void onAnimationStart(Animation animation) {

        }

        @Override public void onAnimationEnd(Animation animation) {
          view.setVisibility(View.GONE);
          if (null != onCloseListener) {
            onCloseListener.onEnd();
          }
        }

        @Override public void onAnimationRepeat(Animation animation) {

        }
      });
      view.startAnimation(animationOut);
    }
  }

  public void pause() {
    if (null != view) {
      view.setVisibility(View.GONE);
    }
  }

  /**
   * bar view holder
   */
  public static class Holder {
    ImageView advImage;
    TextView txt1;
    TextView txt2;
    TextView txtCount;
  }

  /**
   * the impl builder factory
   */
  public static class Builder {
    private View view;
    private int animInId = -1;
    private int animOutId = -1;
    private Context context;
    OnPushListener onPushListener;

    public Builder setOnPushListener(OnPushListener onPushListener) {
      this.onPushListener = onPushListener;
      return this;
    }

    public Builder setContext(Context context) {
      this.context = context;
      return this;
    }

    public Builder setView(View view) {
      this.view = view;
      return this;
    }

    public Builder setAnimInId(int id) {
      this.animInId = id;
      return this;
    }

    public Builder setAnimOutId(int id) {
      this.animOutId = id;
      return this;
    }

    public NaughtyBarImpl build() {
      NaughtyBarImpl impl = new NaughtyBarImpl();
      if (null != this.context && -1 != this.animInId && -1 != this.animOutId && null != view) {
        Animation animation1 = AnimationUtils.loadAnimation(this.context, this.animInId);
        Animation animation2 = AnimationUtils.loadAnimation(this.context, this.animOutId);
        impl.setAnimationIn(animation1);
        impl.setAnimationOut(animation2);
        impl.setView(this.view);
        impl.setOnPushListener(this.onPushListener);
        return impl;
      }
      return null;
    }
  }
}
