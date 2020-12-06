package com.example.quizza;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class WhiteBoard extends Activity {

    private ImageView imageView;
    Question myQuestion;
    Course myCourse;
    Event myEvent;
    String questionKey;


    private StorageReference mStorageRef;

    //whiteboard
    private static int SCREEN_W;
    private static int SCREEN_H;
    private static int Pen = 1;
    private static int Eraser = 2;

    String courseName;
    String eventName;
    String questionTitle;

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
        Button bt_undo=(Button) findViewById(R.id.bt_undo);

        ImageButton im_menuIcon=(ImageButton) findViewById(R.id.im_whiteboard_menu_icon);
        LinearLayout ll_menuList=(LinearLayout) findViewById(R.id.ll_whiteboard_menu);
        LinearLayout ll_clearWarning=(LinearLayout) findViewById(R.id.ll_clearWarning);
        LinearLayout ll_questionView=(LinearLayout) findViewById(R.id.ll_questionView);
        Button bt_warningYes=(Button) findViewById(R.id.bt_warningYes);
        Button bt_warningNo=(Button) findViewById(R.id.bt_warningNo);
        Button bt_viewQuestion=(Button) findViewById(R.id.bt_viewQuestion);
        Button bt_back=(Button) findViewById(R.id.bt_back);


        Button bt_questionBack=(Button) findViewById(R.id.bt_questionBack);
        TextView tv_question=(TextView) findViewById(R.id.tv_question);

        ViewGroup container = (ViewGroup) findViewById(R.id.Whiteboard_container);
        final MyView myView = new MyView(this);
        container.addView(myView);

        questionTitle = getIntent().getStringExtra("questionTitle");
        eventName = getIntent().getStringExtra("eventName");
        courseName = getIntent().getStringExtra("courseName");

        FirebaseDatabase.getInstance().getReference("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnap : snapshot.getChildren()){
                    Log.d("POOP1", itemSnap.getKey());
                    questionKey = itemSnap.getKey();
                    if (itemSnap.getKey().equals(questionTitle)) {
                        myQuestion = itemSnap.getValue(Question.class);
                        tv_question.setText(myQuestion.getQuestionText());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnap : snapshot.getChildren()){
                    if(itemSnap.getValue(Course.class).getCourseName().equals(myQuestion.getCourseLink())){
                        myCourse = itemSnap.getValue(Course.class);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnap : snapshot.getChildren()){
                    if(itemSnap.getValue(Event.class).getQuestionList().contains(questionKey)){
                        myEvent = itemSnap.getValue(Event.class);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        im_menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                im_menuIcon.setVisibility(View.INVISIBLE);
                ll_menuList.setVisibility(View.VISIBLE);
            }
        });
        bt_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.setMode(Pen);
                ll_menuList.setVisibility(View.INVISIBLE);
                im_menuIcon.setVisibility(View.VISIBLE);
            }
        });
        bt_eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.setMode(Eraser);
                ll_menuList.setVisibility(View.INVISIBLE);
                im_menuIcon.setVisibility(View.VISIBLE);
            }
        });
        bt_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.undo();
                ll_menuList.setVisibility(View.INVISIBLE);
                im_menuIcon.setVisibility(View.VISIBLE);
            }
        });
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_menuList.setVisibility(View.INVISIBLE);
                ll_clearWarning.setVisibility(View.VISIBLE);
            }
        });
        bt_viewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_menuList.setVisibility(View.INVISIBLE);
                ll_questionView.setVisibility(View.VISIBLE);
            }
        });
        bt_questionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_questionView.setVisibility(View.INVISIBLE);
                im_menuIcon.setVisibility(View.VISIBLE);
            }
        });
        bt_submit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
                ll_menuList.setVisibility(View.INVISIBLE);
                myView.save(myCourse, myEvent);
                im_menuIcon.setVisibility(View.VISIBLE);
          }
      });
        bt_back.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              finish();
          }
      });
        bt_warningYes.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              myView.clear();
              ll_clearWarning.setVisibility(View.INVISIBLE);
              im_menuIcon.setVisibility(View.VISIBLE);
          }
      });
        bt_warningNo.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              ll_clearWarning.setVisibility(View.INVISIBLE);
              ll_menuList.setVisibility(View.VISIBLE);
          }
      });

}
    private class DrawPath{
        public Path path;
        public Paint paint;

        public DrawPath(Path mPath, Paint mPaint) {
            path=mPath;
            paint=mPaint;
        }
    }
    public class MyView extends View {
        private int mMode = 1;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Paint mEraserPaint;
        private Paint mPaint;

        private ArrayList<DrawPath> savePath;
        private ArrayList<DrawPath> deletePath;
        private DrawPath mlastDP;
        private Path mPath;
        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        public MyView(Context context) {
            super(context);
            setFocusable(true);
            setScreenWH();
            setBackgroundColor(Color.WHITE);
            savePath=new ArrayList<DrawPath>();
            deletePath=new ArrayList<DrawPath>();
            initPaint();
        }
        public MyView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setFocusable(true);
            setScreenWH();
            setBackgroundColor(Color.WHITE);
            savePath=new ArrayList<DrawPath>();
            deletePath=new ArrayList<DrawPath>();
            initPaint();

        }

        public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            setFocusable(true);
            setScreenWH();
            setBackgroundColor(Color.WHITE);
            savePath=new ArrayList<DrawPath>();
            deletePath=new ArrayList<DrawPath>();
            initPaint();
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
            super.onDraw(canvas);
            if (mBitmap != null) {
                canvas.drawBitmap(mBitmap, 0, 0, mPaint);
            }
//            if(mPath!=null)
//            {
//                Log.d("OnDrwa",String.valueOf(mPath)+"  "+String.valueOf(mPaint));
//                if (mMode == Pen) {
//                    mCanvas.drawPath(mPath, mPaint);
//                }
//                if (mMode == Eraser) {
//                    mCanvas.drawPath(mPath, mEraserPaint);
//                }
//            }

        }
        public void clear()
        {
            setScaleY(1);
            setScaleX(1);
            mCanvas.drawColor(0,PorterDuff.Mode.CLEAR);
            initPaint();
            invalidate();
            //clear path for undo function
            savePath.clear();
            deletePath.clear();
        }
        public void save(Course myCourse, Event myEvent)
        {
            Bitmap b = ScreenShot.takescreenshotOfRootView(imageView);

            OutputStream fOut = null;
            Integer counter = 0;

            try {
                File file = new File(getExternalFilesDir(null), "BigPP" + counter + ".png");
                Uri myUri = Uri.fromFile(file);
                StorageReference riversRef = mStorageRef.child(courseName + "/" + eventName + "/" + questionTitle + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".png");
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
        public void undo()
        {
            Log.d("WhiteBoard","Undo:"+String.valueOf(savePath.size()));

            if(savePath!=null && savePath.size()>0)
            {
                mCanvas.drawColor(0,PorterDuff.Mode.CLEAR);
                DrawPath drawPath=savePath.get(savePath.size()-1);
                deletePath.add(drawPath);
                savePath.remove(savePath.size()-1);
                Log.d("WhiteBoard","Undo:"+String.valueOf(savePath.size()));
                //mCanvas.drawColor(0,PorterDuff.Mode.CLEAR);

                for (int i=0;i<savePath.size();i++)
                {
                    DrawPath temp=savePath.get(i);
                    Log.d("Undo", String.valueOf(temp.path)+" "+String.valueOf(temp.paint));
                    mCanvas.drawPath(temp.path,temp.paint);
                }
                invalidate();
            }
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
            mlastDP=new DrawPath(mPath,mPaint);
            savePath.add(mlastDP);
            mPath=null;
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
                    Log.d("WhiteBoard","Action_Down "+moveType);

                    moveType=1;
                    mPath=new Path();
                    mPath.reset();
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(moveType==2)
                    {
                        scale=scale*getSpacing(event) / spacing;
                        Log.d("WhiteBoard","Action_Move zoom "+moveType+" "+scale);
                        if(scale<1)//cancel zoom out
                            break;
                        setPivotX(x);
                        setPivotY(y);
                        setScaleX(scale);
                        setScaleY(scale);
                        invalidate();
                    }
                    else if(moveType==1)
                    {
                        Log.d("WhiteBoard","Action_Move write "+moveType);
                        touch_move(x, y);
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d("WhiteBoard","ACTION_UP  "+moveType);
                    if(moveType!=1)
                        break;
                    touch_up();
                    invalidate();
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    moveType=2;
                    spacing=getSpacing(event);
                    Log.d("WhiteBoard","ACTION_POINTER_DOWN "+moveType);
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    Log.d("WhiteBoard","ACTION_POINTER_UP "+moveType);
                    moveType=0;
                    break;
            }
            return true;
        }
    }
}
