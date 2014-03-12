
package com.seekting.study;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

/**
 */
public class QRCodeResultActivity extends BaseActivity implements OnClickListener {

    public static final String RESULT = "result";
    public ParsedResult mParsedResult;
    private Button mAddContactBtn;
    private Button mCopyBtn;
    private Button mSearchBtn;
    private ClipboardManager mClipboardManager;
    private LinearLayout mAddressBookLayout;
    private LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutInflater = LayoutInflater.from(this);
        mClipboardManager = (ClipboardManager) this.getSystemService(CLIPBOARD_SERVICE);
        setContentView(R.layout.qr_result_layout);
        mAddressBookLayout = (LinearLayout) findViewById(R.id.addressbook_layout);
        mAddContactBtn = (Button) findViewById(R.id.add_contact);
        mCopyBtn = (Button) findViewById(R.id.copy);
        mSearchBtn = (Button) findViewById(R.id.search);
        mCopyBtn.setOnClickListener(this);
        mAddContactBtn.setOnClickListener(this);
        mSearchBtn.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            Object o = intent.getSerializableExtra(RESULT);
            if (o != null && o instanceof ParsedResult) {
                mParsedResult = (ParsedResult) intent.getSerializableExtra(RESULT);
                if (mParsedResult.getType() == ParsedResultType.ADDRESSBOOK) {
                    setAddressBookUi();
                } else {
                    setTextUi();
                }
            }
        }
    }

    private void setAddressBookUi() {
        AddressBookParsedResult addressBookParsedResult = (AddressBookParsedResult) mParsedResult;
        addTypeValueUi("名字", null, addressBookParsedResult.getNames());
        addTypeValueUi("电话", addressBookParsedResult.getPhoneTypes(),
                addressBookParsedResult.getPhoneNumbers());
        addTypeValueUi("地址", addressBookParsedResult.getAddressTypes(),
                addressBookParsedResult.getAddresses());
        addTypeValueUi("邮箱", addressBookParsedResult.getEmailTypes(),
                addressBookParsedResult.getEmails());
        addTypeValueUi("公司", addressBookParsedResult.getOrg());
        addTypeValueUi("职位", addressBookParsedResult.getTitle());
        addTypeValueUi("网址", null, addressBookParsedResult.getURLs());
        addTypeValueUi("备注", addressBookParsedResult.getNote());
    }

    private void setTextUi() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                String url = generateUrl("xx");
                if (!TextUtils.isEmpty(url)) {
                    openUrl(url);
                }
                break;
            case R.id.copy:
                ClipData data = ClipData.newPlainText("qr_result", "www.qrcode.com");
                mClipboardManager.setPrimaryClip(data);
                break;
            case R.id.add_contact:
                addContact();
                break;

            default:
                break;
        }

    }

    private void addContact() {
        AddressBookParsedResult addressBookParsedResult = (AddressBookParsedResult) mParsedResult;
        String[] names = addressBookParsedResult.getNames();
        if (names != null) {
            for (int i = 0; i < names.length; i++) {

            }
        }
        Uri insertUri = ContactsContract.Contacts.CONTENT_URI;
        Intent intent = new Intent(Intent.ACTION_INSERT, insertUri);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, "中文");// 名字显示在名字框
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, "12333333");// 号码显示在号码框

        startActivity(intent);

    }

    private void addTypeValueUi(String tag, String[] type, String[] value) {

        if (value == null || value.length == 0) {
            return;
        }
        if (type == null) {

            type = getTags(tag, value.length);

        }
        for (int i = 0; i < value.length; i++) {
            if (i < type.length) {
                addTypeValueUi(type[i], value[i]);
            }
        }
    }

    private void addTypeValueUi(String tag, String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        LinearLayout item = (LinearLayout) mLayoutInflater.inflate(
                R.layout.addressbook_item, null);
        TextView tagText = (TextView) item.findViewById(R.id.tag);
        TextView valeText = (TextView) item.findViewById(R.id.value);
        tagText.setText(tag);
        valeText.setText(value);
        mAddressBookLayout.addView(item);
    }

    private String[] getTags(String tag, int length) {
        if (length <= 0) {
            return null;
        }
        String[] tags = new String[length];
        tags[0] = tag;
        for (int i = 1; i < length; i++) {
            tags[i] = tag + i;
        }
        return tags;

    }

    private String generateUrl(String keyWord) {
        return "";
    }

    private void openUrl(String url) {
        setResult(Activity.RESULT_OK);
        Intent intent = new Intent();
        intent.setAction(url);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }
}
