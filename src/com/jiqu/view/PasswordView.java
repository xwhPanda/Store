package com.jiqu.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;

public class PasswordView extends RelativeLayout implements OnClickListener,TextWatcher{
	private View view;
	public Button visibleButton;
	public EditText editText;
	public ImageView img;
	

	public PasswordView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public PasswordView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public PasswordView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public String getText(){
		return editText.getText().toString();
	}
	
	private void init(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.password_view, this);
		visibleButton = (Button) view.findViewById(R.id.visibleButton);
		img = (ImageView) view.findViewById(R.id.img);
		editText = (EditText) view.findViewById(R.id.editText); 
		
		
		visibleButton.setOnClickListener(this);
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(img, 64 * MetricsTool.Rx, 64 * MetricsTool.Rx);
		UIUtil.setViewSize(visibleButton, 56 * MetricsTool.Rx, 56 * MetricsTool.Rx);
		
		UIUtil.setTextSize(editText, 45);
	}

	public void addTextChanedListener(){
		editText.addTextChangedListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.visibleButton) {
			if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT 
					| InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
				editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			}else if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
				editText.setInputType(InputType.TYPE_CLASS_TEXT 
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			//设置光标在最后
			Editable editable = editText.getText();
			Selection.setSelection(editable, editable.length());
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		String editable = editText.getText().toString();
		String str = stringFilter(editable);
		if (!editable.equals(str)) {
			editText.setText(str);
			editText.setSelection(editText.length());
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}
	
	public String stringFilter(String str)throws PatternSyntaxException{ 
		 String regEx = "[^a-zA-Z0-9]";
		 Pattern p = Pattern.compile(regEx);
		 Matcher m = p.matcher(str);
		 return m.replaceAll("").trim();
	 }

}
