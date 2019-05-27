package org.femtoframework.coin.ext;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleModelTest {

    @Test
    public void getUid() {

        SimpleModel model = new SimpleModel();
        System.out.println(model.getUid());
    }
}