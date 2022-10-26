package uet.oop.bomberman.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Mobile;
import uet.oop.bomberman.game.BombermanGame;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.generals.Vertex;

import javax.swing.text.html.HTMLDocument;

import static uet.oop.bomberman.game.BombermanGame.FPS;
import static uet.oop.bomberman.game.BombermanGame.stackPane;

public class Renderer {
    //canvas overall
    //ratio
    private double offsetX;
    private double offsetY;
    //actual offset
    private double boundX = 0;
    private double boundY = 0;
    //display position in canvas
    private double shiftX;
    private double shiftY;
    //display position in map
    private double translateX;
    private double translateY;
    //size of display
    private double width;
    private double height;
    //camera movements
    private Vertex goal;
    private Vertex speed;
    private double interval = 0.5;
    private double margin = 50;
    private boolean stable = true;
    private double scale = 1;
    //window center
    public  double centerX = BombermanGame.WIDTH * Sprite.SCALED_SIZE / 2;
    public  double centerY = BombermanGame.HEIGHT * Sprite.SCALED_SIZE / 2;
    private Mobile pov = null;
    public Renderer(double offsetX, double offsetY, double shiftX, double shiftY, double translateX, double translateY, double width, double height, double scale) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.translateX = translateX;
        this.translateY = translateY;
        this.width = width;
        this.height = height;
        this.scale = scale;
        goal = new Vertex(translateX, translateY);
        speed = new Vertex(0,0);
        centerX = width / 2;
        centerY = height / 2;
    }

    public Renderer(double offsetX, double offsetY, double shiftX, double shiftY, double translateX, double translateY, double width, double height, double scale, Mobile pov) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.translateX = translateX;
        this.translateY = translateY;
        this.width = width;
        this.height = height;
        this.scale = scale;
        goal = new Vertex(translateX, translateY);
        speed = new Vertex(0,0);
        this.pov = pov;

    }
    //relocate

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    //

    public double getTranslateX() {
        return translateX;
    }

    public double getTranslateY() {
        return translateY;
    }

    //check on screen
    public boolean onScreen(double x, double y) {
        return (Math.abs(x - translateX - centerX) <= centerX + Sprite.SCALED_SIZE) && (Math.abs(y - translateY - centerY) <= centerY + Sprite.SCALED_SIZE);
    }
    //render images
    public void renderImg(GraphicsContext gc, Image img, double x, double y, boolean reverse) {
        if(!onScreen(x, y)) return;
        double renderX = boundX - translateX + shiftX + (x + (reverse ? img.getWidth(): 0)) * scale;
        double renderY = y * scale + boundY - translateY + shiftY;
        gc.drawImage(img, renderX, renderY,
                scale * img.getWidth() * (reverse ? -1 : 1), scale * img.getHeight());
    }
    public void renderDirectImg(GraphicsContext gc, Image img, double x, double y, boolean reverse) {
        double renderX = boundX - translateX + shiftX + (x + (reverse ? img.getWidth(): 0)) * scale;
        double renderY = y * scale + boundY - translateY + shiftY;
         gc.drawImage(img, renderX, renderY,
                scale * img.getWidth() * (reverse ? -1 : 1), scale * img.getHeight());
    }
    //move camera to somewhere
    public void setGoal(double x, double y) {
        if(Math.abs(translateX - goal.getX()) <= margin &&
                Math.abs(translateY - goal.getY()) <= margin) {
            return;
        }
        goal.set(x, y);
        speed.set((x-translateX) / (interval * FPS), (y - translateY) / (interval * FPS));
        stable = false;
    }
    //update camera positions
    public void update() {
        if(pov != null){
            Vertex trans = pov.translation(width, height);
            setTranslate(trans.getX(), trans.getY());
        }
        if(stable) return ;
        translateX += speed.getX();
        translateY += speed.getY();
        if(Math.abs(translateX - goal.getX()) <= margin &&
                Math.abs(translateY - goal.getY()) <= margin) {
                stable = true;
        }
    }
    //directly set camera positions
    public void setTranslate(double x, double y) {
        if(translateX == x && translateY == y) return;
//        System.out.println("Camera set to"+ " " + x + " " + y);
        translateX = x;
        translateY = y;
    }
    public void setOffSet(Renderer parent) {
        boundX = parent.boundX + parent.width * offsetX - width * scale / 2;
        boundY = parent.boundY + parent.height * offsetY - height * scale / 2;
    }

    public void setOffSet(Canvas canvas) {
        boundX = canvas.getWidth() * offsetX - width * scale /2;
        boundY = canvas.getHeight() * offsetY - height * scale /2;
    }
    public void setPov(Mobile pov){
        this.pov = pov;
    }
    public void setOffSet(double x, double y) {
        boundX = x;
        boundY = y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Mobile getPov() {
        return pov;
    }
}
