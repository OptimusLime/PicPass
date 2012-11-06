package com.example.simplesocket;
 
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
 
public class ThreadedClient extends Activity {
	EditText textOut;
	TextView textIn;
	Socket socket = null;
	ServerSocket ss = null;
   String mClientMsg = "";
   Thread myCommsThread = null;
   protected static final int MSG_ID = 0x1337;
   public static final int SERVERPORT = 14000;
 
   @Override
   public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    TextView tv = (TextView) findViewById(R.id.textin);
    tv.setText("Nothing from client yet");
    
    textOut = (EditText)findViewById(R.id.textout);
    Button buttonSend = (Button)findViewById(R.id.send);
    textIn = (TextView)findViewById(R.id.textin);
    buttonSend.setOnClickListener(buttonSendOnClickListener);
    
    
    this.myCommsThread = new Thread(new CommsThread());
    this.myCommsThread.start();
   }
 
   Button.OnClickListener buttonSendOnClickListener
   = new Button.OnClickListener(){

		
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			DataOutputStream dataOutputStream = null;
			DataInputStream dataInputStream = null;
			
			try {
				if(socket == null)
				{
					socket = new Socket("54.243.248.135", 14000);
				
					dataOutputStream = new DataOutputStream(socket.getOutputStream());
					dataInputStream = new DataInputStream(socket.getInputStream());
				}
				dataOutputStream.writeUTF(textOut.getText().toString());
				//textIn.setText(dataInputStream.readUTF());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				//if (socket != null){
					//try {
						//socket.close();
					//} catch (IOException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					//}
				//}
				
//				if (dataOutputStream != null){
//					try {
//						dataOutputStream.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				
//				if (dataInputStream != null){
//					try {
//						dataInputStream.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
			}
		}};
   @Override
   protected void onStop() {
    super.onStop();
    try {
        // make sure you close the socket upon exiting
        ss.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
   }
 
   Handler myUpdateHandler = new Handler() {
    public void handleMessage(Message msg) {
        switch (msg.what) {
        case MSG_ID:
            TextView tv = (TextView) findViewById(R.id.textin);
            tv.setText(mClientMsg);
            break;
        default:
            break;
        }
        super.handleMessage(msg);
    }
   };
   class CommsThread implements Runnable {
    public void run() {
        Socket s = null;
        try {
            ss = new ServerSocket(SERVERPORT );
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!Thread.currentThread().isInterrupted()) {
            Message m = new Message();
            m.what = MSG_ID;
            try {
                if (s == null)
                    s = ss.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String st = null;
                st = input.readLine();
                mClientMsg = st;
                myUpdateHandler.sendMessage(m);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    }
}