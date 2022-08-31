package com.example.chat

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsMessage
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val USER=0;
    private val  BOT=1;
    private val IMAGE=2;
    private lateinit var mainActivity:ActivityMainBinding
    private lateinit var messageList: ArrayList<messageClass>
    private  lateinit var adapter: MessageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity=ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivity.root)
        messageList= ArrayList<messageClass>()
        val linearLayoutManager=LinearLayoutManager(this)
        linearLayoutManager.orientation=LinearLayoutManager.VERTICAL
        mainActivity.messageList.layoutManager=linearLayoutManager
        adapter=MessageAdapter(this,messageList)
        adapter.setHasStableIds(true)
        mainActivity.messageList.adapter=adapter
        mainActivity.submit.setOnClickListener{
            val msg=mainActivity.messageBox.text.toString()
            if (msg!=""){
                sendMessage(msg)
            }
            else{
                Toast.makeText(this,"Please type your message",Toast.LENGTH_SHORT).show()


            }
//            adapter.notifyItemInserted(messageList.size-1)
//
            Toast.makeText(this,"message sent", Toast.LENGTH_SHORT).show()
            mainActivity.messageBox.setText("")

        }
    }

   private fun sendMessage(message: String) {
       var userMessage=messageClass()
       if (message.isEmpty()){
           Toast.makeText(this,"Please type your message",Toast.LENGTH_SHORT).show()
       }
       else{
           userMessage= messageClass(message,USER)
           messageList.add(userMessage)
           adapter.notifyDataSetChanged()


       }
       val okHttpClient=OkHttpClient()
       val retrofit= Retrofit.Builder().baseUrl("https://5954-103-111-224-49.ngrok.io/webhooks/rest/").client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()
       val messageSender=retrofit.create(MessageSender::class.java)
       val response=messageSender.messageSender(userMessage)
       response.enqueue(object : Callback<ArrayList<BotResponse>>{
           override fun onResponse(
               call: Call<ArrayList<BotResponse>>,
               response: Response<ArrayList<BotResponse>>
           ) {
               if (response.body()!=null||response.body()!!.size !=0)
               {
                   if (response.body()!!.size>1){
                       for (message in 0..response.body()!!.size-1){
                           val currentMessage=response.body()!![message]
                           if (currentMessage.image.isNotEmpty()){
                               messageList.add(messageClass(currentMessage.image,IMAGE))
                           }else if(currentMessage.text.isNotEmpty()){
                               messageList.add(messageClass(currentMessage.text,BOT))

                           }
//                           messageList.add(messageClass(currentMessage.text,BOT))
                           adapter.notifyDataSetChanged()
                       }
                   }
                   else{
                       val currentMessage=response.body()!![0]
                       if (currentMessage.text.isNotEmpty()){
                           messageList.add(messageClass(currentMessage.text,BOT))

                       }
                       else if (currentMessage.image.isNotEmpty()){
                           messageList.add(messageClass(currentMessage.image,IMAGE))

                       }
//                       messageList.add(messageClass(currentMessage.text,BOT))
                       adapter.notifyDataSetChanged()
                   }

               }
           }

           override fun onFailure(call: Call<ArrayList<BotResponse>>, t: Throwable) {
               val message="check your connection"
               messageList.add(messageClass(message,BOT))

           }

       })
    }
}