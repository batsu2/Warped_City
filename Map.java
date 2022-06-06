package com.warpedcity.game;

import static com.warpedcity.game.screens.GameScreen.Pause;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.warpedcity.game.enemy.Drone;
import com.warpedcity.game.enemy.EggTurret;
import com.warpedcity.game.enemy.EnemyBullet;
import com.warpedcity.game.props.BannerBig;
import com.warpedcity.game.props.BannerNeon;
import com.warpedcity.game.props.BigBannerMonitor;
import com.warpedcity.game.props.BoxBanner;
import com.warpedcity.game.props.CokeSign;
import com.warpedcity.game.props.Monitor;
import com.warpedcity.game.props.Prop;
import com.warpedcity.game.props.ScrollSign;
import com.warpedcity.game.props.SideSign;
import com.warpedcity.game.props.SushiSign;

public class Map
{
    public static int EMPTY = 0;
    static int TILE = 0xffffff;
    static int START = 0xff0000;
    static int END = 0xff00ff;
    static int DISPENSER = 0xff0100;
    static int SCROLLSIGN = 0x5507FF;
    static int LADDERS = 0xFFD800;
    static int LADDER_END = 0xA58A00;
    static int LADDER_BOTTOM = 0xA39659;
    static int POWERUPS = 0x7F0000;
    static int MONITORS = 0x00FFFF;
    static int BUILDINGS = 0xFF6A00;
    static int BIGBANNER = 0x00FF21;
    static int BIGBANNERMONITOR = 0x001DFF;
    static int DRONES = 0x808080;
    static int EGGTURRETS = 0xA5A7FF;
    static int SUSHISIGN = 0x75A0FF;
    static int SIDESIGN = 0xFF38D0;
    static int NEONBANNER = 0x7F3300;
    static int COKESIGN = 0xFF2356;
    static int BOXBANNER = 0xC1FFF5;

    Sound pistolSound = Gdx.audio.newSound(Gdx.files.internal("data/plasma_pistol.ogg"));
    //Sound casingDropSound = Gdx.audio.newSound(Gdx.files.internal("data/casing_drop.ogg"));
    Sound overheatSound = Gdx.audio.newSound(Gdx.files.internal("data/overheat.ogg"));
    Sound laserShotSound = Gdx.audio.newSound(Gdx.files.internal("data/laser-shot.ogg"));
    //Sound shotWallHitSound = Gdx.audio.newSound(Gdx.files.internal("data/projectile-hit.ogg"));
    //Sound pauseOnSound = Gdx.audio.newSound(Gdx.files.internal("data/pauseon.ogg"));
    //Sound pauseOffSound = Gdx.audio.newSound(Gdx.files.internal("data/pauseoff.ogg"));
    //public static Sound deathSound = Gdx.audio.newSound(Gdx.files.internal("data/death.mp3"));


    public int[][] tiles;
    public Player alita;
    public float shotDelay = 0.0f;
    public float enReloadDelay = 0.0f;
    //public float enemyShotDelay = 0.0f;
    Array<Dispenser> dispensers = new Array<>();
    public Array<Bullet> bullets = new Array<>();
    public Array<Drone> drones = new Array<>();
    public Array<EggTurret> eggTurrets = new Array<>();
    public Array<EnemyBullet> enemyBullets = new Array<>();
    Array<Building> buildings = new Array<>();
    Array<Ladder> ladders = new Array<>();
    Array<LadderEnd> ladderEnds = new Array<>();
    Array<PowerUp> powerUps = new Array<>();

    //Props
    Array<Prop> props = new Array<>();


    //Controller controller = null;
    //ControllerListener controllerListener;

    Dispenser activeDispenser = null;
    //public Exit exit;

    public Map ()
    {
        loadBinary();
    }


    private void loadBinary ()
    {
        Pixmap pixmap = new Pixmap(Gdx.files.internal("data/levels.png"));

        tiles = new int[pixmap.getWidth()][pixmap.getHeight()];


        //Draw the pixmap SIZE 200x70 bricks
        for (int y = 0; y < 70; y++)
        {
            for (int x = 0; x < 200; x++)
            {
                int pix = (pixmap.getPixel(x, y) >>> 8) & 0xffffff;

                if (match(pix, START))
                {
                    Dispenser dispenser = new Dispenser(x, pixmap.getHeight() - 1 - y);
                    dispensers.add(dispenser);
                    activeDispenser = dispenser;
                    alita = new Player(this, activeDispenser.bounds.x, activeDispenser.bounds.y);
                    Player.state = Player.SPAWN;
                }
                else if (match(pix, DISPENSER))
                {
                    Dispenser dispenser = new Dispenser(x, pixmap.getHeight() - 1 - y);
                    dispensers.add(dispenser);
                }
                else if (match(pix,POWERUPS))
                {
                    PowerUp powerUp = new PowerUp(this, x, pixmap.getHeight() - 1 - y);
                    powerUps.add(powerUp);
                }
                else if (match (pix, DRONES) )
                {
                    Drone drone = new Drone(this, x, pixmap.getHeight() - 1 - y);
                    drones.add(drone);
                }
                else if (match (pix, EGGTURRETS) )
                {
                    EggTurret eggTurret = new EggTurret(this, x, pixmap.getHeight() - 1 - y);
                    eggTurrets.add(eggTurret);
                }
                else if (match(pix,BUILDINGS))
                {
                    Building building = new Building(x, pixmap.getHeight() - 1 - y);
                    buildings.add(building);
                }
                else if (match(pix,BIGBANNER))
                {
                    BannerBig bannerBig = new BannerBig(this, x, pixmap.getHeight() - 1 - y, 1, 2);
                    props.add(bannerBig);
                }
                else if (match(pix,BIGBANNERMONITOR))
                {
                    BigBannerMonitor bigBannerMonitor = new BigBannerMonitor(this, x, pixmap.getHeight() - 1 - y, 2, 1);
                    props.add(bigBannerMonitor);
                }
                else if (match(pix,BOXBANNER))
                {
                    BoxBanner boxBanner = new BoxBanner(this, x, pixmap.getHeight() - 1 - y, 1, 1);
                    props.add(boxBanner);
                }
                else if (match(pix,MONITORS))
                {
                    Monitor monitor = new Monitor(this, x, pixmap.getHeight() - 1 - y, 1, 1);
                    props.add(monitor);
                }
                else if (match(pix,COKESIGN))
                {
                    CokeSign cokeSign = new CokeSign(this, x, pixmap.getHeight() - 1 - y, 1, 1);
                    props.add(cokeSign);
                }
                else if (match(pix,SUSHISIGN))
                {
                    SushiSign sushiSign = new SushiSign(this, x, pixmap.getHeight() - 1 - y, 1, 1);
                    props.add(sushiSign);
                }
                else if (match(pix,SCROLLSIGN))
                {
                    ScrollSign scrollSign = new ScrollSign(this, x, pixmap.getHeight() - 1 - y, 1, 1);
                    props.add(scrollSign);
                }
                else if (match(pix,SIDESIGN))
                {
                    SideSign sideSign = new SideSign(this, x, pixmap.getHeight() - 1 - y, 1, 1);
                    props.add(sideSign);
                }
                else if (match(pix,NEONBANNER))
                {
                    BannerNeon neonBanner = new BannerNeon(this, x, pixmap.getHeight() - 1 - y, 1, 2);
                    props.add(neonBanner);
                }
                //else if(match(pix, SPIKES))
                //{
                    //Spike spike = new Spike(x, pixmap.getHeight() - 1 - y);
                    //spikes.add(spike);
                //}
                //else if (match(pix, ROCKET))
                //{
                   // Rocket rocket = new Rocket(this, x, pixmap.getHeight() - 1 - y);
                    //rockets.add(rocket);
               // }
                //else if (match(pix, MOVING_SPIKES))
                //{
                    //movingSpikes.add(new MovingSpikes(this, x, pixmap.getHeight() - 1 - y));
                //}
                //else if (match(pix, LASER))
                //{
                    //lasers.add(new Laser(this, x, pixmap.getHeight() - 1 - y));
                //}
                /*else if (match(pix, END))
                {
                    exit = new Exit(x, pixmap.getHeight() - 1 - y);
                    exits.add(exit);
                }*/
                else if(match(pix, LADDERS))
                {
                    Ladder ladder = new Ladder(x, pixmap.getHeight() - 1 - y);
                    ladders.add(ladder);

                    //ladders[x][y] = pix;
                }
                else if(match(pix, LADDER_END))
                {
                    LadderEnd ladderEnd = new LadderEnd(x, pixmap.getHeight() - 1 - y);
                    ladderEnds.add(ladderEnd);
                }
                else
                {
                    tiles[x][y] = pix;
                }
            }
        }

        /*for (int i = 0; i < grunts.size; i++)
        {
            grunts.get(i).init();
        }


        for (int i = 0; i < cacties.size; i++)
        {
            cacties.get(i).init();
        }

        for (int i = 0; i < tanks.size; i++)
        {
            tanks.get(i).init();
        }*/
        for (int i = 0; i < drones.size; i++)
        {
            drones.get(i).init();
        }
    }

    boolean match (int src, int dst)
    {
        return src == dst;
    }




    public void update (float deltaTime)
    {
        alita.update(deltaTime);


        if (Player.state == Player.DEAD)
            alita = new Player(this, activeDispenser.bounds.x, activeDispenser.bounds.y);


        if(enReloadDelay <= 0 && alita.energy < 5)
        {
            alita.energy++;
            enReloadDelay = 0.4f;
        }
        else
        {
            enReloadDelay -= deltaTime;
        }


        if(shotDelay <= 0)
        {
            //Crouch Shoot
            if(Player.state != Player.JUMP)
            {
                if ((Player.state == Player.CROUCH_SHOOT) && alita.dir == Player.RIGHT && alita.energy > 0)
                {
                    pistolSound.play(0.2f);
                    //casingDropSound.play(0.2f);
                    Bullet bullet = new Bullet(this, alita.pos.x + 2.1f, alita.pos.y + 0.5f, Player.RIGHT);
                    bullets.add(bullet);

                    if(alita.energy > 0)
                        alita.energy--;

                    shotDelay = 0.15f;

                    if(alita.energy == 0)
                    {
                        enReloadDelay = 1.5f;
                        overheatSound.play(0.7f);
                    }
                    else
                        enReloadDelay = 0.4f;
                }
                else if ((Player.state == Player.CROUCH_SHOOT) && alita.dir == Player.LEFT && alita.energy > 0)
                {
                    pistolSound.play(0.2f);
                    //casingDropSound.play(0.2f);
                    Bullet bullet = new Bullet(this, alita.pos.x, alita.pos.y + 0.5f, Player.LEFT);
                    bullets.add(bullet);

                    if(alita.energy > 0)
                        alita.energy--;

                    shotDelay = 0.15f;

                    if(alita.energy == 0)
                    {
                        enReloadDelay = 1.5f;
                        overheatSound.play(0.7f);
                    }
                    else
                        enReloadDelay = 0.4f;
                }
            }



            //if (alita.state == alita.JUMP || alita.state == alita.JUMP_SHOOT)
            //{
               /* if ((alita.state == alita.JUMP_SHOOT) && alita.dir == alita.RIGHT)
                {
                    Bullet bullet = new Bullet(this, alita.pos.x + 2.1f, alita.pos.y + 0.5f, alita.RIGHT);
                    bullets.add(bullet);

                    shotDelay = 0.1f;
                    alita.state = alita.JUMP;
                }
                else if ((alita.state == alita.JUMP_SHOOT) && alita.dir == alita.LEFT) {
                    Bullet bullet = new Bullet(this, alita.pos.x, alita.pos.y + 0.5f, alita.LEFT);
                    bullets.add(bullet);

                    shotDelay = 0.1f;
                    alita.state = alita.JUMP;

                }*/
            //}

            if ( (Player.state == Player.SHOOT || Player.state == Player.RUN_SHOOT || Player.state == Player.JUMP_SHOOT)
                    && alita.dir == Player.RIGHT  && alita.energy > 0)
            {
                pistolSound.play(0.2f);
                //casingDropSound.play(0.2f);
                Bullet bullet = new Bullet(this, alita.pos.x + 2.1f, alita.pos.y + 1.4f, Player.RIGHT);
                bullets.add(bullet);

                if(alita.energy > 0)
                    alita.energy--;

                if( Player.state == Player.JUMP_SHOOT)
                    Player.state = Player.JUMP;

                shotDelay = 0.15f;

                if(alita.energy == 0)
                {
                    enReloadDelay = 1.5f;
                    overheatSound.play(0.7f);
                }
                else
                    enReloadDelay = 0.4f;

            }
            else if ( (Player.state == Player.SHOOT || Player.state == Player.RUN_SHOOT || Player.state == Player.JUMP_SHOOT)
                    && alita.dir == Player.LEFT  && alita.energy > 0)
            {
                pistolSound.play(0.2f);
                //casingDropSound.play(0.2f);
                Bullet bullet = new Bullet(this, alita.pos.x, alita.pos.y + 1.4f, Player.LEFT);
                bullets.add(bullet);

                if(alita.energy > 0)
                    alita.energy--;

                if( Player.state == Player.JUMP_SHOOT)
                    Player.state = Player.JUMP;

                shotDelay = 0.15f;

                if(alita.energy == 0)
                {
                    enReloadDelay = 1.5f;
                    overheatSound.play(0.7f);
                }
                else
                    enReloadDelay = 0.4f;
            }
        }
        else
        {
            shotDelay -= deltaTime;
        }


        //Set active respawn point
        for (int i = 0; i < dispensers.size; i++)
        {
            if (alita.bounds.overlaps(dispensers.get(i).bounds))
            {
                activeDispenser = dispensers.get(i);
            }
        }



        for (int i = 0; i < eggTurrets.size; i++)
        {
            if (eggTurrets.get(i).dir == EggTurret.LEFT && eggTurrets.get(i).state != EggTurret.EXPLODING)
            {
                if(eggTurrets.get(i).shotRangeLeft.overlaps(alita.bounds) )
                    eggTurrets.get(i).state = EggTurret.SHOOT;
                else
                    eggTurrets.get(i).state = EggTurret.IDLE;
            }
            else if (eggTurrets.get(i).dir == EggTurret.RIGHT && eggTurrets.get(i).state != EggTurret.EXPLODING)
            {
                if(eggTurrets.get(i).shotRangeRight.overlaps(alita.bounds) )
                    eggTurrets.get(i).state = EggTurret.SHOOT;
                else
                    eggTurrets.get(i).state = EggTurret.IDLE;
            }




            if(eggTurrets.get(i).enemyShotDelay <= 0 && eggTurrets.get(i).active)
            {

                if (eggTurrets.get(i).state == EggTurret.SHOOT && eggTurrets.get(i).dir == EggTurret.LEFT)
                {
                    laserShotSound.play(0.2f);

                    EnemyBullet enemyBullet = new EnemyBullet(this, eggTurrets.get(i).pos.x, eggTurrets.get(i).pos.y + 0.6f, EggTurret.LEFT);
                    enemyBullets.add(enemyBullet);

                    eggTurrets.get(i).enemyShotDelay = 1.0f;
                }
                else if (eggTurrets.get(i).state == EggTurret.SHOOT && eggTurrets.get(i).dir == EggTurret.RIGHT)
                {
                    laserShotSound.play(0.2f);

                    EnemyBullet enemyBullet = new EnemyBullet(this, eggTurrets.get(i).pos.x, eggTurrets.get(i).pos.y + 0.6f, EggTurret.RIGHT);
                    enemyBullets.add(enemyBullet);

                    eggTurrets.get(i).enemyShotDelay = 1.0f;
                }

            }
            else
            {
                eggTurrets.get(i).enemyShotDelay -= deltaTime;
            }
        }

        /*//Update coins
        for( int i = 0; i < coins.size; i++)
        {
            if(alita.bounds.overlaps(coins.get(i).bounds))
            {
                if(coins.get(i).active && !coins.get(i).grabbing)
                {
                    coins.get(i).grabbing = true;
                    alita.heldCoins++;
                    coinSound.play(1.0f);
                }
            }
        }

        //Check for spike collision
        for( int i = 0; i < spikes.size; i++)
        {
                //Spike kills
                if (spikes.get(i).active)
                {
                    if (alita.bounds.overlaps(spikes.get(i).bounds))
                    {
                        if (alita.state != Player.DYING)
                        {
                            deathSound.play(0.3f);
                            alita.health=0;
                            alita.lives--;
                            alita.vel.y = 8;

                            coinReset();

                            alita.state = Player.DYING;
                            alita.stateTime = 0;
                        }
                    }
                }
        }



        //Check for slime death
        for( int i = 0; i < slimes.size; i++)
        {
            if(alita.state != Alita.DYING)
            {
                if (alita.hitBox.overlaps(slimes.get(i).bounds))
                {
                    if (slimes.get(i).active)
                    {
                        popSound.play(0.3f);
                        slimes.get(i).active = false;
                        alita.vel.y = 7;
                    }
                }

                //If slime kills
                if (slimes.get(i).active)
                {
                    if (alita.bounds.overlaps(slimes.get(i).bounds) && alita.health == 1)
                    {
                        if (alita.state != Alita.DYING)
                        {
                            deathSound.play(0.3f);
                            alita.health--;
                            alita.lives--;
                            alita.vel.y = 9;
                            alita.vel.x = -(alita.vel.x+3);

                            coinReset();

                            alita.state = Alita.DYING;
                            alita.stateTime = 0;
                        }
                    }
                    else if(alita.bounds.overlaps(slimes.get(i).bounds) && alita.health > 1)
                    {
                        deathSound.play(0.3f);

                        alita.health--;
                        alita.vel.y = 9;
                        alita.vel.x = -(alita.vel.x+3);
                    }
                }
            }
        }

        //Check for grunt death
        for( int i = 0; i < grunts.size; i++)
        {
            if(alita.state != Alita.DYING)
            {
                if (alita.hitBox.overlaps(grunts.get(i).bounds))
                {
                    if (grunts.get(i).active)
                    {
                        popSound.play(0.3f);
                        grunts.get(i).active = false;
                        alita.vel.y = 7;
                    }
                }

                //Grunt kills
                if (grunts.get(i).active)
                {
                    if (alita.bounds.overlaps(grunts.get(i).bounds) && alita.health == 1)
                    {
                        if (alita.state != Alita.DYING)
                        {
                            deathSound.play(0.3f);
                            alita.health--;
                            alita.lives--;
                            alita.vel.y = 9;
                            alita.vel.x = -(alita.vel.x+3);

                            coinReset();

                            alita.state = Alita.DYING;
                            alita.stateTime = 0;
                        }
                    }
                    else if(alita.bounds.overlaps(grunts.get(i).bounds) && alita.health > 1)
                    {
                        deathSound.play(0.3f);

                        alita.health--;
                        alita.vel.y = 9;
                        alita.vel.x = -(alita.vel.x+3);
                    }
                }
            }
        }


        //Check for Cactie death
        for( int i = 0; i < cacties.size; i++)
        {
            if(alita.state != Alita.DYING)
            {
                if (alita.hitBox.overlaps(cacties.get(i).bounds))
                {
                    if (cacties.get(i).active && cacties.get(i).health == 1)
                    {
                        popSound.play(0.3f);
                        cacties.get(i).active = false;
                        alita.vel.y = 7;
                    }
                    else if (cacties.get(i).active && cacties.get(i).health > 1)
                    {
                        popSound.play(0.3f);
                        cacties.get(i).health--;
                        alita.vel.y = 12;
                    }
                }

                //Cactie kills
                /*if (cacties.get(i).active)
                {
                    if (alita.bounds.overlaps(cacties.get(i).bounds))
                    {
                        if (alita.state != Alita.DYING)
                        {
                            deathSound.play(0.3f);

                            coinReset();

                            alita.state = Alita.DYING;
                            alita.stateTime = 0;
                        }
                    }
                }
            }
        }

        //Check for tank death
        for( int i = 0; i < tanks.size; i++)
        {
            if(alita.state != Alita.DYING)
            {
                if (alita.hitBox.overlaps(tanks.get(i).bounds))
                {
                    if (tanks.get(i).active)
                    {
                        popSound.play(0.3f);

                        tanks.get(i).active = false;

                        alita.vel.y = 7;
                    }
                }

                //Tank kills
                if (alita.bounds.overlaps(tanks.get(i).bounds) && alita.health == 1)
                {
                    if (alita.state != Alita.DYING)
                    {
                        deathSound.play(0.3f);
                        alita.health--;
                        alita.lives--;
                        alita.vel.y = 9;
                        alita.vel.x = -(alita.vel.x+6);

                        coinReset();

                        alita.state = Alita.DYING;
                        alita.stateTime = 0;
                    }
                }
                else if(alita.bounds.overlaps(tanks.get(i).bounds) && alita.health > 1)
                {
                    deathSound.play(0.3f);

                    alita.health--;
                    alita.vel.y = 9;
                    alita.vel.x = -(alita.vel.x+3);
                }
            }
        }

        /*for( int i = 0; i < spikes.size; i++)
        {
            if(alita.bounds.overlaps(spikes.get(i).bounds))
            {
                alita.state = Alita.DYING;
            }
        }*/

        //for (int i = 0; i < rockets.size; i++)
        //{
           // Rocket rocket = rockets.get(i);
            //rocket.update(deltaTime);
       // }
        //
        // */

        if(!Pause)
        {

            for (int i = 0; i < bullets.size; i++)
            {
                Bullet bullet = bullets.get(i);
                bullet.update(deltaTime);
            }

            for (int i = 0; i < powerUps.size; i++)
            {
                PowerUp powerUp = powerUps.get(i);
                powerUp.update(deltaTime);
            }

            for (int i = 0; i < eggTurrets.size; i++)
            {
                EggTurret eggTurret = eggTurrets.get(i);
                eggTurret.update(deltaTime);
            }

            for (int i = 0; i < enemyBullets.size; i++)
            {
                EnemyBullet enemyBullet = enemyBullets.get(i);
                enemyBullet.update(deltaTime);
            }

            for (int i = 0; i < drones.size; i++)
            {
                Drone drone = drones.get(i);
                drone.update(deltaTime);
            }

            for (int i = 0; i < props.size; i++)
            {
                Prop prop = props.get(i);
                prop.update(deltaTime);
            }



            /*for (int i = 0; i < tanks.size; i++)
            {
                if (tanks.get(i).active)
                {
                    Tank tank = tanks.get(i);
                    tank.update(deltaTime);
                }
            }*/
        }
        //for (int i = 0; i < lasers.size; i++)
       // {
        //    lasers.get(i).update();
       // }
    }




    /*public void coinReset()
    {
        for(int j = 0; j < coins.size; j++)
        {
            coins.get(j).active = true;
        }
    }

    public boolean isDeadly (int tileId)
    {
        return tileId == SPIKES || tileId == SLIMES || tileId == GRUNTS;
    }*/
}
