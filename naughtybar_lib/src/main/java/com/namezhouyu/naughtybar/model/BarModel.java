package com.namezhouyu.naughtybar.model;

/**
 * FileName：BarModel
 *
 * @Description： bars model
 * v1.0 https://github.com/namezhouyu 2017/9/22 Create
 */
public class BarModel {
  private String name;
  private String tips;
  private String avatar;
  private int count;

  public String getTips() {
    return tips;
  }

  public void setTips(String tips) {
    this.tips = tips;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
