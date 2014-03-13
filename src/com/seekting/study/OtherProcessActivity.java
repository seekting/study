
package com.seekting.study;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class OtherProcessActivity extends BaseActivity {

    public OtherProcessActivity() {
        name = "process:activity,通讯录添加";

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Button btn = new Button(this);
        btn.setText("点击添加通讯录");
        setContentView(btn);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createContact();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }

    private void createContact() {
        Uri insertUri = ContactsContract.Contacts.CONTENT_URI;
        Intent intent = new Intent(Intent.ACTION_INSERT, insertUri);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, "张兴挺");// 名字显示在名字框
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, "13691168978");// 号码显示在号码框
        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, "复星国际中心");// 地址显示在地址框
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, "543090717@qq.com");// 号码显示在号码框
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, "金山网络");// 地址显示在地址框
        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, "软件工程师");// 地址显示在地址框
        intent.putExtra(ContactsContract.Intents.Insert.NOTES, "备注");// 地址显示在地址框
        ArrayList<ContentValues> data = new ArrayList<ContentValues>();
        ContentValues row2 = new ContentValues();
        row2.put(ContactsContract.Contacts.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
        row2.put(ContactsContract.CommonDataKinds.Website.URL, "www.baidu.com");

        data.add(row2);

        row2 = new ContentValues();
        row2.put(ContactsContract.Contacts.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
        row2.put(ContactsContract.CommonDataKinds.Website.URL, "www.yahu.com");
        data.add(row2);
        row2 = new ContentValues();
        row2.put(ContactsContract.Contacts.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        row2.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "136944456456");
        data.add(row2);
        row2 = new ContentValues();
        row2.put(ContactsContract.Contacts.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        row2.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "16");
        row2.put(ContactsContract.CommonDataKinds.Phone.TYPE,
                String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK));
        data.add(row2);

        //地址只能支持一个
        row2 = new ContentValues();
        row2.put(ContactsContract.Contacts.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
        row2.put(ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME, "地址");
        row2.put(ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME);
        data.add(row2);
        intent.putExtra(ContactsContract.Intents.Insert.DATA, data);
        startActivity(intent);

    }
}
