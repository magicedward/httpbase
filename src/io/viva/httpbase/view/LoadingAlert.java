package io.viva.httpbase.view;

import io.viva.httpbase.info.MobileInfo;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingAlert extends FrameLayout {
	private TextView mText;

	public LoadingAlert(Activity activity) {
		super(activity);
		createView(activity);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParams.gravity = Gravity.CENTER;
		activity.addContentView(this, layoutParams);
	}

	public LoadingAlert(Dialog dialog) {
		super(dialog.getContext());
		createView(dialog.getContext());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dialog.getWindow().peekDecorView().getWidth(), dialog.getWindow().peekDecorView().getHeight());
		layoutParams.gravity = Gravity.CENTER;
		dialog.addContentView(this, layoutParams);
	}

	public void createView(Context context) {
		LinearLayout linearLayout = new LinearLayout(context);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		linearLayout.setLayoutParams(layoutParams);
		linearLayout.setPadding(MobileInfo.dip2px(context, 10.0F), MobileInfo.dip2px(context, 20.0F), MobileInfo.dip2px(context, 10.0F), MobileInfo.dip2px(context, 20.0F));
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.setBackgroundColor(Color.TRANSPARENT);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		ProgressBar progressBar = new ProgressBar(context, null, android.R.style.Widget_ProgressBar_Small_Inverse);
		progressBar.setProgressDrawable(new ColorDrawable(Color.BLACK));
		LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(MobileInfo.dip2px(context, 25.0F), MobileInfo.dip2px(context, 25.0F));
		layoutParams1.setMargins(0, 0, MobileInfo.dip2px(context, 5.0F), 0);
		layoutParams1.gravity = Gravity.CENTER;
		progressBar.setLayoutParams(layoutParams1);
		linearLayout.addView(progressBar);
		this.mText = new TextView(context);
		this.mText.setTextColor(Color.BLACK);
		this.mText.setText("正在加载...");
		linearLayout.addView(this.mText);
		addView(linearLayout);
	}

	public void setMessage(String msg) {
		this.mText.setText(msg);
	}
}
