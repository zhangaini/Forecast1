package gllibrary.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity管理类
 */
public class GLActivityManager {
	private static final String TAG = "ActivityManager";

	// 保存创建的所有activity
	private static List<Activity> activityStack ;
	
	private static GLActivityManager instance;
	// 保存创建的所有activity
	private static List<Activity> tempStack ;

	public static GLActivityManager getAppManager() {
		if (instance == null) {
			instance = new GLActivityManager();
		}
		if (activityStack == null) {
			activityStack = new ArrayList<Activity>();
		}
		if(tempStack == null){
			tempStack = new ArrayList<Activity>();
		}
		return instance;
	}

	/**
	 * 压栈
	 */
	public void addActivity(Activity activity) {
		activityStack.add(activity);
	}



	/**
	 * 结束当前Activity
	 */
	public void finishActivity(Activity activity) {
		if (null != activity){
				activityStack.remove(activity);
				activity = null;
		}
	}

	/**
	 * 关闭倒数二个
	 */
	public void finishLastTwoActivity(){
		if(activityStack.size()>2){
			 activityStack.get(activityStack.size()-2).finish();
			activityStack.get(activityStack.size()-1).finish();
		}
	}
	/**
	 * @Description: TODO(保留首页的Activity) 
	 * @return void    返回类型
	 */
	public void keepFirstActivity(){
		for (int i = 1; i < activityStack.size(); i++) {
			activityStack.get(i).finish();
		}
	}
	/**
	 * 结束所有Activity，但保留最后一个
	 */
	public void finishActivitiesAndKeepLastOne() {
		for (int i = 0, size = activityStack.size() - 1; i < size; i++) {
			activityStack.get(0).finish();
			activityStack.remove(0);
		}
	}


	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivityclass(Class<?> cls) {
		if (activityStack != null) {
			for (Activity activity : activityStack) {
				if (activity.getClass().equals(cls)) {
					this.activityStack.remove(activity);
					finishActivity(activity);
					break;
				}
			}
		}

	}
	/**
	 * 关闭所有Activity
	 */
	public void finishAllActivity(){


		if (activityStack.size() > 0) {
			for (int i = 0, size = activityStack.size(); i < size; i++) {
				if (null != activityStack.get(i)) {
					activityStack.get(i).finish();
				}
			}
			activityStack.clear();
			activityStack=null;
		}
	}
	/**
	 *  @author xujp
	 *  @time 2017/4/11 0011  上午 11:30
	 *  @describe 加入临时数据集合
	 */
	public void addTempAct(Activity act){
		tempStack.add(act);
	}
	/**
	 *  @author xujp
	 *  @time 2017/4/11 0011  上午 11:31
	 *  @describe 关闭临时数据集合
	 */
	public void closeTempAct(){

		if(tempStack!=null&&tempStack.size()>0){
			for (Activity act : tempStack) {
				activityStack.remove(act);
				act.finish();
			}
			tempStack.clear();
		}
	}

	/**
	 *  @author xujp
	 *  @time 2017/4/11 0011  上午 11:31
	 *  @describe 关闭临时数据集合
	 */
	public void closeTempAct(String cls){

		if(tempStack!=null&&tempStack.size()>0){
			for (int i = tempStack.size()-1; i >=0 ; i--) {
				if(tempStack.get(i).getClass().getSimpleName().equals(cls)){
					break;
				}else{
					activityStack.remove(tempStack.get(i));
					tempStack.get(i).finish();
				}
			}

		}

	}
	/**
	 *  @author xujp
	 *  @time 2017/4/11 0011  上午 11:31
	 *  @describe 关闭临时数据集合
	 */
	public void removeTempAct(Activity act){
		if(tempStack!=null&&tempStack.size()>0){
				activityStack.remove(act);
		}
	}
}