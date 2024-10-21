/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

class Map {
    private ArrayList<submap> submaps; 
    private submap currentMap;
    public static ArrayList<BufferedImage> imgs;
    public static BufferedImage border,tempImg;
    public static BufferedImage backgr;
    public final static int TILE_WIDTH = 24;
    public final static int TILE_HEIGHT = 18;
    
    public final static int NUMBER_OF_SUBMAP = 3;
    public int id_current;
    
    public Map(){
        submaps = new ArrayList<>();
        imgs = new ArrayList<>();
        submaps.add(new submap());
        loadImage();
        for (int i=1;i<=NUMBER_OF_SUBMAP;i++){
            submaps.add(new submap(i));
        }
        id_current = 1;
        currentMap = submaps.get(id_current);
    }
    
    public submap getCurrentMap()
    {
        return currentMap;
    }
    
    public void nextMap(){
        if(id_current<NUMBER_OF_SUBMAP){
            id_current++;
            currentMap = submaps.get(id_current);
            System.out.println("next "+id_current);
        }else{
            System.out.println("Da qua het tat ca ca cac map!");
        }
    }
    
    public void backMap(){
        if(id_current>1){
            id_current--;
            currentMap = submaps.get(id_current);
            System.out.println("back "+id_current);
        }
        else{
            System.out.println("Dang la map 1");
        }
    }
    
    //Load tai nguyen dung chung cho tat ca cac map
    public void loadImage(){
        try
        {
            URL borderImgUrl = this.getClass().getResource("/rpg/resources/images/map/border.PNG");
            border = ImageIO.read(borderImgUrl);
            imgs.add(border);
            for (int i=1;i<=NUMBER_OF_SUBMAP;i++){
                URL tempImgUrl = this.getClass().getResource("/rpg/resources/images/map/map"+i+".png");
                tempImg = ImageIO.read(tempImgUrl);
                imgs.add(tempImg);
            }
   
          //  URL backgrImgUrl = this.getClass().getResource("/rpg/resources/images/map/bgr.png");
            //backgr = ImageIO.read(backgrImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Ve map hien tai
    public void paint(Graphics2D g2d){
        g2d.drawImage(border, 0, 0,null);
        currentMap.paint(g2d);
    }
   
}
