package com.csclab.hc.androidpratice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.net.ConnectException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    /** Init Variable for Page 1 **/
     String destinationIPAddr;
     int destinationPortNum;

    public Socket socket;
    public PrintWriter writer;
    public BufferedReader reader;

    public ClientThread t1;

    EditText connectTxt;
    Button connectOK;

    EditText inputNumTxt1;
    EditText inputNumTxt2;

    Button btnAdd;
    Button btnSub;
    Button btnMult;
    Button btnDiv;

    /** Init Variable for Page 2 **/
    TextView textResult;

    Button return_button;

    /** Init Variable **/
    String oper = "";

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Func() for setup page 1 **/
        jumpToConnectLayout();
        //jumpToMainLayout();
    }

    public void jumpToConnectLayout(){
        setContentView(R.layout.connect_page);
        System.out.print("aaa");
        connectTxt = (EditText) findViewById(R.id.connectTxt);
        connectOK = (Button) findViewById(R.id.connect_button);

        connectOK.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // TODO
                destinationIPAddr = connectTxt.getText().toString();
                destinationPortNum = 2000;
                System.out.print("qwe");
                Thread t1 = new connect();
                t1.start();
                jumpToMainLayout();
            }

        });
    }

    /** Function for page 1 setup */
    public void jumpToMainLayout() {
        //TODO: Change layout to activity_main
        // HINT: setContentView()
        setContentView(R.layout.activity_main);
        System.out.println("ee");
        //TODO: Find and bind all elements(4 buttons 2 EditTexts)
        // inputNumTxt1, inputNumTxt2
        // btnAdd, btnSub, btnMult, btnDiv
        inputNumTxt1 = (EditText) findViewById(R.id.etNum1);
        inputNumTxt2 = (EditText) findViewById(R.id.etNum2);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnMult = (Button) findViewById(R.id.btnMult);
        btnDiv = (Button) findViewById(R.id.btnDiv);

        //TODO: Set 4 buttons' listener
        // HINT: myButton.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnMult.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
    }




    /** Function for onclick() implement */
    @Override
    public void onClick(View v) {
        float num1 = 0; // Store input num 1
        float num2 = 0; // Store input num 2
        float result = 0; // Store result after calculating
        System.out.println("ww");
        // check if the fields are empty
        if (TextUtils.isEmpty(inputNumTxt1.getText().toString())
                || TextUtils.isEmpty(inputNumTxt2.getText().toString())) {
            return;
        }

        // read EditText and fill variables with numbers
        num1 = Float.parseFloat(inputNumTxt1.getText().toString());
        num2 = Float.parseFloat(inputNumTxt2.getText().toString());

        // defines the button that has been clicked and performs the corresponding operation
        // write operation into oper, we will use it later for output
        //TODO: caculate result
        switch (v.getId()) {
            case R.id.btnAdd:
                oper = "+";
                result = num1 + num2;
                break;
            case R.id.btnSub:
                oper = "-";
                result = num1 - num2;
                break;
            case R.id.btnMult:
                oper = "*";
                result = num1 * num2;
                break;
            case R.id.btnDiv:
                oper = "/";
                result = num1 / num2;
                break;
            default:
                break;
        }
        // HINT:Using log.d to check your answer is correct before implement page turning
        Log.d("debug", "ANS " + result);
        //TODO: Pass the result String to jumpToResultLayout() and show the result at Result view
        System.out.println("qq");
        this.sendMessage(new String(num1 + " " + oper + " " + num2 + " = " + result));
        jumpToResultLayout(new String(num1 + " " + oper + " " + num2 + " = " + result));
    }

    public void jumpToResultLayout(String resultStr){
        setContentView(R.layout.result_page);

        //TODO: Bind return_button and textResult form result view
        // HINT: findViewById()
        // HINT: Remember to give type
        return_button = (Button) findViewById(R.id.return_button);
        textResult = (TextView) findViewById(R.id.textResult);

        if (textResult != null) {
            //TODO: Set the result text
            textResult.setText(resultStr);
        }

        if (return_button != null) {
            //TODO: prepare button listener for return button
            // HINT:
            // mybutton.setOnClickListener(new View.OnClickListener(){
            //      public void onClick(View v) {
            //          // Something to do..
            //      }
            // }
            return_button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    // TODO
                    jumpToMainLayout();
                }

            });
        }
    }

    public class connect extends Thread {
        // Create socket & thread, remember to start your thread
        public void run() {
            try {
                socket = new Socket(destinationIPAddr, destinationPortNum);
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                t1 = new ClientThread(reader);
                t1.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ClientThread extends Thread{
        private BufferedReader reader;
        public ClientThread(BufferedReader reader) {
            this.reader = reader;
        }
        public void run() {
            while(true) {
                try {
                    String line = this.reader.readLine();
                    System.out.println(line);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendMessage(String message) {
//		System.out.println(SwingUtilities.isEventDispatchThread());
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("The result from APP is ").append(message);
        this.writer.println(sBuilder.toString());
        this.writer.flush();
    }

}
