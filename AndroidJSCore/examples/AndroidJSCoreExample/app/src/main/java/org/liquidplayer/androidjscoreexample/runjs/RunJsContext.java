package org.liquidplayer.androidjscoreexample.runjs;

import org.liquidplayer.webkit.javascriptcore.JSContext;

/**
 * Created by plter on 5/20/17.
 */

public class RunJsContext extends JSContext implements IRunJsContext {

    public RunJsContext() {
        super(IRunJsContext.class);
    }

    @Override
    public void trace(String msg) {
        System.out.println(msg);
    }
}
