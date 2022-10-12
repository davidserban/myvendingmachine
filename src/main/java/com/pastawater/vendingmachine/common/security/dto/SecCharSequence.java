package com.pastawater.vendingmachine.common.security.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pastawater.vendingmachine.common.security.serialization.SecCharSequenceDeserializer;

import java.util.Arrays;

@JsonDeserialize(using = SecCharSequenceDeserializer.class)
public class SecCharSequence implements CharSequence {

    private final char[] charArray;

    public SecCharSequence(char[] charArray){
        this.charArray = new char[charArray.length];
        System.arraycopy(charArray, 0, this.charArray, 0, charArray.length);
    }


    public SecCharSequence(char[] charArray, int start, int end){
        this.charArray = new char[end - start];
        System.arraycopy(charArray, start, this.charArray, 0, this.charArray.length);
    }

    @Override
    public int length() {
        return charArray.length;
    }

    @Override
    public char charAt(int index) {
        return charArray[index];
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new SecCharSequence(this.charArray, start, end);
    }

    //Defeating the whole purpose of using char array but the password encoder uses toString:)
    @Override
    public String toString() {
        return String.valueOf(charArray);
    }

    public void clear(){
        Arrays.fill(charArray, '0');
    }
}