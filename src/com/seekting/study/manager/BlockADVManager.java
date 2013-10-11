
package com.seekting.study.manager;

import java.util.List;

import com.seekting.study.view.BlockADVView;

/**
 * @author ’≈–ÀÕ¶
 */
public class BlockADVManager {

    BlockADVView mAdvView;

    private static BlockADVManager instance;

    private List<Object> tabs;

    private BlockADVManager() {

    }

    public static BlockADVManager getInstance() {

        if (instance == null) {
            instance = new BlockADVManager();
        }
        return instance;
    }

    public void switchTab() {

    }

    public void setBlockADVCount(Object tab, int count) {

    }

    public void finish() {

    }

    public void hide() {

    }

}
