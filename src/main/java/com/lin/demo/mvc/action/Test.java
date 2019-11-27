package com.lin.demo.mvc.action;

import com.sun.xml.internal.fastinfoset.util.StringArray;

import java.security.SecureRandom;
import java.util.UUID;

public class Test {
    public static String hexToDecimal(String uuid){
//        if (StringUtils.isEmpty(uuid)){
//            return null;
//        }
        StringBuilder result = new StringBuilder();
        char[] array = uuid.toCharArray();
        for (char index : array){
            int valueTen = Integer.parseInt(String.valueOf(index),16);
            result.append(valueTen);
        }
        return result.toString();
    }

    public static int caclute(String decimal,StringArray numberArray){
        if (decimal.length() != numberArray.getSize()){
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < decimal.length(); i ++ ){
            System.out.println(Integer.valueOf(decimal.charAt(i)) + " * " + Integer.valueOf(numberArray.get(i)) + "=" + Integer.valueOf(decimal.charAt(i)) * Integer.valueOf(numberArray.get(i)));
            sum = sum + (Integer.valueOf(decimal.charAt(i)) * Integer.valueOf(numberArray.get(i)));
            System.out.println(sum);
        }
        System.out.println(sum);
        System.out.println(sum % 16);
        int r = sum % 16;
        if(r<10){
            return (char)(r+'0');
        } else {
            return (char)(r+'a'-10);
        }
    }


    public static StringArray numbering(String decimal){
//        if (StringUtils.isEmpty(decimal)){
//            return null;
//        }

        StringArray result = new StringArray();
        int j = 1;
        for (int i = 0;i < decimal.length(); i++){
            result.add(String.valueOf(j));
            if (j == 10){
                j = 0;
            }
            j ++;
        }
        return result;
    }

    public static void main(String[] args) {

        long rnd = 1610465248;
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[32];
        random.nextBytes(bytes);
        System.out.println(new String(bytes));
        System.out.println(Integer.toHexString(1610465248));
        long gClockSeq = rnd & 0x1FFF;
        System.out.println("gclock" + gClockSeq);
        System.out.println(UUID.randomUUID().version());
        System.out.println("c0aaf972-fee2-46ef-bfca-ffb8a35516ef".replace("-",""));
        String re = hexToDecimal("c0aaf972-fee2-46ef-bfca-ffb8a35516ef".replace("-",""));
        System.out.println(re);
        StringArray numbersArray = numbering(re);
        System.out.println("----------");
        System.out.println(caclute(re,numbersArray));
//        System.out.println("re = " + numbersArray);

    }
}
