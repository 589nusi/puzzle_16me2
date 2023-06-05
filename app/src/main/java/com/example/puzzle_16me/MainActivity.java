package com.example.puzzle_16me;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    Random random =new Random();
    public TextView[] numtext= new TextView[16];//textView
    public TextView goaltext;//クリアのtextview
    private int[] ttid={
            R.id.tt_1, R.id.tt_2, R.id.tt_3, R.id.tt_4,
            R.id.tt_5, R.id.tt_6, R.id.tt_7, R.id.tt_8,
            R.id.tt_9, R.id.tt_10, R.id.tt_11, R.id.tt_12,
            R.id.tt_13, R.id.tt_14, R.id.tt_15, R.id.tt_16,
    };//textviewのid


    private int rannumber;//ランダム数字
    private int[] firstboard= new int[16];//btn押す前の初期盤面ゴール確認
    private int[][] nowboard=new int[6][6];//現在の盤面//枠外を囲むように一回り大きくする
    private int[] randomboard=new int [16];//randombtn押したときの初期盤面
    private int color=-1;//色変更の場所
    private int savenum;//入れ替え用の数字
    private int checknum;//数字があるか判定
    private int goalcheck;//ゴール確認
    Button randombtn,upbtn,downbtn,rightbtn,leftbtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        randombtn = findViewById(R.id.rst_btn);
        upbtn = findViewById(R.id.up_btn);
        downbtn = findViewById(R.id.down_btn);
        leftbtn = findViewById(R.id.left_btn);
        rightbtn = findViewById(R.id.right_btn);
        goaltext = (TextView) findViewById(R.id.goalt);
        goalcheck = 0;


        int i,j;

        //textviewと接続
        for (i = 0; i < 16; i++) {
            numtext[i] = (TextView) findViewById(ttid[i]);
            numtext[i].setId(i);     //　id を数値に置き換える;
        }

        for (i = 0; i < 16; i++) {
            firstboard[i] = i;//初期盤面を0～15の順列に格納//後で数字入れ替え
            randomboard[i] = -1;//あり得ない数字に設定randombtn押したら変更

        }
        for (i = 0; i < 6; i++) {for(j=0;j<6;j++){
            nowboard[i][j] = -1;//現在の盤面は最初-1

        }
            //randombtn押したら変更
        }

        for (i = 0; i < 16; i++) {
            if (firstboard[i] == 0) {
                numtext[i].setText("");
                //numtext[i].setBackgroundColor(Color.TRANSPARENT);
            } else {
                numtext[i].setText(String.valueOf(firstboard[i]));
            }
        }

        //randombtn
        randombtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //盤面リセット
                for (int i = 0; i < 16; i++) {
                    randomboard[i] = -1;
                }
                int count = 0;
                for (int i = 0; i < 16; i++) {
                    checknum = 0;
                    rannumber = random.nextInt(16);//15以下のrandomな数字を取得

                    //被りがないかの確認
                    for (int j = 0; j < 16; j++)
                        if (randomboard[j] != rannumber) {
                            checknum++;
                        }

                    //盤面に入れるための配列を格納
                    if (checknum == 16) {
                        randomboard[i] = rannumber;
                    }   //被りがなかったら++
                    else {
                        i--;
                    }       //１つでも被っていたらやり直し

                }

                for (int i = 1; i < 5; i++) {

                    for (int j = 1; j < 5; j++) {
                        nowboard[i][j] = randomboard[count];
                        count++;
                    }
                }

                for (int i = 0; i < 16; i++) {
                    if (randomboard[i] == 0) {
                        numtext[i].setText("");

                    } else {
                        numtext[i].setText(String.valueOf(randomboard[i]));
                    }
                }

            }
        });

        //goal check
        goalcheck = 0;
        int x,y;
        for( i=0;i<16;i++) {
            x=i/4+1;
            y=i%4+1;
            if ( nowboard[x][y]== i) {
                goalcheck = goalcheck + 1;
            }
            else {
                break;
            }
            if(goalcheck>=15){
                goaltext.setText("It's goal");
            }
        }
        if(goalcheck>=15){
            for( i=0;i<16;i++){
                numtext[i].setClickable(false);
            }
           }

    }

    //ここからボタンの処理
    @SuppressLint("ResourceType")
    public void onNumberTextClick(View view){


        String str = (String)view.getTag();
        int tag = Integer.parseInt(str);
        int x,y;
        x=tag/4+1;
        y=tag%4+1;


        //textviewを触ったとき数字の色を赤に変更

        if(nowboard[x][y]!=0) {
            if (color == -1) {
                numtext[tag].setTextColor(Color.RED);
                color = tag;
            } else {
                numtext[color].setTextColor(Color.BLACK);
                numtext[tag].setTextColor(Color.RED);
                color = tag;
            }
        }

        //upbtnの設定
        upbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(nowboard[x-1][y]==0&&nowboard[x][y]!=0){

                    numtext[tag - 4 ].setText(String.valueOf(nowboard[x][y]));
                    numtext[tag].setText("");
                    nowboard[x-1][y]=nowboard[x][y];
                    nowboard[x][y]=0;

                }
            }
        });
        if(nowboard[x-1][y]!=0){
            upbtn.setEnabled(false);
        }
        if(nowboard[x-1][y]==0) {upbtn.setEnabled(true);}

        //downbtnの設定
        downbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    numtext[tag+4 ].setText(String.valueOf(nowboard[x][y]));
                    numtext[tag ].setText("");
                    nowboard[x+1][y]=nowboard[x][y];
                    nowboard[x][y]=0;
            }
        });
        if(nowboard[x+1][y]!=0){
            downbtn.setEnabled(false);
        }
        if(nowboard[x+1][y]==0) {downbtn.setEnabled(true);}


        //rightbtnの設定
        rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    numtext[tag +1 ].setText(String.valueOf(nowboard[x][y]));
                    numtext[tag ].setText("");
                    nowboard[x][y+1]=nowboard[x][y];
                    nowboard[x][y]=0;


            }
        });
        if(nowboard[x][y+1]!=0){
            rightbtn.setEnabled(false);
        }
        if(nowboard[x][y+1]==0) {rightbtn.setEnabled(true);}


        //leftbtnの設定
        leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    numtext[tag-1  ].setText(String.valueOf(nowboard[x][y]));
                    numtext[tag ].setText("");
                    nowboard[x][y-1]=nowboard[x][y];
                    nowboard[x][y]=0;


            }
        });
        if(nowboard[x][y-1]!=0){
            leftbtn.setEnabled(false);
        }
        if(nowboard[x][y-1]==0) {leftbtn.setEnabled(true);}


    }


}