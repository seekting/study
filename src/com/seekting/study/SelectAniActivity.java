
package com.seekting.study;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.seekting.study.view.ExpandAniLinearLayout;
import com.seekting.study.view.ExpandAniLinearLayout.onItemSelectListener;

public class SelectAniActivity extends BaseActivity implements OnClickListener,
        onItemSelectListener, OnGlobalLayoutListener {
    public SelectAniActivity() {
        name = "一个选择动画";
    }

    Button button;
    ViewGroup selectGroup;
    ExpandAniLinearLayout mSelectLayout = null;
    ViewGroup mRoot;

    LayoutInflater inflater;
    PopupWindow mSeachEngineHelperWindow;
    EditText edit;
    private InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_ani_layout);
        mRoot = (ViewGroup) findViewById(R.id.layout);
        mRoot.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mInputMethodManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        edit = (EditText) findViewById(R.id.edit);
        edit.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        edit.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI
                | EditorInfo.IME_ACTION_DONE);
        button = (Button) findViewById(R.id.start);
        button.setOnClickListener(this);
        inflater = LayoutInflater.from(this);
        selectGroup = (ViewGroup) inflater.inflate(
                R.layout.search_engine_ani_container,
                null);
        mSelectLayout = (ExpandAniLinearLayout) selectGroup.findViewById(R.id.select);
        mSelectLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mSelectLayout.setItemBgRes(R.drawable.search_engine_item_selector);
        mSelectLayout.addItem(R.drawable.search_engine_baidu, R.string.baidu);
        mSelectLayout.addItem(R.drawable.search_engine_google, R.string.google);
        mSelectLayout.addItem(R.drawable.search_engine_novel, R.string.nevel);
        mSelectLayout.addItem(R.drawable.search_engine_video, R.string.video);
        mSelectLayout.addItem(R.drawable.search_engine_taobao, R.string.taobao);
        mSelectLayout.setSelectPosition(3);
        mSelectLayout.setOnItemSelectListener(this);

        mSeachEngineHelperWindow = new PopupWindow(selectGroup,
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        mSeachEngineHelperWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mSeachEngineHelperWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);

        mSeachEngineHelperWindow.setBackgroundDrawable(new ColorDrawable());
        mSeachEngineHelperWindow.setOutsideTouchable(true);
        // mSeachEngineHelperWindow.setAnimationStyle(R.style.PopupEmptyAnimation);
        mSeachEngineHelperWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // updatemSeachIconImgArrow(true);
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (button == v) {
            if (!mSeachEngineHelperWindow.isShowing()) {
                mSeachEngineHelperWindow.showAsDropDown(edit);
                hideKeyboard();
                mSelectLayout.startExpand();
                // mSelectLayout.show();

            }
            else {
                mSeachEngineHelperWindow.dismiss();
            }
        }
    }


    private void hideKeyboard() {
        // mInputMethodManager.hideSoftInputFromWindow(
        // edit.getWindowToken(), 0);
    }

    @Override
    public void onSelect(int position) {

        // mRoot.removeView(mSelectLayout);
        mSeachEngineHelperWindow.dismiss();
        Toast.makeText(this, "position:" + position, 500).show();
    }

    @Override
    public void onGlobalLayout() {
        System.out.println("fuck");
    }
}
