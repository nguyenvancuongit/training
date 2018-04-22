package com.app.temp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.widget.Toast;

import java.util.List;
import java.util.Stack;

/**
 * Created by Windows 7 on 1/4/2016.
 */
public class EmailUtil {
    /**
     * sending a email use intent
     *
     * @param context current context
     * @param TO      = {""}
     * @param CC      = {""}
     */
    public static void sendEmail(Context context, String[] TO, String[] CC, String subject, String message) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_CC, CC);
//            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);

            context.startActivity(createEmailOnlyChooserIntent(context, emailIntent, "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static Intent createEmailOnlyChooserIntent(Context context, Intent source,
                                                      CharSequence chooserTitle) {
        Stack<Intent> intents = new Stack<>();
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
                "info@domain.com", null));
        List<ResolveInfo> activities = context.getPackageManager()
                .queryIntentActivities(i, 0);

        for (ResolveInfo ri : activities) {
            Intent target = new Intent(source);
            target.setPackage(ri.activityInfo.packageName);
            intents.add(target);
        }

        if (!intents.isEmpty()) {
            Intent chooserIntent = Intent.createChooser(intents.remove(0),
                    chooserTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    intents.toArray(new Parcelable[intents.size()]));

            return chooserIntent;
        } else {
            return Intent.createChooser(source, chooserTitle);
        }
    }
}
