package com.example.ma.contextualawarenessapplication;

/**
 * Created by Ma on 2016/4/9.
 */
public class MyBeacon {
    private int _id;
    private String _uuid;
    private String _major;
    private String _minor;
    private double _distance;
    private String _beaconname;

    public MyBeacon(){
    }

    public MyBeacon(String beaconname) {
        this._beaconname = beaconname;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_beaconname(String _beaconname) {
        this._beaconname = _beaconname;
    }

    public int get_id() {
        return _id;
    }

    public String get_beaconname() {
        return _beaconname;
    }

    public void set_uuid(String _uuid) {
        this._uuid = _uuid;
    }

    public String get_uuid() {
        return _uuid;
    }

    public void set_major(String _major) {
        this._major = _major;
    }

    public String get_major() {
        return _major;
    }

    public void set_minor(String _minor) {
        this._minor = _minor;
    }

    public String get_minor() {
        return _minor;
    }

    public void set_distance(double _distance) {
        this._distance = _distance;
    }

    public double get_distance() {
        return _distance;
    }
}
