package uk.co.alexstoneham;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JokeGenerator {

    private static final String JOKE_API = "https://official-joke-api.appspot.com/random_joke";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Joke Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 200);

        JLabel setupLabel = new JLabel("Click 'New Joke' to get started!");
        JLabel punchlineLabel = new JLabel("");
        JButton showPunchlineButton = new JButton("Show Punchline");
        showPunchlineButton.setEnabled(false);
        JButton newJokeButton = new JButton("New Joke");

        setupLabel.setHorizontalAlignment(SwingConstants.CENTER);
        punchlineLabel.setHorizontalAlignment(SwingConstants.CENTER);
        setupLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        punchlineLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.add(setupLabel);
        centerPanel.add(punchlineLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(showPunchlineButton);
        buttonPanel.add(newJokeButton);

        frame.setLayout(new BorderLayout());
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        final String[] currentPunchline = {""};

        // button: new joke
        newJokeButton.addActionListener((ActionEvent e) -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(JOKE_API))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JSONObject json = new JSONObject(response.body());

                String setup = json.getString("setup");
                String punchline = json.getString("punchline");

                setupLabel.setText(setup);
                punchlineLabel.setText("...");
                currentPunchline[0] = punchline;
                showPunchlineButton.setEnabled(true);

            } catch (IOException | InterruptedException ex) {
                setupLabel.setText("Error fetching joke!");
                punchlineLabel.setText("");
                ex.printStackTrace();
            }
        });

        // button: reveal
        showPunchlineButton.addActionListener((ActionEvent e) -> {
            punchlineLabel.setText(currentPunchline[0]);
            showPunchlineButton.setEnabled(false);
        });
    }
}