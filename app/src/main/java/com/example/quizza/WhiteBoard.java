package com.example.quizza;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WhiteBoard extends Activity {

    private MyView WhiteBoard;
    private ImageView imageView;

    private StorageReference mStorageRef;
    private Uri filePath;

    //whiteboard
    private static int SCREEN_W;
    private static int SCREEN_H;
    private static int Pen = 1;
    private static int Eraser = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whiteboard);

        imageView = (ImageView) findViewById(R.id.imageView);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Button bt_submit = (Button) findViewById(R.id.bt_submit);
        Button bt_pen=(Button) findViewById(R.id.bt_pen);
        Button bt_eraser=(Button) findViewById(R.id.bt_eraser);
        Button bt_clear=(Button) findViewById(R.id.bt_clear);

        ViewGroup container = (ViewGroup) findViewById(R.id.Whiteboard_container);
        final MyView myView = new MyView(this);
        container.addView(myView);
        bt_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.setMode(Pen);
            }
        });

        bt_eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.setMode(Eraser);
            }
        });
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { myView.clear(); }
        });

      bt_submit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
                myView.save();
          }
      });
}
    public class MyView extends View {
        private int mMode = 1;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Paint mEraserPaint;
        private Paint mPaint;
        private Path mPath;
        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        public MyView(Context context) {
            super(context);
            setFocusable(true);
            setScreenWH();
            setBackgroundColor(Color.WHITE);
            initPaint();
        }
        public MyView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }


        private void setScreenWH() {
            DisplayMetrics dm = new DisplayMetrics();
            dm = this.getResources().getDisplayMetrics();
            int screenWidth = dm.widthPixels;
            int screenHeight = dm.heightPixels;
            SCREEN_W = screenWidth;
            SCREEN_H = screenHeight;
        }

        public void setMode(int mode){
            this.mMode = mode;
        }

        private void initPaint() {

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setColor(Color.BLACK);
            mPaint.setStrokeWidth(10);

            mEraserPaint = new Paint();
            mEraserPaint.setAlpha(0);

            mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

            mEraserPaint.setAntiAlias(true);
            mEraserPaint.setDither(true);
            mEraserPaint.setStyle(Paint.Style.STROKE);
            mEraserPaint.setStrokeJoin(Paint.Join.ROUND);
            mEraserPaint.setStrokeWidth(30);

            mPath = new Path();

            mBitmap = Bitmap.createBitmap(SCREEN_W, SCREEN_H, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (mBitmap != null) {
                canvas.drawBitmap(mBitmap, 0, 0, mPaint);
            }
            super.onDraw(canvas);
        }
        public void clear()
        {
            setScaleY(1);
            setScaleX(1);
            mCanvas.drawColor(0,PorterDuff.Mode.CLEAR);
            invalidate();
        }
        public void save()
        {
            Bitmap b = ScreenShot.takescreenshotOfRootView(imageView);

            OutputStream fOut = null;
            Integer counter = 0;

            try {
                File file = new File(getExternalFilesDir(null), "BigPP" + counter + ".png");
                Uri myUri = Uri.fromFile(file);
                StorageReference riversRef = mStorageRef.child("BigPP" + counter + ".png");
                riversRef.putFile(myUri);
                if (!file.exists())
                    file.createNewFile();

                try {
                    fOut = new FileOutputStream(file);
                    b.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch(Exception e){
            }
/*           if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
               Toast.makeText(this.getContext(),"No Write External Storage Permission",Toast.LENGTH_LONG).show();
                Log.e("Permission","No Write External Storage");
                return false;
            }
            setDrawingCacheEnabled(true);
            buildDrawingCache();
            Bitmap savedBitmap=getDrawingCache();
            //Bitmap savedBitmap=null;
            try {
                savedBitmap = mBitmap.copy(mBitmap.getConfig(), true);
                Canvas canvas = new Canvas(savedBitmap);
                String fileName=System.currentTimeMillis()+".png";
                FileOutputStream  out=new FileOutputStream(new File(getExternalCacheDir(),fileName));
                savedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                Toast.makeText(this.getContext(),getExternalCacheDir()+"/"+fileName+" save successful!",Toast.LENGTH_LONG).show();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                savedBitmap=null;

            }*/
        }
        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;

            if (mMode == Pen) {
                mCanvas.drawPath(mPath, mPaint);
            }

            if (mMode == Eraser) {
                mCanvas.drawPath(mPath, mEraserPaint);
            }

        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
                if (mMode == Pen) {
                    mCanvas.drawPath(mPath, mPaint);
                }
                if (mMode == Eraser) {
                    mCanvas.drawPath(mPath, mEraserPaint);
                }
            }
        }


        private void touch_up() {
            mPath.lineTo(mX, mY);
            if (mMode == Pen) {
                mCanvas.drawPath(mPath, mPaint);
            }
            if (mMode == Eraser) {
                mCanvas.drawPath(mPath, mEraserPaint);
            }
        }
        private int moveType=0;
        private float scale=1;
        private float spacing;

        private float getSpacing(MotionEvent event) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()&MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    moveType=1;
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(moveType==2)
                    {
                        scale=scale*getSpacing(event) / spacing;
                        setScaleX(scale);
                        setScaleY(scale);
                        invalidate();
                    }
                    else if(moveType==1)
                    {
                        touch_move(x, y);
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    moveType=2;
                    spacing=getSpacing(event);
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    moveType=0;
                    break;
            }
            return true;
        }
    }
}
