package com.example.parktaejun.sixsense;

import android.app.ProgressDialog;

import java.util.Arrays;

/**
 * Created by parktaejun on 2017. 6. 11..
 */

public class Braille {
    private static final String[] FirstSound = {
            "000100","100100","010100","000010","100010","000110","110110","000101","000011","110100","110010","100110","010110"
    };
    private static final String[] MiddleSound = {
            "110001","001110","011100","100011","101001","001101","101100","100101","010101","101010","111010","101110","111001","101111","111100","010111"
    };
    private static final String[] LastSound = {
            "001010","010001","110000","001000","011011","101000","011000","011010","011001","010011","001011","100000","010010","010000"
    };
    private static final String[] Special = {
            "111111", "001001", "001100","000001"
    };
    private static int count = 1;
    private static int SFcount = 0;
    private static int SLcount = 0;
    private static int Scount = 0;
    private static char word[] = {
            ' ',
            ' ',
            ' '
    };
    private static boolean gesture[] = new boolean[6];
    private static String braille;
    private static char text;
    private static int at;

    public static int countCheck(){
        return count;
    }

    public static void recGesture(boolean gesture){
        if(count != 6){
            if(gesture){
                Braille.gesture[count-1] = true;
                count++;
            }else{
                Braille.gesture[count-1] = true;
                count++;
            }
        }else{
            isSix();
        }
    }

    private static void isSix(){
        gestureToBraille(Braille.gesture);
        initGesture();
        MainActivity.addText(text);
    }

    private static void initGesture(){
        count = 0;
        gesture = null;
        MainActivity.initBraille();
    }

    private static void gestureToBraille(boolean gesture[]){
        for(boolean g:gesture){
            if(g==true){
                braille += '1';
            }else{
                braille += '0';
            }
        }
        brailleToText(braille);
    }

    private static void brailleToText(String b){
        switch (hangulAt(b)){
            //초성일 때
            case 1:
                if(SFcount != 0){
                    switch (b){
                        case "000100":text='ㄲ';if(Scount==1)initWord();
                            break;
                        case "010100":text='ㄸ';if(Scount==1)initWord();
                            break;
                        case "000110":text='ㅃ';if(Scount==1)initWord();
                            break;
                        case "000001":text='ㅆ';if(Scount==1)initWord();
                            break;
                        case "000101":text='ㅉ';if(Scount==1)initWord();
                            break;
                        default:
                            break;
                    }
                    SFcount = 0;
                }else{
                    switch (b){
                        case "000100":text='ㄱ';if(Scount==1)initWord();
                            break;
                        case "100100":text='ㄴ';if(Scount==1)initWord();
                            break;
                        case "010100":text='ㄷ';if(Scount==1)initWord();
                            break;
                        case "000010":text='ㄹ';if(Scount==1)initWord();
                            break;
                        case "100010":text='ㅁ';if(Scount==1)initWord();
                            break;
                        case "000110":text='ㅂ';if(Scount==1)initWord();
                            break;
                        case "110110":text='ㅇ';if(Scount==1)initWord();
                            break;
                        case "000101":text='ㅈ';if(Scount==1)initWord();
                            break;
                        case "000011":text='ㅊ';if(Scount==1)initWord();
                            break;
                        case "110100":text='ㅋ';if(Scount==1)initWord();
                            break;
                        case "110010":text='ㅌ';if(Scount==1)initWord();
                            break;
                        case "100110":text='ㅍ';if(Scount==1)initWord();
                            break;
                        case "010110":text='ㅎ';if(Scount==1)initWord();
                            break;
                        default:break;
                    }
                }
                word[0]=text;
                break;

            //중성일 떄
            case 2:
                if(SFcount!=0)SFcount=0;
                switch (b){
                    case "110001":text='ㅏ';
                        if(Scount==1){
                            initWord();
                            break;
                        }else{
                            if(word[0] == ' ')word[0]='ㅇ';
                        }
                        break;
                    case "001110":text='ㅑ';
                        if(Scount==1){
                            initWord();
                            break;
                        }else{
                            if(word[0] == ' ')word[0]='ㅇ';
                        }
                        break;
                    case "011100":text='ㅓ';
                        if(Scount==1){
                            initWord();
                            break;
                        }else{
                            if(word[0] == ' ')word[0]='ㅇ';
                        }
                        break;
                    case "100011":text='ㅕ';
                        if(Scount==1){
                            initWord();
                            break;
                        }else{
                            if(word[0] == ' ')word[0]='ㅇ';
                        }
                        break;
                    case "101001":text='ㅗ';
                        if(Scount==1){
                            initWord();
                            break;
                        }else{
                            if(word[0] == ' ')word[0]='ㅇ';
                        }
                        break;
                    case "001101":text='ㅛ';
                        if(Scount==1){
                            initWord();
                            break;
                        }else{
                            if(word[0] == ' ')word[0]='ㅇ';
                        }
                        break;
                    case "101100":text='ㅜ';
                        if(Scount==1){
                            initWord();
                            break;
                        }else{
                            if(word[0] == ' ')word[0]='ㅇ';
                        }
                        break;
                    case "100101":text='ㅠ';
                        if(Scount==1){
                            initWord();
                            break;
                        }else{
                            if(word[0] == ' ')word[0]='ㅇ';
                        }
                        break;
                    case "010101":text='ㅡ';
                        if(Scount==1){
                            initWord();
                            break;
                        }else{
                            if(word[0] == ' ')word[0]='ㅇ';
                        }
                        break;
                    case "101010":text='ㅣ';
                        if(Scount==1){
                            initWord();
                            break;
                        }else{
                            if(word[0] == ' ')word[0]='ㅇ';
                        }
                        break;
                    case "111010":text='ㅐ';
                        if(Scount==1){
                            initWord();
                            break;
                        }else{
                            if(word[0] == ' ')word[0]='ㅇ';
                        }
                        break;
                    case "101110":text='ㅔ';
                        if(Scount==1){
                            initWord();
                            break;
                        }else{
                            if(word[0] == ' ')word[0]='ㅇ';
                        }
                        break;
                    case "111001":text='ㅘ';
                        if(Scount==1){
                            initWord();
                            break;
                        }else{
                            if(word[0] == ' ')word[0]='ㅇ';
                        }
                        break;
                    case "101111":text='ㅚ';
                        if(Scount==1){
                            initWord();
                            break;
                        }else{
                            if(word[0] == ' ')word[0]='ㅇ';
                        }
                        break;
                    case "111100":text='ㅝ';
                        if(Scount==1){
                            initWord();
                            break;
                        }else{
                            if(word[0] == ' ')word[0]='ㅇ';
                        }
                        break;
                    case "010111":text='ㅢ';
                        if(Scount==1){
                            initWord();
                            break;
                        }else{
                            if(word[0] == ' ')word[0]='ㅇ';
                        }
                        break;
                    default:break;
                }
                word[1]=text;
                break;

            //종성일 때
            case 3:
                if(SLcount != 0){
                    switch (SLcount){
                        //겹받침 'ㄱ' 일 때
                        case 1:SLcount=0;
                            switch (b){
                                case "100000":text='ㄲ';if(Scount==1)initWord();
                                    break;
                                case "001000":text='ㄳ';if(Scount==1)initWord();
                                    break;
                                default:break;
                            }
                            break;
                        //겹받침 'ㄴ' 일 때
                        case 2:SLcount=0;
                            switch (b){
                                case "101000":text='ㄵ';if(Scount==1)initWord();
                                    break;
                                case "001011":text='ㄶ';if(Scount==1)initWord();
                                    break;
                                default:break;
                            }
                            break;
                        //겹받침 'ㄹ' 일 때
                        case 3:SLcount=0;
                            switch (b){
                                case "100000":text='ㄺ';if(Scount==1)initWord();
                                    break;
                                case "010001":text='ㄻ';if(Scount==1)initWord();
                                    break;
                                case "110000":text='ㄼ';if(Scount==1)initWord();
                                    break;
                                case "001000":text='ㄽ';if(Scount==1)initWord();
                                    break;
                                case "011001":text='ㄾ';if(Scount==1)initWord();
                                    break;
                                case "010011":text='ㄿ';if(Scount==1)initWord();
                                    break;
                                case "001011":text='ㅀ';if(Scount==1)initWord();
                                    break;
                                default:break;
                            }
                            break;
                        //겹받침 'ㅂ 일 때
                        case 4:SLcount=0;
                            switch (b){
                                case "001000":text='ㅄ';if(Scount==1)initWord();
                            }
                            break;
                        default:break;
                    }
                    SLcount = 0;
                }else{
                    switch (b){
                        case "001010":text='ㄷ';
                            break;
                        case "010001":text='ㅁ';
                            break;
                        case "001000":text='ㅅ';
                            break;
                        case "011011":text='ㅇ';
                            break;
                        case "101000":text='ㅈ';
                            break;
                        case "011000":text='ㅊ';
                            break;
                        case "011010":text='ㅋ';
                            break;
                        case "011001":text='ㅌ';
                            break;
                        case "010011":text='ㅠ';
                            break;
                        case "001011":text='ㅎ';
                            break;
                        //겹받침 'ㄱ'
                        case "100000":SLcount=1;
                            break;
                        //겹받침 'ㄴ'
                        case "010010":SLcount=2;
                            break;
                        //겹받침 'ㄹ'
                        case "010000":SLcount=3;
                            break;
                        //겹밭침 'ㅂ'
                        case "110000":SLcount=4;
                            break;
                        default:break;
                    }
                }
                word[2]=text;
                if(SLcount == 0)initWord();
                break;

            //특별한 점자일 때
            case 4:
                switch (b){
                    //자음 혹은 모음자가 단독으로 쓰일 때 적는 온표
                    case "111111":Scount = 1;
                        break;
                    //모음자 뒤에 '예'가 나올 떄 사이에 적거나 ‘ㅑ, ㅘ, ㅜ, ㅝ’ 뒤에 '애'가 이어 나올 떄 사이에 적는 붙임표
                    case "001001":Scount = 2;
                        break;
                    //종성의 'ㅆ' 와 모음의 'ㅖ'
                    case "001100":
                        if(word[1] != ' '){
                            text = 'ㅆ';
                            word[2]=text;
                            initWord();
                        }else{
                            text = 'ㅖ';
                            word[1]=text;
                        }
                        break;
                    //'ㅅ' 혹은 된소리 글자 'ㄲ, ㄸ, ㅃ, ㅆ, ㅉ'를 표기할 때 각 'ㄱ, ㄷ, ㅂ, ㅅ, ㅈ' 앞에 적는 된소리표
                    case "000001":text='ㅅ';
                        SFcount++;
                        break;
                    default:break;
                }
            default:break;
        }

    }

    //점자를 인식하여 초성, 중성, 종성인지 파악하는 메소드
    private static int hangulAt(String b) {
        if(Arrays.asList(FirstSound).contains(b)){
            if(Scount == 0 || word[2] == ' ')initWord();
            at = 1;
        }else if(Arrays.asList(MiddleSound).contains(b)){
            at = 2;
        }else if(Arrays.asList(LastSound).contains(b)){
            at = 3;
        }else if(Arrays.asList(Special).contains(b)){
            at = 4;
        }
        return at;
    }

    //word 배열을 초기화하는 메소드
    private static void initWord(){
        MainActivity.addText(Hangul.CombineHangul(word));
        for(int i = 0; i < 3; i++)word[i]=' ';
    }
}
