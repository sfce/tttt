package us.cmcc.sms.cleaner.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import me.sfce.library.activity.BaseFragment;
import us.cmcc.sms.cleaner.SmsThreadActivity;
import us.cmcc.sms.cleaner.MyApplication;
import us.cmcc.sms.cleaner.R;
import us.cmcc.sms.cleaner.dao.DaoMaster;
import us.cmcc.sms.cleaner.dao.ThreadDirDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-14
 * Time: 下午4:41
 */
public class IncomeListFragment extends BaseFragment {
    private long dirId = 0;
    @Override
    protected View onPrepareView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fillMSG();
    }

    private void fillMSG() {
        String selection = null;
        if (dirId != 0) {// 查询目录中的threadid
            DaoMaster.DevOpenHelper dbOpenHelper = new DaoMaster.DevOpenHelper(getActivity(), MyApplication.DB_NAME, null);
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            ThreadDirDao threadDirDao = daoMaster.newSession().getThreadDirDao();
            Cursor myCursor = db.query(true, threadDirDao.getTablename(), threadDirDao.getAllColumns(),
                    "dirId=?", new String[]{dirId + ""}, null, null, null, null);
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            while (myCursor.moveToNext()) {
                sb.append(myCursor.getString(1)).append(",");
            }
            myCursor.close();
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.lastIndexOf(","));
            }
            sb.append(")");

            selection = "thread_id in " + sb;
        }
        ListView listView = (ListView) getView().findViewById(android.R.id.list);
        Cursor cursor = getActivity().getContentResolver().query(Uri.parse("content://sms/conversations"),
                new String[]{"thread_id AS _id", "sms.address AS address", "msg_count", "snippet", "sms.date AS date"},
                selection, null, "date DESC");
        Cursor smsCursor = new CursorWrapper(cursor) {
            @Override
            public String getString(int columnIndex) {
                if (columnIndex == 1) {// address列
                    String address = super.getString(1);
                    // com.android.contacts;contacts明明是两种主机名，为什么简写的不可以？？？？？
                    Cursor contactsCursor = getActivity().getContentResolver().query(
                            Uri.parse("content://com.android.contacts/phone_lookup/" + address), null, null, null, null);
                    if (contactsCursor.moveToFirst()) {
                        address = contactsCursor.getString(contactsCursor.getColumnIndex("display_name"));
                    }
                    if (super.getInt(2) != 1) {// 如果短信数量不为１,就拼接为address(msg_count)
                        return address + "(" + super.getString(2) + ")";
                    } else {
                        return address;
                    }
                }
                if (columnIndex == 4) {// date列，返回格式化后的字符串
                    Date date = new Date(super.getLong(4));
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
                }
                return super.getString(columnIndex);
            }
        };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.sms_item, smsCursor, new String[] { "address", "snippet", "date" },
                new int[] { android.R.id.title, android.R.id.content, android.R.id.summary });

        listView.setAdapter(adapter);

        if (dirId == 0) {
            registerForContextMenu(getActivity().findViewById(android.R.id.list));
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SmsThreadActivity.class);
                intent.putExtra("thread_id", id);//将会话ID传过去
                startActivity(intent);
            }
        });
    }
}
