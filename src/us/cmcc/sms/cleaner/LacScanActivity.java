package us.cmcc.sms.cleaner;

import android.app.AlertDialog;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-9
 * Time: 下午11:41
 */
public class LacScanActivity extends SherlockListActivity {

    private ArrayAdapter<Item> adapter;

    private class Item {
        GsmCellLocation cellLocation;
        List<NeighboringCellInfo> neighboringCellInfo;
        String time;

        private Item(GsmCellLocation cellLocation, List<NeighboringCellInfo> neighboringCellInfo, String time) {
            this.cellLocation = cellLocation;
            this.neighboringCellInfo = neighboringCellInfo;
            this.time = time;
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Item item = adapter.getItem(adapter.getCount() - position - 1);
        List<NeighboringCellInfo> ncis = item.neighboringCellInfo;
        if (ncis.size() == 0) return;
        String[] items = new String[ncis.size()];
        int i = 0;
        for (NeighboringCellInfo nci : ncis) {
            items[i++] = "LAC: " + nci.getLac() + "\nCID: " + nci.getCid() + "\nRSSI: " + nci.getRssi();
        }
        new AlertDialog.Builder(this).setItems(items, null).create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("基站扫描");
        getSherlock().getActionBar().setDisplayShowHomeEnabled(false);
        setContentView(R.layout.layout_list);

        final List<Item> items = new ArrayList<Item>();
        adapter = new ArrayAdapter<Item>(this, R.layout.item_lac, items) {
            int count;

            @Override
            public int getCount() {
                count = super.getCount();
                return count;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                if (null == convertView) {
                    view = View.inflate(parent.getContext(), R.layout.item_lac, null);
                } else {
                    view = convertView;
                }
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                TextView hint = (TextView) view.findViewById(android.R.id.hint);
                TextView summary = (TextView) view.findViewById(android.R.id.summary);
                GsmCellLocation cellLocation = getItem(count - position - 1).cellLocation;
                text1.setText("LAC:" + cellLocation.getLac());
                text2.setText("CID:" + cellLocation.getCid());
                hint.setText(getItem(count - position - 1).time);
                summary.setText(getItem(count - position - 1).neighboringCellInfo.size() + "");
                return view;
            }
        };
        setListAdapter(adapter);


        final TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onCellLocationChanged(CellLocation location) {
                super.onCellLocationChanged(location);
                List<NeighboringCellInfo> neighboringCellInfo = telephonyManager.getNeighboringCellInfo();
                items.add(new Item((GsmCellLocation) location, neighboringCellInfo, df.format(new Date())));
                adapter.notifyDataSetChanged();
            }
        }, PhoneStateListener.LISTEN_CELL_LOCATION);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                new AlertDialog.Builder(this).setMessage("当前显示的是手机所连接基站的LAC（位置区编码）、CID（小区编码）、RSSI（信号强度）\n" +
                        "以及当前信号范围内最多6个基站的信息")
                        .setPositiveButton(android.R.string.ok, null).create().show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
