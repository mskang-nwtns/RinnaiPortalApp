package kr.co.rinnai.dms.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.CommonValue;

public class CustomApplyButtonView extends RelativeLayout {

    RelativeLayout bg;
    ImageView symbol;

    int symbol_resID;
    int symbol_click_resID;


    public CustomApplyButtonView(Context context) {
        super(context);
        initView();

    }

    public CustomApplyButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);

    }

    public CustomApplyButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView();
        getAttrs(attrs, defStyle);

    }

    private void initView() {
        inflate(getContext(), R.layout.layout_apply_button, this);

        bg = (RelativeLayout) findViewById(R.id.button_bg);
        symbol = (ImageView) findViewById(R.id.symbol);

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

        symbol_resID = typedArray.getResourceId(R.styleable.CustomButton_symbol, R.drawable.menu_01_normal);
        symbol.setImageResource(symbol_resID);
        symbol_click_resID = typedArray.getResourceId(R.styleable.CustomButton_pressSymbol, R.drawable.menu_01_press);

        String text_string = typedArray.getString(R.styleable.CustomButton_text);
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
            symbol.setImageResource(symbol_click_resID);
        } else {
            bg.setBackgroundResource(R.drawable.product_move_bg_normal);
            symbol.setImageResource(symbol_resID);
        }
        //symbol.setImageResource(symbol_resID);
    }


}
