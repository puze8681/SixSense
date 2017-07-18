package com.example.parktaejun.sixsense.Function;

/**
 * Created by parktaejun on 2017. 6. 11..
 */


public class Hangul {
    private static final char[] FirstSound = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };
    private static final char[] MiddleSound = {
            'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
    };
    private static final char[] LastSound = {
            ' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ',
            'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ',
            'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    private static int position = 0;
    private static char[][] whereSound = new char[3][]; // 초성, 중성, 종성의 위치별로 형태소를 넣음

    // whereSound 초기화 함수
    public static void initWhereSound() {
        whereSound = new char[3][];
        initPosition();
    }

    // whereSound 받아오는 함수
    public static char[][] getWhere() {
        return whereSound;
    }

    public static boolean IsHangul(char c) {
        if (c < 0xAC00 || c > 0xD743) {
            return false;
        }
        return true;
    }

    public static boolean IsHangul(String s) {
        if (s == null)
            return false;
        int len = s.length();
        if (len == 0)
            return false;
        for (int i = 0; i < len; i++) {
            if (IsHangul(s.charAt(i)) == false)
                return false;
        }
        return true;
    }

    public static int[] split(char c) {
        if(Hangul.IsHangul(c)) {
            int sub[] = new int[3];
            sub[0] = (c - 0xAC00) / (21 * 28); //초성
            sub[1] = ((c - 0xAC00) % (21 * 28)) / 28; //중성
            sub[2] = (c - 0xAC00) % (28);//종성
            return sub;
        }
        else {
            int t[] = new int[]{(c-0xAC00)};
            return t;
        }
    }

    public static String HangulAlphabet(int[] c) {
        String returnString = "";
        if(c.length > 2) {
            char[] rt = new char[3];
            if (c[0] == 0) rt[0] = ' ';
            else rt[0] = FirstSound[c[0]];
            if (c[1] == 0) rt[1] = ' ';
            else rt[1] = MiddleSound[c[1]];
            if (c[2] == 0) rt[2] = ' ';
            else rt[2] = LastSound[c[2]];
            returnString = returnString + rt[0] + rt[1] + rt[2];
            whereSound[0][position] = rt[0];
            whereSound[1][position] = rt[1];
            whereSound[2][position] = rt[2];
        }
        else
        {
            switch(c[0])
            {
                case -0xABD0:
                    whereSound[0][position] = '0';
                    returnString += '0';
                    break;
                case -0xABCF:
                    whereSound[0][position] = '1';
                    returnString += '1';
                    break;
                case -0xABCE:
                    whereSound[0][position] = '2';
                    returnString += '2';
                    break;
                case -0xABCD:
                    whereSound[0][position] = '3';
                    returnString += '3';
                    break;
                case -0xABCC:
                    whereSound[0][position] = '4';
                    returnString += '4';
                    break;
                case -0xABCB:
                    whereSound[0][position] = '5';
                    returnString += '5';
                    break;
                case -0xABCA:
                    whereSound[0][position] = '6';
                    returnString += '6';
                    break;
                case -0xABC9:
                    whereSound[0][position] = '7';
                    returnString += '7';
                    break;
                case -0xABC8:
                    whereSound[0][position] = '8';
                    returnString += '8';
                    break;
                case -0xABC7:
                    whereSound[0][position] = '9';
                    returnString += '9';
                    break;
            }
            whereSound[1][position] = ' ';
            whereSound[2][position] = ' ';
        }
        position++;
        return returnString;
    }

    //초성 중성 종성을 받아와 글자를 조합하는 메소드
    public static char CombineHangul(char[] c) //종성이 없어도 줘야함
    {
        if((c[0]==' ') && (c[1]==' ') && (c[2]==' ')) return ' ';
        int[] hindex = new int[3];
        for (int i = 0; i < FirstSound.length; i++) {
            if (FirstSound[i] == c[0]) {
                hindex[0] = i;
                break;
            }
        }
        for (int i = 0; i < MiddleSound.length; i++) {
            if (MiddleSound[i] == c[1]) {
                hindex[1] = i;
                break;
            }
        }
        for (int i = 0; i < LastSound.length; i++) {
            if (LastSound[i] == c[2]) {
                hindex[2] = i;
                break;
            }
            if (i == LastSound.length - 1)
                hindex[2] = 0;
        }
        return Combine(hindex);
    }

    public static char Combine(int[] c) {
        char[] chars = new char[1];
        chars[0] = (char) (0xAC00 + (c[0] * 21 * 28) + (c[1] * 28) + c[2]);
        return chars[0];
    }

    public static void initPosition() {
        position = 0;
    }
}