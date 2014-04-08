package io.viva.httpbase;

import io.viva.httpbase.view.LoadingAlert;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class BaseActivity extends Activity {
	private ArrayList<WorkAsyncTask<?>> mTaskList;
	private LoadingAlert mLoading;
	private Animation mShowAction = null;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	}

	protected void onDestroy() {
		super.onDestroy();
		cancelTask();
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	public void runTask(WorkAsyncTask<?> workAsyncTask) {
		if (this.mTaskList == null)
			this.mTaskList = new ArrayList<WorkAsyncTask<?>>();
		try {
			this.mTaskList.add(workAsyncTask);
			workAsyncTask.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Animation showAnimation() {
		this.mShowAction = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, -1.0F, 1, 0.0F);
		this.mShowAction.setDuration(150L);
		return this.mShowAction;
	}

	public void cancelTask() {
		if (this.mTaskList != null) {
			Iterator<WorkAsyncTask<?>> it = this.mTaskList.iterator();
			while (it.hasNext()) {
				WorkAsyncTask<?> workAsyncTask = it.next();
				if (!workAsyncTask.isCancelled())
					try {
						workAsyncTask.cancel(true);
						workAsyncTask = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
		this.mTaskList = null;
	}

	public void showLoading() {
		showLoading("正在加载...");
	}

	/**
	 * 显示loading dialog
	 * 
	 * @param msg
	 */
	public void showLoading(String msg) {
		if (this.mLoading == null) {
			this.mLoading = new LoadingAlert(this);
		}
		this.mLoading.setMessage(msg);
		this.mLoading.setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏loading dialog
	 */
	public void hideLoading() {
		if (this.mLoading != null) {
			this.mLoading.setVisibility(View.GONE);
		}
	}

	public void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int resId) {
		Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
	}

	public void alert(int msgId) {
		alert(msgId, "提示");
	}

	public void alert(String msg) {
		alert(msg, "提示");
	}

	public void alert(int msgId, String title) {
		alert(getString(msgId), title);
	}

	public void alert(int msgId, int titleId) {
		alert(getString(msgId), getString(titleId));
	}

	public void alert(String msg, String title) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		}).create().show();
	}

	public void hideSoftKeyBoard(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService("input_method");
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void hideSoftKeyBoard() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService("input_method");
		View view = getCurrentFocus();
		if (view != null) {
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public void showSoftKeyBoard() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService("input_method");
		inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void showSoftKeyBoard(View view) {
		view.setFocusable(true);
		view.requestFocus();
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService("input_method");
		inputMethodManager.showSoftInput(view, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
}
