package com.example.petio.onlinebelote;


import android.service.wallpaper.WallpaperService;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.renderscript.Float2;
import android.view.MotionEvent;

import com.lfk.justweengine.Anim.FrameAnimation;
import com.lfk.justweengine.Anim.MoveAnimation;
import com.lfk.justweengine.Anim.VelocityAnimation;
import com.lfk.justweengine.Drawable.Sprite.BaseSub;
import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Engine.GameTextPrinter;
import com.lfk.justweengine.Engine.GameTexture;
import com.lfk.justweengine.Engine.GameTimer;
import com.lfk.justweengine.Info.UIdefaultData;
//import com.lfk.justweengine.Sprite.BaseSprite;
//import com.lfk.justweengine.Sprite.BaseSub;
//import com.lfk.justweengine.Sprite.FrameType;

import java.util.Random;

/**
 * Created by stoeff
 */

public class Game extends Engine {
    // Please init your var in constructor.

    GameTextPrinter printer;
    Paint paint;
    Canvas canvas;
    Random random;
    Bitmap backGround2X;
    Rect bg_rect;
    Point bg_scroll;

    public Game() {
        // If open debug mode. If you open debug mode, you can print log, frame number, and parse on screen.
        super(true);
        paint = new Paint();
        canvas = null;
        printer = new GameTextPrinter();
        printer.setTextColor(Color.BLACK);
        printer.setTextSize(24);
        printer.setLineSpaceing(28);
        random = new Random();

    }

    // load some UI parameters. And set screen direction, default background color, set screen's scan method.
    @Override
    public void init() {
        // init UI default par, you must use at here . Some var in UIdefaultData for more phones should be init.
        UIdefaultData.init(this);
        super.setScreenOrientation(ScreenMode.PORTRAIT);
    }

    // load sprite , background , picture and other BaseSub
    @Override
    public void load() {

        GameTexture tex = new GameTexture(this);
        if (!tex.loadFromAsset("background.jpg")) {
            fatalError("Error loading space");
        }
        backGround2X = Bitmap.createBitmap(
                UIdefaultData.screenWidth,
                UIdefaultData.screenHeight * 2,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(backGround2X);
        Rect dst = new Rect(0, 0, UIdefaultData.screenWidth - 1,
                UIdefaultData.screenHeight);
        canvas.drawBitmap(tex.getBitmap(), null, dst, null);
        dst = new Rect(0, UIdefaultData.screenHeight,
                UIdefaultData.screenWidth,
                UIdefaultData.screenHeight * 2);
        canvas.drawBitmap(tex.getBitmap(), null, dst, null);

        bg_rect = new Rect(0, 0, UIdefaultData.screenWidth, UIdefaultData.screenHeight);
        bg_scroll = new Point(0, 0);
    }


    // draw and update in a new Thread
    // update message and sprite's msg
    @Override
    public void draw() {
        Canvas canvas = getCanvas();
        GameTextPrinter printer = new GameTextPrinter(canvas);
        printer.drawText("Hello", 100, 100);

        GameTexture texture = new GameTexture(this);
        //king should be k as the json input
        texture.loadFromAsset("kinghearts.png");
        //texture.draw(canvas, 100, 100);

        canvas = super.getCanvas();
        canvas.drawBitmap(backGround2X, bg_rect, bg_rect, paint);
        printer.setCanvas(canvas);
        printer.drawText("Engine demo", 10, 20);
    }

    @Override
    public void update() {

    }

    // receive touch event , its function depend on screen's scan mode.
    @Override
    public void touch(MotionEvent event) {

    }

    // receive collision event , BaseSub is the father class of all the sprites and others.
    // use to solve collision event default use rect collision.
    @Override
    public void collision(BaseSub baseSub) {

    }
}
