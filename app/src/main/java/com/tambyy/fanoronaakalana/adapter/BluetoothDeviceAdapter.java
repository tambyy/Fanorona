package com.tambyy.fanoronaakalana.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tambyy.fanoronaakalana.R;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothDeviceAdapter extends BaseAdapter {

    int selectedImage = 0;

    // FOR DATA
    private final ArrayList<BluetoothDevice> bluetoothDevices;
    private final Context context;
    private final LayoutInflater inflater;

    public BluetoothDeviceAdapter(Context context, Set<BluetoothDevice> bluetoothDevices) {
        this.context = context;
        this.bluetoothDevices = new ArrayList<>(bluetoothDevices);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return bluetoothDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return bluetoothDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BluetoothDeviceAdapter.ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new BluetoothDeviceAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_bluetooth_device_list, parent, false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BluetoothDeviceAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.deviceName = (TextView) convertView.findViewById(R.id.bluetooth_device_name);
        viewHolder.deviceAddress = (TextView) convertView.findViewById(R.id.bluetooth_device_address);

        BluetoothDevice bluetoothDevice = (BluetoothDevice) getItem(position);

        viewHolder.deviceName.setText(bluetoothDevice.getName());
        viewHolder.deviceAddress.setText(bluetoothDevice.getAddress());

        return convertView;
    }

    private static class ViewHolder {
        public TextView deviceName;
        public TextView deviceAddress;
    }

    public void addBluetoothDevice(BluetoothDevice bluetoothDevice) {
        bluetoothDevices.add(bluetoothDevice);
        notifyDataSetChanged();
    }
}