package eim.systems.cs.pub.ro.practicaltest02;

/**
 * Created by student on 22.05.2018.
 */

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{
    private String address;
    private int port;
    private String city;
    private TextView citiesTextView;

    private Socket socket;

    public ClientThread(String address, int port, String city, TextView citiesTextView) {
        this.address = address;
        this.port = port;
        this.city = city;
        this.citiesTextView = citiesTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(city);
            printWriter.flush();

            String allCities;
            while ((allCities = bufferedReader.readLine()) != null) {
                final String finalizedCities = allCities;
                citiesTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        citiesTextView.setText(finalizedCities);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
