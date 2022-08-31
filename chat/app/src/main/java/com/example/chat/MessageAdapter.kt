package com.example.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.widget.LinearLayout as LinearLayout

class MessageAdapter(var context:Context, var messageList:ArrayList<messageClass>):

    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private val USER_LAYOUT=0
    private  val BOT_LAYOUT=1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        if (viewType.equals(USER_LAYOUT)){
            val view=LayoutInflater.from(context).inflate(R.layout.user_message_box, parent,false)
            return MessageViewHolder(view)
        }
        else{
            val view=LayoutInflater.from(context).inflate(R.layout.bot_message_layout, parent,false)
            return MessageViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage=messageList[position]
        if(currentMessage.sender.equals(0)){
            holder.message_view.setText(currentMessage.message)
//            val v=getUserLayout()
//            holder.linear_layout.addView(v)
//            val message_tv=v?.findViewById<TextView>(R.id.message_tv_user)
//            message_tv?.setText(message.message)
        }
        else if(currentMessage.sender.equals(1)){
            holder.message_view.setText(currentMessage.message)
            holder.image.visibility=View.GONE
//            val v=getBotLayout()
//            holder.linear_layout.addView(v)
//            val message_tv=v?.findViewById<TextView>(R.id.message_tv_bot)
//            message_tv?.setText(message.message)
        }
        else if (currentMessage.sender.equals(2)){
            Glide.with(context).load(currentMessage.message).into(holder.image)
            holder.message_view.visibility=View.GONE
            holder.time_view.visibility=View.GONE

        }


    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        val view=messageList[position]
        if (view.sender.equals(USER_LAYOUT)){
            return USER_LAYOUT

        }else{
            return BOT_LAYOUT

        }
    }
//    fun getUserLayout():FrameLayout?{
//        val inflater:LayoutInflater=LayoutInflater.from(context)
//        return inflater.inflate(R.layout.user_message_box,null) as FrameLayout?
//    }
//    fun getBotLayout():FrameLayout?{
//        val inflater:LayoutInflater=LayoutInflater.from(context)
//        return inflater.inflate(R.layout.bot_message_layout,null) as FrameLayout?
//    }
    class MessageViewHolder(view: View):RecyclerView.ViewHolder(view){
        val message_view=view.findViewById<TextView>(R.id.message_tv)
        val time_view=view.findViewById<TextView>(R.id.time_tv)
        val image=view.findViewById<ImageView>(R.id.image)

//        val linear_layout=view.findViewById<LinearLayout>(R.id.linear_layout)
    }
}