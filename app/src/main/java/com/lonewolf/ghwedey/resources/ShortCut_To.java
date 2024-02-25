package com.lonewolf.ghwedey.resources;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ShortCut_To {
    public static final String DATEWITHTIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATEWITHTIMEDDMMYYY = "dd-MM-yyyy'T'HH:mm:ss.SSS'Z'";
    public static final String DATEFORMATDDMMYYYY = "dd/MM/yyyy";
    public static final String DATEFORMATYYYYMMDD = "yyyy-MM-dd";
    public static final String TIME = "hh:mm a";

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String[] dropList = {"Select type", "Flyers", "Posters", "Stickers", "Logo", "Business cards", "Invitation cards", "Letterhead", "Calendar", "Testimonials"};

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMATYYYYMMDD, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentDateFormat2() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMATDDMMYYYY, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentDatewithTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEWITHTIMEDDMMYYY, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String[] getServices = {"Select Service",  "Technical Support", "Android App",  "Web App","Apple App", "Website Development", "Enquiries", "Other"};

    public static Bitmap decodeBase64(String input) {

        try {
            byte[] decodedByte = Base64.decode(input, 0);
            return BitmapFactory.decodeByteArray(decodedByte, 0,
                    decodedByte.length);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }


    public static String getTimeFromDate(String str){
        if(str != null && !str.equalsIgnoreCase("null") && str.trim().length()!=0){
            SimpleDateFormat sdf1 = new SimpleDateFormat(DATEWITHTIME, Locale.US);
            SimpleDateFormat sdf2 = new SimpleDateFormat(TIME, Locale.US);

            try {
                Date date = sdf1.parse(str);
                return sdf2.format(date);
            }catch (Exception e){
                e.printStackTrace();
                return "";
            }
        }else {
            return " ";
        }
    }

    public static byte[] uriToByteArray(Uri uri, Activity activity)
    {

        try {
            InputStream inputStream =activity.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            inputStream.close();
            return byteBuffer.toByteArray();


        }catch(Exception e) {
            Log.d("exception", "Oops! Something went wrong.");
        }
        return null;
    }

    public static void sortData(ArrayList<HashMap<String, String>> list, final String field){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(list, Comparator.comparing(lhs -> lhs.get(field)));
        }else {
            Collections.sort(list, (lhs, rhs) -> lhs.get(field).compareTo(rhs.get(field)));
        }


    }

    public static void sortDataInvert(ArrayList<HashMap<String, String>> list, final String field){

        Collections.sort(list, (lhs, rhs) -> Objects.requireNonNull(rhs.get(field)).compareTo(Objects.requireNonNull(lhs.get(field))));

    }

    public static void blinkAnim(LinearLayout linearLayout){
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        //anim.setRepeatMode(Animation.REVERSE);
        //anim.setRepeatCount(1);
        linearLayout.startAnimation(anim);
    }


    public static int getRandomNumber(int min, int max){
        return (int)(Math.random()*(max-min+1)+min);
    }

    public static List<String> getCategory(){
        List<String> list = new ArrayList<>();
        list.add("General");
        list.add("Insult");
        list.add("Cuss Words");
        list.add("Insult and Curse Words");

        return list;

    }
}
