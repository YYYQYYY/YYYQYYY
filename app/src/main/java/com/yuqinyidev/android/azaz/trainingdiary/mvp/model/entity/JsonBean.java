package com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

public class JsonBean implements IPickerViewData {


    /**
     * level1Key : TTT
     * level2List : [{"level2Key":"TTTT","level3List":[1,2,3]}]
     */

    private String level1Key;
    private List<Level2Bean> level2List;

    public String getLevel1Key() {
        return level1Key;
    }

    public void setLevel1Key(String level1Key) {
        this.level1Key = level1Key;
    }

    public List<Level2Bean> getLevel2List() {
        return level2List;
    }

    public void setLevel2List(List<Level2Bean> level2List) {
        this.level2List = level2List;
    }

    @Override
    public String getPickerViewText() {
        return this.level1Key;
    }

    public class Level2Bean {
        /**
         * level2Key : TTTT
         * level3List : [1,2,3
         */

        private String level2Key;
        private List<Integer> level3List;

        public String getLevel2Key() {
            return level2Key;
        }

        public void setLevel2Key(String level2Key) {
            this.level2Key = level2Key;
        }

        public List<Integer> getLevel3List() {
            return level3List;
        }

        public void setLevel3List(List<Integer> level3List) {
            this.level3List = level3List;
        }

    }

}
