
package com.seekting.study;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

/**
 */
public class QRCodeResultActivity extends BaseActivity implements OnClickListener {

    public static final String RESULT = "result";
    public ParsedResult mParsedResult;
    private ClipboardManager mClipboardManager;
    private TableLayout mAddressBookLayout;
    private LayoutInflater mLayoutInflater;
    private LinearLayout addressbookParent;
    private LinearLayout btns;
    private TextView mQRResultTextView;
    private TextView btnLeft;
    private TextView btnRight;
    private boolean isBookMark;

    public QRCodeResultActivity() {
        name = "qrcode";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutInflater = LayoutInflater.from(this);
        mClipboardManager = (ClipboardManager) this.getSystemService(CLIPBOARD_SERVICE);
        setContentView(R.layout.qr_result_layout);
        btnLeft = (TextView) findViewById(R.id.btn_left);
        btnRight = (TextView) findViewById(R.id.btn_right);
        mQRResultTextView = (TextView) findViewById(R.id.qr_txt_result);
        addressbookParent = (LinearLayout) findViewById(R.id.addressbook_parent);
        ColorDrawable colorDrawable = new DividerColorDrawable(Color.parseColor("#ffe2e2e2"));
        colorDrawable.setBounds(0, 0, 1, 1);
        addressbookParent.setDividerDrawable(colorDrawable);
        btns = (LinearLayout) findViewById(R.id.btns);
        btns.setDividerDrawable(colorDrawable);
        mAddressBookLayout = (TableLayout) findViewById(R.id.addressbook_layout);
        mAddressBookLayout.setDividerDrawable(colorDrawable);
        btnLeft = (TextView) findViewById(R.id.btn_left);
        btnRight = (TextView) findViewById(R.id.btn_right);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        handlerIntent();

        // testAddressBook();
        // testText();
    }

    private void handlerIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            Object o = intent.getSerializableExtra(RESULT);
            if (o != null && o instanceof ParsedResult) {
                mParsedResult = (ParsedResult) intent.getSerializableExtra(RESULT);
                isBookMark = mParsedResult.getType() == ParsedResultType.ADDRESSBOOK;
                setUi();
            } else {
                testAddressBook();
                // testText();
            }
        }
    }

    private void setUi() {
        if (isBookMark) {
            setAddressBookUi();
        } else {
            setTextUi();
        }
    }

    private void setAddressBookUi() {
        mQRResultTextView.setVisibility(View.GONE);
        mAddressBookLayout.setVisibility(View.VISIBLE);
        btnRight.setText(getString(R.string.qr_result_add_contact));
        AddressBookParsedResult addressBookParsedResult = (AddressBookParsedResult) mParsedResult;
        addTypeValueUi(getString(R.string.qr_result_name), addressBookParsedResult.getNames());
        addTypeValueUi(getString(R.string.qr_result_phone),
                addressBookParsedResult.getPhoneNumbers());
        addTypeValueUi(getString(R.string.qr_result_address),
                addressBookParsedResult.getAddresses());
        addTypeValueUi(getString(R.string.qr_result_mail),
                addressBookParsedResult.getEmails());
        addTypeValueUi(getString(R.string.qr_result_org), addressBookParsedResult.getOrg());
        addTypeValueUi(getString(R.string.qr_result_job_title), addressBookParsedResult.getTitle());
        addTypeValueUi(getString(R.string.qr_result_website), addressBookParsedResult.getURLs());
        addTypeValueUi(getString(R.string.qr_result_note), addressBookParsedResult.getNote());
    }

    private void setTextUi() {
        mQRResultTextView.setVisibility(View.VISIBLE);
        mAddressBookLayout.setVisibility(View.GONE);
        btnRight.setText(getString(R.string.qr_result_search));
        mQRResultTextView.setText(mParsedResult.getDisplayResult());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_right:
                if (isBookMark) {
                    addContact();
                } else {
                    search();
                }
                break;
            case R.id.btn_left:
                if (mParsedResult != null) {
                    mParsedResult.toString();
                    ClipData data = ClipData
                            .newPlainText("qr_result", mParsedResult.getDisplayResult());
                    mClipboardManager.setPrimaryClip(data);
                    Toast.makeText(this, R.string.has_clip, Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    private void search() {
        if (mParsedResult == null)
            return;
        String url = generateUrl(mParsedResult.getDisplayResult());
        if (!TextUtils.isEmpty(url)) {
            openUrl(url);
        }
    }

    private void addContact() {
        if (mParsedResult == null)
            return;
        Uri insertUri = ContactsContract.Contacts.CONTENT_URI;
        Intent intent = new Intent(Intent.ACTION_INSERT, insertUri);
        AddressBookParsedResult addressBookParsedResult = (AddressBookParsedResult) mParsedResult;
        ArrayList<ContentValues> data = new ArrayList<ContentValues>();
        String[] names = addressBookParsedResult.getNames();

        // 加名字
        addSubContact(intent, ContactsContract.Intents.Insert.NAME, names);
        String[] phoneTypes = addressBookParsedResult.getPhoneTypes();
        String[] phoneNumbers = addressBookParsedResult.getPhoneNumbers();
        // 加电话
        addSubContact(data, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                phoneTypes,
                phoneNumbers);
        // 加地址
        addSubContact(intent, ContactsContract.Intents.Insert.POSTAL,
                addressBookParsedResult.getAddresses());
        // 加邮箱
        addSubContact(data, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                addressBookParsedResult.getEmailTypes(), addressBookParsedResult.getEmails());
        // 加公司
        addSubContact(intent, ContactsContract.Intents.Insert.COMPANY,
                addressBookParsedResult.getOrg());
        // 加职位
        addSubContact(intent, ContactsContract.Intents.Insert.JOB_TITLE,
                addressBookParsedResult.getTitle());
        // 加网页
        addSubContact(data, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Website.URL,
                null, addressBookParsedResult.getURLs());

        // 加备注
        addSubContact(intent, ContactsContract.Intents.Insert.NOTES,
                addressBookParsedResult.getNote());
        if (data.size() > 0) {
            intent.putExtra(ContactsContract.Intents.Insert.DATA, data);
        }
        startActivity(intent);

    }

    private void addSubContact(Intent intent, String tag, String... values) {
        if (values == null || values.length == 0) {
            return;
        }
        intent.putExtra(tag, values[0]);
    }

    private void addSubContact(List<ContentValues> data, String uri, String tag, String[] types,
            String[] values) {

        if (values == null) {
            return;
        }
        for (int i = 0; i < values.length; i++) {
            if (TextUtils.isEmpty(values[i])) {
                continue;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put(ContactsContract.Contacts.Data.MIMETYPE, uri);
            if (uri.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                if (types != null && types.length > i) {
                    String type = types[i];

                    parsePhoneType(contentValues, type);
                }
            }
            contentValues.put(tag, values[i]);
            data.add(contentValues);
        }
    }

    private void parsePhoneType(ContentValues contentValues, String type) {
        if (type != null) {
            type = type.toUpperCase();
            if (type.equals("CELL")) {
                contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            } else if (type.equals("WORK")) {
                contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
            }
            else if (type.equals("HOME")) {
                contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_HOME);

            }
        }
    }

    private void addTypeValueUi(String tag, String... values) {
        if (values == null || values.length == 0) {
            return;
        }
        TableRow item = (TableRow) mLayoutInflater.inflate(
                R.layout.addressbook_item, null);
        LinearLayout itemsLayout = (LinearLayout) item.findViewById(R.id.value_items_layout);
        TextView tagText = (TextView) item.findViewById(R.id.tag);
        boolean hasItem = false;
        for (String value : values) {
            if (TextUtils.isEmpty(value)) {
                continue;
            }
            hasItem = true;
            TextView valueText = (TextView) mLayoutInflater
                    .inflate(R.layout.qr_result_value_text, null);
            valueText.setLinksClickable(true);
            tagText.setText(tag);
            valueText.setText(value);
            itemsLayout.addView(valueText);
        }

        if (hasItem)
            mAddressBookLayout.addView(item);
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

    public static class DividerColorDrawable extends ColorDrawable {

        public DividerColorDrawable(int color) {
            super(color);
        }

        @Override
        public int getIntrinsicHeight() {
            Rect r = getBounds();
            if (r != null) {
                return r.bottom - r.top;
            }
            return super.getIntrinsicHeight();
        }

        @Override
        public int getIntrinsicWidth() {
            Rect r = getBounds();
            if (r != null) {
                return r.right - r.left;
            }
            return super.getIntrinsicWidth();
        }

    }

    private void testAddressBook() {
        mQRResultTextView.setVisibility(View.GONE);
        mAddressBookLayout.setVisibility(View.VISIBLE);
        btnRight.setText(getString(R.string.qr_result_add_contact));
        String[] names = new String[] {
                "张三", "李四"
        };
        String[] phones = new String[] {
                "13691168978", "010-7325059"
        };

        String[] addresses = new String[] {
                "复星国际中心", "天通苑"
        };

        String[] emails = new String[] {
                "zhangxingtingz.comzhangxingtingz.comzhangxingtingz.comzhangxingtingz.comzhangxingtingz.com",
                "543090717@qq.com"
        };
        addTypeValueUi("名字", names);
        addTypeValueUi("电话",
                phones);
        addTypeValueUi("地址",
                addresses);
        addTypeValueUi("邮箱",
                emails);
        addTypeValueUi("公司", "金山网络");
        addTypeValueUi("职位", "android研发");
        addTypeValueUi("网址",
                "www.test.com");
        addTypeValueUi("备注", "dota 走起");
    }

    private void testText() {
        mQRResultTextView.setVisibility(View.VISIBLE);
        mAddressBookLayout.setVisibility(View.GONE);
        btnRight.setText(getString(R.string.qr_result_search));
        mQRResultTextView
                .setText("zhangxingtingzhangxingtingzhangxingtingzhangxingtingzhangxingting@ijinshan.com");
    }
}
