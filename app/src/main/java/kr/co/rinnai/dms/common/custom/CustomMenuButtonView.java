package kr.co.rinnai.dms.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kr.co.rinnai.dms.R;

public class CustomMenuButtonView extends RelativeLayout {

    RelativeLayout bg;
    ImageView symbol;
    TextView text;

    int symbol_resID;
    int symbol_click_resID;


    public CustomMenuButtonView(Context context) {
        super(context);
        initView();

    }

    public CustomMenuButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);

    }

    public CustomMenuButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView();
        getAttrs(attrs, defStyle);

    }

    private void initView() {
        inflate(getContext(), R.layout.layout_menu_button, this);

        bg = (RelativeLayout) findViewById(R.id.button_bg);
        symbol = (ImageView) findViewById(R.id.symbol);
        text = (TextView) findViewById(R.id.tv_title);

        bg.setBackgroundResource(R.drawable.main_menu_bg_normal);
        text.setTextColor(Color.parseColor("#0a0916"));

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
        text.setText(text_string);
        typedArray.recycle();
    }

    void setBg(int bg_resID) {
        bg.setBackgroundResource(bg_resID);
    }
    void setSymbol(int symbol_resID) {
        symbol.setImageResource(symbol_resID);
    }
    void setTextColor(int color) {
        text.setTextColor(color);
    }
    void setText(String text_string) {
        text.setText(text_string);
    }
    void setText(int text_resID) {
        text.setText(text_resID);
    }

    public void buttonClick(boolean isTouch) {

        if(isTouch ) {
            bg.setBackgroundResource(R.drawable.main_menu_bg_press);
            text.setTextColor(Color.parseColor("#FFFFFF"));
            symbol.setImageResource(symbol_click_resID);
        } else {
            bg.setBackgroundResource(R.drawable.main_menu_bg_normal);
            text.setTextColor(Color.parseColor("#0a0916"));
            symbol.setImageResource(symbol_resID);
        }
        //symbol.setImageResource(symbol_resID);
    }


}
