package com.shogek.spinoza.models

class Conversation(
    val threadId: Number,
    /** 'stripped' meaning it contains no spaces and parentheses. */
    val senderPhoneStripped: String,
    var contact: Contact?,
    var messages: MutableList<Message>?,
    var latestMessageText: String,
    var latestMessageTimestamp: Long,
    var latestMessageWasRead: Boolean,
    var latestMessageIsOurs: Boolean
    ) {
    fun getDisplayName(): String = contact?.displayName ?: senderPhoneStripped
}