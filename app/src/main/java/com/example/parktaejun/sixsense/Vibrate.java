package com.example.parktaejun.sixsense;

/**
 * Created by parktaejun on 2017. 6. 11..
 */
public class Vibrate {

    private static int[] whatBraille;
    private int caseNum = 0;

    private static void put(int i){
        whatBraille[whatBraille.length] = i;
    }
    private static void put(int i, int j){
        whatBraille[whatBraille.length] = i;
        whatBraille[whatBraille.length] = j;
    }
    private static void put(int i, int j, int k, int h){
        whatBraille[whatBraille.length] = i;
        whatBraille[whatBraille.length] = j;
        whatBraille[whatBraille.length] = k;
        whatBraille[whatBraille.length] = h;
    }
    private static void put(int i, int j, int k, int h, int m, int n){
        whatBraille[whatBraille.length] = i;
        whatBraille[whatBraille.length] = j;
        whatBraille[whatBraille.length] = k;
        whatBraille[whatBraille.length] = h;
        whatBraille[whatBraille.length] = m;
        whatBraille[whatBraille.length] = n;
    }

    public static int[] makeBraille(){
        char[][] whereSound = Hangul.getWhere();
        int len = whereSound.length;
        for(int i = 0; i < len; i++){
            for(int j = 0; j < 3; j++){
                if(whereSound[0][i] != ' ' && whereSound[1][i] == ' ' && whereSound[2][i] == ' '){
                    switch (whereSound[0][i]){
                        case 'ㄱ':put(7,7,0,4);
                            break;
                        case 'ㄴ':put(7,7,4,4);
                            break;
                        case 'ㄷ':put(7,7,2,4);
                            break;
                        case 'ㄹ':put(7,7,0,2);
                            break;
                        case 'ㅁ':put(7,7,4,2);
                            break;
                        case 'ㅂ':put(7,7,0,6);
                            break;
                        case 'ㅅ':put(7,7,0,1);
                            break;
                        case 'ㅇ':put(7,7,6,6);
                            break;
                        case 'ㅈ':put(7,7,0,5);
                            break;
                        case 'ㅊ':put(7,7,0,3);
                            break;
                        case 'ㅋ':put(7,7,6,4);
                            break;
                        case 'ㅌ':put(7,7,6,2);
                            break;
                        case 'ㅍ':put(7,7,4,6);
                            break;
                        case 'ㅎ':put(7,7,2,6);
                            break;
                        case 'ㄲ':put(7,7,0,1,0,4);
                            break;
                        case 'ㄸ':put(7,7,0,1,2,4);
                            break;
                        case 'ㅃ':put(7,7,0,1,0,6);
                            break;
                        case 'ㅆ':put(7,7,0,1,1,1);
                            break;
                        case 'ㅉ':put(7,7,0,1,0,5);
                            break;
                    }
                }else if(whereSound[0][i] == ' ' && whereSound[1][i] != ' ' && whereSound[2][i] == ' ') {
                    switch (whereSound[1][i]) {
                        case 'ㅏ':
                            put(7,7,6,1);
                            break;
                        case 'ㅐ':
                            put(7,7,7,2);
                            break;
                        case 'ㅑ':
                            put(7,7,1,6);
                            break;
                        case 'ㅒ':
                            put(7,7,1,6,7,2);
                            break;
                        case 'ㅓ':
                            put(7,7,3,4);
                            break;
                        case 'ㅔ':
                            put(7,7,5,6);
                            break;
                        case 'ㅕ':
                            put(7,7,4,3);
                            break;
                        case 'ㅖ':
                            put(7,7,1,4);
                            break;
                        case 'ㅗ':
                            put(7,7,5,1);
                            break;
                        case 'ㅘ':
                            put(7,7,7,1);
                            break;
                        case 'ㅙ':
                            put(7,7,7,1,7,2);
                            break;
                        case 'ㅚ':
                            put(7,7,5,7);
                            break;
                        case 'ㅛ':
                            put(7,7,1,5);
                            break;
                        case 'ㅜ':
                            put(7,7,5,4);
                            break;
                        case 'ㅝ':
                            put(7,7,7,4);
                            break;
                        case 'ㅞ':
                            put(7,7,7,4,7,2);
                            break;
                        case 'ㅟ':
                            put(7,7,5,4,7,2);
                            break;
                        case 'ㅠ':
                            put(7,7,4,5);
                            break;
                        case 'ㅡ':
                            put(7,7,2,5);
                            break;
                        case 'ㅢ':
                            put(7,7,2,7);
                            break;
                        case 'ㅣ':
                            put(7,7,5,2);
                            break;
                    }
                }else if(whereSound[0][i] == ' ' && whereSound[1][i] == ' ' && whereSound[2][i] != ' '){
                    switch (whereSound[2][i]){
                        case 'ㄳ':put(4,0,1,0);
                            break;
                        case 'ㄵ':put(2,2,5,0);
                            break;
                        case 'ㄶ':put(2,2,1,3);
                            break;
                        case 'ㄺ':put(2,0,4,0);
                            break;
                        case 'ㄻ':put(2,0,2,1);
                            break;
                        case 'ㄼ':put(2,0,6,0);
                            break;
                        case 'ㄽ':put(2,0,1,0);
                            break;
                        case 'ㄾ':put(2,0,3,1);
                            break;
                        case 'ㄿ':put(2,0,2,3);
                            break;
                        case 'ㅀ':put(2,0,1,3);
                            break;
                        case 'ㅄ':put(6,0,1,0);
                            break;
                    }
                }else if(whereSound[0][i] == ' ' && whereSound[1][i] == ' ' && whereSound[2][i] == ' '){
                    put(8);
                }else{
                    if(j == 0){
                        switch (whereSound[j][i]){
                            case 'ㄱ':put(0,4);
                                break;
                            case 'ㄴ':put(4,4);
                                break;
                            case 'ㄷ':put(2,4);
                                break;
                            case 'ㄹ':put(0,2);
                                break;
                            case 'ㅁ':put(4,2);
                                break;
                            case 'ㅂ':put(0,6);
                                break;
                            case 'ㅅ':put(0,1);
                                break;
                            case 'ㅇ':put(6,6);
                                break;
                            case 'ㅈ':put(0,5);
                                break;
                            case 'ㅊ':put(0,3);
                                break;
                            case 'ㅋ':put(6,4);
                                break;
                            case 'ㅌ':put(6,2);
                                break;
                            case 'ㅍ':put(4,6);
                                break;
                            case 'ㅎ':put(2,6);
                                break;
                            case 'ㄲ':put(0,1,0,4);
                                break;
                            case 'ㄸ':put(0,1,2,4);
                                break;
                            case 'ㅃ':put(0,1,0,6);
                                break;
                            case 'ㅆ':put(0,1,1,1);
                                break;
                            case 'ㅉ':put(0,1,0,5);
                                break;
                        }
                    }else if(j == 1){
                        switch (whereSound[j][i]){
                            case 'ㅏ':put(6,1);
                                break;
                            case 'ㅐ':put(7,2);
                                break;
                            case 'ㅑ':put(1,6);
                                break;
                            case 'ㅒ':put(1,6,7,2);
                                break;
                            case 'ㅓ':put(3,4);
                                break;
                            case 'ㅔ':put(5,6);
                                break;
                            case 'ㅕ':put(4,3);
                                break;
                            case 'ㅖ':put(1,4);
                                break;
                            case 'ㅗ':put(5,1);
                                break;
                            case 'ㅘ':put(7,1);
                                break;
                            case 'ㅙ':put(7,1,7,2);
                                break;
                            case'ㅚ':put(5,7);
                                break;
                            case 'ㅛ':put(1,5);
                                break;
                            case 'ㅜ':put(5,4);
                                break;
                            case 'ㅝ':put(7,4);
                                break;
                            case 'ㅞ':put(7,4,7,2);
                                break;
                            case 'ㅟ':put(5,4,7,2);
                                break;
                            case 'ㅠ':put(4,5);
                                break;
                            case 'ㅡ':put(2,5);
                                break;
                            case 'ㅢ':put(2,7);
                                break;
                            case 'ㅣ':put(5,2);
                                break;
                            case ' ':put(8);
                                break;
                        }
                    }else if(j == 2){
                        switch (whereSound[j][i]){
                            case 'ㄱ':put(4,0);
                                break;
                            case 'ㄴ':put(2,2);
                                break;
                            case 'ㄷ':put(1,2);
                                break;
                            case 'ㄹ':put(2,0);
                                break;
                            case 'ㅁ':put(2,1);
                                break;
                            case 'ㅂ':put(6,0);
                                break;
                            case 'ㅅ':put(1,0);
                                break;
                            case 'ㅇ':put(3,3);
                                break;
                            case 'ㅈ':put(5,0);
                                break;
                            case 'ㅊ':put(3,0);
                                break;
                            case 'ㅋ':put(3,2);
                                break;
                            case 'ㅌ':put(3,1);
                                break;
                            case 'ㅍ':put(2,3);
                                break;
                            case 'ㅎ':put(1,3);
                                break;
                            case 'ㄲ':put(4,0,4,0);
                                break;
                            case 'ㄸ':put(4,0,1,2);
                                break;
                            case 'ㅃ':put(4,0,6,0);
                                break;
                            case 'ㅆ':put(1,4);
                                break;
                            case 'ㅉ':put(4,0,5,0);
                                break;
                            case 'ㄳ':put(4,0,1,0);
                                break;
                            case 'ㄵ':put(2,2,5,0);
                                break;
                            case 'ㄶ':put(2,2,1,3);
                                break;
                            case 'ㄺ':put(2,0,4,0);
                                break;
                            case 'ㄻ':put(2,0,2,1);
                                break;
                            case 'ㄼ':put(2,0,6,0);
                                break;
                            case 'ㄽ':put(2,0,1,0);
                                break;
                            case 'ㄾ':put(2,0,3,1);
                                break;
                            case 'ㄿ':put(2,0,2,3);
                                break;
                            case 'ㅀ':put(2,0,1,3);
                                break;
                            case 'ㅄ':put(6,0,1,0);
                                break;
                            case ' ':put(8);
                                break;
                        }
                    }
                }
            }
        }
        return whatBraille;
    }

    public static void makeVibe(int c){
        Vibe(makeBraille(), c);
    }

    private static void Vibe(int[] s, int c){
        if(c == 1){
            for(int ss : s){
                switch(ss){
                    //000
                    case 0:
                        break;
                    //001
                    case 1:
                        break;
                    //010
                    case 2:
                        break;
                    //011
                    case 3:
                        break;
                    //100
                    case 4:shortVibe();
                        break;
                    //101
                    case 5:shortVibe();
                        break;
                    //110
                    case 6:shortVibe();shortVibe();
                        break;
                    //111
                    case 7:shortVibe();shortVibe();shortVibe();
                        break;
                    //null
                    case 8:
                        break;
                }
            }
        }else if(c == 2){
            for(int ss : s){
                switch(ss){
                    //000
                    case 0:
                        break;
                    //001
                    case 1:longVibe();
                        break;
                    //010
                    case 2:shortVibe();
                        break;
                    //011
                    case 3:shortVibe();shortVibe();
                        break;
                    //100
                    case 4:
                        break;
                    //101
                    case 5:longVibe();
                        break;
                    //110
                    case 6:
                        break;
                    //111
                    case 7:
                        break;
                    //null
                    case 8:
                        break;
                }
            }
        }
    }

    //짧은 진동 (1초)
    private static void shortVibe(){
        MainActivity.vibrator.vibrate(1000);
    }

    //긴 진동 (3초)
    private static void longVibe(){
        MainActivity.vibrator.vibrate(3000);
    }
}
