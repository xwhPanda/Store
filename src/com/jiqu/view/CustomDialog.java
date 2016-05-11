package com.jiqu.view;

import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomDialog extends Dialog {
	private Context context;
	private ImageView img;
	private TextView tip;
	private RelativeLayout parent;
	private Button cancle;
	private Button login;
	
	private CustomDialog dialog;

	private OnClickListener positiveButtonClickListener;
	private OnClickListener negativeButtonClickListener;

	public CustomDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		
	}

	public CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public CustomDialog setPositiveButton(OnClickListener listener) {
		this.positiveButtonClickListener = listener;
		return this;
	}

	public CustomDialog setNegativeButton(OnClickListener listener) {
		this.negativeButtonClickListener = listener;
		return this;
	}

	public CustomDialog initView() {
		dialog = new CustomDialog(context, R.style.Dialog);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.custom_dialog_layout, null);
		img = (ImageView) view.findViewById(R.id.img);
		tip = (TextView) view.findViewById(R.id.tip);
		parent = (RelativeLayout) view.findViewById(R.id.parent);
		cancle = (Button) view.findViewById(R.id.cancle);
		login = (Button) view.findViewById(R.id.login);

		if (positiveButtonClickListener != null) {
			login.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
				}
			});
		}

		if (negativeButtonClickListener != null) {
			cancle.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
				}
			});
		}

		initViewSize(view);

		dialog.setContentView(view);
		dialog.setCancelable(false);

		return dialog;
	}

	private void initViewSize(View view) {
		UIUtil.setViewSize(parent, 690 * MetricsTool.Rx, 495 * MetricsTool.Ry);
		UIUtil.setViewSize(img, 320 * MetricsTool.Rx, 190 * MetricsTool.Ry);
		UIUtil.setViewSize(cancle, 270 * MetricsTool.Rx, 85 * MetricsTool.Ry);
		UIUtil.setViewWidth(login, 270 * MetricsTool.Rx);

		UIUtil.setTextSize(tip, 25);
		UIUtil.setTextSize(cancle, 35);
		UIUtil.setTextSize(login, 35);

		try {
			UIUtil.setViewSizeMargin(login, 45 * MetricsTool.Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(tip, 0, 20 * MetricsTool.Rx, 0, 20 * MetricsTool.Rx);
			UIUtil.setViewSizeMargin(img, 0, 65 * MetricsTool.Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
