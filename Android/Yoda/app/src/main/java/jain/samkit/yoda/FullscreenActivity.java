package jain.samkit.yoda;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;

public class FullscreenActivity extends AppCompatActivity {
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final String IP_ADDR = "192.168.1.43";
    private final int IP_ADDR_PORT = 8080;
    private TextView txtSpeechInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        txtSpeechInput = (TextView) findViewById(R.id.text_view);
        Button btnSpeak = (Button) findViewById(R.id.speech_button);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));

                    callPuter(result.get(0).toLowerCase());
                }

                break;
            }

        }
    }

    private void callPuter(final String str) {
        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    InetAddress serverAddr = InetAddress.getByName(IP_ADDR);
                    Socket s = new Socket();
                    s.connect(new InetSocketAddress(serverAddr, IP_ADDR_PORT), 5000);
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                    byte[] buf = str.getBytes("UTF-8");
                    dos.write(buf, 0, buf.length);

                    //read input stream
                    //DataInputStream dis2 = new DataInputStream(s.getInputStream());
                    //InputStreamReader disR2 = new InputStreamReader(dis2);
                    //BufferedReader br = new BufferedReader(disR2);//create a BufferReader object for input

                    //print the input to the application screen
                    //final TextView receivedMsg = (TextView) findViewById(R.id.textView2);
                    //receivedMsg.setText(br.toString());
                    //Log.v("tagga", br.toString());

                    //dis2.close();
                    s.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        t.start();
    }
}
