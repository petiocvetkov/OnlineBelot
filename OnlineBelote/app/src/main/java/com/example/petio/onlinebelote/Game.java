package com.example.petio.onlinebelote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.renderscript.Float2;
import android.util.Log;
import android.view.MotionEvent;

import com.lfk.justweengine.Anim.FrameAnimation;
import com.lfk.justweengine.Anim.MoveAnimation;
import com.lfk.justweengine.Anim.VelocityAnimation;
import com.lfk.justweengine.Drawable.Button.TextureButton;
import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Engine.GameTextPrinter;
import com.lfk.justweengine.Engine.GameTexture;
import com.lfk.justweengine.Engine.GameTimer;
import com.lfk.justweengine.Info.UIdefaultData;
import com.lfk.justweengine.Drawable.Sprite.BaseSprite;
import com.lfk.justweengine.Drawable.Sprite.BaseSub;
import com.lfk.justweengine.Drawable.Sprite.FrameType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.Random;

public class Game extends Engine {
    GameTextPrinter printer;
    Paint paint;
    Canvas canvas;
    GameTimer timer;
    Bitmap backGround2X;
    float startX, startY, offsetX, offsetY;
    BaseSprite[] cards;
    BaseSprite[] playedCards;
    Random random;
    float screenWidth;
    float screenHeight;
    boolean played;
    String cardStrings[] ;//= new String[8];
    Objects object;

    public Game(String[] cardStrings) {
        super(false);
        paint = new Paint();
        canvas = null;
        printer = new GameTextPrinter();
        printer.setTextColor(Color.BLACK);
        printer.setTextSize(24);
        printer.setLineSpaceing(28);
        timer = new GameTimer();
        random = new Random();
        cards = new BaseSprite[8];
        playedCards = new BaseSprite[8];
        boolean played = false;
    }

    @Override
    public void init() {
        super.setScreenOrientation(ScreenMode.PORTRAIT);
        UIdefaultData.init(this);
        screenWidth = UIdefaultData.screenWidth;
        screenHeight = UIdefaultData.screenHeight;
    }
    @Override
    public void load() {
        // load ship
        for (int i = 0; i < cards.length; i++) {
            GameTexture current = new GameTexture(this);
            current.loadFromAsset("pic/"+ cardStrings[i] +".png");
            cards[i] = new BaseSprite(
                    this,
                    current.getBitmap().getWidth(),
                    current.getBitmap().getHeight(),
                    FrameType.SIMPLE);
            cards[i].setTexture(current);
            float customScale = screenWidth / (8 * (float)current.getBitmap().getWidth());
            cards[i].setScale(customScale);
            cards[i].setPosition(
                    i * current.getBitmap().getWidth() * customScale,
                    screenHeight - current.getBitmap().getHeight() * customScale);

            addToSpriteGroup(cards[i]);
        }

        GameTexture tex = new GameTexture(this);
        if (!tex.loadFromAsset("pic/background.jpg")) {
            fatalError("Error loading space");
        }
        backGround2X = Bitmap.createBitmap(
                UIdefaultData.screenWidth,
                UIdefaultData.screenHeight,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(backGround2X);
        Rect dst = new Rect(0, 0,
                UIdefaultData.screenWidth * 2,
                UIdefaultData.screenHeight * 2);
        canvas.drawBitmap(tex.getBitmap(), null, dst, null);
    }


    @Override
    public void draw() {
        canvas = super.getCanvas();
        canvas.drawBitmap(backGround2X,
                null,
                new Rect(0, 0, UIdefaultData.screenWidth, UIdefaultData.screenHeight),
                paint);
        printer.setCanvas(canvas);
    }

    @Override
    public void update() {
    }

    @Override
    public void touch(MotionEvent event) {
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].getBounds().contains(event.getX(), event.getY()) && played == false) {
                played = true;
                cards[i].setPosition(cards[i].getPosition().x, cards[i].getPosition().y / 2);
                break;
            }
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                startY = (int) event.getY();
                break;
        }
    }

    @Override
    public void collision(BaseSub baseSub) {
        Log.d("LOL", "colliding");
    }

    private void resetEvent(MotionEvent event) {
        startX = (int) event.getX();
        startY = (int) event.getY();
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baoS = new ByteArrayOutputStream();
        int options = 100;
        image.compress(Bitmap.CompressFormat.JPEG, 100, baoS);
        while (baoS.toByteArray().length / 1024 > 100) {
            baoS.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baoS);
            options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baoS.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, null);
    }


    public static Bitmap compressWithMeasure(Bitmap image, int width, int height) {
        ByteArrayOutputStream baoS = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baoS);
        if (baoS.toByteArray().length / 1024 > 1024) {
            baoS.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 50, baoS);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baoS.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int be = 1;
        if (w > h && w > width) {
            be = newOpts.outWidth / width;
        } else if (w < h && h > height) {
            be = newOpts.outHeight / height;
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;
        isBm = new ByteArrayInputStream(baoS.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);
    }


}