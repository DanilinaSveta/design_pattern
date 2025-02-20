package org.example;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        SwingUtilities.invokeLater(()->{
            ShapeDrawer drawer = new ShapeDrawer();
            drawer.setVisible(true);
        });
    }
}