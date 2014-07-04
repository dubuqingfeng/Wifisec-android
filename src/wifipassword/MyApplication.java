package wifipassword;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application
{
  private static MyApplication instance;
  private List<Activity> activityList = new LinkedList();

  public static MyApplication getInstance()
  {
    if (instance == null)
      instance = new MyApplication();
    return instance;
  }

  public void addActivity(Activity paramActivity)
  {
    this.activityList.add(paramActivity);
  }

  public void exit()
  {
    Iterator localIterator = this.activityList.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
      {
        System.exit(0);
        return;
      }
      ((Activity)localIterator.next()).finish();
    }
  }
}