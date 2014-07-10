package com.rqmod.util;
import com.weijia.mymod.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyProgressDialog {

	static ImageView spaceshipImage = null;
	static Dialog loadingDialog = null;
/**

* ��ʾһ���ȴ���

* 

* @param context�����Ļ���

* @param isCancel�Ƿ����÷���ȡ��

* @param isRighttrue�������ұ�false������

*/

public static void show(Context context, boolean isCancel, boolean isRight) {

	creatDialog(context, "", isCancel, isRight);

}


/**

* ��ʾһ���ȴ���

* 

* @param context�����Ļ���

* @param msg�ȴ��������

* @param isCancel�Ƿ����÷���ȡ��

* @param isRighttrue�������ұ�false������

*/

public static void show(Context context, String msg, boolean isCancel, boolean isRight) {

	creatDialog(context, msg, isCancel, isRight);

}
public static void stop() {

	if(null != spaceshipImage)
	{
		//spaceshipImage.clearAnimation();
		loadingDialog.dismiss();
	}
}

private static void creatDialog(Context context, String msg, boolean isCancel, boolean isRight) {

	LinearLayout.LayoutParams wrap_content = new LinearLayout.LayoutParams(
	
	LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
	LinearLayout.LayoutParams wrap_content0 = new LinearLayout.LayoutParams(
	
	LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
	LinearLayout main = new LinearLayout(context);
	
	main.setBackgroundColor(Color.WHITE);

	if (isRight) {

		main.setOrientation(LinearLayout.HORIZONTAL);
		
		wrap_content.setMargins(10, 0, 35, 0);
		
		wrap_content0.setMargins(35, 25, 0, 25);

	} else {

		main.setOrientation(LinearLayout.VERTICAL);
		
		wrap_content.setMargins(10, 5, 10, 15);
		
		wrap_content0.setMargins(35, 25, 35, 0);

	}

	main.setGravity(Gravity.CENTER);

	spaceshipImage = new ImageView(context);

	spaceshipImage.setImageResource(R.drawable.publicloading);

	TextView tipTextView = new TextView(context);

	tipTextView.setText("���Ժ�...");

	// ������ת����
	
	Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context,R.animator.loading_animation);

	// ʹ��ImageView��ʾ����
	
	spaceshipImage.startAnimation(hyperspaceJumpAnimation);
	
	if (msg != null && !"".equals(msg))
	
		tipTextView.setText(msg);// ���ü�����Ϣ,�������Ĭ��ֵ
	
	loadingDialog = new Dialog(context, R.style.loading_dialog);// �����Զ�����ʽdialog
	
	loadingDialog.setCancelable(isCancel);// �Ƿ�����÷��ؼ�ȡ��
	
	main.addView(spaceshipImage, wrap_content0);
	
	main.addView(tipTextView, wrap_content);
	
	LinearLayout.LayoutParams fill_parent = new LinearLayout.LayoutParams(
	
	LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
	
	loadingDialog.setContentView(main, fill_parent);// ���ò���
	
	loadingDialog.show();

	}

}