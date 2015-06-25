package uwaterloo.ca.shimmertest;

import java.util.Collection;

import com.shimmerresearch.android.Shimmer;
import com.shimmerresearch.driver.Configuration;
import com.shimmerresearch.driver.FormatCluster;
import com.shimmerresearch.driver.ObjectCluster;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityFragment extends Fragment {

	private static Shimmer[] mMultiShimmer = new Shimmer[6];
	private TextView acceleration = null;
	private String info = "Bluetooth Address:\nAccelerometer:";

	private double[] acceleValue1 = new double[3];
	private double[] acceleValue2 = new double[3];
	private double[] acceleValue3 = new double[3];
	private double[] acceleValue4 = new double[3];
	private double[] acceleValue5 = new double[3];
	private double[] acceleValue6 = new double[3];

	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Shimmer.MESSAGE_READ:
				if ((msg.obj instanceof ObjectCluster))
					frameEvent((ObjectCluster) msg.obj);
				break;
			case Shimmer.MESSAGE_STATE_CHANGE:
				stateChangeEvent(msg.arg1);
				break;
			}
		}
	};

	public MainActivityFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		acceleration = (TextView) rootView.findViewById(R.id.acceleration1);

		for (int i = 0; i < 6; i++) {
			mMultiShimmer[i] = new Shimmer(rootView.getContext(), mHandler,
					"Shimmer " + (i + 1), false);
			Log.d(".", "Initialized");
		}

		return rootView;
	}

	public static void connect(String address) {
		int i = 0;
		while (mMultiShimmer[i].getShimmerState() == Shimmer.STATE_CONNECTED) {
			i++;
		}
		mMultiShimmer[i].connect(address, "default");
	}

	public void stateChangeEvent(int arg1) {
		switch (arg1) {
		case Shimmer.MSG_STATE_FULLY_INITIALIZED:
			if (mMultiShimmer[0].getShimmerState() == Shimmer.STATE_CONNECTED) {
				Log.d("ConnectionStatus", "Successful");
				Toast.makeText(getActivity().getApplicationContext(),
						"Connection Established", Toast.LENGTH_LONG).show();
				mMultiShimmer[0].startStreaming();
			}
			if (mMultiShimmer[1].getShimmerState() == Shimmer.STATE_CONNECTED) {
				Log.d("ConnectionStatus", "Successful");
				Toast.makeText(getActivity().getApplicationContext(),
						"Connection Established", Toast.LENGTH_LONG).show();
				mMultiShimmer[1].startStreaming();
			}
			if (mMultiShimmer[2].getShimmerState() == Shimmer.STATE_CONNECTED) {
				Log.d("ConnectionStatus", "Successful");
				Toast.makeText(getActivity().getApplicationContext(),
						"Connection Established", Toast.LENGTH_LONG).show();
				mMultiShimmer[2].startStreaming();
			}
			if (mMultiShimmer[3].getShimmerState() == Shimmer.STATE_CONNECTED) {
				Log.d("ConnectionStatus", "Successful");
				Toast.makeText(getActivity().getApplicationContext(),
						"Connection Established", Toast.LENGTH_LONG).show();
				mMultiShimmer[3].startStreaming();
			}
			if (mMultiShimmer[4].getShimmerState() == Shimmer.STATE_CONNECTED) {
				Log.d("ConnectionStatus", "Successful");
				Toast.makeText(getActivity().getApplicationContext(),
						"Connection Established", Toast.LENGTH_LONG).show();
				mMultiShimmer[4].startStreaming();
			}
			if (mMultiShimmer[5].getShimmerState() == Shimmer.STATE_CONNECTED) {
				Log.d("ConnectionStatus", "Successful");
				Toast.makeText(getActivity().getApplicationContext(),
						"Connection Established", Toast.LENGTH_LONG).show();
				mMultiShimmer[5].startStreaming();
			}
			break;
		case Shimmer.STATE_CONNECTING:
			Log.d("ConnectionStatus", "Connecting");
			Toast.makeText(getActivity().getApplicationContext(), "Connecting",
					Toast.LENGTH_LONG).show();
			break;
		case Shimmer.STATE_NONE:
			Log.d("ConnectionStatus", "No State");
			Toast.makeText(getActivity().getApplicationContext(),
					"Failed To Connect", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	public void frameEvent(ObjectCluster objectCluster) {
		Collection<FormatCluster> accelXFormats = objectCluster.mPropertyCluster
				.get(Configuration.Shimmer3.ObjectClusterSensorName.ACCEL_LN_X);
		FormatCluster formatCluster = ((FormatCluster) ObjectCluster
				.returnFormatCluster(accelXFormats, "CAL"));
		getValue(formatCluster, objectCluster, 0);

		Collection<FormatCluster> accelYFormats = objectCluster.mPropertyCluster
				.get(Configuration.Shimmer3.ObjectClusterSensorName.ACCEL_LN_Y);
		formatCluster = ((FormatCluster) ObjectCluster.returnFormatCluster(
				accelYFormats, "CAL"));
		getValue(formatCluster, objectCluster, 1);

		Collection<FormatCluster> accelZFormats = objectCluster.mPropertyCluster
				.get(Configuration.Shimmer3.ObjectClusterSensorName.ACCEL_LN_Z);
		formatCluster = ((FormatCluster) ObjectCluster.returnFormatCluster(
				accelZFormats, "CAL"));
		getValue(formatCluster, objectCluster, 2);

		info = String.format(
				"Bluetooth Address: %s\nAccelerometer: %f, %f, %f",
				mMultiShimmer[0].getBluetoothAddress(), acceleValue1[0],
				acceleValue1[1], acceleValue1[2]);
		info += String.format(
				"\nBluetooth Address: %s\nAccelerometer: %f, %f, %f",
				mMultiShimmer[1].getBluetoothAddress(), acceleValue2[0],
				acceleValue2[1], acceleValue2[2]);
		info += String.format(
				"\nBluetooth Address: %s\nAccelerometer: %f, %f, %f",
				mMultiShimmer[2].getBluetoothAddress(), acceleValue3[0],
				acceleValue3[1], acceleValue3[2]);
		info += String.format(
				"\nBluetooth Address: %s\nAccelerometer: %f, %f, %f",
				mMultiShimmer[3].getBluetoothAddress(), acceleValue4[0],
				acceleValue4[1], acceleValue4[2]);
		info += String.format(
				"\nBluetooth Address: %s\nAccelerometer: %f, %f, %f",
				mMultiShimmer[4].getBluetoothAddress(), acceleValue5[0],
				acceleValue5[1], acceleValue5[2]);
		info += String.format(
				"\nBluetooth Address: %s\nAccelerometer: %f, %f, %f",
				mMultiShimmer[5].getBluetoothAddress(), acceleValue6[0],
				acceleValue6[1], acceleValue6[2]);

		acceleration.setText(info);
	}

	public static void stop() {
		for (Shimmer s : mMultiShimmer) {
			s.stopStreaming();
			s.stop();
		}
	}

	private void getValue(FormatCluster formatCluster,
			ObjectCluster objectCluster, int address) {
		if (formatCluster != null) {
			switch (objectCluster.mMyName) {
			case "Shimmer 1":
				acceleValue1[address] = formatCluster.mData;
				break;
			case "Shimmer 2":
				acceleValue2[address] = formatCluster.mData;
				break;
			case "Shimmer 3":
				acceleValue3[address] = formatCluster.mData;
				break;
			case "Shimmer 4":
				acceleValue4[address] = formatCluster.mData;
				break;
			case "Shimmer 5":
				acceleValue5[address] = formatCluster.mData;
				break;
			case "Shimmer 6":
				acceleValue6[address] = formatCluster.mData;
				break;
			}
		}
	}
}
