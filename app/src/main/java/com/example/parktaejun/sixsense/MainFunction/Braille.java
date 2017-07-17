package com.example.parktaejun.sixsense.MainFunction;

import com.example.parktaejun.sixsense.SMSFunction.SendMessageActivity;

import java.util.Arrays;

/**
 * Created by parktaejun on 2017. 6. 11..
 */

public class Braille {
    private static final String[] FirstSound = {
            "000100", "100100", "010100", "000010", "100010", "000110", "110110", "000101", "000011", "110100", "110010", "100110", "010110"
    };
    private static final String[] MiddleSound = {
            "110001", "001110", "011100", "100011", "101001", "001101", "101100", "100101", "010101", "101010", "111010", "101110", "111001", "101111", "111100", "010111"
    };
    private static final String[] LastSound = {
            "001010", "010001", "110000", "001000", "011011", "101000", "011000", "011010", "011001", "010011", "001011", "100000", "010010", "010000"
    };
    private static final String[] Special = {
            "111111", "001001", "001100", "000001", "000000", "001111"
    };
    private static int count = 0; // 점자를 입력하는 칸의 횟수
    private static int SFcount = 0; // 초성에서 특별한 경우를 체크해줌 / 0 : 일반, 1 : 쌍자음
    private static int SLcount = 0; // 종성에서 특별한 경우를 체크해줌 / 0 : 일반, 1 : 'ㄱ'겹받침, 2 : 'ㄴ'겹받침, 3 : 'ㄹ'겹받침, 4 : 'ㅂ'겹받침
    private static int Scount = 0; // 예외의 특별한 경우를 체크해줌  / 0 : 일반, 1 : 자음 혹은 모음이 단독으로 쓰일 때, 2 : 모음자 뒤에 '예'가 나올 떄 사이에 적거나 ‘ㅑ, ㅘ, ㅜ, ㅝ’ 뒤에 '애'가 이어 나올 떄 사이에 적는 붙임표, 3 : 숫자 앞에 붙여지는 수
    private static char word[] = {
            ' ',
            ' ',
            ' '
    }; // 각 배열에 초성, 중성, 종성이 들어감

    private static boolean gesture[] = new boolean[6]; // 점자 6칸에 위로 드래그, 아래로 드래그를 나타냄
    private static String braille = ""; // 점자 6칸을 각 형태소로 나타냄
    private static char text = ' '; // 각 형태소를 합쳐 한 글자로 나타냄
    private static int at = 0; // 점자의 위치가 초성인지 중성인지 종성인지 나타냄

    // 점자의 몇번째 칸인지 확인해줌
    public static int countCheck() {
        return count+1;
    }

    // 제스처가 위 드래그인지, 아래 드래그인지 파악하여 제스처 배열에 값을 넣어줌
    public static void recGesture(boolean gesture) {
        if(count == 6)initGesture();// 제스처 변수를 초기화
        if (gesture) {
            Braille.gesture[count] = true;
        } else {
            Braille.gesture[count] = false;
        }
        count++;
        if(count == 6)isSix();// 배열 6칸이 다 찼는지 확인하여 isSix() 함수를 실행함
    }

    // 배열 6칸이 다 찼는지 확인하여 1. 점자를 형태소로 바꿈, 2. 제스처 변수를 초기화, 3. 메인액티비티에 텍스트 추가
    private static void isSix() {
        gestureToBraille(Braille.gesture); // 점자를 형태소로 바꿈
    }

    // 제스처를 점자(형태소)로 나타냄
    private static void gestureToBraille(boolean gesture[]) {
        for (boolean g : gesture) {
            if (g) {
                braille += '1'; // 제스처가 참(위로 드래그)이면 1을 추가함
            } else {
                braille += '0'; // 제스처가 거짓(아래로 드래그)이면 0을 추가함
            }
        }
        brailleToText(braille);
        braille = "";
    }

    // 형태소를 텍스트로 바꿔줌
    private static void brailleToText(String b) {
        switch (hangulAt(b)) {
            //초성일 때
            case 1:
                for (int i = 0; i < 3; i++) word[i] = ' '; // word 배열을 초기화함
                if (SFcount != 0) {
                    switch (b) {
                        case "000100":
                            text = 'ㄲ';
                            if (Scount == 1) initFirstWord();
                            break;
                        case "010100":
                            if(Scount == 3)initNumber(9);
                            else{
                                text = 'ㄸ';
                                if (Scount == 1) initFirstWord();
                            }

                            break;
                        case "000110":
                            text = 'ㅃ';
                            if (Scount == 1) initFirstWord();
                            break;
                        case "000001":
                            text = 'ㅆ';
                            if (Scount == 1) initFirstWord();
                            break;
                        case "000101":
                            text = 'ㅉ';
                            if (Scount == 1) initFirstWord();
                            break;
                        default:
                            break;
                    }
                    SFcount = 0;
                } else {
                    switch (b) {
                        case "000100":
                            text = 'ㄱ';
                            if (Scount == 1) initFirstWord();
                            break;
                        case "100100":
                            if(Scount == 3)initNumber(3);
                            else{
                                text = 'ㄴ';
                                if (Scount == 1) initFirstWord();
                            }

                            break;
                        case "010100":
                            if(Scount == 3)initNumber(9);
                            else{
                                text = 'ㄷ';
                                if (Scount == 1) initFirstWord();
                            }

                            break;
                        case "000010":
                            text = 'ㄹ';
                            if (Scount == 1) initFirstWord();
                            break;
                        case "100010":
                            if(Scount==3)initNumber(5);
                            else{
                                text = 'ㅁ';
                                if (Scount == 1) initFirstWord();
                            }

                            break;
                        case "000110":
                            text = 'ㅂ';
                            if (Scount == 1) initFirstWord();
                            break;
                        case "110110":
                            if(Scount == 3)initNumber(7);
                            else{
                                text = 'ㅇ';
                                if (Scount == 1) initFirstWord();
                            }

                            break;
                        case "000101":
                            text = 'ㅈ';
                            if (Scount == 1) initFirstWord();
                            break;
                        case "000011":
                            text = 'ㅊ';
                            if (Scount == 1) initFirstWord();
                            break;
                        case "110100":
                            if(Scount==3)initNumber(6);
                            else{
                                text = 'ㅋ';
                                if (Scount == 1) initFirstWord();
                            }
                            break;
                        case "110010":
                            if(Scount == 3)initNumber(8);
                            else{
                                text = 'ㅌ';
                                if (Scount == 1) initFirstWord();
                            }
                            break;
                        case "100110":
                            if(Scount==3)initNumber(4);
                            else{
                                text = 'ㅍ';
                                if (Scount == 1) initFirstWord();
                            }

                            break;
                        case "010110":
                            if(Scount == 3)initNumber(0);
                            else{
                                text = 'ㅎ';
                                if (Scount == 1) initFirstWord();
                            }

                            break;
                        default:
                            break;
                    }
                }
                word[0] = text;
                break;

            //중성일 떄
            case 2:
                if (SFcount != 0) SFcount = 0;
                switch (b) {
                    case "110001":
                        text = 'ㅏ';
                        if (Scount == 1) {
                            initMiddleWord();
                            break;
                        } else {
                            if (word[0] == ' ') word[0] = 'ㅇ';
                        }
                        break;
                    case "001110":
                        text = 'ㅑ';
                        if (Scount == 1) {
                            initMiddleWord();
                            break;
                        } else {
                            if (word[0] == ' ') word[0] = 'ㅇ';
                        }
                        break;
                    case "011100":
                        text = 'ㅓ';
                        if (Scount == 1) {
                            initMiddleWord();
                            break;
                        } else {
                            if (word[0] == ' ') word[0] = 'ㅇ';
                        }
                        break;
                    case "100011":
                        text = 'ㅕ';
                        if (Scount == 1) {
                            initMiddleWord();
                            break;
                        } else {
                            if (word[0] == ' ') word[0] = 'ㅇ';
                        }
                        break;
                    case "101001":
                        text = 'ㅗ';
                        if (Scount == 1) {
                            initMiddleWord();
                            break;
                        } else {
                            if (word[0] == ' ') word[0] = 'ㅇ';
                        }
                        break;
                    case "001101":
                        text = 'ㅛ';
                        if (Scount == 1) {
                            initMiddleWord();
                            break;
                        } else {
                            if (word[0] == ' ') word[0] = 'ㅇ';
                        }
                        break;
                    case "101100":
                        text = 'ㅜ';
                        if (Scount == 1) {
                            initMiddleWord();
                            break;
                        } else {
                            if (word[0] == ' ') word[0] = 'ㅇ';
                        }
                        break;
                    case "100101":
                        text = 'ㅠ';
                        if (Scount == 1) {
                            initMiddleWord();
                            break;
                        } else {
                            if (word[0] == ' ') word[0] = 'ㅇ';
                        }
                        break;
                    case "010101":
                        text = 'ㅡ';
                        if (Scount == 1) {
                            initMiddleWord();
                            break;
                        } else {
                            if (word[0] == ' ') word[0] = 'ㅇ';
                        }
                        break;
                    case "101010":
                        text = 'ㅣ';
                        if (Scount == 1) {
                            initFirstWord();
                            break;
                        } else {
                            if (word[0] == ' ') word[0] = 'ㅇ';
                        }
                        break;
                    case "111010":
                        text = 'ㅐ';
                        if (Scount == 1) {
                            initMiddleWord();
                            break;
                        } else {
                            if (word[0] == ' ') word[0] = 'ㅇ';
                        }
                        break;
                    case "101110":
                        text = 'ㅔ';
                        if (Scount == 1) {
                            initMiddleWord();
                            break;
                        } else {
                            if (word[0] == ' ') word[0] = 'ㅇ';
                        }
                        break;
                    case "111001":
                        text = 'ㅘ';
                        if (Scount == 1) {
                            initMiddleWord();
                            break;
                        } else {
                            if (word[0] == ' ') word[0] = 'ㅇ';
                        }
                        break;
                    case "101111":
                        text = 'ㅚ';
                        if (Scount == 1) {
                            initMiddleWord();
                            break;
                        } else {
                            if (word[0] == ' ') word[0] = 'ㅇ';
                        }
                        break;
                    case "111100":
                        text = 'ㅝ';
                        if (Scount == 1) {
                            initMiddleWord();
                            break;
                        } else {
                            if (word[0] == ' ') word[0] = 'ㅇ';
                        }
                        break;
                    case "010111":
                        text = 'ㅢ';
                        if (Scount == 1) {
                            initMiddleWord();
                            break;
                        } else {
                            if (word[0] == ' ') word[0] = 'ㅇ';
                        }
                        break;
                    default:
                        break;
                }
                word[1] = text;
                break;

            //종성일 때
            case 3:
                if (SLcount != 0) {
                    switch (SLcount) {
                        //겹받침 'ㄱ' 일 때
                        case 1:
                            SLcount = 0;
                            switch (b) {
                                case "100000":
                                    if(Scount == 3)initNumber(1);
                                    else{
                                        text = 'ㄲ';
                                        if (Scount == 1) initLastWord();
                                    }

                                    break;
                                case "001000":
                                    text = 'ㄳ';
                                    if (Scount == 1) initLastWord();
                                    break;
                                default:
                                    break;
                            }
                            break;
                        //겹받침 'ㄴ' 일 때
                        case 2:
                            SLcount = 0;
                            switch (b) {
                                case "101000":
                                    text = 'ㄵ';
                                    if (Scount == 1) initLastWord();
                                    break;
                                case "001011":
                                    text = 'ㄶ';
                                    if (Scount == 1) initLastWord();
                                    break;
                                default:
                                    break;
                            }
                            break;
                        //겹받침 'ㄹ' 일 때
                        case 3:
                            SLcount = 0;
                            switch (b) {
                                case "100000":
                                    if(Scount == 3)initNumber(1);
                                    else{
                                        text = 'ㄺ';
                                        if (Scount == 1) initLastWord();
                                    }

                                    break;
                                case "010001":
                                    text = 'ㄻ';
                                    if (Scount == 1) initLastWord();
                                    break;
                                case "110000":
                                    if(Scount==3)initNumber(2);
                                    else{
                                        text = 'ㄼ';
                                        if (Scount == 1) initLastWord();
                                    }

                                    break;
                                case "001000":
                                    text = 'ㄽ';
                                    if (Scount == 1) initLastWord();
                                    break;
                                case "011001":
                                    text = 'ㄾ';
                                    if (Scount == 1) initLastWord();
                                    break;
                                case "010011":
                                    text = 'ㄿ';
                                    if (Scount == 1) initLastWord();
                                    break;
                                case "001011":
                                    text = 'ㅀ';
                                    if (Scount == 1) initLastWord();
                                    break;
                                default:
                                    break;
                            }
                            break;
                        //겹받침 'ㅂ 일 때
                        case 4:
                            SLcount = 0;
                            switch (b) {
                                case "001000":
                                    text = 'ㅄ';
                                    if (Scount == 1) initLastWord();
                            }
                            break;
                        default:
                            break;
                    }
                    SLcount = 0;
                } else {
                    switch (b) {
                        case "001010":
                            text = 'ㄷ';
                            break;
                        case "010001":
                            text = 'ㅁ';
                            break;
                        case "001000":
                            text = 'ㅅ';
                            break;
                        case "011011":
                            text = 'ㅇ';
                            break;
                        case "101000":
                            text = 'ㅈ';
                            break;
                        case "011000":
                            text = 'ㅊ';
                            break;
                        case "011010":
                            text = 'ㅋ';
                            break;
                        case "011001":
                            text = 'ㅌ';
                            break;
                        case "010011":
                            text = 'ㅠ';
                            break;
                        case "001011":
                            text = 'ㅎ';
                            break;
                        //겹받침 'ㄱ'
                        case "100000":
                            if(Scount == 3)initNumber(1);
                            else{
                                SLcount = 1;
                            }
                            break;
                        //겹받침 'ㄴ'
                        case "010010":
                            SLcount = 2;
                            break;
                        //겹받침 'ㄹ'
                        case "010000":
                            SLcount = 3;
                            break;
                        //겹밭침 'ㅂ'
                        case "110000":
                            if(Scount==3)initNumber(2);
                            else{
                                SLcount = 4;
                            }
                            break;
                        default:
                            break;
                    }
                }
                word[2] = text;
                if (SLcount == 0) initLastWord();
                break;

            //특별한 점자일 때
            case 4:
                switch (b) {
                    //자음 혹은 모음자가 단독으로 쓰일 때 적는 온표
                    case "111111":
                        Scount = 1;
                        break;
                    //모음자 뒤에 '예'가 나올 떄 사이에 적거나 ‘ㅑ, ㅘ, ㅜ, ㅝ’ 뒤에 '애'가 이어 나올 떄 사이에 적는 붙임표
                    case "001001":
                        Scount = 2;
                        break;
                    //종성의 'ㅆ' 와 모음의 'ㅖ'
                    case "001100":
                        if (word[1] != ' ') {
                            text = 'ㅆ';
                            word[2] = text;
                            initLastWord();
                        } else {
                            text = 'ㅖ';
                            if (Scount == 1) {
                                initMiddleWord();
                                break;
                            } else {
                                if (word[0] == ' ') word[0] = 'ㅇ';
                            }
                            word[1] = text;
                        }
                        break;
                    //'ㅅ' 혹은 된소리 글자 'ㄲ, ㄸ, ㅃ, ㅆ, ㅉ'를 표기할 때 각 'ㄱ, ㄷ, ㅂ, ㅅ, ㅈ' 앞에 적는 된소리표
                    case "000001":
                        text = 'ㅅ';
                        SFcount++;
                        break;
                    case "000000":
                        text = ' ';
                        word[0]=' ';word[1]=' ';word[2]=' ';initLastWord();
                        initSpecial();
                        break;
                    case "001111":
                        Scount=3;
                        break;
                    default:
                        break;
                }
            default:
                break;
        }

    }

    //점자를 인식하여 초성, 중성, 종성인지 파악하는 메소드
    private static int hangulAt(String b) {
        if (Arrays.asList(FirstSound).contains(b)) {
            at = 1;
        } else if (Arrays.asList(MiddleSound).contains(b)) {
            if (Scount == 0 || word[2] == ' ') initMiddleWord(); // 특별한 경우가 없이 다음 글자를 위해서 글자를 조합하는 메소드
            at = 2;
        } else if (Arrays.asList(LastSound).contains(b)) {
            if (Scount == 0) initLastWord();
            at = 3;
        } else if (Arrays.asList(Special).contains(b)) {
            at = 4;
        }
        return at;
    }

    // 초성 글자를 조합하여 메인액티비티에 추가하고, word 배열을 초기화함
    private static void initFirstWord() {
        SendMessageActivity.addFirstText(Hangul.CombineHangul(word)); // 글자를 조합하여 메인액티비티에 추가함
    }

    // 초성 글자를 조합하여 메인액티비티에 추가하고, word 배열을 초기화함
    private static void initMiddleWord() {
        SendMessageActivity.addMiddleText(Hangul.CombineHangul(word)); // 글자를 조합하여 메인액티비티에 추가함
    }

    // 초성 글자를 조합하여 메인액티비티에 추가하고, word 배열을 초기화함
    private static void initLastWord() {
        SendMessageActivity.addLastText(Hangul.CombineHangul(word)); // 글자를 조합하여 메인액티비티에 추가함
    }


    // 숫자를 텍스트에 추가
    private static void initNumber(int n){
        SendMessageActivity.addNumber(n);
        for (int i = 0; i < 3; i++) word[i] = ' '; // word 배열을 초기화함
    }

    // SFcount, SLcount, Scount 를 모두 초기화 해줌
    private static void initSpecial(){
        SFcount=0;SLcount=0;Scount=0;
        for (int i = 0; i < 3; i++) word[i] = ' '; // word 배열을 초기화함
    }

    private static void initWord(){
        for (int i = 0; i < 3; i++) word[i] = ' '; // word 배열을 초기화함
    }

    // 제스처를 초기화함
    private static void initGesture() {
        count = 0; // 점자 칸의 차례를 초기화
        for (int i = 0; i < gesture.length; i++) {
            gesture[i] = false; // 제스처 변수를 모두 거짓으로 초기화
        }
        SendMessageActivity.initBraille(); // 점자가 나타나는 텍스트를 모두 빈 텍스트로 초기화
    }

    //전부 초기화함
    public static void init(){
        initWord();
        initSpecial();
        initGesture();
        count = 0;
        braille = "";
        text=' ';
        at = 0;
    }
}
