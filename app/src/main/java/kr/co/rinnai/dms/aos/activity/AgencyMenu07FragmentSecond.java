package kr.co.rinnai.dms.aos.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.adapter.AgencyMenu07ModelInsertListAdapter;
import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteModelInfo;
import kr.co.rinnai.dms.common.custom.CustomMenuButtonView;
import kr.co.rinnai.dms.common.custom.RinnaiImageViewDialog;
import kr.co.rinnai.dms.common.http.model.SensorData;
import kr.co.rinnai.dms.common.util.Descending;
import kr.co.rinnai.dms.common.util.Util;
import kr.co.rinnai.dms.eos.activity.EmployeeMenu01Activity;
import kr.co.rinnai.dms.eos.activity.EmployeeMenu02Activity;
import kr.co.rinnai.dms.eos.activity.EmployeeMenu03Activity;
import kr.co.rinnai.dms.eos.activity.EmployeeMenu05Activity;
import kr.co.rinnai.dms.eos.activity.EmployeeMenu06Activity;
import kr.co.rinnai.dms.eos.activity.EmployeeMenu07Activity;

//import android.support.v4.app.Fragment;

public class AgencyMenu07FragmentSecond extends Fragment implements View.OnClickListener, View.OnTouchListener, AdapterView.OnItemClickListener {
	private View convertView = null;

	RelativeLayout  btnZoomImage1, btnZoomImage2;
	RelativeLayout btnImage1 , btnImage2, btnCamera1, btnCamera2;
	RelativeLayout btnInsert, btnDelete;
	ImageView ivSpotPic1, ivSpotPic2;

	EditText etSearchValue;

	private File tempFile;

	static Context con;

	private RinnaiImageViewDialog dialog;

	private String image1Path, image2Path;
	private ListView lvModelInfo;
	private List<AgencyMenu07SiteModelInfo> modelInfos;
	private AgencyMenu07ModelInsertListAdapter adapter;
	private boolean file1Change = false, file2Change = false;

	public static Fragment newInstance(Context context, int position, ArrayList<SensorData> sensorDataList) {
		Bundle b = new Bundle();
		b.putInt("pos", position);
		con = context;
		return Fragment.instantiate(context, AgencyMenu07FragmentSecond.class.getName(), b);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}

		convertView = inflater.inflate(R.layout.activity_agency_menu_07_fragment_two, container, false);


		btnImage1 = (RelativeLayout) convertView.findViewById(R.id.btn_add_image_1);
		btnImage2 = (RelativeLayout) convertView.findViewById(R.id.btn_add_image_2);
		btnCamera1 = (RelativeLayout) convertView.findViewById(R.id.btn_add_camera_1);
		btnCamera2 = (RelativeLayout) convertView.findViewById(R.id.btn_add_camera_2);
		btnZoomImage1 = (RelativeLayout) convertView.findViewById(R.id.btn_zoom_image_1);
		btnZoomImage2 = (RelativeLayout) convertView.findViewById(R.id.btn_zoom_image_2);

		ivSpotPic1 = (ImageView) convertView.findViewById(R.id.iv_spot_pic_1);
		ivSpotPic2 = (ImageView) convertView.findViewById(R.id.iv_spot_pic_2);
		etSearchValue = (EditText) convertView.findViewById(R.id.et_agency_menu_07_search_value);

		btnInsert = (RelativeLayout) convertView.findViewById(R.id.btn_agency_menu_07_model_insert);
		btnDelete = (RelativeLayout) convertView.findViewById(R.id.btn_agency_menu_07_model_delete);

		lvModelInfo = (ListView) convertView.findViewById(R.id.lv_agency_menu_07_model_info);

		btnImage1.setOnClickListener(AgencyMenu07FragmentSecond.this);
		btnImage2.setOnClickListener(AgencyMenu07FragmentSecond.this);

		btnCamera1.setOnClickListener(AgencyMenu07FragmentSecond.this);
		btnCamera2.setOnClickListener(AgencyMenu07FragmentSecond.this);

		btnZoomImage1.setOnClickListener(AgencyMenu07FragmentSecond.this);
		btnZoomImage2.setOnClickListener(AgencyMenu07FragmentSecond.this);

		btnInsert.setOnClickListener(AgencyMenu07FragmentSecond.this);
		btnDelete.setOnClickListener(AgencyMenu07FragmentSecond.this);

		lvModelInfo.setOnItemClickListener(AgencyMenu07FragmentSecond.this);

//		downloadImg();

		return convertView;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if(id == btnImage1.getId()){
			goToAlbum(1);
		} else if (id == btnImage2.getId()) {
			goToAlbum(2);
		} else if(id == btnCamera1.getId()) {
			takePhoto(3);
		} else if(id == btnCamera2.getId()) {
			takePhoto(4);
		} else if(id == btnZoomImage1.getId()) {
			if(image1Path !=  null) {
				dialog = new RinnaiImageViewDialog(getActivity(), image1Path);
				dialog.show();
			} else {
				((AgencyMenu07Activity)getActivity()).showRinnaiDialog(getActivity(), getString(R.string.msg_title_noti),"등록된 이미지가 없습니다.");
			}
		} else if(id == btnZoomImage2.getId()) {
			if(image2Path !=  null) {
				dialog = new RinnaiImageViewDialog(getActivity(), image2Path);
				dialog.show();
			} else {
				((AgencyMenu07Activity)getActivity()).showRinnaiDialog(getActivity(), getString(R.string.msg_title_noti),"등록된 이미지가 없습니다.");
			}
		} else if(id == btnInsert.getId()) {
			String keyword = etSearchValue.getText().toString();

			if("".equals(keyword)) {
				((AgencyMenu07Activity)getActivity()).showRinnaiDialog(getActivity(), getString(R.string.msg_title_noti),"검색할 모델명을 입력하세요.");
			} else {
				((AgencyMenu07Activity) getActivity()).getSiteModelInfo(keyword.toUpperCase());
			}

		} else if(id == btnDelete.getId()) {
			List<Integer> selectList = null;

			if(adapter != null) {
				selectList = adapter.getSelectItem();
			}

			if(selectList == null) {

			} else if(selectList != null) {
				Collections.sort(selectList, new Descending());
				for(int i = 0; i < selectList.size(); i ++) {
//					adapter.removeObj(selectList.get(i));
					((AgencyMenu07Activity)getActivity()).removeModelInfos(selectList.get(i));
				}
				adapter.notifyDataSetChanged();
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		adapter.setSelectItem(position);
		adapter.notifyDataSetChanged();
	}

	private void goToAlbum(int i) {

		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, i);
	}



	public boolean onTouch(View v, MotionEvent event) {
		int id = v.getId();


		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 1 || requestCode == 2) {

				Uri photoUri = data.getData();

				Cursor cursor = null;

				try {

					/*
					 *  Uri 스키마를
					 *  content:/// 에서 file:/// 로  변경한다.
					 */
					String[] proj = {MediaStore.Images.Media.DATA};

					assert photoUri != null;
					cursor = getActivity().getContentResolver().query(photoUri, proj, null, null, null);

					assert cursor != null;
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

					cursor.moveToFirst();

					tempFile = new File(cursor.getString(column_index));

				} finally {
					if (cursor != null) {
						cursor.close();
					}
				}

				setImage(requestCode);

			} else if (requestCode == 3 || requestCode == 4) {
				setImage(requestCode / 2);
			}
		}
	}

	private void setImage(int i) {

		BitmapFactory.Options options = new BitmapFactory.Options();




		int orientation = Util.getOrientationOfImage(tempFile.getAbsolutePath());
		Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

		try {
			originalBm = Util.getRotatedBitmap(originalBm, orientation);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(i == 1) {
			image1Path = tempFile.getAbsolutePath();
			ivSpotPic1.setImageBitmap(originalBm);
			file1Change = true;
		} else if (i == 2) {
			ivSpotPic2.setImageBitmap(originalBm);
			image2Path = tempFile.getAbsolutePath();
			file2Change = true;
		}

	}

	private void takePhoto(int i) {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		try {
			tempFile = createImageFile();
		} catch (IOException e) {
			Toast.makeText(getActivity(), "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
			//finish();
			e.printStackTrace();
		}
		if (tempFile != null) {

//			Uri photoUri = Uri.fromFile(tempFile);
			Uri photoUri = FileProvider.getUriForFile(getContext(), "kr.co.rinnai.dms.fileprovider", tempFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(intent, i);
		}
	}

	private File createImageFile() throws IOException {

		// 이미지 파일 이름 ( blackJin_{시간}_ )
		String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
		String imageFileName = "dms" + timeStamp + "_";

		// 이미지가 저장될 폴더 이름 ( blackJin )
		File storageDir = new File(Environment.getExternalStorageDirectory() + "/dms/");
		if (!storageDir.exists()) storageDir.mkdirs();

		// 빈 파일 생성
		File image = File.createTempFile(imageFileName, ".jpg", storageDir);



		return image;
	}


	public String getImageFilePath(int i) {

		if(i == 0) {
			return image1Path;
		} else {
			return image2Path;
		}
	}

	protected void datasetChange() {
		modelInfos = ((AgencyMenu07Activity)getActivity()).getModelInfos();
		adapter = new AgencyMenu07ModelInsertListAdapter(getActivity(), modelInfos);
		lvModelInfo.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	protected void setdownloadImg(Bitmap myBitmap,int position, String fileName) {

		int i = fileName.lastIndexOf(".");

		String filename = fileName.substring(0, i);
		String fileExt = fileName.substring(i, fileName.length());

		String filePath = Util.saveBitmaptoJpeg(myBitmap, "SampleFile" + position, fileExt);

		if(position == 1) {
			ivSpotPic1.setImageBitmap(myBitmap);

			image1Path = filePath;

		} else if (position == 2) {
			ivSpotPic2.setImageBitmap(myBitmap);
			image2Path = filePath;
		}




	}

	protected boolean isFileChange(String name) {
		boolean isChange = false;
		if("file".equals(name)) {
			isChange = file1Change;
		} else if ("file1".equals(name)) {
			isChange = file2Change;
		}

		return isChange;
	}


}
