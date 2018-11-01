package com.zhaoqy.app.demo.notebook.milletnotes.db;

import java.util.HashMap;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.telephony.PhoneNumberUtils;

public class Contact 
{
	private static HashMap<String, String> sContactCache;
    private static final String CALLER_ID_SELECTION = "PHONE_NUMBERS_EQUAL(" + Phone.NUMBER + ",?) AND " + Data.MIMETYPE + "='" +
	                            Phone.CONTENT_ITEM_TYPE + "'" + " AND " + Data.RAW_CONTACT_ID + " IN " + "(SELECT raw_contact_id " 
	                            + " FROM phone_lookup" + " WHERE min_match = '+')";

    public static String getContact(Context context, String phoneNumber) 
    {
        if(sContactCache == null) 
        {
            sContactCache = new HashMap<String, String>();
        }

        if(sContactCache.containsKey(phoneNumber)) 
        {
            return sContactCache.get(phoneNumber);
        }

        String selection = CALLER_ID_SELECTION.replace("+", PhoneNumberUtils.toCallerIDMinMatch(phoneNumber));
        Cursor cursor = context.getContentResolver().query(
                Data.CONTENT_URI,
                new String [] { Phone.DISPLAY_NAME },
                selection,
                new String[] { phoneNumber },
                null);

        if (cursor != null && cursor.moveToFirst()) 
        {
            try 
            {
                String name = cursor.getString(0);
                sContactCache.put(phoneNumber, name);
                return name;
            } 
            catch (IndexOutOfBoundsException e) 
            {
                return null;
            } 
            finally 
            {
                cursor.close();
            }
        } 
        else 
        {
            return null;
        }
    }
}
