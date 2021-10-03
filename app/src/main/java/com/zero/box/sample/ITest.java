package com.zero.box.sample;

import com.zero.support.box.plugin.invoke.BoxName;

public interface ITest {
    @BoxName("test")
    String hello(String test);


}
