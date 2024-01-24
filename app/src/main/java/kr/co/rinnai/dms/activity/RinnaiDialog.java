package kr.co.rinnai.dms.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.user.LoginActivity;

public class RinnaiDialog extends Dialog implements OnClickListener {

	private Button btnConfirm;
	private TextView tvTitle, tvMsg;
	private String title, msg;
	private MediaPlayer mAudio = null;
	private String path = null;

	private Context context;
	private AudioManager am;
	private int currentVolume;


	public RinnaiDialog(Context context, String title, String msg) {
		super(context);
		
		this.title = title;
		this.msg = msg;
	}

	public RinnaiDialog(Context context, String title, String msg, String path ) {
		super(context);

		this.context = context;
		this.title = title;
		this.msg = msg;
		this.path = path;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		getWindow().setBackgroundDrawable(new ColorDrawable((Color.YELLOW)));
		setContentView(R.layout.dialog_rinnai);
		
		btnConfirm = (Button) findViewById(R.id.btn_confirm);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvMsg = (TextView) findViewById(R.id.tv_msg);
		
		btnConfirm.setOnClickListener(this);
		
		tvTitle.setText(title);
		tvMsg.setText(msg);

		if(null != path) {

			try {
				mAudio = new MediaPlayer();

				mAudio.setDataSource(path);
				mAudio.setLooping(false);
				mAudio.prepare();
				mAudio.start();

				am = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

				currentVolume  = am.getStreamVolume(AudioManager.STREAM_MUSIC);
				int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				int max =  (int) Math. round(maxVolume * 0.7);
				am.setStreamVolume(AudioManager.STREAM_MUSIC, max, AudioManager.STREAM_MUSIC);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	}

	@Override
	public void onClick(View v) {
		
		if (btnConfirm == v) {
			dismiss();
			if(null != path) {
				mAudio.stop();
				mAudio.release();

				mAudio = null;

				am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.STREAM_MUSIC);

			}
		}
	}


}
