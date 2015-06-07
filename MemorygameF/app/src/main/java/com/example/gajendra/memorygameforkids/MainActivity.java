package com.example.gajendra.memorygameforkids;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;
import java.lang.Object;


public class MainActivity extends ActionBarActivity {

    private TextView status, timerText;
    private MediaPlayer player, pl1, pl2,end;
    private HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
    private Integer[] images,imageIDs;
    private Integer[] voices,voiceIDs;
    private int match=0, countMove = 0;
    private Card firstImage, secondImage;
    private int attempts=0, Size, pos;
    private long START_TIME,END_TIME,TIME_ELAPSED;
    private Card image;
    private AdapterView.OnItemClickListener listen;
    private Button reset,board;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private long startTime = 0L;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Size = getIntent().getIntExtra("size", 4);
        START_TIME=System.currentTimeMillis();
        startTime=SystemClock.uptimeMillis();
        final GridView gridView = (GridView) findViewById(R.id.gridView);
        reset =(Button)findViewById(R.id.reset);
        board=(Button)findViewById(R.id.board);
        images = new Integer[Size * Size];
        imageIDs=new Integer[18];
        voices = new Integer[Size * Size];
        voiceIDs=new Integer[18];
        status = (TextView) findViewById(R.id.status);
        status.setTextColor(Color.BLACK);
        gridView.setNumColumns(Size);
        final ImageAdapter adapter = new ImageAdapter(this);
        gridView.setAdapter(adapter);
        displayStatus();
        player = MediaPlayer.create(MainActivity.this, R.raw.african_music);
        end=MediaPlayer.create(MainActivity.this,R.raw.end);

        for(int i=0;i<18;i++){
            String im = "image" + i;
            imageIDs[i]=getResources().getIdentifier(im, "drawable", getPackageName());
            String vo = "voice" + i;
            voiceIDs[i]=getResources().getIdentifier(vo, "raw", getPackageName());
            hm.put(imageIDs[i], voiceIDs[i]);
        }

        shuffle(imageIDs);

        for (int i = 0; i < (Size * Size); i++) {
            images[i]=imageIDs[i%Size];
            //hm.put(images[i], voices[i]);
        }

        // The following code shuffles the array
        shuffle(images);

        player.start();
        player.setLooping(true);
        listen=new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                image = (Card) view;
                pos = position;
                attempts++;
                if (countMove != 2) {

                    if (image.isDiscovered() == false) {
                       
                                image.setImageResource(images[pos]);
                                image.setTag(images[pos]);
                                image.setDiscovered(true);
                                makeMove(image);
                    }
                }
                if (countMove == 2) {
                    gridView.setOnItemClickListener(null);
                    if (firstImage.getTag().equals(secondImage.getTag())) {

                                firstImage.setDiscovered(true);
                                secondImage.setDiscovered(true);
                                match++;
                                animate(firstImage);
                                animate(secondImage);
                        image.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                resetMove();
                                gridView.setOnItemClickListener(listen);
                            }
                        }, 700);


                    } else {

                        image.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                        firstImage.setImageResource(R.drawable.hidden);
                                        secondImage.setImageResource(R.drawable.hidden);
                                        firstImage.setDiscovered(false);
                                        secondImage.setDiscovered(false);
                                        resetMove();
                                        gridView.setOnItemClickListener(listen);


                            }
                        }, 1000);
                    }
                    displayStatus();
                }

                if (isFinished()) {
                    gridView.setEnabled(false);
                    animate(status);
                    timeSwapBuff += timeInMilliseconds;
                    customHandler.removeCallbacks(updateTimerThread);
                    END_TIME=System.currentTimeMillis();
                    image.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            player.stop();
                            end.start();
                            TIME_ELAPSED=END_TIME-START_TIME;
                            Intent end = new Intent(MainActivity.this, End.class);
                            end.putExtra("attempts",attempts);
                            end.putExtra("time_elapsed",TIME_ELAPSED);
                            finish();
                            startActivity(end);

                        }
                    },2000);

                }
            }
        };
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.stop();
                player.reset();
                player.release();
                player = null;
                Intent main = new Intent(MainActivity.this, MainActivity.class);
                main.putExtra("size", Size);
                finish();
                startActivity(main);

            }
        });
        gridView.setOnItemClickListener(listen);
        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.stop();
                player.reset();
                player.release();
                player = null;
                Intent board = new Intent(MainActivity.this, Board.class);
                startActivity(board);
                finish();
            }
        });

    }

    public void displayStatus() {
        status.setText("No. of Match: "+match);
    }

    public void makeMove(Card box) {
        if (countMove == 0) {
            firstImage = box;
            pl1 = MediaPlayer.create(getApplicationContext(), hm.get(images[pos]));
            pl1.start();
        } else {
            secondImage = box;
            pl2 = MediaPlayer.create(getApplicationContext(), hm.get(images[pos]));
            pl2.start();
        }
        countMove++;
    }

    public void resetMove() {
        firstImage = null;
        secondImage = null;
        countMove = 0;
        pl1.release();
        pl2.release();
        pl1 = null;
        pl2 = null;

    }

    public boolean isMatch() {
        if (firstImage.getTag().equals(secondImage.getTag())) {
            return true;
        }
        return false;
    }

    public boolean isFinished() {
        if (match==(Size*Size)/2) {
            return true;
        }
        return false;
    }

    public void shuffle(Integer[] array){
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }

    public void animate(View v){
        final AlphaAnimation blink= new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        blink.setDuration(300); // duration - half a second
        blink.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blink.setRepeatCount(6); // Repeat animation infinitely
        blink.setRepeatMode(Animation.REVERSE);
        v.startAnimation(blink);
    }


    public class ImageAdapter extends BaseAdapter {

        private Card imageView;
        private Context context;

        public ImageAdapter(Context c) {
            context = c;
        }

        //---returns the number of imageIDs---
        public int getCount() {
            return images.length;
        }

        //---returns the ID of an item---
        public Object getItem(int position) {
            return position;
        }

        //---returns the ID of an item---
        public long getItemId(int position) {
            return position;
        }

        //---returns an ImageView view---
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                imageView = new Card(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                imageView = (Card) convertView;
            }
            imageView.setImageResource(R.drawable.hidden);
            //imageView.setImageBitmap(B);
            //imageView.setAlpha(0.5f);
            return imageView;
        }
    }

    @Override
    public void onBackPressed() {
        if(player.isPlaying()){
            player.stop();
            player.release();
            startActivity(new Intent(MainActivity.this, Board.class));
        }
        else if(player==null){
            startActivity(new Intent(MainActivity.this, Board.class));

        }
        else if(end.isPlaying())
        {
            end.stop();
            end.release();
            startActivity(new Intent(MainActivity.this, Board.class));
        }
        else{
            end.release();
            startActivity(new Intent(MainActivity.this, Board.class));
        }
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timer, menu);

        MenuItem timerItem = menu.findItem(R.id.break_timer);
        timerText = (TextView) MenuItemCompat.getActionView(timerItem);

        timerText.setPadding(10, 0, 10, 0); //Or something like that...
        customHandler.postDelayed(updateTimerThread, 0);
        //startTimer(30000, 1000); //One tick every second for 30 seconds
        return true;
    }

    /*private void startTimer(long duration, long interval) {

        CountDownTimer timer = new CountDownTimer(duration, interval) {

            @Override
            public void onFinish() {
                //TODO Whatever's meant to happen when it finishes
            }

            @Override
            public void onTick(long millisecondsLeft) {
                int secondsLeft = (int) Math.round((millisecondsLeft / (double) 1000));
                timerText.setText(secondsToString(secondsLeft));
            }
        };

        timer.start();
    }*/

    /*private String secondsToString(int improperSeconds) {

        //Seconds must be fewer than are in a day

        Time secConverter = new Time();

        secConverter.hour = 0;
        secConverter.minute = 0;
        secConverter.second = 0;

        secConverter.second = improperSeconds;
        secConverter.normalize(true);

        String hours = String.valueOf(secConverter.hour);
        String minutes = String.valueOf(secConverter.minute);
        String seconds = String.valueOf(secConverter.second);

        if (seconds.length() < 2) {
            seconds = "0" + seconds;
        }
        if (minutes.length() < 2) {
            minutes = "0" + minutes;
        }
        if (hours.length() < 2) {
            hours = "0" + hours;
        }

        String timeString = hours + ":" + minutes + ":" + seconds;
        return timeString;
    }*/

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerText.setText("" + mins + ":"
                            + String.format("%02d", secs) + ":"
                            + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }
    };

    public void flip(Card view_new){

        FlipAnimator animator = new FlipAnimator(view_new,view_new, view_new.getWidth() / 2, view_new.getHeight() / 2);
        /*if (view_old.getVisibility() == View.GONE) {
            animator.reverse();
        }*/
        view_new.startAnimation(animator);

           }
}

