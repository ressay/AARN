package com.pre.omc.aarn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
/**
 * Created by ressay on 10/05/18.
 */

public class GesturesConverter
{
    final int MAXSIZE = 4;
    HashMap<int[],String> map = new HashMap<>();
    public int[] currentGesture = new int[MAXSIZE];
    int currentIndex = 0;

    public GesturesConverter()
    {
        initCurrentGesture();
        putGesturesMap("A",1,2);
        putGesturesMap("B",1,3);
        putGesturesMap("C",1,4);
        putGesturesMap("D",1,5);
        putGesturesMap("E",2,1);
        putGesturesMap("F",2,3);
        putGesturesMap("G",2,4);
        putGesturesMap("H",2,5);
        putGesturesMap("I",3,1);
        putGesturesMap("J",3,2);
        putGesturesMap("K",3,4);
        putGesturesMap("L",3,5);
        putGesturesMap("M",4,1);
        putGesturesMap("N",4,2);
        putGesturesMap("O",4,3);
        putGesturesMap("P",4,5);
        putGesturesMap("Q",5,1,2);
        putGesturesMap("R",5,1,3);
        putGesturesMap("S",5,1,4);
        putGesturesMap("T",5,1,5);
        putGesturesMap("U",5,2,1);
        putGesturesMap("V",5,2,3);
        putGesturesMap("W",5,2,4);
        putGesturesMap("X",5,2,5);
        putGesturesMap("Y",5,3,1);
        putGesturesMap("Z",5,3,2);
    }

    protected void initCurrentGesture()
    {
        currentIndex = 0;
        for (int i = 0; i < MAXSIZE; i++) {
            currentGesture[i] = -1;
        }
    }

    protected void something()
    {

    }

    protected void shiftLeft()
    {
        if(currentIndex == 0)
            return;
        for (int i = 0; i < currentIndex-1; i++) {
            currentGesture[i] = currentGesture[i+1];
        }
        currentGesture[--currentIndex] = -1;
    }

    public String addGesture(int gesture)
    {
        currentGesture[currentIndex++] = gesture;
        if(!isStartOfWord(currentGesture))
        {
            shiftLeft();
            return null;
        }
        String word = getWord(currentGesture);
        if(word != null)
        {
            initCurrentGesture();
            return word;
        }
        return null;
    }

    protected void putGesturesMap(String word,int... gestures)
    {
        int[] gests = new int[MAXSIZE];
        for (int i = 0; i < MAXSIZE; i++) {
            if(i < gestures.length)
                gests[i] = gestures[i];
            else
                gests[i] = -1;
        }
        map.put(gests,word);
    }

    String getWord(int[] gestures)
    {
        for(int[] key : map.keySet())
        {
            if(Arrays.equals(key,gestures))
                return map.get(key);
        }
        return null;
    }

    protected boolean isStartOfArray(int[] a, int[] b)
    {
        if(a.length <= b.length)
        {
            for (int i = 0; i < a.length; i++)
                if(a[i] != b[i] && a[i] != -1)
                    return false;
            return true;
        }
        return false;
    }

    boolean isStartOfWord(int[] gestures)
    {
        for(int[] key : map.keySet())
            if(isStartOfArray(gestures,key))
                return true;
        return false;
    }
}
