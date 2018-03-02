package com.gercep.alarem;

import android.telephony.gsm.GsmCellLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Globals var class
 * Created by Gilang PC on 22/02/2018.
 */

public class Globals {
    private static Globals instance;
    private static int Test;
    private static float CurrentLatitude;
    private static float CurrentLongitude;
    private static float RingsLatitude;
    private static float RingsLongitude;
    private static boolean TerShake = false;
    private static float Proximity;
    private static double Jarak;
    private static ArrayList<Integer> listJam = new ArrayList<Integer>();
    private static ArrayList<Integer> listMenit = new ArrayList<Integer>();
    private static boolean TerSwipe = false;

    private Globals(){}

    public void setTest(int T) {
        Globals.Test = T;
    }

    public int getTest() {
        return Globals.Test;
    }

    public void setCurrentLatitude(float l) { Globals.CurrentLatitude = l; }

    public float getCurrentLatitude() { return Globals.CurrentLatitude; }

    public void setCurrentLongitude(float l) { Globals.CurrentLongitude = l; }

    public float getCurrentLongitude() { return Globals.CurrentLongitude; }

    public void setRingsLatitude(float l) { Globals.RingsLatitude = l; }

    public float getRingsLatitude() { return Globals.RingsLatitude; }

    public void setRingsLongitude(float l) { Globals.RingsLongitude = l; }

    public float getRingsLongitude() { return Globals.RingsLongitude; }

    public void setJarak(double d) { Globals.Jarak = d; }

    public double getJarak() { return Globals.Jarak; }

    public void setTerShake(boolean s) { Globals.TerShake = s; }

    public boolean getTerShake() { return Globals.TerShake; }

    public void setProximity(float p) { Globals.Proximity = p; }

    public float getProximity() { return Globals.Proximity; }

    public void addListJam(Integer jam) {
        Globals.listJam.add(jam);
    }

    public int getListJam(int index) {
        return Globals.listJam.get(index);
    }

    public void addListMenit(Integer menit) {
        Globals.listMenit.add(menit);
    }

    public int getListMenit(int index) {
        return Globals.listMenit.get(index);
    }

    public int getListLength() {
        return Globals.listJam.size();
    }

    public void setTerSwipe(boolean b) { Globals.TerSwipe = b; }

    public boolean getTerSwipe() { return Globals.TerSwipe; }

    public void deleteAlarm() {
        Globals.listJam.clear();
        Globals.listMenit.clear();
        Globals.listJam.add(6);
        Globals.listMenit.add(30);
    }

    public static synchronized Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }
}
