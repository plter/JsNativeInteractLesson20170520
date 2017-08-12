package org.liquidplayer.androidjscoreexample.runjs;

/**
 * Created by plter on 5/20/17.
 */

public interface IRunJsContext {


    void trace(String msg);

    void load(String fileName);

    int createButton();

    void addButton(int btnId);

    void setButtonText(int btnId, String text);

}
