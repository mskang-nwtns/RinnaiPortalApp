package kr.co.rinnai.dms.wms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.common.custom.CustomButtonLogoutView;
import kr.co.rinnai.dms.common.custom.CustomMenuButtonView;

public class WmsMainActivity extends BaseActivity {

    Button btnPickingRelease;
    Button btnAgencyBarcode;

    CustomMenuButtonView menu01;
    CustomMenuButtonView menu02;

    CustomMenuButtonView menu03;
    CustomMenuButtonView menu04;

    CustomMenuButtonView menu05;
    CustomMenuButtonView menu06;

    CustomButtonLogoutView btnLogout;

    boolean isPressed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wms_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        //btnPickingRelease = findViewById(R.id.btn_picking_release);
        //btnAgencyBarcode = findViewById(R.id.btn_agency_barcode);

        //btnAgencyBarcode.setOnClickListener(WmsMainActivity.this);
        //btnPickingRelease.setOnClickListener(WmsMainActivity.this);

//        int tmp = ScanManager.getInstance().aDecodeGetMultiScanNumber();
// DB safer 20.7
        menu01 = findViewById(R.id.menu_01);
        menu02 = findViewById(R.id.menu_02);

        menu03 = findViewById(R.id.menu_03);
        menu04 = findViewById(R.id.menu_04);

        menu05 = findViewById(R.id.menu_05);
        menu06 = findViewById(R.id.menu_06);

        btnLogout = findViewById(R.id.btn_logout);

        menu01.setOnTouchListener(WmsMainActivity.this);
        menu01.setOnClickListener(WmsMainActivity.this);

        menu02.setOnTouchListener(WmsMainActivity.this);
        menu02.setOnClickListener(WmsMainActivity.this);

        menu03.setOnTouchListener(WmsMainActivity.this);
        menu03.setOnClickListener(WmsMainActivity.this);

        menu04.setOnTouchListener(WmsMainActivity.this);
        menu04.setOnClickListener(WmsMainActivity.this);

        menu05.setOnTouchListener(WmsMainActivity.this);
        menu05.setOnClickListener(WmsMainActivity.this);

        menu06.setOnTouchListener(WmsMainActivity.this);
        menu06.setOnClickListener(WmsMainActivity.this);


        btnLogout.setOnTouchListener(WmsMainActivity.this);
        btnLogout.setOnClickListener(WmsMainActivity.this);

    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        /*
        if (id == btnAgencyBarcode.getId()) {
            Intent agreementIntent = new Intent(WmsMainActivity.this, WmsMenu08Activity.class);


            startActivity(agreementIntent);
        } else if (id == btnPickingRelease.getId()) {
            Intent agreementIntent = new Intent(WmsMainActivity.this, WmsMenu03Activity.class);


            startActivity(agreementIntent);
        } else if (id == menu01.getId()) {
            //Log.d("test","menu01");
        }
        */

    }

    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        isPressed = v.isPressed();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(id == menu01.getId()) {
                    menu01.buttonClick(true);
                } else if(id == menu02.getId()) {
                    menu02.buttonClick(true);
                } else if(id == menu03.getId()) {
                    menu03.buttonClick(true);
                } else if(id == menu04.getId()) {
                    menu04.buttonClick(true);
                } else if(id == menu05.getId()) {
                    menu05.buttonClick(true);
                } else if(id == menu06.getId()) {
                    menu06.buttonClick(true);
                } else if(id == btnLogout.getId()) {
                    btnLogout.buttonClick(true);
                }

                Log.d("test", "Action_DOWN " + id);
                break;
            case MotionEvent.ACTION_UP:
                Log.d("test", "Action_UP"  + id);
                Intent intent = null;
                if(id == menu01.getId()) {

                    menu01.buttonClick(false);
                    if(isPressed) {
                        intent = new Intent(WmsMainActivity.this, WmsMenu01Activity.class);
                    }
                } else if(id == menu02.getId()) {

                    menu02.buttonClick(false);
                    intent = new Intent(WmsMainActivity.this, WmsMenu02Activity.class);
                } else if(id == menu03.getId()) {
                    intent = new Intent(WmsMainActivity.this, WmsMenu03Activity.class);
                    menu03.buttonClick(false);
                } else if(id == menu04.getId()) {
                    intent = new Intent(WmsMainActivity.this, WmsMenu08Activity.class);
                    menu04.buttonClick(false);
                } else if(id == menu05.getId()) {
                    intent = new Intent(WmsMainActivity.this, WmsMenu05Activity.class);
                    menu05.buttonClick(false);
                } else if(id == menu06.getId()) {
                    intent = new Intent(WmsMainActivity.this, WmsMenu07Activity.class);
                    menu06.buttonClick(false);
                } else if(id == btnLogout.getId()) {
                    // intent = new Intent(WmsMainActivity.this, WmsMenu03Activity.class);
                    btnLogout.buttonClick(false);
                }
                if(intent != null) {
                    startActivity(intent);
                }
                break;

        }

        return false;
    }
}

