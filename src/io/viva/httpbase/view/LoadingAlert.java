package io.viva.httpbase.view;

import io.viva.base.info.MobileInfo;
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

	public LoadingAlert(Activity paramActivity) {
		super(paramActivity);
		createView(paramActivity);
		LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		localLayoutParams.gravity = Gravity.CENTER;
		paramActivity.addContentView(this, localLayoutParams);
	}

	public LoadingAlert(Dialog paramDialog) {
		super(paramDialog.getContext());
		createView(paramDialog.getContext());
		LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(paramDialog.getWindow().peekDecorView().getWidth(), paramDialog.getWindow().peekDecorView()
				.getHeight());
		localLayoutParams.gravity = Gravity.CENTER;
		paramDialog.addContentView(this, localLayoutParams);
	}

	public void createView(Context paramContext) {
		LinearLayout localLinearLayout = new LinearLayout(paramContext);
		FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		localLayoutParams.gravity = Gravity.CENTER;
		localLinearLayout.setLayoutParams(localLayoutParams);
		localLinearLayout.setPadding(MobileInfo.dip2px(paramContext, 10.0F), MobileInfo.dip2px(paramContext, 20.0F), MobileInfo.dip2px(paramContext, 10.0F),
				MobileInfo.dip2px(paramContext, 20.0F));
		localLinearLayout.setGravity(Gravity.CENTER);
		localLinearLayout.setBackgroundColor(Color.TRANSPARENT);
		localLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		ProgressBar localProgressBar = new ProgressBar(paramContext, null, android.R.style.Widget_ProgressBar_Small_Inverse);
		localProgressBar.setProgressDrawable(new ColorDrawable(Color.BLACK));
		LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(MobileInfo.dip2px(paramContext, 25.0F), MobileInfo.dip2px(paramContext, 25.0F));
		localLayoutParams1.setMargins(0, 0, MobileInfo.dip2px(paramContext, 5.0F), 0);
		localLayoutParams1.gravity = Gravity.CENTER;
		localProgressBar.setLayoutParams(localLayoutParams1);
		localLinearLayout.addView(localProgressBar);
		this.mText = new TextView(paramContext);
		this.mText.setTextColor(Color.BLACK);
		this.mText.setText("正在加载...");
		localLinearLayout.addView(this.mText);
		addView(localLinearLayout);
	}

	public void setMessage(String paramString) {
		this.mText.setText(paramString);
	}
}
