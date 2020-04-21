package com.lawyus.study.generic;

import java.util.List;
import java.util.Map;

/**
 * TODO describe please!
 *
 * @author yushing
 * @since 2020/4/5
 */
public class Tree<T> {

    private List<T> seed;

    public List<T> getSeed() {
        return seed;
    }

    public void setSeed(List<T> seed) {
        this.seed = seed;
    }
}
