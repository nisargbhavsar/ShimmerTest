package uwaterloo.ca.shimmertest;

import java.util.Comparator;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private BluetoothAdapter mAdapter = null;
	private Set<BluetoothDevice> pairedDevices = null;
	private ArrayAdapter<String> BTArrayAdapter = null;
	private ListView connectShimmer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		connectShimmer = (ListView) findViewById(R.id.listView1);

		// Get BlueTooth Adapter and check BlueTooth state
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		checkBTState();
		BTArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new MainActivityFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_connect) {
			// Clear BlueTooth list
			BTArrayAdapter.clear();

			// Get paired devices
			pairedDevices = mAdapter.getBondedDevices();

			for (BluetoothDevice device : pairedDevices) {
				if (device.getName().contains("Shimmer"))
					BTArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
			}

			// Sort the ListView
			BTArrayAdapter.sort(new Comparator<String>() {
				public int compare(String object1, String object2) {
					int res = String.CASE_INSENSITIVE_ORDER.compare(
							object1.toString(), object2.toString());
					return res;
				}
			});

			// Build an alert dialog
			final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					MainActivity.this);
			LayoutInflater inflater = getLayoutInflater();
			View convertView = inflater.inflate(R.layout.custom, null);
			alertDialog.setView(convertView);
			alertDialog.setTitle("Connect Shimmer");
			connectShimmer = (ListView) convertView
					.findViewById(R.id.listView1);
			connectShimmer.setAdapter(BTArrayAdapter);
			final Dialog dialog = alertDialog.show();

			connectShimmer.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// Get BlueTooth Address
					String temp = (String) connectShimmer.getAdapter().getItem(
							position);
					String address = temp.substring(temp.length() - 17,
							temp.length());
					Log.d("FUCK",address);
					// Connect Device
					new MainActivityFragment().connect(address);

					// Exit Dialog
					dialog.dismiss();
				}
			});
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void checkBTState() {
		if (mAdapter == null) {
			Toast.makeText(this, "Bluetooth Not Supported", Toast.LENGTH_SHORT)
					.show();
		} else {
			if (!mAdapter.isEnabled()) {
				startActivityForResult(new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
			}
		}
	}
}
