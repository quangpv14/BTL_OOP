package rpg;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;


public abstract class Canvas extends JPanel implements KeyListener, MouseListener {
    
    // Keyboard states - Here are stored states for keyboard keys - is it down or not.
    private static boolean[] keyboardState = new boolean[525];
    
    // Mouse states - Here are stored states for mouse keys - is it down or not.
    private static boolean[] mouseState = new boolean[3];
        
    private static long mousePressedTime;
    private static boolean clickDetected;
    
    public Canvas()
    {
        // We use double buffer to draw on the screen.
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(Color.black);
        
        // If you will draw your own mouse cursor or if you just want that mouse cursor disapear, 
        // insert "true" into if condition and mouse cursor will be removed.
        if(false)
        {
            BufferedImage blankCursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankCursorImg, new Point(0, 0), null);
            this.setCursor(blankCursor);
        }
        
        // Adds the keyboard listener to JPanel to receive key events from this component.
        this.addKeyListener(this);
        // Adds the mouse listener to JPanel to receive mouse events from this component.
        this.addMouseListener(this);
    }
    
    
    // This method is overridden in Framework.java and is used for drawing to the screen.
    public abstract void Draw(Graphics2D g2d);
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;        
        super.paintComponent(g2d);        
        Draw(g2d);
    }
       
    
    // Keyboard
    /**
     * Is keyboard key "key" down?
     * 
     * @param key Number of key for which you want to check the state.
     * @return true if the key is down, false if the key is not down.
     */
    public static boolean keyboardKeyState(int key)
    {
        return keyboardState[key];
    }
    
    // Methods of the keyboard listener.
    @Override
    public void keyPressed(KeyEvent e) 
    {
        keyboardState[e.getKeyCode()] = true;
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        keyboardState[e.getKeyCode()] = false;
        keyReleasedFramework(e);
    }
    
    @Override
    public void keyTyped(KeyEvent e) { }
    
    public abstract void keyReleasedFramework(KeyEvent e);
    
    
    // Mouse
    /**
     * Is mouse button "button" down?
     * Parameter "button" can be "MouseEvent.BUTTON1" - Indicates mouse button #1
     * or "MouseEvent.BUTTON2" - Indicates mouse button #2 ...
     * 
     * @param button Number of mouse button for which you want to check the state.
     * @return true if the button is down, false if the button is not down.
     */
    public static boolean mouseButtonState(int button)
    {
        return mouseState[button - 1];
    }
    
    public boolean isMouseClicked() {
        long currentTime = System.currentTimeMillis();
        //Chỉ trả về true khi nằm trong thời gian cho phép (40ms) và có nhấn chuột từ trước đó
        if (clickDetected && (currentTime - mousePressedTime <= 40)) {
            clickDetected = false; 
            return true;
        }
        return false;
    }
    
    // Sets mouse key status.
    private void mouseKeyStatus(MouseEvent e, boolean status)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
            mouseState[0] = status;
        else if(e.getButton() == MouseEvent.BUTTON2)
            mouseState[1] = status;
        else if(e.getButton() == MouseEvent.BUTTON3)
            mouseState[2] = status;
    }
    
    // Methods of the mouse listener.
    @Override
    public void mousePressed(MouseEvent e)
    {	//Chỉ cập nhật các trạng thái khi trước đó chưa có nhấn chuột
    	if (!mouseState[e.getButton()]) { 
            mousePressedTime = System.currentTimeMillis();
            clickDetected = true; 
        }
        mouseKeyStatus(e, true);
        System.out.println("Mouse pressed");
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {	
        mouseKeyStatus(e, false);
        System.out.println("Mouse released");
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked");
    }
    
    @Override
    public void mouseEntered(MouseEvent e) { }
    
    @Override
    public void mouseExited(MouseEvent e) { }
    
}
