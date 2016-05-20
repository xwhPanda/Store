package com.jiqu.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.jiqu.application.StoreApplication;

import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class UIUtil {

	public static void setViewSize(View view, float width, float height) {
		LayoutParams lp = view.getLayoutParams();
		lp.width = (int) width;
		lp.height = (int) height;
		view.setLayoutParams(lp);
	}

	public static void setViewWidth(View view, float width) {
		LayoutParams lp = view.getLayoutParams();
		lp.width = (int) width;
		view.setLayoutParams(lp);
	}

	public static void setViewHeight(View view, float height) {
		LayoutParams lp = view.getLayoutParams();
		lp.height = (int) height;
		view.setLayoutParams(lp);
	}

	public static void setViewSizeMargin(View view, float left, float top, float right, float bottom) throws Exception {
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		if (lp instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams mlp = (MarginLayoutParams) lp;
			mlp.leftMargin = (int) left;
			mlp.topMargin = (int) top;
			mlp.rightMargin = (int) right;
			mlp.bottomMargin = (int) bottom;
			view.setLayoutParams(mlp);
		} else {
			Log.w("TAG", "View's LayoutParams is not MarginLayoutParams !");
		}
	}

	public static void setTextSize(TextView view, float size) {
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size * MetricsTool.Rx);
	}

	public static void setViewPadding(View view, int left, int top, int right, int bottom) {
		view.setPadding(left, top, right, bottom);
	}

	public static String getVersionName(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static List<String> getDataFromXML(Context context, int resID) {
		String[] strings = context.getResources().getStringArray(resID);
		return Arrays.asList(strings);
	}

	/**
	 * 返回byte的数据大小对应的文本
	 * 
	 * @param size
	 * @return
	 */
	public static String getDataSize(long size) {
		DecimalFormat formater = new DecimalFormat("####.0");
		if (size >= 0) {
			if (size < 1024) {
				return size + "B";
			} else if (size < 1024 * 1024) {
				float kbsize = size / 1024f;
				return formater.format(kbsize) + "KB";
			} else if (size < 1024 * 1024 * 1024) {
				float mbsize = size / 1024f / 1024f;
				return formater.format(mbsize) + "M";
			} else if (size < 1024 * 1024 * 1024 * 1024) {
				float gbsize = size / 1024f / 1024f / 1024f;
				return formater.format(gbsize) + "G";
			} else {
				return 0 + "KB";
			}
		} else {
			return 0 + "KB";
		}
	}
	
	public static Bitmap bytesToBitmap(byte[] bytes){
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
//		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
//		Drawable drawable = bitmapDrawable;
		return bitmap;
	}
	
	public static byte[] Bitmap2Bytes(Bitmap bm){  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();    
	    bm.compress(Bitmap.CompressFormat.PNG, 100, baos);    
	    return baos.toByteArray();  
	   }  
	
	public static String getFormatedDateTime(String pattern, long dateTime) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date(dateTime + 0));
    }

	public static void showToast(int stringId) {
		Toast.makeText(StoreApplication.context, StoreApplication.context.getResources().getString(stringId), Toast.LENGTH_SHORT).show();
	}

	public static BitmapDrawable readBitmapDrawable(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BitmapDrawable(context.getResources(), bitmap);

	}
	
	public static BitmapDrawable readBitmapDrawableNotChange(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BitmapDrawable(context.getResources(), bitmap);

	}

	public static Bitmap readBitmap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	public static StateListDrawable setbg(Context context, int normalId, int pressId) {
		StateListDrawable bg = new StateListDrawable();
		Drawable idNormal = readBitmapDrawable(context, normalId);
		Drawable idPressed = context.getResources().getDrawable(pressId);
		bg.addState(new int[] { android.R.attr.state_pressed}, idPressed);
		bg.addState(new int[] { android.R.attr.state_enabled}, idNormal);
		bg.addState(new int[] {}, idNormal);
		return bg;
	}
	
	public static Bitmap toRoundBitmap(Bitmap bitmap) {  
        //圆形图片宽高  
        int width = bitmap.getWidth();  
        int height = bitmap.getHeight();  
        //正方形的边长  
        int r = 0;  
        //取最短边做边长  
        if(width > height) {  
            r = height;  
        } else {  
            r = width;  
        }  
        //构建一个bitmap  
        Bitmap backgroundBmp = Bitmap.createBitmap(width,  
                 height, Config.ARGB_8888);  
        //new一个Canvas，在backgroundBmp上画图  
        Canvas canvas = new Canvas(backgroundBmp);  
        Paint paint = new Paint();  
        //设置边缘光滑，去掉锯齿  
        paint.setAntiAlias(true);  
        //宽高相等，即正方形  
        RectF rect = new RectF(0, 0, r, r);  
        //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，  
        //且都等于r/2时，画出来的圆角矩形就是圆形  
        canvas.drawRoundRect(rect, r/2, r/2, paint);  
        //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉  
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
        //canvas将bitmap画在backgroundBmp上  
        canvas.drawBitmap(bitmap, null, rect, paint);  
        //返回已经绘画好的backgroundBmp  
        return backgroundBmp;  
    }  
	
	public static void saveBitmap(String fileName, Bitmap mBitmap) {  
        File f = new File(fileName);  
        if (f.exists()) {
			f.delete();
		}
        FileOutputStream fOut = null;  
        try {  
            f.createNewFile();  
            fOut = new FileOutputStream(f);  
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);  
            fOut.flush();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                fOut.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
  
    }  
	
	public static String getPath(final Context context, final Uri uri) {
		//Build.VERSION_CODES.KITKAT = 19
		final boolean isKitKat = Build.VERSION.SDK_INT >= 19;
		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
				// TODO handle non-primary volumes
			}
			else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			}
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();
			return getDataColumn(context, uri, null, null);
		}
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	private static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
}
