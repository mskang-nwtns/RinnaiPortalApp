package kr.co.rinnai.dms.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.CommonValue;

public class CustomMoveButtonView extends RelativeLayout {

    RelativeLayout bg;
    ImageView symbol;


    int symbol_resID;
    int symbol_click_resID;


    public CustomMoveButtonView(Context context) {
        super(context);
        initView();

    }

    public CustomMoveButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);

    }

    public CustomMoveButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView();
        getAttrs(attrs, defStyle);

    }

    private void initView() {
        inflate(getContext(), R.layout.layout_move, this);

        bg = (RelativeLayout) findViewById(R.id.button_bg);


        bg.setBackgroundResource(R.drawable.product_move_bg_normal);

    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomButton);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomButton, defStyle, 0); setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {


        typedArray.recycle();
    }

    void setBg(int bg_resID) {
        bg.setBackgroundResource(bg_resID);
    }
    void setSymbol(int symbol_resID) {
        symbol.setImageResource(symbol_resID);
    }


    public void buttonClick(boolean isTouch) {

        Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "로케이션 읽음 : " + isTouch);
        if(isTouch ) {
            bg.setBackgroundResource(R.drawable.product_move_bg_press);
//            text.setTextColor(Color.parseColor("#FFFFFF"));
            //symbol.setImageResource(symbol_click_resID);
        } else {
            bg.setBackgroundResource(R.drawable.product_move_bg_normal);
//            text.setTextColor(Color.parseColor("#FFFFFF"));
//            symbol.setImageResource(symbol_resID);
        }
        //symbol.setImageResource(symbol_resID);
    }


}
