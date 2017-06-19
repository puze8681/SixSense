package com.example.parktaejun.sixsense;

/**
 * Created by parktaejun on 2017. 6. 11..
 */


public class Hangul {
    private static final char[] FirstSound = {
            'ㄱ','ㄲ','ㄴ','ㄷ','ㄸ','ㄹ','ㅁ','ㅂ','ㅃ','ㅅ','ㅆ','ㅇ','ㅈ','ㅉ','ㅊ','ㅋ','ㅌ','ㅍ','ㅎ'
    };
    private static final char[] MiddleSound = {
            'ㅏ','ㅐ','ㅑ','ㅒ','ㅓ','ㅔ','ㅕ','ㅖ','ㅗ','ㅘ','ㅙ','ㅚ','ㅛ','ㅜ','ㅝ','ㅞ','ㅟ','ㅠ','ㅡ','ㅢ','ㅣ'
    };
    private static final char[] LastSound = {
            ' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ',
            'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ',
            'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    private static char[][] whereSound = {
            {},
            {},
            {}
    };

    public static char[][] getWhere(){
        return whereSound;
    }

    public static boolean IsHangul(char c)
    {
        if(c<0xAC00||c>0xD743)
        {
            return false;
        }
        return true;
    }

    public static boolean IsHangul(String s)
    {
        if(s == null)
            return false;
        int len = s.length();
        if(len == 0)
            return false;
        for(int i = 0;i<len;i++)
        {
            if(IsHangul(s.charAt(i)) == false)
                return false;
        }
        return true;
    }

    public static int[] split(char c){
        int sub[] = new int[3];
        sub[0] = (c - 0xAC00) / (21*28); //초성의 위치
        sub[1] = ((c - 0xAC00) % (21*28)) / 28; //중성의 위치
        sub[2] = (c -0xAC00) % (28);//종성의 위치
        return sub;
    }

    public static String HangulAlphabet(int[] c)
    {
        char[] rt = new char[3];
        rt[0] = FirstSound[c[0]];
        rt[1] = MiddleSound[c[1]];
        if(c[2]!=0)
            rt[2] = LastSound[c[2]];
        String returnString = "";
        returnString = returnString + rt[0] + rt[1] + rt[2];

        whereSound[0][whereSound[0].length] = rt[0];
        whereSound[1][whereSound[1].length] = rt[1];
        whereSound[2][whereSound[2].length] = rt[2];

        return returnString;
    }

    //초성 중성 종성을 받아와 글자를 조합하는 메소드
    public static char CombineHangul(char[] c) //종성이 없어도 줘야함
    {
        int[] hindex = new int[3];
        for(int i = 0; i<FirstSound.length;i++)
        {
            if(FirstSound[i] == c[0]) {
                hindex[0] = i;
                break;
            }
        }
        for(int i = 0; i<MiddleSound.length;i++)
        {
            if(MiddleSound[i] == c[1]) {
                hindex[1] = i;
                break;
            }
        }
        for(int i = 0; i<LastSound.length;i++)
        {
            if(LastSound[i] == c[2]) {
                hindex[2] = i;
                break;
            }
            if(i == LastSound.length - 1)
                hindex[2] = 0;
        }
        return Combine(hindex);
    }

    public static char Combine(int[] c)
    {
        char[] chars = new char[1];
        chars[0] = (char) (0xAC00 + (c[0]*21*28) + (c[1]*28) + c[2]);
        return chars[0];
    }
}