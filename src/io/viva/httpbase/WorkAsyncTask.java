package io.viva.httpbase;

import io.viva.httpbase.exception.ExceptionManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

public abstract class WorkAsyncTask<Result> extends AsyncTask<Object, Object, Result> {
	protected View[] mViews;
	private boolean mIsHttpTask = true;
	private Context mContext;
	private Exception mException = null;

	public WorkAsyncTask(Context context) {
		this(context, true, (View[]) null);
	}

	public WorkAsyncTask(Context context, View[] views) {
		this(context, true, views);
	}

	public WorkAsyncTask(Context context, boolean isHttpTask) {
		this(context, isHttpTask, (View[]) null);
	}

	public WorkAsyncTask(Context context, boolean isHttpTask, View[] views) {
		this.mContext = context;
		this.mViews = views;
	}

	protected final void onPreExecute() {
		try {
			if (this.mViews != null) {
				for (View v : this.mViews) {
					v.setEnabled(false);
				}
			}
			onPre();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected final Result doInBackground(Object[] objects) {
		try {
			if (isCancelled()) {
				return null;
			}
			return doProgress();
		} catch (Exception e) {
			this.mException = e;
		}
		return null;
	}

	protected final void onProgressUpdate(Object[] objects) {
		try {
			onUpdate(objects);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected final void onPostExecute(Result result) {
		if (this.mViews != null) {
			for (View v : this.mViews) {
				v.setEnabled(true);
			}
		}
		try {
			if (this.mException != null) {
				boolean bool = ExceptionManager.handleException(this.mContext, this.mException);
				if (!bool) {
					Log.e("WorkAsyncTask", "onPostExecute -- mException!=null -- ");
					onError(this.mException);
				}
				onPost(false, result);
			} else {
				onPost(true, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected final void onCancelled() {
		super.onCancelled();
		if (this.mViews != null) {
			for (View v : this.mViews) {
				v.setEnabled(true);
			}
		}
		try {
			if (this.mException != null) {
				boolean bool = ExceptionManager.handleException(this.mContext, this.mException);
				if (!bool) {
					onError(this.mException);
				}
				onCancel(false);
			} else {
				onCancel(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract Result doProgress() throws Exception;

	public void onPre() throws Exception {
	}

	public void onUpdate(Object[] paramArrayOfObject) throws Exception {
	}

	public void onPost(boolean paramBoolean, Result result) throws Exception {
	}

	public void onCancel(boolean paramBoolean) {
	}

	public void onError(Exception paramException) {
	}

	protected Context getContext() {
		return this.mContext;
	}
}
