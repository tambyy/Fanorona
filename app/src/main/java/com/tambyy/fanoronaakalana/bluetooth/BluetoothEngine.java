package com.tambyy.fanoronaakalana.bluetooth;

import com.tambyy.fanoronaakalana.engine.Engine;

public class BluetoothEngine extends Engine {
    private BluetoothService bluetoothService;

    public BluetoothEngine(BluetoothService bluetoothService) {
        super(0);

        this.bluetoothService = bluetoothService;
    }
}
