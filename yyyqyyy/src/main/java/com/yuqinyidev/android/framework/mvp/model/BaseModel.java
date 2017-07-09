package com.yuqinyidev.android.framework.mvp.model;

import com.yuqinyidev.android.framework.integration.IRepositoryManager;

/**
 * Created by RDX64 on 2017/6/28.
 */

public class BaseModel implements IModel {
    /*
    管理网络请求层，以及数据缓存层
     */
    protected IRepositoryManager mRepositoryManager;

    public BaseModel(IRepositoryManager repositoryManager) {
        this.mRepositoryManager = repositoryManager;
    }

    @Override
    public void onDestroy() {
        mRepositoryManager = null;
    }
}
