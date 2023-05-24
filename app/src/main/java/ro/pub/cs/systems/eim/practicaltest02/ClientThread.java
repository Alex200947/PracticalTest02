package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private final String address;
    private final int port;
    private final String pokemon;

    private TextView textViewAilities;
    private TextView textViewPokemonTypes;

    private Socket socket;


    public ClientThread(String address, int port, String pokemon, TextView textViewAilities, TextView textViewPokemonTypes) {
        this.address = address;
        this.port = port;
        this.pokemon = pokemon;
        this.textViewAilities = textViewAilities;
       this.textViewPokemonTypes = textViewPokemonTypes;
    }


    @Override
    public void run() {
        try {
            socket = new Socket(address, port);

            // gets the reader and writer for the socket
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            // sends the city and information type to the server
            printWriter.println(pokemon);
            printWriter.flush();

            String abilities;
            String types;
            while ((abilities = bufferedReader.readLine()) != null &&
                    (types = bufferedReader.readLine()) != null) {
                final String finalizedAbilities = abilities;
                final String finalizedTypes = types;
                textViewAilities.post(() -> textViewAilities.setText(finalizedAbilities));
                textViewPokemonTypes.post(() -> textViewPokemonTypes.setText(finalizedTypes));
            }


        }  catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    // closes the socket regardless of errors or not
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
