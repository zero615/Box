package com.zero.box;

import com.zero.support.box.plugin.invoke.BoxName;

public interface ITestCaller {
    @BoxName("test")
    String caller(String test);
}
