package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;

import com.tambyy.fanoronaakalana.adapter.BluetoothDeviceAdapter;
import com.tambyy.fanoronaakalana.adapter.SavedGameAdapter;

import java.util.Set;

public class BluetoothDeviceListActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 3;

    @BindView(R.id.bluetooth_devices_list)
    GridView gridViewBluetoothDevices;

    @BindView(R.id.bluetooth_devices_scan)
    Button buttonBluetoothDevicesScan;

    BluetoothDeviceAdapter bluetoothDeviceAdapter;

    /**
     * Member fields
     */
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device_list);
        ButterKnife.bind(this);

        setupPopupSize();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        initBluetoothDevicesList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    private void setupPopupSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        getWindow().setLayout((int) (width * 0.85), WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;

        getWindow().setAttributes(params);
    }

    private void initBluetoothDevicesList() {
        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        bluetoothDeviceAdapter = new BluetoothDeviceAdapter(this, pairedDevices);
        gridViewBluetoothDevices.setAdapter(bluetoothDeviceAdapter);
        gridViewBluetoothDevices.setOnItemClickListener((parent, view, position, id) -> {
            // Cancel discovery because it's costly and we're about to connect
            bluetoothAdapter.cancelDiscovery();

            BluetoothDevice bluetoothDevice = (BluetoothDevice) bluetoothDeviceAdapter.getItem(position);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(SavedGamesActivity.EXTRA_DEVICE_ADDRESS, bluetoothDevice.getAddress());

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    public void doDiscovery(View view) {

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        // setTitle(R.string.scanning);

        // Turn on sub-title for new devices
        // findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        bluetoothAdapter.startDiscovery();

        buttonBluetoothDevicesScan.setText(R.string.bt_devices_scanning);
        buttonBluetoothDevicesScan.setEnabled(false);
    }

    /**
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    bluetoothDeviceAdapter.addBluetoothDevice(device);
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                buttonBluetoothDevicesScan.setText(R.string.bt_devices_scan);
                buttonBluetoothDevicesScan.setEnabled(true);
            }
        }
    };

}