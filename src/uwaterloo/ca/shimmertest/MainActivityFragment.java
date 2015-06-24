package uwaterloo.ca.shimmertest;

import java.util.Collection;

import com.shimmerresearch.android.Shimmer;
import com.shimmerresearch.driver.Configuration;
import com.shimmerresearch.driver.FormatCluster;
import com.shimmerresearch.driver.ObjectCluster;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
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

	private static Shimmer mShimmerDevice1 = null;
	private static Shimmer mShimmerDevice2 = null;
	private static boolean isFirstConnected = false;
	private TextView acceleration1 = null;
	private TextView acceleration2 = null;
	private double[] acceleValue1 = new double[3];
	private double[] acceleValue2 = new double[3];

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

		acceleration1 = (TextView) rootView.findViewById(R.id.acceleration1);
		acceleration2 = (TextView) rootView.findViewById(R.id.acceleration2);
		mShimmerDevice1 = new Shimmer(rootView.getContext(), mHandler,
				"RightArm", false);
		mShimmerDevice2 = new Shimmer(rootView.getContext(), mHandler,
				"LeftArm", false);

		return rootView;
	}

	public void connect(String address) {
		if (!isFirstConnected) {
			mShimmerDevice1.connect(address, "default");
			isFirstConnected = true;
		} else {
			mShimmerDevice2.connect(address, "default");
		}
	}

	public void stateChangeEvent(int arg1) {
		switch (arg1) {
		case Shimmer.MSG_STATE_FULLY_INITIALIZED:
			if (mShimmerDevice1.getShimmerState() == Shimmer.STATE_CONNECTED) {
				Log.d("ConnectionStatus", "Successful");
				Toast.makeText(
						getActivity().getApplicationContext(),
						"Shimmer " + mShimmerDevice1.getBluetoothAddress()
								+ " Connection Established", Toast.LENGTH_LONG)
						.show();
				mShimmerDevice1.startStreaming();
				mShimmerDevice2.startStreaming();
			}
			break;
		case Shimmer.STATE_CONNECTING:
			Log.d("ConnectionStatus", "Connecting");
			Toast.makeText(getActivity().getApplicationContext(), "Connecting",
					Toast.LENGTH_LONG).show();
			break;
		case Shimmer.STATE_NONE:
			Log.d("ConnectionStatus", "No State");
			break;
		}
	}

	public void frameEvent(ObjectCluster objectCluster) {
		Collection<FormatCluster> accelXFormats = objectCluster.mPropertyCluster
				.get(Configuration.Shimmer3.ObjectClusterSensorName.ACCEL_LN_X);
		FormatCluster formatCluster = ((FormatCluster) ObjectCluster
				.returnFormatCluster(accelXFormats, "CAL"));
		if (formatCluster != null && objectCluster.mMyName.equals("RightArm"))
			acceleValue1[0] = formatCluster.mData;
		else if (formatCluster != null
				&& objectCluster.mMyName.equals("LeftArm"))
			acceleValue2[0] = formatCluster.mData;

		Collection<FormatCluster> accelYFormats = objectCluster.mPropertyCluster
				.get(Configuration.Shimmer3.ObjectClusterSensorName.ACCEL_LN_Y);
		formatCluster = ((FormatCluster) ObjectCluster.returnFormatCluster(
				accelYFormats, "CAL"));
		if (formatCluster != null && objectCluster.mMyName.equals("RightArm"))
			acceleValue1[1] = formatCluster.mData;
		else if (formatCluster != null
				&& objectCluster.mMyName.equals("LeftArm"))
			acceleValue2[1] = formatCluster.mData;

		Collection<FormatCluster> accelZFormats = objectCluster.mPropertyCluster
				.get(Configuration.Shimmer3.ObjectClusterSensorName.ACCEL_LN_Z);
		formatCluster = ((FormatCluster) ObjectCluster.returnFormatCluster(
				accelZFormats, "CAL"));
		if (formatCluster != null && objectCluster.mMyName.equals("RightArm"))
			acceleValue1[2] = formatCluster.mData;
		else if (formatCluster != null
				&& objectCluster.mMyName.equals("LeftArm"))
			acceleValue2[2] = formatCluster.mData;

		acceleration1.setText(String.format("%f, %f, %f", acceleValue1[0],
				acceleValue1[1], acceleValue1[2]));
		acceleration2.setText(String.format("%f, %f, %f", acceleValue2[0],
				acceleValue2[1], acceleValue2[2]));
	}
}
