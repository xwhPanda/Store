package com.jiqu.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.VolleyError;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.Account;
import com.jiqu.interfaces.DialogDismissObserver;
import com.jiqu.interfaces.LoginOutObserver;
import com.jiqu.store.BaseActivity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vr.store.R;
import com.jiqu.tools.Constants;
import com.jiqu.tools.MD5;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.umeng.UMengManager;
import com.jiqu.view.CircleImageView;
import com.jiqu.view.InformationBrithDialog;
import com.jiqu.view.InformationGenderDialog;
import com.jiqu.view.TitleView;

import de.greenrobot.dao.query.QueryBuilder;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ShowAccountInformatiomActivity extends BaseActivity implements OnClickListener, DialogDismissObserver,UMAuthListener{
	private final int IMAGE_REQUEST_CODE = 0;
	private final int CUT_REQUEST_KITKAT_CODE = 1;
	private final int RESIZE_REQUEST_CODE = 2;
	private final int SELECT_PIC_KITKAT = 4;
	private final String REQUEST_TAG = "modifyInformaiontRequest";
	private RelativeLayout parent;
	private TitleView titleView;
	private CircleImageView accountImg;
	private TextView nickName;
	private TextView level;
	private Button modiftBtn;
	private Button loginOut;
	private RelativeLayout genderRel, birthRel, phoneRel, qqRel;
	private TextView gender, birth, phone, qq;
	private Button genderBtn;
	private TextView genderTx;
	private TextView birthTx;
	private EditText phoneEdit;
	private EditText qqEdit;
	private Button birthBtn;
	private InformationGenderDialog dialog;
	private InformationBrithDialog brithDialog;

	private String genderStr = "", birthStr = "";
	private String phoneStr, qqStr;
	private Account info;
	private BTN_STATE state = BTN_STATE.MODIFY;
	private boolean modifing = false;

	private RequestTool requestTool;
	
	private String loginType = "";

	private static enum BTN_STATE {
		MODIFY, COMMIT;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		loginType = getIntent().getStringExtra("loginType");
		initView();
//		if (loginType.equals("")) {
		initData();
//		}else {
//			loadDataFromOther();
//		}
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.account_information;
	}

	private void initView() {
		parent = (RelativeLayout) findViewById(R.id.parent);
		parent.setBackgroundDrawable(StoreApplication.BG_IMG);
		titleView = (TitleView) findViewById(R.id.titleView);
		accountImg = (CircleImageView) findViewById(R.id.accountImg);
		nickName = (TextView) findViewById(R.id.nickName);
		level = (TextView) findViewById(R.id.level);
		modiftBtn = (Button) findViewById(R.id.modifyBtn);
		loginOut = (Button) findViewById(R.id.loginOut);
		gender = (TextView) findViewById(R.id.gender);
		birth = (TextView) findViewById(R.id.birth);
		phone = (TextView) findViewById(R.id.phone);
		qq = (TextView) findViewById(R.id.qq);
		genderRel = (RelativeLayout) findViewById(R.id.genderRel);
		birthRel = (RelativeLayout) findViewById(R.id.birthRel);
		phoneRel = (RelativeLayout) findViewById(R.id.phoneRel);
		qqRel = (RelativeLayout) findViewById(R.id.qqRel);
		genderBtn = (Button) findViewById(R.id.genderBtn);
		birthBtn = (Button) findViewById(R.id.birthBtn);
		genderTx = (TextView) findViewById(R.id.genderTx);
		birthTx = (TextView) findViewById(R.id.birthTx);
		phoneEdit = (EditText) findViewById(R.id.phoneEdit);
		qqEdit = (EditText) findViewById(R.id.qqEdit);

		changeState(false);

		accountImg.setOnClickListener(this);
		modiftBtn.setOnClickListener(this);
		loginOut.setOnClickListener(this);
		genderBtn.setOnClickListener(this);
		birthBtn.setOnClickListener(this);

		dialog = new InformationGenderDialog(this);
		dialog.setObserver(this);

		brithDialog = new InformationBrithDialog(this);
		brithDialog.setObserver(this);

		titleView.setActivity(this);
		titleView.tip.setText(R.string.memberInformation);

		initViewSize();
	}

	private void initData() {
		QueryBuilder qb = StoreApplication.daoSession.getAccountDao().queryBuilder();
		info = (Account) qb.unique();
		if (info != null) {
			File file = new File(Constants.ACCOUNT_ICON);
			ImageListener listener;
//			if (file.exists()) {
//				Bitmap bitmap = BitmapFactory.decodeFile(Constants.ACCOUNT_ICON);
//				listener = ImageLoader.getImageListener(accountImg, bitmap, bitmap);
//			}else {
				listener = ImageLoader.getImageListener(accountImg, R.drawable.yonghuicon, R.drawable.yonghuicon);
//			}
			StoreApplication.getInstance().getImageLoader().get(info.getPhoto(), listener);
			if (info.getUsername() != null && !TextUtils.isEmpty(info.getUsername())) {
				nickName.setText(info.getUsername());
			}else {
				nickName.setText(info.getNickname());
			}
			genderStr = String.valueOf(info.getGender());
			if (info.getGender() == 1) {
				genderTx.setText(getResources().getString(R.string.man));
				genderBtn.setBackgroundResource(0);
			} else if (info.getGender() == 2) {
				genderTx.setText(getResources().getString(R.string.female));
				genderBtn.setBackgroundResource(0);
			}

			phoneEdit.setText(info.getPhone());
			qqEdit.setText(info.getQq());
			if (info.getBirthday() != null && !TextUtils.isEmpty(info.getBirthday())) {
				birthTx.setText(info.getBirthday());
				birthBtn.setBackgroundResource(0);
			}
			level.setText("LV " + info.getLevel());
		}
	}
	
	private void loadDataFromOther(){
		if ("qq".equals(loginType)) {
			UMengManager.getInstance().getQqInfo(this, this);
		}else if (modifing) {
			
		}
	}
	
	private void cancleAuth(){
		if ("qq".equals(loginType)) {
			UMengManager.getInstance().cancleQqAuth(this, listener);
		}else if ("weixin".equals(loginType)) {
			UMengManager.getInstance().cancleWeixinAuth(this, listener);
		}
	}
	
	UMAuthListener listener = new UMAuthListener() {
		
		@Override
		public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onComplete(SHARE_MEDIA arg0, int arg1, Map<String, String> arg2) {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(1);
		}
		
		@Override
		public void onCancel(SHARE_MEDIA arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
	};

	private void initViewSize() {
		UIUtil.setViewSize(accountImg, 250 * Rx, 250 * Rx);
		UIUtil.setViewSize(level, 155 * Rx, 50 * Ry);
		UIUtil.setViewSize(genderBtn, 35 * MetricsTool.Rx, 35 * MetricsTool.Rx);
		UIUtil.setViewSize(birthBtn, 35 * MetricsTool.Rx, 35 * MetricsTool.Rx);
		UIUtil.setViewHeight(modiftBtn, 100 * Ry);
		UIUtil.setViewHeight(loginOut, 100 * Ry);
		UIUtil.setViewSize(genderRel, 1040 * Rx, 175 * Ry);
		UIUtil.setViewHeight(birthRel, 175 * Ry);
		UIUtil.setViewHeight(phoneRel, 175 * Ry);
		UIUtil.setViewHeight(qqRel, 175 * Ry);

		UIUtil.setViewWidth(phoneEdit, 300 * Rx);
		UIUtil.setViewWidth(qqEdit, 400 * Rx);

		UIUtil.setTextSize(gender, 40);
		UIUtil.setTextSize(birth, 40);
		UIUtil.setTextSize(phone, 40);
		UIUtil.setTextSize(qq, 40);
		UIUtil.setTextSize(genderTx, 40);
		UIUtil.setTextSize(birthTx, 40);
		UIUtil.setTextSize(phoneEdit, 40);
		UIUtil.setTextSize(qqEdit, 40);
		UIUtil.setTextSize(level, 25);
		UIUtil.setTextSize(modiftBtn, 50);
		UIUtil.setTextSize(loginOut, 50);

		try {
			UIUtil.setViewSizeMargin(accountImg, 0, 240 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(level, 0, 40 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(modiftBtn, 0, 60 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(loginOut, 0, 25 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(nickName, 0, 45 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(genderRel, 0, 60 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(birthRel, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(phoneRel, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(qqRel, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(gender, 35 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(birth, 35 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(phone, 35 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(qq, 35 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(genderTx, 0, 0, 35 * Rx, 0);
			UIUtil.setViewSizeMargin(genderBtn, 0, 0, 35 * Rx, 0);
			UIUtil.setViewSizeMargin(birthTx, 0, 0, 35 * Rx, 0);
			UIUtil.setViewSizeMargin(birthBtn, 0, 0, 35 * Rx, 0);
			UIUtil.setViewSizeMargin(phoneEdit, 0, 0, 35 * Rx, 0);
			UIUtil.setViewSizeMargin(qqEdit, 0, 0, 35 * Rx, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.accountImg:
			chooseImage();
			break;
		case R.id.genderBtn:
			showDialog(dialog);
			setBottomBtnVisible(View.GONE);
			break;

		case R.id.birthBtn:
			showDialog(brithDialog);
			setBottomBtnVisible(View.GONE);
			break;

		case R.id.modifyBtn:
			if (state == BTN_STATE.MODIFY) {
				state = BTN_STATE.COMMIT;
				changeState(true);
			} else if (state == BTN_STATE.COMMIT) {
				if (modifing) {
					UIUtil.showToast(R.string.modifing);
				} else {
					modiftBtn.setText(R.string.modifyCommiting);
					phoneStr = phoneEdit.getText().toString().trim();
					qqStr = qqEdit.getText().toString().trim();
					modifing = true;
					modifyRequest();
				}
			}
			break;

		case R.id.loginOut:
			if (!"".equals(loginType)) {
				cancleAuth();
			}else {
				deleteLoginInfo();
			}
			break;
		}
	}
	
	private void deleteLoginInfo(){
		File file = new File(Constants.ACCOUNT_ICON);
		if (file.exists()) {
			file.delete();
		}
		StoreApplication.daoSession.getAccountDao().deleteAll();
		if (StoreApplication.loginOutObservers != null) {
			for(LoginOutObserver observer : StoreApplication.loginOutObservers){
				observer.onLoginOut();
			}
		}
		finish();
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				deleteLoginInfo();
			}
		};
	};

	private void modifyRequest() {
		if (info == null) {
			modifing = true;
			return;
		}
		requestTool.getMap().clear();
		requestTool.setParam("uid", String.valueOf(info.getUid()));
		requestTool.setParam("gender", genderStr);
		if (info.getNickname() == null) {
			requestTool.setParam("nickname", "");
		} else {
			requestTool.setParam("nickname", info.getNickname());
		}
		requestTool.setParam("phone", phoneStr);
		requestTool.setParam("birthday", birthStr);
		requestTool.setParam("qq", qqStr);
		String token = info.getUid() + genderStr + info.getNickname() + phoneStr + birthStr + qqStr + RequestTool.PRIKEY;
		requestTool.setParam("token", MD5.GetMD5Code(token));
		requestTool.startStringRequest(Method.POST, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				modifing = false;
				modiftBtn.setText(R.string.modify);
				if (arg0.contains("status")) {
					int status = JSON.parseObject(arg0).getInteger("status");
					if (status == 1) {
						/** 修改成功 **/
						UIUtil.showToast(R.string.modifySuccess);
						info.setBirthday(birthStr);
						info.setGender(Integer.parseInt(genderStr));
						info.setPhone(phoneStr);
						info.setQq(qqStr);
						StoreApplication.daoSession.getAccountDao().deleteAll();
						StoreApplication.daoSession.getAccountDao().insertOrReplace(info);
						if (StoreApplication.loginOutObservers != null) {
							for(LoginOutObserver observer : StoreApplication.loginOutObservers){
								observer.onRefresh(info);
							}
						}
						changeState(false);
					} else if (status == 0) {
						/** 修改失败 **/
						UIUtil.showToast(R.string.modifyFailed);
					} else if (status == -1 || status == -9) {
						/** 修改失败 **/
						UIUtil.showToast(R.string.modifyFailed);
					} else if (status == -2) {
						/** 用户不存在 **/
						UIUtil.showToast(R.string.modifyAccountNotExist);
					}
				}
			}
		}, RequestTool.MODIFY_URL, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				modiftBtn.setText(R.string.modify);
				modifing = false;
				UIUtil.showToast(R.string.modifyFailed);
			}
		}, requestTool.getMap(), REQUEST_TAG);
	}

	private void changeState(boolean enabled) {
		accountImg.setEnabled(enabled);
		genderBtn.setEnabled(enabled);
		birthBtn.setEnabled(enabled);
		phoneEdit.setEnabled(enabled);
		qqEdit.setEnabled(enabled);
		if (enabled) {
			modiftBtn.setText(getResources().getString(R.string.commit));
		} else {
			modiftBtn.setText(getResources().getString(R.string.modify));
		}
	}

	private void showDialog(Dialog dialog) {
		Window window = dialog.getWindow();
		window.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		wl.width = WindowManager.LayoutParams.FILL_PARENT;
		wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		dialog.show();
	}

	private void setBottomBtnVisible(int visible) {
		modiftBtn.setVisibility(visible);
		loginOut.setVisibility(visible);
	}

	@Override
	public void onDialogStateChange(int state) {
		// TODO Auto-generated method stub
		if (state == 0) {
			setBottomBtnVisible(View.VISIBLE);
		}
	}

	@Override
	public void onDialogSave(int type, String value) {
		// TODO Auto-generated method stub
		if (type == 0) {// 性别选择
			if (value != null && value.length() > 0 && !"".equals(value)) {
				genderStr = value;
				genderTx.setText(value);
				genderBtn.setBackgroundDrawable(null);
				UIUtil.setViewSize(genderBtn, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				if (getResources().getString(R.string.man).equals(value)) {
					genderStr = "1";
				} else if (getResources().getString(R.string.female).equals(value)) {
					genderStr = "2";
				}
			} else {
				genderStr = "";
			}
		} else if (type == 1) {// 生日选择
			if (value.length() > 0 && !"".equals(value)) {
				birthStr = value;
				birthTx.setText(value);
				birthBtn.setBackgroundDrawable(null);
				UIUtil.setViewSize(birthBtn, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			} else {
				birthStr = "";
			}
		}
		setBottomBtnVisible(View.VISIBLE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			return;
		} else {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				resizeImage(data.getData());
				break;

			case SELECT_PIC_KITKAT:
				Uri selectedImage = data.getData();
				String imagePath = UIUtil.getPath(this, selectedImage); // 获取图片的绝对路径
				Uri newUri = Uri.parse("file:///" + imagePath); // 将绝对路径转换为URL
				startPhotoZoom(newUri);
				break;
			case CUT_REQUEST_KITKAT_CODE:
				if (data != null) {
					setPicToView(data);
				}
				break;
			case RESIZE_REQUEST_CODE:
				if (data != null) {
					setPicToView(data);
				}
				break;
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
//			photo = UIUtil.toRoundBitmap(photo);
            UIUtil.saveBitmap(Constants.ACCOUNT_ICON,photo);
			postImage(photo);
		}
	}
	
	private void postImage(final Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        //base64 encode
        byte[] encode = Base64.encode(bytes,Base64.DEFAULT);
        String photo = new String(encode);
        
		requestTool.getMap().clear();
		requestTool.setParam("uid", info.getUid());
		requestTool.setParam("type", 2);
		requestTool.setParam("photo", photo);
		requestTool.setParam("token", MD5.GetMD5Code(info.getUid() + "2" + RequestTool.PRIKEY));
		requestTool.startStringRequest(Method.POST, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				JSONObject json = JSON.parseObject(arg0);
				if (json.containsKey("status") && json.getIntValue("status") == 1) {
					if (json.containsKey("data")) {
						JSONObject data = json.getJSONObject("data");
						if (data.containsKey("url")) {
							String url = data.getString("url");
							if (!TextUtils.isEmpty(url)) {
								QueryBuilder qb = StoreApplication.daoSession.getAccountDao().queryBuilder();
								Account account = (Account) qb.unique();
								account.setPhoto(url);
								StoreApplication.daoSession.getAccountDao().insertOrReplace(account);
								ImageListener listener = ImageLoader.getImageListener(accountImg, bitmap, bitmap);
								StoreApplication.getInstance().getImageLoader().get(url, listener);
								if (StoreApplication.loginOutObservers != null) {
									for(LoginOutObserver observer : StoreApplication.loginOutObservers){
										observer.onRefresh(account);
									}
								}
							}
						}
					}
				}else {
					UIUtil.showToast(R.string.modifyFailed);
				}
			}
		}, RequestTool.MODIFY_PHOTO_URL, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
			}
		}, requestTool.getMap(), "sss");
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", true);
		intent.putExtra("circleCrop","true");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("scale",true);
		intent.putExtra("scaleUpIfNeeded", true);
		startActivityForResult(intent, CUT_REQUEST_KITKAT_CODE);
	}

	public void chooseImage() {
		Intent intent;
		if (Build.VERSION.SDK_INT >= 19) {
			intent = new Intent("android.intent.action.OPEN_DOCUMENT"); // 4.4推荐用此方式，4.4以下的API需要再兼容
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
		}else {
			intent = new Intent(Intent.ACTION_PICK,null); 
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		}
		if (android.os.Build.VERSION.SDK_INT >= 19) {
			startActivityForResult(intent, SELECT_PIC_KITKAT);// 4.4版本
		} else {
			 startActivityForResult(intent, IMAGE_REQUEST_CODE);//4.4以下版本，先不处理
		}
	}

	public void resizeImage(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("circleCrop","true");
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("scale",true);
		intent.putExtra("scaleUpIfNeeded", true);
		startActivityForResult(intent, RESIZE_REQUEST_CODE);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		requestTool.stopRequest(REQUEST_TAG);
	}

	@Override
	public void onCancel(SHARE_MEDIA arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onComplete(SHARE_MEDIA arg0, int arg1, Map<String, String> data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}
}
