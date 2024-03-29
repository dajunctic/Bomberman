package uet.oop.bomberman.entities.enemy.special;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.enemy.Enemy;
import uet.oop.bomberman.entities.functional.Buff;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.explosive.Flame;
import uet.oop.bomberman.explosive.special.ShockWave;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.graphics.*;
import uet.oop.bomberman.music.Audio;
import uet.oop.bomberman.music.LargeSound;
import uet.oop.bomberman.music.Sound;
import static java.lang.Math.PI;
import static uet.oop.bomberman.game.Gameplay.*;
import static uet.oop.bomberman.others.Basic.inf;
public class Mage extends Enemy {
    private static SpriteSheet mage = new SpriteSheet("/sprites/enemy/Mage/move.png", 2);
    private static SpriteSheet mage_dead = new SpriteSheet("/sprites/enemy/Mage/dead.png", 12);
    public static SpriteSheet mage_staff = new SpriteSheet("/sprites/enemy/Mage/staff.png", 13);
    private final int damage = 10;
    private final long cooldown = 2000;
    private final long chargeTime = 2000;
    private long chargeBegin = 0;
    private long lastAttack = 0;
    private boolean charging = false;
    public Mage(double xPixel, double yPixel) {
        super(xPixel, yPixel);

    }
    public void load() {
        enemy = new Anim(mage, 50);
        killed = new DeadAnim(mage_dead, 10, 1);
        attack = new DeadAnim(mage_staff, 6, 1);
        setHP(800);
        sight_angle = PI / 3;
        sight_depth = 8;
        attackRange = Sprite.SCALED_SIZE * 3;
        margin = 0;
    }

    @Override
    public void update(Bomber player) {
        //Make appearance
        if(!appear.isDead()){
            appear.update();
            return;
        }
        //hp update
        super.update();
        //status
        if(!isDead){
            if(isAttacking) {
                attack.update();
                if(attack.isDead()) {
                    attack(player);
                }
            } else {
                if(!charging) move();
                else {
                    if(System.currentTimeMillis() - chargeBegin >= chargeTime) isAttacking = true;
                }
                if(player.vulnerable()) distance.set(player.getPosition().x - x, player.getPosition().y - y);
                    else distance.set(inf, inf);
                enemy.update();

            }

        }
        else killed.update();
        //search for player
        if(enemy.getTime() % frequency == 0) {
            search(player);
        }
    }

    /** Tracking player*/
    @Override
    protected void search(Bomber player) {

        if(!player.vulnerable()) return;
        if(System.currentTimeMillis() - lastAttack <= cooldown) return;
        Vertex line = new Vertex(player.getPosition().x - x, player.getPosition().y - y);
        if(Math.abs(direction.angle(line)) <= sight_angle
                &&  line.abs() <= sight_depth * Sprite.SCALED_SIZE && !charging){
            charging = true;
            chargeBegin = System.currentTimeMillis();
            sounds.add(new LargeSound(x, y, Audio.copy(Audio.nuke), chargeTime / 1000));
        }
    }

    @Override
    public void attack(Bomber player) {
        Vertex temp = new Vertex(new Vertex(0,0), distance);
        temp.normalize();
        if(distance.abs() <= attackRange) {
            sounds.add(new Sound(x, y, Audio.copy(Audio.nuke_explosion), -1, 5 *  Sprite.SCALED_SIZE));
            entities.add(new ShockWave(x, y, false, attackRange / (Sprite.SCALED_SIZE * 1.5), damage * 2, 0.5, false));
        }
            else {
                if(Math.round(Math.random()) >= 1) entities.add(new Flame(getCenterX(), getCenterY(),
                                                                    10 * Sprite.SCALED_SIZE,
                                                                            temp.getX(), temp.getY(),
                                                                        2, 1.5, damage,
                                                                        false, true ));
                // Tà đạo vl :)))
                else Gameplay.addEnemy(new Suicider(x, y));
        }
        isAttacking = false;
        charging = false;
        lastAttack = System.currentTimeMillis();
        attack.reset();
    }

    @Override
    public void render(GraphicsContext gc, Renderer renderer) {

        /* * Hiển thị máu */
        renderHP(gc, renderer);
        if(charging) gc.setEffect(effect);
        // If it is going backward
        renderer.renderImg(gc, this.getImg(), x + shiftX, y + shiftY, reversed);
        gc.setEffect(null);

        //the STAFF, the FUCKING STAFFFF
        if(!isDead) renderer.renderImg(gc, attack.getImage(), x + shiftX, y + shiftY, reversed);
    }

    @Override
    public boolean isExisted() {
        return !killed.isDead();
    }

    @Override
    public void deadAct(Gameplay gameplay) {
        if(Math.round(Math.random()) >= 1) {
            int i = (int) Math.max(0, Math.floor(getCenterX() / Sprite.SCALED_SIZE));
            int j = (int) Math.max(0, Math.floor(getCenterY() / Sprite.SCALED_SIZE));
            buffs.put(tileCode(i, j), new Buff(i, j, Buff.ITEM_STAFF));
        } else {
            //  RUN :)
            for(int i = 0; i < 4; i ++) gameplay.addEnemy(new Suicider(x, y));
        }
    }
}
