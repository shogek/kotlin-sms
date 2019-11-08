package com.shogek.spinoza.activities

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.shogek.spinoza.R
import com.shogek.spinoza.adapters.MessageListRecyclerAdapter
import com.shogek.spinoza.models.Message
import kotlinx.android.synthetic.main.activity_message_list.*

class MessageListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)

        // Change activity title to display sender's name
        title = intent.getStringExtra("SENDER_NAME")

        // Change activity title's background color
        val color = ContextCompat.getColor(this, R.color.colorWhite)
        actionBar?.setBackgroundDrawable(ColorDrawable(color))

        rv_messageList.layoutManager = LinearLayoutManager(this)
        rv_messageList.adapter = MessageListRecyclerAdapter(this, listOf(
            Message("Hey, you still ready for tomorrow?", false),
            Message("They moved the date a bit earlier, by the way, so I'll pick you up sooner. Is that ok? Is that ok? Is that ok? Is that ok?", true),
            Message("They moved the date a bit earlier, by the way, so I'll pick you up sooner. Is that ok? Is that ok? Is that ok? Is that ok?", false),
            Message("Hey", true),
            Message("Yeah, that's fine with me", true),
            Message("So let's say 6pm?", false),
            Message("Hey, you still ready for tomorrow?", false),
            Message("They moved the date a bit earlier, by the way, so I'll pick you up sooner. Is that ok? Is that ok? Is that ok? Is that ok?", true),
            Message("They moved the date a bit earlier, by the way, so I'll pick you up sooner. Is that ok? Is that ok? Is that ok? Is that ok?", false),
            Message("Hey", true),
            Message("Yeah, that's fine with me", true),
            Message("So let's say 6pm?", false),
            Message("Sure!", true)
        ))
    }
}