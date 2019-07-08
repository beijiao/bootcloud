package com.dj.p2p.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dj.p2p.pojo.BaseData;
import com.dj.p2p.pojo.BaseDataVo;

import java.util.List;
import java.util.Map;

public interface BaseDataService extends IService<BaseData> {

    /**
     * 注册页面所需的基础数据
     *
     * @param parentId
     * @return
     * @throws Exception
     */
    Map<String, List<BaseDataVo>> findBaseDataRegister() throws Exception;

    /**
     * 开户页面所需的基础数据
     *
     * @return
     * @throws Exception
     */
    List<BaseDataVo> findopenAccount() throws Exception;

    /**
     * 发标页面需要的基础数据
     *
     * @return
     * @throws Exception
     */
    Map<String, List<BaseDataVo>> findBidIssuingData() throws Exception;
}
