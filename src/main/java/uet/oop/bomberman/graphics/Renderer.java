package uet.oop.bomberman.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import uet.oop.bomberman.entities.Mobile;
import uet.oop.bomberman.game.BombermanGame;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.generals.Triplets;
import uet.oop.bomberman.generals.Vertex;

import java.util.ArrayList;

import static uet.oop.bomberman.game.BombermanGame.FPS;
import static uet.oop.bomberman.graphics.LightProbe.gradients;

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
     private Vertex span = new Vertex(1,1);
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

    /** Loại trừ ảnh ngoài khung hình*/
    //check on screen
    public boolean onScreen(double x, double y) {
        return (Math.abs(x - translateX - width / 2) <= width / 2 + Sprite.SCALED_SIZE * 2)
                && (Math.abs(y - translateY - height / 2) <= height / 2 + Sprite.SCALED_SIZE * 2);
    }
    //render images
    public boolean renderImg(GraphicsContext gc, Image img, double x, double y, boolean reverse) {
        if(!onScreen(x, y)) return false;
        double renderX = boundX - translateX + shiftX + x + (reverse ? img.getWidth(): 0);
        double renderY = y + boundY - translateY + shiftY;
        gc.drawImage(img, renderX, renderY,
                 img.getWidth() * (reverse ? -1 : 1),  img.getHeight());
        return true;
    }
    /** Render ảnh trực tiếp không qua loại trừ*/
    public void renderDirectImg(GraphicsContext gc, Image img, double x, double y, boolean reverse) {

        double renderX = boundX - translateX + shiftX + (x + (reverse ? img.getWidth(): 0)) * scale;
        double renderY = y * scale + boundY - translateY + shiftY;
         gc.drawImage(img, renderX, renderY,
                scale * img.getWidth() * (reverse ? -1 : 1), scale * img.getHeight());
    }
    //move camera to somewhere
    /** Cài đặt điểm đến của camera */
    public void setGoal(double x, double y) {
        if(Math.abs(translateX - goal.getX()) <= margin &&
                Math.abs(translateY - goal.getY()) <= margin) {
            return;
        }
        goal.set(x, y);
        speed.set((x-translateX) / (interval * FPS), (y - translateY) / (interval * FPS));
        stable = false;
    }

    /** Render ảnh */
    public void renderImg(GraphicsContext gc, Image img, double x, double y, boolean reverse, double scale) {
        if(!onScreen(x, y)) return;
        double offX = img.getWidth() * (1 - scale);
        double offY = img.getHeight() * (1 - scale);
        double renderX = boundX - translateX + shiftX + x + (reverse ? img.getWidth(): 0) * scale + offX * (reverse ? -1 : 1);
        double renderY = y + boundY - translateY + shiftY  + offY;
        gc.drawImage(img, renderX, renderY,
                scale * img.getWidth() * (reverse ? -1 : 1)
                    , scale * img.getHeight());
    }

    /** Render ảnh tại trung tâm */
    public void renderCenterImg(GraphicsContext gc, Image img, double x, double y, boolean reverse, double scale) {
        if(!onScreen(x, y)) return;
        double offX = img.getWidth() * scale / 2;
        double offY = img.getHeight() * scale / 2;
        double renderX = x + boundX - translateX + shiftX - offX * (reverse ? -1 : 1);
        double renderY = y + boundY - translateY + shiftY - offY;
        gc.drawImage(img, renderX, renderY,
                scale * img.getWidth() * (reverse ? -1 : 1)
                , scale * img.getHeight());
    }

    /** Render một layer*/
    public void renderLayer(GraphicsContext gc, Image img, Triplets details, boolean reverse) {
        double x = details.v1 * width;
        double y = details.v2 * height;
        double scale = details.v3 * width / img.getWidth();
        if(!onScreen(x, y)) return;
        if(!onScreen(x, y)) return;
        double offX = img.getWidth() * (1 - scale);
        double offY = img.getHeight() * (1 - scale);
        double renderX = boundX - translateX + shiftX + x + (reverse ? img.getWidth(): 0) * scale + offX * (reverse ? -1 : 1);
        double renderY = y + boundY - translateY + shiftY  + offY;
        gc.drawImage(img, renderX, renderY,
                scale * img.getWidth() * (reverse ? -1 : 1)
                , scale * img.getHeight());
    }

    //update camera positions
    /** Fitting vị trí cùng khung cha*/
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

    /** Thay đổi thông số lần lượt: vị trí, độ lệch so với gốc khung hình cha hoặc canvas tổng, zoom*/
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
        resize(canvas);
        boundX = canvas.getWidth() * offsetX - width * scale /2;
        boundY = canvas.getHeight() * offsetY - height * scale /2;
    }
    public void resize(Canvas canvas) {
        span.set(width, height);
        width = canvas.getWidth();
        height = canvas.getHeight();
        span.divide(width, height);
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

    public Vertex getSpan() {
        return span;
    }
    public Vertex getOrigin() {
        return new Vertex(boundX - translateX + shiftX, boundY - translateY + shiftY);
    }
    /** Vẽ đường thẳng*/
    public void drawTileLine(GraphicsContext gc, Vertex p0, Vertex p1) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        gc.setGlobalAlpha(1);
        double renderX = boundX - translateX + shiftX;
        double renderY = boundY - translateY + shiftY;
//        System.out.println(String.format("Line drawn from %.0f, %.0f to %.0f, %.0f", p0.x * Sprite.SCALED_SIZE + renderX, p0.y * Sprite.SCALED_SIZE + renderY,
//                                                                                    p1.x * Sprite.SCALED_SIZE + renderX, p1.y*Sprite.SCALED_SIZE + renderY));
        gc.strokeLine(p0.x * Sprite.SCALED_SIZE + renderX, p0.y * Sprite.SCALED_SIZE + renderY,
                        p1.x * Sprite.SCALED_SIZE + renderX, p1.y*Sprite.SCALED_SIZE + renderY);
        gc.setGlobalAlpha(1);
    }

    /** Vẽ polygon từ tập */
    public void drawPolygon(GraphicsContext gc, ArrayList<Vertex> vertices, Effect effect, double radius, double scale) {
        double renderX = boundX - translateX + shiftX;
        double renderY = boundY - translateY + shiftY;
        double[] x = new double[vertices.size()];
        double[] y = new double[vertices.size()];
        int i = 0;
        for(Vertex g : vertices) {
            if(g == null) continue;
            x[i] = pov.getCenterX() +  (g.x * Sprite.SCALED_SIZE - pov.getCenterX()) * scale + renderX;
            y[i] = pov.getCenterY() +  (g.y * Sprite.SCALED_SIZE - pov.getCenterY()) * scale + renderY;
            i++;
        }
        gc.setEffect(effect);
        gc.setFill(new RadialGradient(0, 0, pov.getCenterX() + renderX, pov.getCenterY() + renderY, radius * Sprite.SCALED_SIZE, false, CycleMethod.NO_CYCLE, gradients));
        gc.fillPolygon(x, y, i);
        gc.setEffect(null);
        gc.setFill(Color.BLACK);
    }
    /** Vẽ polygon */
    public void drawPolygon(GraphicsContext gc, double[] xPoints, double[] yPoints, int nPoints, Effect effect, double radius, double scale) {
        double renderX = boundX - translateX + shiftX;
        double renderY = boundY - translateY + shiftY;
        double[] x = new double[nPoints];
        double[] y = new double[nPoints];
        for(int i = 0;i < nPoints;i ++) {
            x[i] = pov.getCenterX() +  (xPoints[i] * Sprite.SCALED_SIZE - pov.getCenterX()) * scale + renderX;
            y[i] = pov.getCenterY() +  (yPoints[i] * Sprite.SCALED_SIZE - pov.getCenterY()) * scale + renderY;
            i++;
        }
        gc.setFill(new RadialGradient(0, 0, pov.getCenterX() + renderX, pov.getCenterY() + renderY, radius * Sprite.SCALED_SIZE, false, CycleMethod.NO_CYCLE, gradients));
        gc.fillPolygon(x, y, nPoints);
        gc.setFill(Color.BLACK);
    }
    /** Vẽ polygon từ tập điểm sau chuân hóa*/
    public void renderPolygonPreset(GraphicsContext gc, double[] x, double[] y, int nPoints, double radius, double scale) {
        double renderX = boundX - translateX + shiftX;
        double renderY = boundY - translateY + shiftY;
        gc.setFill(new RadialGradient(0, 0, pov.getCenterX() + renderX, pov.getCenterY() + renderY, radius * Sprite.SCALED_SIZE, false, CycleMethod.NO_CYCLE, gradients));
        gc.fillPolygon(x, y, nPoints);
        gc.setFill(Color.BLACK);
    }
    /** Vẽ tile trên màn hình */
    public void fillTile(GraphicsContext gc, Point tile, double w, double h, boolean isCenter) {
        if(!onScreen(tile.x * Sprite.SCALED_SIZE, tile.y * Sprite.SCALED_SIZE)) return;
        double renderX = boundX - translateX + shiftX + tile.x * Sprite.SCALED_SIZE - (isCenter ? w * Sprite.SCALED_SIZE / 2 : 0);
        double renderY = boundY - translateY + shiftY + tile.y * Sprite.SCALED_SIZE - (isCenter ? h * Sprite.SCALED_SIZE / 2: 0);
        gc.setFill(Color.WHITE);
        gc.fillRect(renderX, renderY, w * Sprite.SCALED_SIZE, h * Sprite.SCALED_SIZE);
        gc.setFill(Color.BLACK);
    }
}
