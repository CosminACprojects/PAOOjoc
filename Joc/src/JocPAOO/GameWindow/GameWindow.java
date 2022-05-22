package JocPAOO.GameWindow;
import JocPAOO.KeyboardController.Keyboard;

import java.awt.*;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;

import JocPAOO.MouseEvents.Mouse;

import javax.swing.*;

public class GameWindow  {
    public JFrame wndFrame;
    private String wndTitle;
    private int wndWidth;
    private int wndHeight;
    private Canvas canvas;

    public GameWindow(String title, int width, int height) {
        this.wndTitle = title;
        this.wndWidth = width;
        this.wndHeight = height;
        this.wndFrame = null;
    }

    public void BuildGameWindow() {
        if (this.wndFrame == null) {
            this.wndFrame = new JFrame(this.wndTitle);
            this.wndFrame.setSize(this.wndWidth, this.wndHeight);
            this.wndFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.wndFrame.setResizable(false);
            this.wndFrame.setLocationRelativeTo((Component)null);
            this.wndFrame.setVisible(true);
            this.wndFrame.addMouseListener(new Mouse());
            this.wndFrame.addKeyListener(new Keyboard());
            this.wndFrame.setFocusable(true);
            this.canvas = new Canvas();
            this.canvas.addMouseListener(new Mouse());
            this.canvas.addKeyListener(new Keyboard());
            this.canvas.setPreferredSize(new Dimension(this.wndWidth, this.wndHeight));
            this.canvas.setMaximumSize(new Dimension(this.wndWidth, this.wndHeight));
            this.canvas.setMinimumSize(new Dimension(this.wndWidth, this.wndHeight));
            this.wndFrame.add(this.canvas);
            this.wndFrame.pack();

        }
    }

    public int GetWndWidth() {
        return this.wndWidth;
    }

    public int GetWndHeight() {
        return this.wndHeight;
    }

    public Canvas GetCanvas() {
        return this.canvas;
    }


}

