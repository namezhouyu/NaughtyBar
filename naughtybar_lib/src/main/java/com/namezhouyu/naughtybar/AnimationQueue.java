package com.namezhouyu.naughtybar;

import android.os.Handler;
import android.os.Message;
import com.namezhouyu.naughtybar.listener.OnCloseListener;
import com.namezhouyu.naughtybar.model.BarModel;
import java.util.LinkedList;
import java.util.Random;

import static com.namezhouyu.naughtybar.AnimationQueue.Type.ANIM_IN;
import static com.namezhouyu.naughtybar.AnimationQueue.Type.ANIM_OUT;

/**
 * FileName：AnimationQueue
 *
 * @Description： message queue
 * v1.0 https://github.com/namezhouyu 2017/8/28 Create
 */
public class AnimationQueue implements OnCloseListener {
  private static AnimationQueue animationQueue;
  private Thread thread;
  private AnimImplHandler handler = new AnimImplHandler();
  private NaughtyBarImpl impl;
  private LinkedList<BarModel> list = new LinkedList<>();
  private int QUEUE_SIZE = 10;//Queue size
  private long TIME_LENGTH = 10 * 1000;//Message presentation time
  private boolean advClosedLoop = true;//A message`s animation complete
  private boolean canQueueUse = true;//Whether the queue is available

  /**
   * @return Whether the queue runnable
   */
  private boolean canQueueRun() {
    return advClosedLoop && canQueueUse;
  }

  public static AnimationQueue getInstance() {
    if (null == animationQueue) {
      animationQueue = new AnimationQueue();
    }
    return animationQueue;
  }

  private AnimationQueue() {
    Runnable runnable = new Runnable() {
      @Override public void run() {
        BarModel model = deQueue();
        Message message = new Message();
        message.obj = model;
        //pop out
        message.what = ANIM_IN.getCode();
        handler.sendMessage(message);
        //close
        handler.sendEmptyMessageDelayed(ANIM_OUT.getCode(), TIME_LENGTH);
      }
    };
    thread = new Thread(runnable);
  }

  public void setImpl(NaughtyBarImpl impl) {
    if (null != impl) {
      impl.setOnCloseListener(this);
      this.impl = impl;
    }
  }

  /**
   * 入队
   */
  public void enQueue(LinkedList<BarModel> models) {
    int addSize = models.size();
    int lessSize = QUEUE_SIZE - list.size();
    //If the new length is greater than the remaining length, the element is eliminated from the head
    if (addSize > lessSize) {
      int removeCount = addSize - lessSize;
      for (int i = 0; i < removeCount; i++) {
        list.removeFirst();
      }
    }
    //Add new elements to the end of the team
    for (int i = 0; i < models.size(); i++) {
      list.addLast(models.get(i));
    }
  }

  /**
   * Out of the team
   */
  public BarModel deQueue() {
    if (!list.isEmpty()) {
      return list.removeFirst();
    }
    return null;
  }

  /**
   * Random 3 - 10s begin to work
   */
  Random random = new Random();
  int max = 10;
  int min = 3;
  Runnable runnable = new Runnable() {
    @Override public void run() {
      if (canQueueUse) {
        thread.run();
      }
    }
  };

  /**
   * working
   *
   * @param workDelayed Whether to delay work
   */
  public synchronized void work(boolean workDelayed) {
    if (!list.isEmpty()) {
      if (canQueueRun()) {
        int randomNum = random.nextInt(max) % (max - min + 1) + min;
        advClosedLoop = false;
        if (workDelayed) {
          handler.postDelayed(runnable, randomNum * 1000);
        } else {
          if (canQueueUse) {
            thread.run();
          }
        }
      }
    }
  }

  /**
   * pause
   */
  public void pause() {
    canQueueUse = false;
    handler.removeMessages(ANIM_IN.getCode());
    handler.removeMessages(ANIM_OUT.getCode());
    if (null != impl) {
      impl.pause();
    }
  }

  /**
   * resume
   */
  public void resume() {
    canQueueUse = true;
    advClosedLoop = true;
    this.work(false);
  }

  /**
   * Popup window closing animation ends
   */
  @Override public void onEnd() {
    advClosedLoop = true;
    this.work(true);
  }

  /**
   * destroy
   */
  public void destroy() {
    handler.removeCallbacksAndMessages(null);
    animationQueue = null;
    advClosedLoop = true;
    canQueueUse = true;
  }

  /**
   * Pop up or shrink
   */
  enum Type {
    ANIM_IN(0), ANIM_OUT(1);
    int code;

    Type(int code) {
      this.code = code;
    }

    public int getCode() {
      return code;
    }
  }

  /**
   * Use the handler to pop up the pop-up ads in the queue
   */
  public class AnimImplHandler extends Handler {
    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (canQueueUse) {
        if (ANIM_IN.getCode() == msg.what) {
          BarModel model = (BarModel) msg.obj;
          if (null != impl && null != model) {
            impl.setData(model);
            impl.animIn();
          }
        } else if (ANIM_OUT.getCode() == msg.what) {
          impl.animOut();
        }
      }
    }
  }
}
