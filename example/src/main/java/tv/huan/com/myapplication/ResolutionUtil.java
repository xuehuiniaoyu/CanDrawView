package tv.huan.com.myapplication;

import android.content.Context;
import android.util.DisplayMetrics;

public class ResolutionUtil {

	public static int dip2px(Context context, float dipValue) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int width = (int)(displayMetrics.widthPixels/displayMetrics.density);
//		float scale = displayMetrics.density;
//		if(width >= 1280){
//			if(width == 1920 && scale == 1.0){
//				return (int) (dipValue * 1.5 + 0.5f);
//			}
//			return (int) (dipValue * scale + 0.5f);
//		}
//		return (int)Math.ceil(dipValue*0.75*displayMetrics.density);
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
