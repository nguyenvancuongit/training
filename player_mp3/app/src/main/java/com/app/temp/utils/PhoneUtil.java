package com.app.temp.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.List;
import java.util.Locale;

/**
 * Created by Windows 7 on 1/4/2016.
 */
public class PhoneUtil {
    /**
     * make a phone call
     *
     * @param context     the context
     * @param phoneNumber string contain phone number
     */
    public static void call(Context context, String phoneNumber) {
        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
        try {
            context.startActivity(in);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context.getApplicationContext(), "Could not make a call.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * get country code or country name from GPS
     *
     * @param context the context
     * @param lat     from gps
     * @param lng     from gps
     * @return string contain country code ex 84
     */
    public static String getCountryCodeFromLatLon(Context context, double lat, double lng) {
        try {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getCountryCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * get national number (from local (ex 098x xxx xxx) to national (ex +8498x xxx xxx))
     *
     * @param number      0982022699
     * @param countryCode VI
     * @return +84982022699
     */
    public static String getNationalNumberFromPhoneNumber(String number, String countryCode) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(number, countryCode);
            return phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
        return "";
    }
}
