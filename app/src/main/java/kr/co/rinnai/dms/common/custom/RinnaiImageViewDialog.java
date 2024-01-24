package kr.co.rinnai.dms.common.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.RinnaiDialog;
import kr.co.rinnai.dms.adapter.SearchListAdapter;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.listener.DialogListener;
import kr.co.rinnai.dms.common.util.Util;

public class RinnaiImageViewDialog extends Dialog implements OnClickListener {


	private RelativeLayout btnClose;
	private ImageView ivZoomImage;
	private Context context;
	private String imgPath;

	private ScaleGestureDetector mScaleGestureDetector;
	private float mScaleFactor = 1.0f;



	public RinnaiImageViewDialog(Context context, String imgPath) {
		super(context);
		this.context= context;
		this.imgPath = imgPath;


	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_image_view);

		btnClose = (RelativeLayout)findViewById(R.id.btn_dialog_image_view_close);

		ivZoomImage = (ImageView) findViewById(R.id.iv_zoom_image);

		BitmapFactory.Options options = new BitmapFactory.Options();



//		int orientation = getOrientationOfImage(tempFile.getAbsolutePath());
//		Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
//
//		try {
//			originalBm = getRotatedBitmap(originalBm, orientation);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}


		int orientation = Util.getOrientationOfImage(imgPath);

		if(orientation == 0) {
			orientation = 90;
		} else if (orientation == 180) {
			orientation += 90;
		}
		Bitmap originalBm = BitmapFactory.decodeFile(imgPath, options);
//
		try {
			originalBm = Util.getRotatedBitmap(originalBm, orientation);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ivZoomImage.setImageBitmap(originalBm);

		mScaleGestureDetector = new ScaleGestureDetector(this.context, new ScaleListener());

		btnClose.setOnClickListener(this);

	}

	@Override
	public void onBackPressed() {
		dismiss();
	}


	@Override
	public void onClick(View v) {

		int id = v.getId();

		if(id == btnClose.getId()) {
			dismiss();
		}


	}

	public boolean onTouchEvent(MotionEvent motionEvent) {
		//변수로 선언해 놓은 ScaleGestureDetector
		mScaleGestureDetector.onTouchEvent(motionEvent);
		return true;
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector scaleGestureDetector){
			// ScaleGestureDetector에서 factor를 받아 변수로 선언한 factor에 넣고
			mScaleFactor *= scaleGestureDetector.getScaleFactor();

			// 최대 10배, 최소 10배 줌 한계 설정
			mScaleFactor = Math.max(0.1f,
					Math.min(mScaleFactor, 10.0f));

			// 이미지뷰 스케일에 적용
			ivZoomImage.setScaleX(mScaleFactor);
			ivZoomImage.setScaleY(mScaleFactor);
			return true;
		}
	}


}