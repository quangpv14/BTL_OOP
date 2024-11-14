package rpg;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
import java.awt.Rectangle;


public class Game {

    private Map map;

    private Player player;

    private long time_start_sword;
    private long time_start_fireball;
    private long time_start_transform;
    private final int COOLDOWN_ATTACK = 60;
    private int cooldown;
    private int fireball_cooldown;
    private boolean isEnd = false;

    public Game() {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread() {
            @Override
            public void run() {
                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();

                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }

    public Game(File f) {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread() {
            @Override
            public void run() {
                //Load previous game state.
                loadGame(f);
                // Load game files (images, sounds, ...)
                LoadContent();

                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }

    /**
     * Set variables and objects for the game.
     */
    private void Initialize() {
        map = new Map();
        player = new Player();
        cooldown = 0;
        fireball_cooldown = 0;
    }

    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent() {

    }

    private void loadGame(File f) {

    }

    /**
     * Restart game - reset some variables.
     */
    public void RestartGame() {

    }

    /**
     * Update game logic.
     *
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition) {
        player_move();
        playerAction(gameTime);
        monsterAction();

    }

    public int valid_location(double nx, double ny) {
        if (map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 0
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 0
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 0
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 0) {
            return 0;
        } else if (map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 10
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 10
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 10
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 10) {
            return 10;
        } else if (map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 21
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 21
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 21
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 21) {
            return 21;
        } else if (map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 30
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 30
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 30
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 30) {
            return 30;
        } else if (map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 35
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 35
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 35
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 35) {
            return 35;
        } else if (map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 15
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 15
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 15
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 15) {
            return 15;
        }
        return 0;

    }

    public void monster_move()//logic move's monster
    {
        for (int i = 0; i < map.getCurrentMap().numberOfMonster; i++) {
            double v = map.getCurrentMap().arrayMonster.get(i).getSpeed_base();
            double nx = map.getCurrentMap().arrayMonster.get(i).getX();
            double ny = map.getCurrentMap().arrayMonster.get(i).getY();
            if (map.getCurrentMap().arrayMonster.get(i).getDirection() == Entity.Direction.RIGHT) {
                nx = nx + v;
            }
            if (map.getCurrentMap().arrayMonster.get(i).getDirection() == Entity.Direction.LEFT) {
                nx = nx - v;
            }
            if (map.getCurrentMap().arrayMonster.get(i).getDirection() == Entity.Direction.UP) {
                ny = ny - v;
            }
            if (map.getCurrentMap().arrayMonster.get(i).getDirection() == Entity.Direction.DOWN) {
                ny = ny + v;
            }

            if (valid_location(nx, ny) == submap.BLOCK) {
                return;
            } else {
                map.getCurrentMap().arrayMonster.get(i).move(nx, ny);
            }

        }
    }

    public void player_move()// logic move's character
    {
        double dx = 0, dy = 0; //new speed_base
        double nx = player.getX(), ny = player.getY(); //new location

        if (Canvas.keyboardKeyState(KeyEvent.VK_LEFT)) {
            dx = -player.speed_base;
        }
        if (Canvas.keyboardKeyState(KeyEvent.VK_RIGHT)) {
            dx = player.speed_base;
        }
        if (Canvas.keyboardKeyState(KeyEvent.VK_UP)) {
            dy = -player.speed_base * 4 / 3;
        }
        if (Canvas.keyboardKeyState(KeyEvent.VK_DOWN)) {
            dy = player.speed_base * 4 / 3;
        }
        nx += dx;
        ny += dy;

        if (valid_location(nx, ny) == submap.CLEAR)
        {
            player.setVisible(true);
            player.move(nx, ny);
        }
        if (valid_location(nx, ny) == submap.REDRAW)
        {
            player.setVisible(false);
            player.move(nx, ny);
        }
        if (valid_location(nx, ny) == submap.END)
        {   
        	if(map.id_current == Map.NUMBER_OF_SUBMAP && isEnd == false) {
        		isEnd = true;
        	}
        	else {
	            map.nextMap();
	            player.move(map.getCurrentMap().STARTx * Map.TILE_WIDTH, map.getCurrentMap().STARTy * Map.TILE_HEIGHT);
            }
        }
        
        if (valid_location(nx, ny) == submap.START)
        {       	
            map.backMap();
            player.move(map.getCurrentMap().STARTx * Map.TILE_WIDTH, map.getCurrentMap().STARTy * Map.TILE_HEIGHT);
        }
        
        if (valid_location(nx, ny) == submap.BLOCK)
        {
            return;
        }
    }

    public void monsterAction() {
        
    }

    public void playerAction(long gameTime) {
        if (player.die()) {
            Framework.gameState = Framework.GameState.GAMEOVER;
        }
        if (isEnd) {
        	Framework.gameState = Framework.GameState.DESTROYED;
        }
        if (cooldown == 0) {
            if (Canvas.keyboardKeyState(KeyEvent.VK_SPACE)) {
                cooldown = COOLDOWN_ATTACK;
                player_att(gameTime);
            }
        }
        if (player.getLevel() >= 2) {
            if (fireball_cooldown == 0) {
                if (Canvas.keyboardKeyState(KeyEvent.VK_F)) {
                    player.set_Xfireball(player.getX());
                    player.set_Yfireball(player.getY());
                    fireball_cooldown = COOLDOWN_ATTACK;
                    player_fireball(gameTime);
                }
            }
        }
        if (Canvas.keyboardKeyState(KeyEvent.VK_R)) {     //Bam R de bien thanh titan
        	player.transform();
        	time_start_transform = gameTime;
        } 
        if(time_start_transform != 0 && gameTime - time_start_transform > 15 * Framework.secInNanosec ) {
        	player.reform();
        	time_start_transform = 0;
        }
        player.setSwordAni();
        player.setFireballAni();
        if (cooldown > 0) {
            cooldown--;
        }
        if (fireball_cooldown > 0) {
            fireball_cooldown--;
        }

        if (time_start_sword != 0 && gameTime - time_start_sword > Framework.secInNanosec / 4) {
            player.set_att(false);
        }
        if (time_start_fireball != 0 && gameTime - time_start_fireball > Framework.secInNanosec) {
            player.set_fireball_att(false);
        }

        player_att_monster();
        player.player_be_attacked();
        player.regen();
    }

    public void player_att(long gameTime) {
        player.set_att(true);
        time_start_sword = gameTime;
    }

    public void player_fireball(long gameTime) {
        player.set_fireball_att(true);
        time_start_fireball = gameTime;

    }
    
    public void player_att_monster()
    {
        
    }

    public int xPlayer() {
        return (int) player.getX();
    }

    public int yPlayer() {
        return (int) player.getY();
    }

    public void Draw(Graphics2D g2d, Point mousePosition) {
        g2d.translate(-player.x + 512, -player.y + 384);
        map.paint(g2d);
        player.paint(g2d);
    }

}
