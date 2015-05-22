package com.example.gajendra.memorygameforkids;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

    private TextView status;
    private MediaPlayer player, pl1, pl2,end;
    private HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
    private Integer[] images,imageIDs;
    private Integer[] voices,voiceIDs;
    private int match=0, COUNT_MOVE = 0;
    private BoxView firstImage, secondImage;
    private int firstPlayerScore;
    private int secondPlayerScore;
    private int turn, Size, pos;
    private BoxView image,clone;
    private AdapterView.OnItemClickListener listen;
    private Button smallbutton,board;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Size = getIntent().getIntExtra("size",4);
        final GridView gridView = (GridView) findViewById(R.id.gridView);
        smallbutton=(Button)findViewById(R.id.smallbutton);
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
                image = (BoxView) view;
                pos = position;
                if (COUNT_MOVE != 2) {
                    //gridView.setOnItemClickListener(null);
                    if (image.isDiscovered() == false) {
                        //flip(image);
                                image.setImageResource(images[pos]);
                                image.setTag(images[pos]);
                                image.setDiscovered(true);
                                makeMove(image);
                                gridView.setOnItemClickListener(listen);


                    }
                }
                if (COUNT_MOVE == 2) {
                    gridView.setOnItemClickListener(null);
                    if (firstImage.getTag().equals(secondImage.getTag())) {

                                firstImage.setDiscovered(true);
                                secondImage.setDiscovered(true);

                                match++;
                        image.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                animate(firstImage);
                                animate(secondImage);
                                resetMove();
                                gridView.setOnItemClickListener(listen);
                                //gridView.setEnabled(true);
                                //gridView.setClickable(true);
                            }
                        }, 1000);


                    } else {

                        image.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //flip(firstImage);
                                //flip(secondImage);

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
                    image.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            player.stop();
                            end.start();
                            Intent end = new Intent(MainActivity.this, End.class);
                            finish();
                            startActivity(end);

                        }
                    },3000);

                }
            }
        };
        smallbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.stop();
                player.reset();
                player.release();
                player = null;
                Intent main = new Intent(MainActivity.this,MainActivity.class);
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

    public void makeMove(BoxView box) {
        if (COUNT_MOVE == 0) {
            firstImage = box;
            pl1 = MediaPlayer.create(getApplicationContext(), hm.get(images[pos]));
            pl1.start();
        } else {
            secondImage = box;
            pl2 = MediaPlayer.create(getApplicationContext(), hm.get(images[pos]));
            pl2.start();
        }
        COUNT_MOVE++;
    }

    public void resetMove() {
        firstImage = null;
        secondImage = null;
        COUNT_MOVE = 0;
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
        final AlphaAnimation blinkanimation= new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        blinkanimation.setDuration(300); // duration - half a second
        blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blinkanimation.setRepeatCount(6); // Repeat animation infinitely
        blinkanimation.setRepeatMode(Animation.REVERSE);
        v.startAnimation(blinkanimation);
    }


    public class ImageAdapter extends BaseAdapter {

        private BoxView imageView;
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
                imageView = new BoxView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                imageView = (BoxView) convertView;
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
    public void flip(BoxView view_new){

        FlipAnimator animator = new FlipAnimator(view_new,view_new, view_new.getWidth() / 2, view_new.getHeight() / 2);
        /*if (view_old.getVisibility() == View.GONE) {
            animator.reverse();
        }*/
        view_new.startAnimation(animator);

           }
}

