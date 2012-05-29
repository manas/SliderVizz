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

class HashLib {

    public static long hash(int algo, String key) {
        switch(algo % 11) {
        case 0:
            return RSHash(key);
        case 1:
            return JSHash(key);
        case 2:
            return PJWHash(key);
        case 3:
            return ELFHash(key);
        case 4:
            return BKDRHash(key);
        case 5:
            return SDBMHash(key);
        case 6:
            return DJBHash(key);
        case 7:
            return DEKHash(key);
        case 8:
            return BPHash(key);
        case 9:
            return FNVHash(key);
        case 10:
            return APHash(key);
        default:
            return -1;
        }
    }

    public static long RSHash(String str) {
        int b     = 378551;
        int a     = 63689;
        long hash = 0;

        for(int i = 0; i < str.length(); i++) {
            hash = hash * a + str.charAt(i);
            a    = a * b;
        }

        return hash;
    }
    /* End Of RS Hash Function */


    public static long JSHash(String str) {
        long hash = 1315423911;

        for(int i = 0; i < str.length(); i++) {
            hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
        }

        return hash;
    }
    /* End Of JS Hash Function */


    public static long PJWHash(String str) {
        long BitsInUnsignedInt = (long)(4 * 8);
        long ThreeQuarters     = (long)((BitsInUnsignedInt  * 3) / 4);
        long OneEighth         = (long)(BitsInUnsignedInt / 8);
        long HighBits          = (long)(0xFFFFFFFF) << (BitsInUnsignedInt - OneEighth);
        long hash              = 0;
        long test              = 0;

        for(int i = 0; i < str.length(); i++) {
            hash = (hash << OneEighth) + str.charAt(i);

            if((test = hash & HighBits)  != 0) {
                hash = (( hash ^ (test >> ThreeQuarters)) & (~HighBits));
            }
        }

        return hash;
    }
    /* End Of  P. J. Weinberger Hash Function */


    public static long ELFHash(String str) {
        long hash = 0;
        long x    = 0;

        for(int i = 0; i < str.length(); i++) {
            hash = (hash << 4) + str.charAt(i);

            if((x = hash & 0xF0000000L) != 0) {
                hash ^= (x >> 24);
            }
            hash &= ~x;
        }

        return hash;
    }
    /* End Of ELF Hash Function */


    public static long BKDRHash(String str) {
        long seed = 131; // 31 131 1313 13131 131313 etc..
        long hash = 0;

        for(int i = 0; i < str.length(); i++) {
            hash = (hash * seed) + str.charAt(i);
        }

        return hash;
    }
    /* End Of BKDR Hash Function */


    public static long SDBMHash(String str) {
        long hash = 0;

        for(int i = 0; i < str.length(); i++) {
            hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
        }

        return hash;
    }
    /* End Of SDBM Hash Function */


    public static long DJBHash(String str) {
        long hash = 5381;

        for(int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) + hash) + str.charAt(i);
        }

        return hash;
    }
    /* End Of DJB Hash Function */


    public static long DEKHash(String str) {
        long hash = str.length();

        for(int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
        }

        return hash;
    }
    /* End Of DEK Hash Function */


    public static long BPHash(String str) {
        long hash = 0;

        for(int i = 0; i < str.length(); i++) {
            hash = hash << 7 ^ str.charAt(i);
        }

        return hash;
    }
    /* End Of BP Hash Function */


    public static long FNVHash(String str) {
        long fnv_prime = 0x811C9DC5;
        long hash = 0;

        for(int i = 0; i < str.length(); i++) {
            hash *= fnv_prime;
            hash ^= str.charAt(i);
        }

        return hash;
    }
    /* End Of FNV Hash Function */


    public static long APHash(String str) {
        long hash = 0xAAAAAAAA;

        for(int i = 0; i < str.length(); i++) {
            if ((i & 1) == 0) {
                hash ^= ((hash << 7) ^ str.charAt(i) * (hash >> 3));
            } else {
                hash ^= (~((hash << 11) + str.charAt(i) ^ (hash >> 5)));
            }
        }

        return hash;
    }
    /* End Of AP Hash Function */

}
