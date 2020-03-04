package com.shogek.spinoza.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import androidx.lifecycle.Observer
import com.shogek.spinoza.db.conversation.Conversation
import com.shogek.spinoza.db.conversation.ConversationDao
import com.shogek.spinoza.db.conversation.ConversationRoomDatabase
import com.shogek.spinoza.db.message.Message
import com.shogek.spinoza.db.message.MessageRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking

class MessageBroadcastReceiver: BroadcastReceiver(), CoroutineScope {

    private var job = Job()

    private companion object {
        val LOG = MessageBroadcastReceiver::class.java.simpleName

        /** Extract received SMS body and sender's phone number. */
        fun parseReceivedMessage(intent: Intent): BasicMessage? {
            val timestamp = System.currentTimeMillis() // when was message received

            val pdus = (intent.extras?.get("pdus") as Array<*>).filterIsInstance<ByteArray>()
            val format = intent.extras?.get("format") as String?
            if (pdus.isEmpty() || format == null) {
                Log.e(LOG, "Failed to parse received SMS message")
                return null
            }

            val messageText = pdus.fold("") { acc, bytes -> acc + SmsMessage.createFromPdu(bytes, format).displayMessageBody }
            val senderPhone = SmsMessage.createFromPdu(pdus.first(), format).originatingAddress
            if (senderPhone == null) {
                Log.e(LOG, "Failed to parse received SMS message's sender phone number")
                return null
            }

            return BasicMessage(senderPhone, messageText, timestamp)
        }

        /** Create a new conversation or update an existing one with the latest message. */
        fun upsertConversation(
            allConversations: List<Conversation>?,
            conversationDao: ConversationDao,
            message: BasicMessage
        ): Long {
            // TODO: [Refactor] Use async
            var ownerConversation = allConversations?.find { it.phone == message.senderPhone }
            if (ownerConversation == null) {
                ownerConversation = Conversation(message.senderPhone, message.messageText, message.timestamp, snippetIsOurs = false, snippetWasRead = false)
                runBlocking { ownerConversation.id = conversationDao.insert(ownerConversation) }
            } else {
                runBlocking { conversationDao.update(ownerConversation.id, message.messageText, message.timestamp, snippetIsOurs = false, snippetWasRead = false) }
            }

            return ownerConversation.id
        }
    }

    /** Called when a new SMS is received. */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            Log.e(LOG, "'context' or 'intent' is null")
            return
        }

        val basicMessage = parseReceivedMessage(intent)
            ?: return

        val conversationDao = ConversationRoomDatabase.getDatabase(context, ).conversationDao()
        val messageDao = MessageRoomDatabase.getDatabase(context).messageDao()
        val conversationData = conversationDao.getAll()
        conversationData.observeForever(object : Observer<List<Conversation>> {
            override fun onChanged(allConversations: List<Conversation>?) {
                val id = upsertConversation(allConversations, conversationDao, basicMessage)

                // TODO: [Bug] Check if message was received while in a conversation (so we can mark message as read)
                // TODO: [Refactor] Use async
                val message = Message(id, basicMessage.messageText, basicMessage.timestamp, isOurs = false)
                runBlocking { messageDao.insert(message) }

                conversationData.removeObserver(this)
            }
        })
    }
}

data class BasicMessage(
    val senderPhone: String,
    val messageText: String,
    val timestamp: Long
)
