package com.shogek.spinoza.db.conversation

import android.content.ContentResolver
import android.provider.Telephony
import android.util.Log

object ConversationDatabaseHelper {

    private val TAG: String = ConversationDatabaseHelper::class.java.simpleName

    /** Retrieve conversations saved in phone. */
    fun retrieveAllPhoneConversations(resolver: ContentResolver): List<Conversation> {
        val projection = arrayOf(
            Telephony.Sms.Conversations.ADDRESS     + " as " + Telephony.Sms.Conversations.ADDRESS,
            Telephony.Sms.Conversations.BODY        + " as " + Telephony.Sms.Conversations.BODY,
            Telephony.Sms.Conversations.DATE        + " as " + Telephony.Sms.Conversations.DATE,
            Telephony.Sms.Conversations.TYPE        + " as " + Telephony.Sms.Conversations.TYPE,
            Telephony.Sms.Conversations.READ        + " as " + Telephony.Sms.Conversations.READ
        )

        val cursor = resolver.query(
            Telephony.Sms.Conversations.CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        if (cursor == null) {
            Log.e(TAG, "Cursor is null")
            return listOf()
        }

        val conversations = mutableListOf<Conversation>()

        while (cursor.moveToNext()) {
            val phone= cursor.getString(cursor.getColumnIndex(Telephony.Sms.Conversations.ADDRESS))
            val snippet= cursor.getString(cursor.getColumnIndex(Telephony.Sms.Conversations.BODY))
            val timestamp= cursor.getLong(cursor.getColumnIndex(Telephony.Sms.Conversations.DATE))
            val type= cursor.getInt(cursor.getColumnIndex(Telephony.Sms.Conversations.TYPE))
            val read= cursor.getInt(cursor.getColumnIndex(Telephony.Sms.Conversations.READ))

            val isOurs = type == Telephony.Sms.Conversations.MESSAGE_TYPE_SENT
            val wasRead = read == 1

            val conversation = Conversation(null, phone, snippet, timestamp, isOurs, wasRead)
            conversations.add(conversation)
        }

        cursor.close()
        return conversations
    }
}