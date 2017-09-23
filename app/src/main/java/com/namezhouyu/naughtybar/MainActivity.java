package com.namezhouyu.naughtybar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.namezhouyu.naughtybar.listener.OnPushListener;
import com.namezhouyu.naughtybar.model.BarModel;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    NaughtyBarImpl impl = new NaughtyBarImpl.Builder().setView(findViewById(R.id.bar))
        .setAnimInId(R.anim.pop_in_anim)
        .setAnimOutId(R.anim.pop_out_anim)
        .setContext(MainActivity.this)
        .setOnPushListener(new OnPushListener() {
          @Override public void start() {
            Toast.makeText(MainActivity.this, "start", Toast.LENGTH_SHORT).show();
          }
        })
        .build();

    AnimationQueue animQueue = AnimationQueue.getInstance();
    animQueue.setImpl(impl);

    LinkedList<BarModel> data = new LinkedList<>();
    for (int i = 1; i <= 5; i++) {
      BarModel model = new BarModel();
      model.setCount(i);
      model.setName("徐晃");
      model.setTips("送出了火箭");
      data.addLast(model);
    }
    animQueue.enQueue(data);
    animQueue.work(false);
  }
}
