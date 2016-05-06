package com.jiqu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jiqu.store.BaseActivity;
import com.jiqu.tools.Constants;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.vr.store.R;

public class WeiboShareActivity extends BaseActivity implements IWeiboHandler.Response{
	private EditText shareText;
	private Button share;
	private IWeiboShareAPI mWeiboShareAPI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		shareText = (EditText) findViewById(R.id.shareText);
		share = (Button) findViewById(R.id.share);
		
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);
		mWeiboShareAPI.registerApp();
		
		share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage(true, false, false, false, false, false);
			}
		});
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.weibo_share_layout;
	}

	private TextObject getTextObj() {
	    TextObject textObject = new TextObject();
	    textObject.text = shareText.getText().toString();
	    return textObject;
	}
	
	private void sendMessage(boolean hasText, boolean hasImage, 
			boolean hasWebpage, boolean hasMusic, boolean hasVideo, boolean hasVoice) {
        
            if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
                int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
                if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                    sendMultiMessage(hasText, hasImage, hasWebpage, hasMusic, hasVideo, hasVoice);
                } else {
//                    sendSingleMessage(hasText, hasImage, hasWebpage, hasMusic, hasVideo/*, hasVoice*/);
                }
            } else {
            }
        }
	
	private void sendMultiMessage(boolean hasText, boolean hasImage, boolean hasWebpage,
	        boolean hasMusic, boolean hasVideo, boolean hasVoice) {
	    WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
	    if (hasText) {
	        weiboMessage. textObject = getTextObj();
	    }
	    SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
	    request.transaction = String.valueOf(System.currentTimeMillis());
	    request.multiMessage = weiboMessage;
	    mWeiboShareAPI.sendRequest(this,request); //发送请求消息到微博，唤起微博分享界面
	}
	
	  @Override
	    protected void onNewIntent(Intent intent) {
	        super.onNewIntent(intent);
	        
	        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
	        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
	        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
	        mWeiboShareAPI.handleWeiboResponse(intent, this);
	    }

	@Override
	public void onResponse(BaseResponse baseResp) {
		// TODO Auto-generated method stub
		if(baseResp!= null){
            switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
            	Log.i("TAG", "ERR_OK");
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
            	Log.i("TAG", "ERR_CANCEL");
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
            	Log.i("TAG", "ERR_FAIL :　" + "Error Message: " + baseResp.errMsg);
                break;
            }
        }
	}
}
