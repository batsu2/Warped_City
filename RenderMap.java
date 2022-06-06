package com.warpedcity.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector3;
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

public class RenderMap extends Map
{
    Map map;
    OrthographicCamera cam;
    SpriteCache cache;
    SpriteBatch batch = new SpriteBatch(5460);
    int[][] blocks;
    TextureRegion tile;
    TextureRegion tileTop;
    TextureRegion tileBrick;


    Music music;

    Animation<TextureRegion> playerLeft;
    Animation<TextureRegion> playerRight;
    Animation<TextureRegion> playerJumpLeft;
    Animation<TextureRegion> playerJumpRight;
    Animation<TextureRegion> playerBackJumpLeft;
    Animation<TextureRegion> playerBackJumpRight;
    Animation<TextureRegion> playerJumpShootLeft;
    Animation<TextureRegion> playerJumpShootRight;
    Animation<TextureRegion> playerIdleLeft;
    Animation<TextureRegion> playerIdleRight;
    Animation<TextureRegion> playerShootLeft;
    Animation<TextureRegion> playerShootRight;
    Animation<TextureRegion> playerRunShootLeft;
    Animation<TextureRegion> playerRunShootRight;
    Animation<TextureRegion> playerWalkLeft;
    Animation<TextureRegion> playerWalkRight;
    Animation<TextureRegion> playerCrouchLeft;
    Animation<TextureRegion> playerCrouchRight;
    Animation<TextureRegion> playerClimb;
    Animation<TextureRegion> playerHurtLeft;
    Animation<TextureRegion> playerHurtRight;
    Animation<TextureRegion> droneAnim;
    Animation<TextureRegion> droneReverseAnim;
    Animation<TextureRegion> eggTurretIdleLeftAnim;
    Animation<TextureRegion> eggTurretIdleRightAnim;
    Animation<TextureRegion> eggTurretShootLeftAnim;
    Animation<TextureRegion> eggTurretShootRightAnim;
    Animation<TextureRegion> explosionAnim;
    TextureRegion dispenser;
    Animation<TextureRegion> spawn;
    Animation<TextureRegion> dying;
    Animation<TextureRegion> enemyShotAnim;
    Animation<TextureRegion> shotLeft;
    Animation<TextureRegion> shotRight;
    Animation<TextureRegion> shotHitLeft;
    Animation<TextureRegion> shotHitRight;
    Animation<TextureRegion> powerUpAnim;

    Animation<TextureRegion> bigBannerAnim;
    Animation<TextureRegion> boxBannerAnim;
    Animation<TextureRegion> bigBannerMonitorAnim;
    Animation<TextureRegion> monitorAnim;
    Animation<TextureRegion> cokeSignAnim;
    Animation<TextureRegion> sushiSignAnim;
    Animation<TextureRegion> sideSignAnim;
    Animation<TextureRegion> scrollSignAnim;
    Animation<TextureRegion> neonBannerAnim;
    
    TextureRegion building;
    FPSLogger fps = new FPSLogger();




    public RenderMap (Map map)
    {
        this.map = map;

        //Set up camera zoom
        this.cam = new OrthographicCamera(24, 13);
        this.cam.position.set(map.alita.pos.x, map.alita.pos.y + 4.5f, 0);

        System.out.println(map.alita.pos.x);
        System.out.println(map.alita.pos.y);


        this.cache = new SpriteCache(this.map.tiles.length * this.map.tiles[0].length, false);
        this.blocks = new int[(int)Math.ceil(this.map.tiles.length / 24.0f)][(int)Math.ceil(this.map.tiles[0].length / 16.0f)];

        //Start Music
        music = Gdx.audio.newMusic(Gdx.files.internal("data/city-ambiance.wav"));
        music.play();
        music.setLooping(true);
        music.setVolume(0.8f);

        createAnimations();

    }




    private void createAnimations ()
    {
        this.tile = new TextureRegion(new Texture(Gdx.files.internal("data/tileset.png")), 336, 16, 16, 16);

        this.tileTop = new TextureRegion(new Texture(Gdx.files.internal("data/tileset.png")), 336, 16, 16, 16);
        this.tileBrick = new TextureRegion(new Texture(Gdx.files.internal("data/tileset.png")), 336, 16, 16, 16);

        this.building = new TextureRegion(new Texture(Gdx.files.internal("data/warpedCityBuilding2.png")), 23, 10, 335, 460);

        //Grab main character sprites

        Texture alitaTexture = new Texture(Gdx.files.internal("data/arcade_platformerV2.png"));

        //Read in sprite sheets
        Texture playerTexture = new Texture(Gdx.files.internal("player/idle/all_idle.png"));
        Texture runTexture = new Texture(Gdx.files.internal("player/run/all_run.png"));
        Texture jumpTexture = new Texture(Gdx.files.internal("player/jump/all_jump.png"));
        Texture backJumpTexture = new Texture(Gdx.files.internal("player/back-jump/all_back-jump.png"));
        Texture shootTexture = new Texture(Gdx.files.internal("player/shoot/shoot.png"));
        Texture runShootTexture = new Texture(Gdx.files.internal("player/run-shoot/all_run_shoot.png"));
        Texture walkTexture = new Texture(Gdx.files.internal("player/walk/all_walk.png"));
        Texture crouchTexture = new Texture(Gdx.files.internal("player/crouch/crouch.png"));
        Texture climbTexture = new Texture(Gdx.files.internal("player/climb/all_climb.png"));
        Texture hurtTexture = new Texture(Gdx.files.internal("player/hurt/hurt.png"));
        Texture shotTexture = new Texture(Gdx.files.internal("shot/all_shot.png"));
        Texture shotHitTexture = new Texture(Gdx.files.internal("shot-hit/all_shot_hit.png"));
        Texture powerUp = new Texture (Gdx.files.internal("data/power-up.png"));
        Texture droneTexture = new Texture (Gdx.files.internal("drone/all_drone.png"));
        Texture eggTurretIdleTexture = new Texture (Gdx.files.internal("egg-turret/Idle/all_idle.png"));
        Texture eggTurretShootTexture = new Texture (Gdx.files.internal("egg-turret/Shoot/all_shoot.png"));
        Texture enemyBulletTexture = new Texture(Gdx.files.internal("enemy-shoot/all_enemy-shoot.png"));
        Texture explosionTexture = new Texture (Gdx.files.internal("explosion/all_explosion.png"));
        Texture bigBannerTexture = new Texture (Gdx.files.internal("props/banner-big/all-banner-big.png"));
        Texture monitorTexture = new Texture (Gdx.files.internal("props/monitorface/all_monitor-face.png"));
        Texture cokeSignTexture = new Texture (Gdx.files.internal("props/banner-coke/all_coke-sign.png"));
        Texture sushiSignTexture = new Texture (Gdx.files.internal("props/banner-sushi/all_banner-sushi.png"));
        Texture neonBannerTexture = new Texture (Gdx.files.internal("props/banner-neon/all_banner-neon.png"));
        Texture sideSignTexture = new Texture (Gdx.files.internal("props/banner-side/all_banner-side.png"));
        Texture scrollSignTexture = new Texture (Gdx.files.internal("props/banner-scroll/all_banner-scroll.png"));
        Texture bigBannerMonitorTexture = new Texture (Gdx.files.internal("props/big-banner-monitor/all_big-banner-monitor.png"));
        Texture boxBannerTexture = new Texture (Gdx.files.internal("props/box-banner/all_box-banner.png"));


        //Setting up Splits
        
        //Player
        TextureRegion[] split1 = new TextureRegion(playerTexture).split(71, 67)[0];
        TextureRegion[] mirror1 = new TextureRegion(playerTexture).split(71, 67)[0];

        TextureRegion[] splitRun = new TextureRegion(runTexture).split(71, 67)[0];
        TextureRegion[] mirrorRun = new TextureRegion(runTexture).split(71, 67)[0];

        TextureRegion[] splitWalk = new TextureRegion(walkTexture).split(71, 67)[0];
        TextureRegion[] mirrorWalk = new TextureRegion(walkTexture).split(71, 67)[0];

        TextureRegion[] splitJump = new TextureRegion(jumpTexture).split(71, 67)[0];
        TextureRegion[] mirrorJump = new TextureRegion(jumpTexture).split(71, 67)[0];

        TextureRegion[] splitBackJump = new TextureRegion(backJumpTexture).split(71, 67)[0];
        TextureRegion[] mirrorBackJump = new TextureRegion(backJumpTexture).split(71, 67)[0];

        TextureRegion[] splitRunShoot = new TextureRegion(runShootTexture).split(71, 67)[0];
        TextureRegion[] mirrorRunShoot = new TextureRegion(runShootTexture).split(71, 67)[0];

        TextureRegion[] splitShoot = new TextureRegion(shootTexture).split(71, 67)[0];
        TextureRegion[] mirrorShoot = new TextureRegion(shootTexture).split(71, 67)[0];

        TextureRegion[] splitCrouch = new TextureRegion(crouchTexture).split(71, 67)[0];
        TextureRegion[] mirrorCrouch = new TextureRegion(crouchTexture).split(71, 67)[0];

        TextureRegion[] splitClimb = new TextureRegion(climbTexture).split(71, 67)[0];

        TextureRegion[] splitHurt = new TextureRegion(hurtTexture).split(71,67)[0];
        TextureRegion[] mirrorHurt = new TextureRegion(hurtTexture).split(71,67)[0];

        TextureRegion[] splitShot = new TextureRegion(shotTexture).split(15, 11)[0];
        TextureRegion[] mirrorShot = new TextureRegion(shotTexture).split(15, 11)[0];

        TextureRegion[] splitShotHit = new TextureRegion(shotHitTexture).split(15, 11)[0];
        TextureRegion[] mirrorShotHit = new TextureRegion(shotHitTexture).split(15, 11)[0];

        //Enemies
        TextureRegion[] splitDrone = new TextureRegion(droneTexture).split(55, 52)[0];
        TextureRegion[] mirrorDrone = new TextureRegion(droneTexture).split(55, 52)[0];

        TextureRegion[] splitEggTurretIdle = new TextureRegion(eggTurretIdleTexture).split(44, 62)[0];
        TextureRegion[] mirrorEggTurretIdle = new TextureRegion(eggTurretIdleTexture).split(44, 62)[0];

        TextureRegion[] splitEggTurretShoot = new TextureRegion(eggTurretShootTexture).split(44, 62)[0];
        TextureRegion[] mirrorEggTurretShoot = new TextureRegion(eggTurretShootTexture).split(44, 62)[0];

        TextureRegion[] splitEnemyBullet = new TextureRegion(enemyBulletTexture).split(12,12)[0];

        //Effects & Items
        TextureRegion[] splitExplosion = new TextureRegion(explosionTexture).split(55, 52)[0];

        TextureRegion[] splitPowerUp = new TextureRegion(powerUp).split(23, 21)[0];

        //Props
        TextureRegion[] splitBigBanner = new TextureRegion(bigBannerTexture).split(35, 92)[0];
        TextureRegion[] splitMonitor = new TextureRegion(monitorTexture).split(21, 18)[0];
        TextureRegion[] splitCokeSign = new TextureRegion(cokeSignTexture).split(27, 78)[0];
        TextureRegion[] splitNeonBanner = new TextureRegion(neonBannerTexture).split(19, 48)[0];
        TextureRegion[] splitSushiBanner = new TextureRegion(sushiSignTexture).split(36, 13)[0];
        TextureRegion[] splitSideSign = new TextureRegion(sideSignTexture).split(19, 76)[0];
        TextureRegion[] splitScrollSign = new TextureRegion(scrollSignTexture).split(13, 47)[0];
        TextureRegion[] splitBigBannerMonitor = new TextureRegion(bigBannerMonitorTexture).split(90, 60)[0];
        TextureRegion[] splitBoxBanner = new TextureRegion(boxBannerTexture).split(54, 65)[0];





        //Flip IT!

        for (TextureRegion region : mirror1)
            region.flip(true, false);

        for (TextureRegion region : mirrorJump)
            region.flip(true, false);

        for (TextureRegion region : mirrorRun)
            region.flip(true, false);

        for (TextureRegion region : mirrorRunShoot)
            region.flip(true, false);

        for (TextureRegion region : mirrorShoot)
            region.flip(true, false);

        for (TextureRegion region : mirrorWalk)
            region.flip(true, false);

        for (TextureRegion region : mirrorCrouch)
            region.flip(true, false);

        for (TextureRegion region : mirrorHurt)
            region.flip(true, false);

        for (TextureRegion region : mirrorShot)
            region.flip(true, false);

        for (TextureRegion region : mirrorShotHit)
            region.flip(true, false);

        for (TextureRegion region : mirrorBackJump)
            region.flip(true, false);

        for (TextureRegion region : mirrorDrone)
            region.flip(false, true);

        for (TextureRegion region : mirrorEggTurretIdle)
            region.flip(true, false);

        for (TextureRegion region : mirrorEggTurretShoot)
            region.flip(true, false);




        //Set animation frames using splits

        //Player
        playerIdleRight = new Animation(0.1f, split1[0], split1[1], split1[2], split1[3]);
        playerIdleLeft = new Animation(0.1f, mirror1[0], mirror1[1], mirror1[2], mirror1[3]);

        playerJumpRight = new Animation(0.2f, splitJump[0], splitJump[1], splitJump[2], splitJump[3], splitJump[3]);
        playerJumpLeft = new Animation(0.2f, mirrorJump[0], mirrorJump[1], mirrorJump[2], mirrorJump[3], mirrorJump[3]);

        playerBackJumpRight = new Animation(0.1f, splitBackJump[1], splitBackJump[2], splitBackJump[3], splitBackJump[4]);
        playerBackJumpLeft = new Animation(0.1f, mirrorBackJump[1], mirrorBackJump[2], mirrorBackJump[3], mirrorBackJump[4]);


        playerJumpShootRight = new Animation(0.1f, splitJump[3], splitJump[3]);
        playerJumpShootLeft = new Animation(0.1f, mirrorJump[3], mirrorJump[3]);

        playerRight = new Animation(0.1f, splitRun[0], splitRun[1], splitRun[2], splitRun[3], splitRun[4], splitRun[5],
                splitRun[6], splitRun[7]);
        playerLeft = new Animation(0.1f, mirrorRun[0], mirrorRun[1], mirrorRun[2], mirrorRun[3], mirrorRun[4], mirrorRun[5],
                mirrorRun[6], mirrorRun[7]);

        playerRunShootRight = new Animation(0.1f, splitRunShoot[0], splitRunShoot[1], splitRunShoot[2], splitRunShoot[3], splitRunShoot[4],
                splitRunShoot[5], splitRunShoot[6], splitRunShoot[7]);
        playerRunShootLeft = new Animation(0.1f, mirrorRunShoot[0], mirrorRunShoot[1], mirrorRunShoot[2], mirrorRunShoot[3], mirrorRunShoot[4],
                mirrorRunShoot[5], mirrorRunShoot[6], mirrorRunShoot[7]);

        playerWalkRight = new Animation(0.1f, splitWalk[0], splitWalk[1], splitWalk[2], splitWalk[3], splitWalk[4],
                splitWalk[5], splitWalk[6], splitWalk[7], splitWalk[8], splitWalk[9], splitWalk[10], splitWalk[11], splitWalk[12],
                splitWalk[13], splitWalk[14], splitWalk[15]);
        playerWalkLeft = new Animation(0.1f, mirrorWalk[0], mirrorWalk[1], mirrorWalk[2], mirrorWalk[3], mirrorWalk[4],
                mirrorWalk[5], mirrorWalk[6], mirrorWalk[7], mirrorWalk[8], mirrorWalk[9], mirrorWalk[10], mirrorWalk[11], mirrorWalk[12],
                mirrorWalk[13], mirrorWalk[14], mirrorWalk[15]);

        playerClimb = new Animation(0.1f, splitClimb[0], splitClimb[1], splitClimb[2],
                splitClimb[3], splitClimb[4], splitClimb[5]);

        playerHurtRight = new Animation(0.3f, splitHurt[0]);
        playerHurtLeft = new Animation(0.3f, mirrorHurt[0]);

        playerShootRight = new Animation(0.1f, splitShoot[0], splitShoot[0], splitShoot[0], splitShoot[0] );
        playerShootLeft = new Animation(0.1f, mirrorShoot[0], mirrorShoot[0], mirrorShoot[0], mirrorShoot[0] );

        playerCrouchRight = new Animation(0.1f, splitCrouch[0], splitCrouch[0], splitCrouch[0], splitCrouch[0]);
        playerCrouchLeft = new Animation(0.1f, mirrorCrouch[0], mirrorCrouch[0], mirrorCrouch[0], mirrorCrouch[0]);

        shotRight = new Animation(0.1f, splitShot[0], splitShot[1], splitShot[2]);
        shotLeft = new Animation(0.1f, mirrorShot[0], mirrorShot[1], mirrorShot[2]);

        shotHitRight = new Animation(0.1f, splitShotHit[0], splitShotHit[1], splitShotHit[2], splitShotHit[3]);
        shotHitLeft = new Animation(0.1f, mirrorShotHit[0], mirrorShotHit[1], mirrorShotHit[2], mirrorShotHit[3]);


        //Enemies
        enemyShotAnim = new Animation(0.1f, splitEnemyBullet[0], splitEnemyBullet[1], splitEnemyBullet[2]);

        droneAnim = new Animation(0.1f, splitDrone[0], splitDrone[1], splitDrone[2], splitDrone[3], splitDrone[4]);
        droneReverseAnim = new Animation(0.1f, mirrorDrone[0], mirrorDrone[1], mirrorDrone[2], mirrorDrone[3], mirrorDrone[4]);

        eggTurretIdleLeftAnim = new Animation(0.1f, splitEggTurretIdle[0], splitEggTurretIdle[1]);
        eggTurretIdleRightAnim = new Animation(0.1f, mirrorEggTurretIdle[0], mirrorEggTurretIdle[1]);

        eggTurretShootLeftAnim = new Animation(0.1f, splitEggTurretShoot[0], splitEggTurretShoot[1], splitEggTurretShoot[2], splitEggTurretShoot[3]);
        eggTurretShootRightAnim = new Animation(0.1f, mirrorEggTurretShoot[0], mirrorEggTurretShoot[1], mirrorEggTurretShoot[2], mirrorEggTurretShoot[3]);


        //Effects & Items
        explosionAnim = new Animation(0.1f, splitExplosion[0], splitExplosion[1], splitExplosion[2],
                splitExplosion[3], splitExplosion[4], splitExplosion[5], splitExplosion[6]);

        powerUpAnim = new Animation(0.1f, splitPowerUp[0], splitPowerUp[1], splitPowerUp[2], splitPowerUp[3],
                                     splitPowerUp[4], splitPowerUp[5], splitPowerUp[6]);

        //Props
        bigBannerAnim = new Animation(0.1f, splitBigBanner[0], splitBigBanner[1], splitBigBanner[2], splitBigBanner[3]);
        neonBannerAnim = new Animation(0.1f, splitNeonBanner[0], splitNeonBanner[1], splitNeonBanner[2], splitNeonBanner[3]);
        scrollSignAnim = new Animation(0.1f, splitScrollSign[0], splitScrollSign[1], splitScrollSign[2], splitScrollSign[3]);
        monitorAnim = new Animation(0.1f, splitMonitor[0], splitMonitor[1], splitMonitor[2], splitMonitor[3], splitMonitor[4]);
        sushiSignAnim = new Animation(0.1f, splitSushiBanner[0], splitSushiBanner[1], splitSushiBanner[2]);
        cokeSignAnim = new Animation(0.1f, splitCokeSign[0], splitCokeSign[1], splitCokeSign[2],
                splitCokeSign[3], splitCokeSign[4], splitCokeSign[5], splitCokeSign[6]);
        sideSignAnim = new Animation(0.1f, splitSideSign[0], splitSideSign[1], splitSideSign[2], splitSideSign[3]);
        bigBannerMonitorAnim = new Animation(0.1f, splitBigBannerMonitor[0], splitBigBannerMonitor[1], splitBigBannerMonitor[2], splitBigBannerMonitor[3]);
        boxBannerAnim = new Animation(0.1f, splitBoxBanner[0], splitBoxBanner[1], splitBoxBanner[2], splitBoxBanner[3]);

    }




    float stateTime = 0;
    float bjAnimTime = 0.0f;
    Vector3 lerpTarget = new Vector3();



    public void render (float deltaTime)
    {
        //Set camera boundaries (on map)
        if(map.alita.pos.x >= 13 && map.alita.pos.x <= 130)
            cam.position.lerp(lerpTarget.set(map.alita.pos.x + 1.0f, map.alita.pos.y + 4.5f, 0), 8f * deltaTime);
        else if(map.alita.pos.x > 130)
            cam.position.lerp(lerpTarget.set(130, map.alita.pos.y + 4.5f, 0), 8f * deltaTime);
        else
            cam.position.lerp(lerpTarget.set(13.0f, map.alita.pos.y + 4.5f, 0), 8f * deltaTime);


        cam.update();

        cache.setProjectionMatrix(cam.combined);
        Gdx.gl.glDisable(GL20.GL_BLEND);
        cache.begin();
        int b = 0;

        for (int blockY = 0; blockY < 4; blockY++)
        {
            for (int blockX = 0; blockX < 6; blockX++)
            {
                cache.draw(blocks[blockX][blockY]);
                b++;
            }
        }

        cache.end();
        stateTime += deltaTime;
        batch.setProjectionMatrix(cam.combined);
        batch.begin();


        //Render Assets
        renderBuilding();
        renderProps();
        renderDrone();
        renderEggTurret();
        renderEnemyBullet();
        renderPlayer();
        renderBullet();
        batch.end();

        fps.log();
    }



    private void renderPlayer ()
    {
        Animation<TextureRegion> anim = null;
        boolean loop = true;


        switch (Player.state)
        {
            case Player.WALK:
                if (map.alita.dir == Player.LEFT)
                    anim = playerWalkLeft;
                else
                    anim = playerWalkRight;

                break;
            case Player.RUN:
                if (map.alita.dir == Player.LEFT)
                    anim = playerLeft;
                else
                    anim = playerRight;

                break;
            case Player.RUN_SHOOT:
                if (map.alita.dir == Player.LEFT)
                    anim = playerRunShootLeft;
                else
                    anim = playerRunShootRight;

                break;
            case Player.SHOOT:
                if ( map.alita.dir == Player.LEFT)
                    anim = playerShootLeft;
                else
                    anim = playerShootRight;

                break;
            case Player.CROUCH:
                if ( map.alita.dir == Player.LEFT)
                    anim = playerCrouchLeft;
                else
                    anim = playerCrouchRight;

                break;
            case Player.CROUCH_SHOOT:
                if ( map.alita.dir == Player.LEFT)
                    anim = playerCrouchLeft;
                else
                    anim = playerCrouchRight;

                break;
            case Player.IDLE:
                if (map.alita.dir == Player.LEFT)
                    anim = playerIdleLeft;
                else
                    anim = playerIdleRight;

                break;


            case Player.JUMP:
                if (map.alita.dir == Player.LEFT && !playerBackJumpLeft.isAnimationFinished(stateTime))
                {
                    anim = playerJumpLeft;
                    loop=false;
                }
                else if(map.alita.dir == Player.RIGHT && !playerBackJumpRight.isAnimationFinished(stateTime))
                {
                    anim = playerJumpRight;
                    loop=false;
                }
                else if (map.alita.dir == Player.LEFT)
                {
                    anim = playerJumpLeft;
                    loop=true;
                }
                else if(map.alita.dir == Player.RIGHT)
                {
                    anim = playerJumpRight;
                    loop=true;
                }

                break;
            case Player.BACK_JUMP:

                if(map.alita.dir == Player.RIGHT)
                {
                    anim = playerBackJumpRight;
                    loop=false;
                }
                else if(map.alita.dir == Player.LEFT)
                {
                    anim = playerBackJumpLeft;
                    loop=false;
                }

                bjAnimTime += 0.01f;

                break;


            case Player.JUMP_SHOOT:
                if (map.alita.dir == Player.LEFT)
                    anim = playerJumpShootLeft;
                else
                    anim = playerJumpShootRight;

                loop=false;
                break;
            case Player.CLIMB:
                anim = playerClimb;
                loop=true;

                break;
            case Player.SPAWN:
                anim = spawn;
                loop = false;

                break;
            case Player.DYING:
                if (map.alita.dir == Player.LEFT)
                    anim = playerHurtLeft;
                else
                    anim = playerHurtRight;


                loop = false;

                break;
        }



        if(Player.state == Player.BACK_JUMP)
        {
            batch.draw(anim.getKeyFrame(bjAnimTime, loop), map.alita.pos.x, map.alita.pos.y, 3, 3);

        }
        else
        {
            batch.draw(anim.getKeyFrame(stateTime, loop), map.alita.pos.x, map.alita.pos.y, 3, 3);
            bjAnimTime = 0.0f;
        }
    }


    private void renderBullet()
    {
        for (int i = 0; i < map.bullets.size; i++)
        {
            Bullet bullet = map.bullets.get(i);


            if (bullet.state == Bullet.FLYING)
            {
                TextureRegion frame;

                if (map.alita.dir == Player.LEFT)
                    frame = this.shotRight.getKeyFrame(bullet.stateTime, true);
                else
                    frame = this.shotRight.getKeyFrame(bullet.stateTime, true);


                batch.draw(frame, bullet.pos.x, bullet.pos.y, 0.5f, 0.5f, 1, 1, 0.5f, 0.5f, bullet.vel.angle());
            }
            else if (bullet.state != Bullet.DEAD)
            {
                TextureRegion frame;

                if (map.alita.dir == Player.LEFT)
                    frame = this.shotHitLeft.getKeyFrame(bullet.stateTime, false);
                else
                    frame = this.shotHitRight.getKeyFrame(bullet.stateTime, false);


                batch.draw(frame, bullet.pos.x, bullet.pos.y, 1, 1);
            }
        }
    }






    private void renderEggTurret()
    {
        for (int i = 0; i < map.eggTurrets.size; i++)
        {
            if(map.eggTurrets.get(i).active)
            {
                EggTurret eggTurret = map.eggTurrets.get(i);

                if (eggTurret.state == EggTurret.IDLE && eggTurret.dir == EggTurret.LEFT)
                {
                    TextureRegion frame = this.eggTurretIdleLeftAnim.getKeyFrame(stateTime, true);

                    batch.draw(frame, eggTurret.pos.x, eggTurret.pos.y, 2, 2.5f);
                }
                else if (eggTurret.state == EggTurret.IDLE && eggTurret.dir == EggTurret.RIGHT)
                {
                    TextureRegion frame = this.eggTurretIdleRightAnim.getKeyFrame(stateTime, true);

                    batch.draw(frame, eggTurret.pos.x, eggTurret.pos.y, 2, 2.5f);
                }
                else if (eggTurret.state == EggTurret.SHOOT && eggTurret.dir == EggTurret.LEFT)
                {
                    TextureRegion frame = this.eggTurretShootLeftAnim.getKeyFrame(stateTime, false);

                    batch.draw(frame, eggTurret.pos.x, eggTurret.pos.y, 2, 2.5f);
                }
                else if (eggTurret.state == EggTurret.SHOOT && eggTurret.dir == EggTurret.RIGHT)
                {
                    TextureRegion frame = this.eggTurretShootRightAnim.getKeyFrame(stateTime, false);

                    batch.draw(frame, eggTurret.pos.x, eggTurret.pos.y, 2, 2.5f);
                }
                else
                {
                    TextureRegion frame = this.explosionAnim.getKeyFrame(eggTurret.stateTime, false);
                    batch.draw(frame, eggTurret.pos.x, eggTurret.pos.y, 2, 2.5f);
                }
            }
        }
    }



    //Utilizes polymorphism inheritance to print all props
    private void renderProps()
    {
        for (int i = 0; i < map.props.size; i++)
        {
            Prop prop = map.props.get(i);
            TextureRegion frame = new TextureRegion();

            if(prop instanceof Monitor)
            {
                frame = this.monitorAnim.getKeyFrame(prop.stateTime, true);
            }
            else if(prop instanceof BannerBig)
            {
                frame = this.bigBannerAnim.getKeyFrame(prop.stateTime, true);
            }
            else if(prop instanceof BannerNeon)
            {
                frame = this.neonBannerAnim.getKeyFrame(prop.stateTime, true);
            }
            else if(prop instanceof BigBannerMonitor)
            {
                frame = this.bigBannerMonitorAnim.getKeyFrame(prop.stateTime, true);
            }
            else if(prop instanceof BoxBanner)
            {
                frame = this.boxBannerAnim.getKeyFrame(prop.stateTime, true);
            }
            else if(prop instanceof CokeSign)
            {
                frame = this.cokeSignAnim.getKeyFrame(prop.stateTime, true);
            }
            else if(prop instanceof ScrollSign)
            {
                frame = this.scrollSignAnim.getKeyFrame(prop.stateTime, true);
            }
            else if(prop instanceof SideSign)
            {
                frame = this.sideSignAnim.getKeyFrame(prop.stateTime, true);
            }
            else if(prop instanceof SushiSign)
            {
                frame = this.sushiSignAnim.getKeyFrame(prop.stateTime, true);
            }


            batch.draw(frame, prop.pos.x - 0.5f, prop.pos.y - 0.5f, prop.renderWidth, prop.renderHeight);
        }
    }




    public void dispose ()
    {
        cache.dispose();
        batch.dispose();
        tile.getTexture().dispose();
        tileTop.getTexture().dispose();
    }

}
