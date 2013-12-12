package us.cmcc.sms.cleaner.fragment;

import java.util.List;

import us.cmcc.sms.cleaner.MyApplication;
import us.cmcc.sms.cleaner.R;
import us.cmcc.sms.cleaner.dao.BlackWord;
import us.cmcc.sms.cleaner.dao.BlackWordDao;
import us.cmcc.sms.cleaner.dao.DaoMaster;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-8
 * Time: 下午8:03
 */
public class BlackWordFragment extends ListFragment {

    private BlackWordDao dao;
    private List<BlackWord> blackWords;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView empty = (TextView) getView().findViewById(android.R.id.empty);
        empty.setText("包含展示在这里的关键字的短信将被拦截");
        DaoMaster.DevOpenHelper dbOpenHelper = new DaoMaster.DevOpenHelper(getActivity(), MyApplication.DB_NAME, null);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        dao = daoMaster.newSession().getBlackWordDao();
        blackWords = dao.loadAll();
        setListAdapter(new ArrayAdapter<BlackWord>(getActivity(), android.R.layout.simple_list_item_1, blackWords));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        new AlertDialog.Builder(getActivity()).setItems(new String[]{"修改", "删除", "短信", "呼叫"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        }).create().show();
    }
}
