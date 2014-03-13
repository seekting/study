
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
        name = "process:activity,ͨѶ¼���";

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Button btn = new Button(this);
        btn.setText("������ͨѶ¼");
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
        intent.putExtra(ContactsContract.Intents.Insert.NAME, "����ͦ");// ������ʾ�����ֿ�
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, "13691168978");// ������ʾ�ں����
        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, "���ǹ�������");// ��ַ��ʾ�ڵ�ַ��
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, "543090717@qq.com");// ������ʾ�ں����
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, "��ɽ����");// ��ַ��ʾ�ڵ�ַ��
        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, "�������ʦ");// ��ַ��ʾ�ڵ�ַ��
        intent.putExtra(ContactsContract.Intents.Insert.NOTES, "��ע");// ��ַ��ʾ�ڵ�ַ��
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

        //��ַֻ��֧��һ��
        row2 = new ContentValues();
        row2.put(ContactsContract.Contacts.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
        row2.put(ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME, "��ַ");
        row2.put(ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME);
        data.add(row2);
        intent.putExtra(ContactsContract.Intents.Insert.DATA, data);
        startActivity(intent);

    }
}
