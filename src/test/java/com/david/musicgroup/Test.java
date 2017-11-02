package com.david.musicgroup;

public class Test {

    @org.junit.Test
    public void name() throws Exception {

        Double rule = 2.00d;
        Double amount = -3.55d;

        Double sum = rule - ((-1)*amount % rule);
        System.out.println(sum);

    }
}
