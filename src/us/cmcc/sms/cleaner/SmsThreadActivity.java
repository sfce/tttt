package us.cmcc.sms.cleaner;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.internal.widget.IcsToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SmsThreadActivity extends SherlockActivity {

    private String phoneNum = null;
    private String contactName = null;
    private long thread_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSherlock().getActionBar().setDisplayShowHomeEnabled(false);
        setContentView(R.layout.conversation);
        Bundle bundle = getIntent().getExtras();
        thread_id = bundle.getLong("thread_id");
        initInfo();
        initList();
    }

    private void initInfo() {
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/"), new String[]{"address"},
                "sms.thread_id=?", new String[]{thread_id + ""}, null);
        if (cursor.moveToFirst()) {
            phoneNum = cursor.getString(0);
            Cursor contactsCursor = getContentResolver().query(
                    Uri.parse("content://com.android.contacts/phone_lookup/" + phoneNum), null, null, null, null);
            if (contactsCursor.moveToFirst()) {
                contactName = contactsCursor.getString(contactsCursor.getColumnIndex("display_name"));
            }
            contactsCursor.close();
        }
        cursor.close();
        if (contactName != null) {
            setTitle(contactName + "<" + phoneNum + ">");
        } else {
            setTitle(phoneNum);
        }
    }

    private void initList() {
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms"),
                new String[]{
                        "sms.thread_id AS _id",
                        "sms.address AS address",
                        "sms.body AS content",
                        "sms.date AS date",
                        "sms.type AS type"},
                "sms.thread_id=?", new String[]{thread_id + ""}, "sms.date ASC");
        ListView listView = (ListView) findViewById(android.R.id.list);
        Cursor messageCursor = new CursorWrapper(cursor) {
            @Override
            public String getString(int columnIndex) {
                switch (columnIndex) {
                    case 1://address
                        if (super.getInt(4) == 2) {
                            return "我:";
                        } else {
                            if (contactName != null) {
                                return contactName + ":";
                            } else {
                                return super.getString(columnIndex) + ":";
                            }
                        }
                    case 3://date
                        Date date = new Date(super.getLong(3));
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        SimpleDateFormat sdf = new SimpleDateFormat();
                        if (calendar.getTime().after(date)) {
                            sdf.applyLocalizedPattern("yyyy-MM-dd HH:mm");
                            return sdf.format(date);
                        } else {
                            sdf.applyLocalizedPattern("HH:mm");
                            return sdf.format(date);
                        }
                    default:
                        return super.getString(columnIndex);
                }
            }
        };
//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.conversationitem,
//                messageCursor, new String[]{"address", "date", "content"}, new int[]{android.R.id.message, R.id.date, android.R.id.content});
        CursorAdapter adapter = new CursorAdapter(this, messageCursor, true) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return View.inflate(parent.getContext(), R.layout.conversationitem, null);
            }

            @Override
            public void bindView(View view, final Context context, final Cursor cursor) {
                TextView message = (TextView) view.findViewById(android.R.id.message);
                TextView content = (TextView) view.findViewById(android.R.id.content);
                TextView date = (TextView) view.findViewById(R.id.date);
                Button report = (Button) view.findViewById(android.R.id.cut);
                date.setText(cursor.getString(3));
                final String body = cursor.getString(2);
                if (cursor.getInt(4) == 2) {
                    report.setVisibility(View.GONE);
                    content.setVisibility(View.GONE);
                    message.setVisibility(View.VISIBLE);
                    message.setText(body);
                } else {
                    report.setVisibility(View.VISIBLE);
                    content.setVisibility(View.VISIBLE);
                    message.setVisibility(View.GONE);
                    content.setText(body);
                    report.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String telephone;
                            if (phoneNum != null && phoneNum.length() > 11) {
                                telephone = phoneNum.substring(phoneNum.length() - 11);
                            } else {
                                telephone = phoneNum;
                            }
                            String smsContent = telephone + "*" + body;
                            SmsManager smsManager = SmsManager.getDefault();
                            List<String> content = smsManager.divideMessage(smsContent);
                            for (String text : content) {
                                smsManager.sendTextMessage("10086999", null, text, null, null);
                            }
                            IcsToast.makeText(SmsThreadActivity.this, "举报成功", IcsToast.LENGTH_SHORT).show();
                            Intent service = new Intent(SmsThreadActivity.this, ReportService.class);
                            service.putExtra("sms", smsContent);
                            startService(service);
                        }
                    });
                }
            }
        };
        listView.setAdapter(adapter);
    }

    public void send(View view) {
        EditText smsContentText = (EditText) findViewById(R.id.content_to_send);
        String smsContent = smsContentText.getText().toString();
        SmsManager smsManager = SmsManager.getDefault();
        List<String> content = smsManager.divideMessage(smsContent);
        for (String text : content) {
            smsManager.sendTextMessage(phoneNum, null, text, null, null);
        }
        IcsToast.makeText(SmsThreadActivity.this, "短信已发出", IcsToast.LENGTH_SHORT).show();
        smsContentText.setText("");
        //将短信存至已发信息组。
        ContentValues values = new ContentValues();
        values.put("address", phoneNum);
        values.put("body", smsContent);
        values.put("date", System.currentTimeMillis());
        values.put("type", 2);
        getContentResolver().insert(Uri.parse("content://sms"), values);
    }
}
