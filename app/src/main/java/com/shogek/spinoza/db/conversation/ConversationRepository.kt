package com.shogek.spinoza.db.conversation

import androidx.lifecycle.LiveData

class ConversationRepository(private val conversationDao: ConversationDao) {

    fun getAll(): LiveData<List<Conversation>> {
        return conversationDao.getAll()
    }

    fun get(id: Long): LiveData<Conversation> {
        return conversationDao.get(id)
    }

    suspend fun insert(conversation: Conversation): Long {
        return conversationDao.insert(conversation)
    }

    suspend fun update(
        id: Long,
        snippet: String,
        snippetTimestamp: Long,
        snippetIsOurs: Boolean,
        snippetWasRead: Boolean
    ) {
        conversationDao.update(id, snippet, snippetTimestamp, snippetIsOurs, snippetWasRead)
    }
}