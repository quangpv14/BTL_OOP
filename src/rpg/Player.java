package rpg;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


class Player extends Entity {

    protected final int MAXLEVEL = 8;
    protected int HP, MP;
    protected int HPMax, MPMax;
    protected int EXP;
    protected int level;
    protected int ATK;
    protected int ATK_FIREBALL;
    protected int DEF;
    protected double speed;
    protected double att_x = 0;
    protected double att_y = 0;
    protected double att_fireball_x = 0;
    protected double att_fireball_y = 0;
    protected Entity.Direction dir_fire;
    protected int index_att;
    protected int countSword;
    protected int countFireball;
    protected int countRegen;
    public Bag bag;
    protected Point range_Att1 = new Point();
    protected Point range_Att2 = new Point();
    protected Point range_Att3 = new Point();
    protected Point range_fireball = new Point();
    protected int countdownImmortal, countAtRest, countTime;
    protected int index_img;
    protected int index_fireball;

    protected BufferedImage character;
    protected Image[][] Character = new Image[4][3];
    protected BufferedImage titan;
    protected Image[][] Titan = new Image[4][3];
    protected BufferedImage character_spritesheet;
    protected BufferedImage titan_spritesheet;
   
    protected int width, height;
    protected int Width, Height;//new
    /* img for att
    array of att sword img
    att current img
     */
    protected BufferedImage[][] sword = new BufferedImage[4][3];
    protected BufferedImage current_sword_att;

    /*img for skill att
    array of fire ball att
    fire ball current img
     */
    protected Image[][] fire_ball = new Image[4][8];
    protected BufferedImage fireball_sheet;
    protected BufferedImage current_fireball;

    protected boolean att = false;// attack sword
    protected boolean fireball_att = false;
    protected boolean isTitan = false;

    //private Item items[];
    public void Load_image() {
        try {
            
            URL character_sheetUrl = this.getClass().getResource("/rpg/resources/character/full_character.png");
            character_spritesheet = ImageIO.read(character_sheetUrl);
            URL fireball_sheetUrl = this.getClass().getResource("/rpg/resources/character/attack/fireball.png");
            fireball_sheet = ImageIO.read(fireball_sheetUrl);
            URL titan_sheetUrl = this.getClass().getResource("/rpg/resources/character/titan.png");
            titan_spritesheet = ImageIO.read(titan_sheetUrl);
            

            //load img of att
            for (int i = 0; i < 3; i++) {
                URL att_leftUrl = this.getClass().getResource("/rpg/resources/character/attack/sword_left_" + (i + 1) + ".png");
                sword[0][i] = ImageIO.read(att_leftUrl);
            }
            for (int i = 0; i < 3; i++) {
                URL att_rightUrl = this.getClass().getResource("/rpg/resources/character/attack/sword_right_" + (i + 1) + ".png");
                sword[1][i] = ImageIO.read(att_rightUrl);
            }
            for (int i = 0; i < 3; i++) {
                URL att_upUrl = this.getClass().getResource("/rpg/resources/character/attack/sword_up_" + (i + 1) + ".png");
                sword[2][i] = ImageIO.read(att_upUrl);
            }
            for (int i = 0; i < 3; i++) {
                URL att_downUrl = this.getClass().getResource("/rpg/resources/character/attack/sword_down_" + (i + 1) + ".png");
                sword[3][i] = ImageIO.read(att_downUrl);
            }
            this.Width = character_spritesheet.getWidth() / 12;
            this.Height = character_spritesheet.getHeight() / 8;

            //load img of character
            int x = 0, y = 0;
            switch(Framework.index_character) {
            case 0: x = 0; y = 0;
            break;
            case 1: x = this.Width*3; y = 0;
            break;
            case 2: x = this.Width*6; y = 0;
            break;
            case 3: x = this.Width*9; y = 0;
            break;
            }
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    Character[i][j] = character_spritesheet.getSubimage(x, y, this.Width, this.Height);
                    x += this.Width;
                }
                x -= 3 * this.Width;
                y += this.Height;
            }
            
          
            
            //load img of fireball | height and width = 64
            x = 0;
            y = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    fire_ball[i][j] = fireball_sheet.getSubimage(x, y, 64, 64);
                    x += 64;
                }
                x = 0;
                y += 64 * 2;
            }
            
            //load img of titan | height and width
            x = 0; y = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    Titan[i][j] = titan_spritesheet.getSubimage(x, y, 80, 105);
                    x += 80;
                }
                x = 0;
                y += 105;
            }
        } catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
            

    }
    
    
    //Set toa do mac dinh cho nhan vat, set Speed
    public Player() {
        this.bag = new Bag();
        this.Load_image();
        character = (BufferedImage) Character[0][1];
        current_sword_att = sword[3][2];
        current_fireball = (BufferedImage) fire_ball[0][1];
        this.visible = true;
        this.HP = 100;
        this.HPMax = 100;
        this.MP = 100;
        this.MPMax = 100;
        this.level = 1;
        this.EXP = 0;
        this.ATK = 10;
        this.ATK_FIREBALL = 50;
        this.DEF = 2;
        x = 6 * Map.TILE_WIDTH;
        y = 30 * Map.TILE_HEIGHT;
        speed_base = 3;
        index_att = 0;
        this.index_img = 0;
        this.index_fireball = 0;
        this.countTime = 0;
        this.countRegen = 0;
        this.direction = Direction.DOWN;
        dir_fire = this.direction;
    }

    public int get_ATK_FIREBALL() {
        return this.ATK_FIREBALL;
    }

    public void set_Xfireball(double x) {
        this.att_fireball_x = x - 32;
    }

    public void set_Yfireball(double y) {
        this.att_fireball_y = y - 32;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getEXP() {
        return EXP;
    }

    public void setEXP(int EXP) {
        this.EXP = EXP;
    }

    public void addEXP(int aEXP) {
        int newEXP = this.EXP + aEXP;
        if (levelUp(this.EXP, newEXP)) {
            this.level++;
            this.HP_base = 100 * level;
            this.MP_base = 80 * level;
            this.DEF_base = 3 * level;
            this.attack_base = 4 * level;
            UpdatePlayer();
            HP = HPMax;
            MP = MPMax;
        }
        this.EXP += aEXP;
    }

    private boolean levelUp(int oldEXP, int newEXP) {
        for (int i = 0; i <= MAXLEVEL; i++) {
            int cotmoc = (int) Math.pow(2, i) * 100;
            if (((oldEXP - cotmoc) * (newEXP - cotmoc)) < 0 || (newEXP == cotmoc)) {
                return true;
            }
        }
        return false;
    }

    public void UpdatePlayer() {
        HPMax = this.HP_base + bag.getHP();
        MPMax = this.MP_base + bag.getMP();
        this.ATK = this.attack_base + bag.getATK();
        this.DEF = this.DEF_base + bag.getDEF();
        this.speed = this.speed_base + bag.getSpeed();

    }

    public Player(double x, double y, int attack_base, int DEF_base, int HP_base, int speed) {
        this.setX(x);
        this.setY(y);
        this.setSpeed(speed);
        this.setHP_base(HP_base);
        this.setDEF_base(DEF_base);
        this.setAttack_base(attack_base);
    }

    @SuppressWarnings("null")
	public void transform() {
    	this.isTitan = true;
    	this.HPMax = this.HPMax + 800;
    	this.HP = this.HP + 800;
    	this.ATK = this.ATK + 100;
    	this.DEF = this.DEF + 100;
    	this.speed = this.speed - 10;
    }
    
    public void reform() {
    	this.isTitan = false;
    }
    public void decreaseHP(int dame) {
        if (countdownImmortal == 0) {
            if(dame>this.DEF){
            this.HP -= (dame - this.DEF);System.out.println("Player HP = " + HP);
            countAtRest = 10;
            countdownImmortal = 40;
            }
        }
    }

    public boolean immortal() {
        return (countdownImmortal > 0);
    }

    public boolean die() {
        return this.HP < 0;
    }
    
    public void move(double nx, double ny) {
        this.x = nx;
        this.y = ny;
        countTime++;
    }

    public void player_be_attacked() {
        if (countdownImmortal > 0) {
            countdownImmortal--;
        }
        if (countAtRest > 0) {
            countAtRest--;
        }
    }

    public void setSwordAni() {
        if (att == true) {
        } else {
        }
    }

    public void setFireballAni() {
        if (fireball_att == true) {
            
        } else {
        }
    }

    public void set_att(boolean att) {
        this.att = att;
    }

    public boolean get_att() {
        return this.att;
    }

    public void set_fireball_att(boolean att) {

        if (att) {
            if (MP >= 45) {
                this.fireball_att = att;
                this.MP -= 45;
            } else {
                this.fireball_att = false;
            }
        } else {
            this.fireball_att = att;
        }

    }

    public void regen() {
        if (countRegen % 40 == 0) {
            if (HP < HPMax - 2) {
                HP += 1;
            }
            if (MP < MPMax - 5) {
                MP += 1;
            }
        }
        countRegen++;
    }

    public boolean get_fireball_att() {
        return this.fireball_att;
    }

    public int getATK() {
        return this.ATK;
    }

    public int getLevel() {
        return this.level;
    }

    public BufferedImage get_att_range() // lay anh current att
    {
        return current_sword_att;
    }

    public Point get_fireball_att_point() {
        return range_fireball;
    }

    public Point get_att_point1() // tra ve diem thu nhat de xet toa do att
    {
        return range_Att1;
    }

    public Point get_att_point2()// tra ve diem thu 2 de xet toa do att
    {
        return range_Att2;
    }

    public Point get_att_point3()// tra ve diem thu 3 de xet toa do att
    {
        return range_Att3;
    }

    void draw_direction() {
        if (countAtRest > 0) {
            countAtRest--;
        }
        if (countAtRest == 0) {
            if (countTime * speed_base % 30 == 0) {
                if (index_img > 0) {
                    index_img--;
                } else if (index_img == 0) {
                    index_img = 2;
                }
            }
        }
        if (direction == Direction.LEFT) {
        if(isTitan) character = (BufferedImage) Titan[1][index_img];
        else
            character = (BufferedImage) Character[1][index_img];
            current_sword_att = sword[0][index_att];
            att_x = x - 25;
            att_y = y - 27;
            range_Att1.setLocation(att_x, att_y);
            range_Att3.setLocation(att_x, att_y + current_sword_att.getHeight());
            range_Att2.setLocation((range_Att1.getX() + range_Att3.getX()) / 2, (range_Att1.getY() + range_Att3.getY()) / 2);
        }
        if (direction == Direction.RIGHT) {
        	if(isTitan) character = (BufferedImage) Titan[2][index_img];
        	else
            character = (BufferedImage) Character[2][index_img];
            current_sword_att = sword[1][index_att];
            att_x = x + 30;
            att_y = y - 29;
            range_Att1.setLocation(att_x, att_y);
            range_Att3.setLocation(att_x + current_sword_att.getWidth(), att_y + current_sword_att.getHeight());
            range_Att2.setLocation((range_Att1.getX() + range_Att3.getX()) / 2, (range_Att1.getY() + range_Att3.getY()) / 2);
        }
        if (direction == Direction.UP) {
        	if(isTitan) character = (BufferedImage) Titan[3][index_img];
        	else
            character = (BufferedImage) Character[3][index_img];
            current_sword_att = sword[2][index_att];
            att_x = x - 10;
            att_y = y - 50;
            range_Att1.setLocation(att_x, att_y);
            range_Att3.setLocation(att_x + current_sword_att.getWidth(), att_y);
            range_Att2.setLocation((range_Att1.getX() + range_Att3.getX()) / 2, (range_Att1.getY() + range_Att3.getY()) / 2);
        }
        if (direction == Direction.DOWN) {
        	if(isTitan) character = (BufferedImage) Titan[0][index_img];
        	else
            character = (BufferedImage) Character[0][index_img];
            current_sword_att = sword[3][index_att];

            att_x = x - 8;
            att_y = y + 20;
            range_Att1.setLocation(att_x, att_y);
            range_Att3.setLocation(att_x + current_sword_att.getWidth(), att_y + current_sword_att.getHeight());
            range_Att2.setLocation((range_Att1.getX() + range_Att3.getX()) / 2, (range_Att1.getY() + range_Att3.getY()) / 2);
        }

    }

    void paint(Graphics2D g2d) {
        if (visible) {
        	if(isTitan) g2d.drawImage(character, (int) (x - 13), (int) (y-52), 80, 105, null);
        	else
            g2d.drawImage(character, (int) (x - 8), (int) (y - 23), this.Width, this.Height, null);
            drawHPbar(g2d);
        }
        if (att) {
            g2d.drawImage(current_sword_att, (int) att_x, (int) att_y, current_sword_att.getWidth(), current_sword_att.getHeight(), null);
        }
        if (fireball_att) {
            g2d.drawImage(current_fireball, (int) att_fireball_x, (int) att_fireball_y, 100, 100, null);
        }
        draw_direction();

    }

    void drawHPbar(Graphics2D g2d) {
        g2d.setColor(Color.green);
        g2d.fillOval((int) x - 21, (int) y - 41, 15, 15);
        g2d.setColor(Color.MAGENTA);
        g2d.setFont(new Font("Verdana", Font.BOLD, 15));
        g2d.drawString(String.valueOf(this.level), (int) x - 19, (int) y - 28);
        g2d.setColor(Color.green);
        g2d.drawRect((int) x - 6, (int) y - 38, 35, 5);
        g2d.setColor(Color.yellow);
        g2d.fillRect((int) x - 5, (int) y - 37, (int) 34 * HP / HPMax, 4);
        g2d.setColor(Color.blue);
        g2d.drawRect((int) x - 6, (int) y - 32, 35, 4);
        g2d.fillRect((int) x - 5, (int) y - 31, (int) 34 * this.MP / this.MPMax, 3);
    }

}
