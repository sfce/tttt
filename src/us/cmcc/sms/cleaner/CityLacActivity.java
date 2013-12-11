package us.cmcc.sms.cleaner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockListActivity;
import us.cmcc.sms.cleaner.bean.BaseStation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-9
 * Time: 下午11:42
 */
public class CityLacActivity extends SherlockListActivity {

    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("城市基站");
        setContentView(R.layout.layout_list);
        final ArrayList<BaseStation> stations = new ArrayList<BaseStation>();
        for (int i = 0; i < 100; i++) {
            int lac = (int) (Math.random() * 10000 + 10000);
            int count = ((int) (Math.random() * 10000)) % 3 + 1;
            ArrayList<Integer> cids = new ArrayList<Integer>(count);
            for (int j = 0; j < count; j++) {
                cids.add((int) (Math.random() * 20000 + 10000));
            }
            double lat = Math.random() * (34.857971 - 34.668612) + 34.668612;
            lat = mathScale(lat);
            double lon = Math.random() * (113.812738 - 113.466424) + 113.466424;
            lon = mathScale(lon);

            stations.add(new BaseStation("基站" + (i + 1), lac, cids, lat, lon));
        }

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return stations.size();
            }

            @Override
            public BaseStation getItem(int position) {
                return stations.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
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
                BaseStation item = getItem(position);
                text1.setText(item.getName());
                text2.setText(item.getLac() + "");
                hint.setText(String.valueOf(item.getLatitude() + "," + item.getLongitude()));
                summary.setText(item.getCids().size() + "");
                return view;
            }
        };
        setListAdapter(adapter);
    }

    private double mathScale(double lat) {
        return ((int) (lat * 1000000)) / 1000000.0d;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final BaseStation item = (BaseStation) adapter.getItem(position);
        new AlertDialog.Builder(this).setItems(new String[]{"查看小区编码", "在地图上查看"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    List<Integer> cids = item.getCids();
                    if (cids.size() == 0) return;
                    String[] items = new String[cids.size()];
                    int i = 0;
                    for (Integer cid : cids) {
                        items[i++] = String.valueOf(cid);
                    }
                    new AlertDialog.Builder(CityLacActivity.this).setItems(items, null).create().show();
                } else {
                    Intent intent = new Intent(CityLacActivity.this, MapActivity.class);
                    intent.putExtra("location", String.format("%f,%f", item.getLatitude(), item.getLongitude()));
                    intent.putExtra("data", item.getName() + "|LAC:" + item.getLac());
                    CityLacActivity.this.startActivity(intent);
                }
            }
        }).create().show();
    }
}
