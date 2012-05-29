/****Copyright (c) <2012> <Manas Agrawal>.
All rights reserved.

Redistribution and use in source and binary forms are permitted
provided that the above copyright notice and this paragraph are
duplicated in all such forms and that any documentation,
advertising materials, and other materials related to such
distribution and use acknowledge that the software was developed
by the <organization>.  The name of the
University may not be used to endorse or promote products derived
from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.*****/


package edu.osu.cse;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;



public class CMSketch {
    private int[][] sketch;
    public final static int d;
    public final static int w;

    public int estimate(String key) {
        int estimate = Integer.MAX_VALUE;
        for (int i = 0; i < sketch.length; i++) {
            int candidate = sketch[i][ (int) (Math.abs(HashLib.hash(i, key)) % w)];
            estimate = Math.min(candidate, estimate);
        }
        return estimate;
    }

    public void populate(String key, int count){
    	for (int i = 0; i < sketch.length; i++) {
            sketch[i][(int)(Math.abs(HashLib.hash(i, key)) % w)] = sketch[i][(int)(Math.abs(HashLib.hash(i, key)) % w)] + count;
    	}    
    	
    }
    
    public synchronized int increment(String key) {
        int estimate = Integer.MAX_VALUE;
        for (int i = 0; i < sketch.length; i++) {
            int result = ++sketch[i][   (int) (Math.abs(HashLib.hash(i, key)) % w) ];
       //     System.out.println(key) ;
       //     System.out.println((int) (Math.abs(HashLib.hash(i, key)) % w)) ;
            estimate = Math.min(estimate, result);
        }
        return estimate;
    }

    public CMSketch() {
    	sketch = new int[d][w] ;
    	System.out.println(sketch.length+"here");
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < w; j++) {
                sketch[i][j] = 0;
            }
        }
    }

    public int[][] getMatrix() {
        return sketch;
    }

    public CMSketch combine(CMSketch sk) {
        int[][] matrix = sk.getMatrix();
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < w; j++) {
                sketch[i][j] += matrix[i][j];
            }
        }
        return this;
    }

    public void write(DataOutput out) throws IOException {
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < w; j++) {
                out.writeInt(sketch[i][j]);
            }
        }
    }

    public void readFields(DataInput in) throws IOException {
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < w; j++) {
                sketch[i][j] = in.readInt();
            }
        }
    }

    public String toString() {
        String ret = "";
        for (int i = 0; i < d; i++){
            for (int j = 0; j < w; j++) {
                ret += Integer.toString(sketch[i][j]) + " ";
            }
        ret += "\n";
        }

        return ret;
    }

    static {
        d = 10;
        w = 50;
    }
}
