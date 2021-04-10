package com.anningtex.ancustomqrcode.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.anningtex.ancustomqrcode.R;

/**
 * @author: Song
 */
public class TitleEditText extends FrameLayout {
    private String hint;
    private int imageRes;
    private String labe;
    private EditText editText;
    private ImageView mImageView;
    private String inputType;
    private boolean enabled;
    private Button btn;
    private int background;

    public TitleEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);

    }

    public TitleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public TitleEditText(Context context) {
        super(context);
        init(context, null, -1);
    }

    @SuppressLint("NewApi")
    private void init(Context context, AttributeSet attrs, int defStyle) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_title_edit_text, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleEditText);
        hint = typedArray.getString(R.styleable.TitleEditText_hintInfo);
        labe = typedArray.getString(R.styleable.TitleEditText_label);
        imageRes = typedArray.getResourceId(R.styleable.TitleEditText_imageRes, 0);
        inputType = typedArray.getString(R.styleable.TitleEditText_inputType);
        enabled = typedArray.getBoolean(R.styleable.TitleEditText_enabled, true);
        typedArray.recycle();
        editText = view.findViewById(R.id.edit);
        mImageView = view.findViewById(R.id.image);
        btn = view.findViewById(R.id.btn);
        mImageView.setImageResource(imageRes);
        editText.setHint(hint);
        editText.setEnabled(enabled);
        editText.setText(labe);
        //根据类型显示需要的样式，在布局文件中设置
        if ("number".equals(inputType)) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if ("phone".equals(inputType)) {
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
            setMaxLength(11);
        } else if ("float".equals(inputType)) {
            editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else if ("pwd".equals(inputType)) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        }
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setHint(String hint) {
        editText.setHint(hint);
    }

    public void setTitleText(String title) {
//        mImageView.setText(year);
    }

    public void setEditText(String value) {
        editText.setText(value);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled == false) {
//        editText.setInputType(InputType.TYPE_CLASS_TEXT);
//        editText.setSingleLine(false);
//        editText.setBackground(null);
//        this.setBackgroundResource(R.drawable.icon_horizontal_line_bg);
        }
        editText.setEnabled(enabled);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        editText.setOnClickListener(l);
    }

    public void setMaxLength(int maxLength) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public void setOnBtnClickListener(OnClickListener l) {
        btn.setVisibility(View.VISIBLE);
//        btn.setBackgroundResource(R.mipmap.arraw_down_gray);
        btn.setOnClickListener(l);

    }

    public void setTextBtn(String text, int textColor, OnClickListener l) {
        btn.setVisibility(View.VISIBLE);
        btn.setText(text);
        btn.setTextColor(textColor);
        btn.setOnClickListener(l);
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        editText.addTextChangedListener(textWatcher);
    }

    public void setSelection(int length) {
        editText.setSelection(length);
    }

    public void removeTextChangedListener(TextWatcher watcher) {
        editText.removeTextChangedListener(watcher);
    }

    public int getSelectionStart() {
        return editText.getSelectionStart();
    }

    public int getSelectionEnd() {
        return editText.getSelectionEnd();
    }

    public void setEditEnable(boolean isEdit) {
        editText.setEnabled(isEdit);
    }

    public void setKeyListener(KeyListener listener) {
        editText.setKeyListener(listener);
    }
}
