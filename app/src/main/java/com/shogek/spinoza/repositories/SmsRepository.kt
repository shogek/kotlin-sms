package com.shogek.spinoza.repositories

import android.content.ContentResolver
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import android.provider.Telephony
import android.util.Log
import com.shogek.spinoza.utils.DateUtils
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object SmsRepository {
    fun getAllSms(resolver: ContentResolver) {
        val uri = Telephony.Sms.CONTENT_URI
        val projection = arrayOf(
            Telephony.Sms._ID,
            Telephony.Sms.ADDRESS,      /*      TEXT       ex.: +37067787874       The address of the other party.                                                 */
            Telephony.Sms.PERSON,       /*      INT        ex.: 1                  The ID of the sender of the conversation, if present.                           */
            Telephony.Sms.BODY,         /*      TEXT       ex.: How are you?       The body of the message.                                                        */
            Telephony.Sms.DATE_SENT,    /*      LONG       ex.: 1571762897000      The date the message was sent.                                                  */
            Telephony.Sms.READ,         /*      INT        ex.: 1                  Has the message been read?                                                      */
            Telephony.Sms.SEEN,         /*      INT        ex.: 1                  Has the message been seen? Determines whether we need to show a notification.   */
            Telephony.Sms.THREAD_ID,    /*      INT        ex.: 4                  The thread ID of the message.                                                   */
            Telephony.Sms.TYPE          /*      INT        ex.: 1                  The type of message.                                                            */
            /*
                MESSAGE_TYPE_ALL    = 0;
                MESSAGE_TYPE_INBOX  = 1;
                MESSAGE_TYPE_SENT   = 2;
                MESSAGE_TYPE_DRAFT  = 3;
                MESSAGE_TYPE_OUTBOX = 4;
                MESSAGE_TYPE_FAILED = 5; // for failed outgoing messages
                MESSAGE_TYPE_QUEUED = 6; // for messages to send later
             */
        )
        val selection = null
        val selectionArgs = null
        val sortOrder = null

        val cursor = resolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        if (cursor == null)
            return

        var text = "\n "
        while (cursor.moveToNext()) {
            for (column in cursor.columnNames) {
                var columnIndex = cursor.getColumnIndex(column)
                var columnName = column
                var columnValue = cursor.getString(columnIndex)

                text += "\n"
                text += "INDEX: $columnIndex \t"
                text += "NAME:  $columnName  \t"
                if (columnName == "date_sent") {
                    var timestamp = DateUtils.getDateTime(columnValue)
                    text += "VALUE: $timestamp"
                } else {
                    text += "VALUE: $columnValue"
                }
            }
        }
        Log.w("1", text)

        cursor.close()
    }
}