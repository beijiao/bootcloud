package com.dj.p2p.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.p2p.cnum.BaseDataEnum;
import com.dj.p2p.cnum.BidIssuingDataEnum;
import com.dj.p2p.common.BaseDataConstant;
import com.dj.p2p.mapper.BaseDataMapper;
import com.dj.p2p.pojo.BaseData;
import com.dj.p2p.pojo.BaseDataVo;
import com.dj.p2p.service.BaseDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础数据实现类
 */
@Service
public class BaseDataServiceImpl extends ServiceImpl<BaseDataMapper, BaseData> implements BaseDataService {

    @Autowired
    private BaseDataMapper baseDataMapper;

    /**
     * 根据parentId查询基础数据
     *
     * @param parentId
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, List<BaseDataVo>> findBaseDataRegister() throws Exception {
        BaseDataEnum[] values = BaseDataEnum.values();
        Map<String, List<BaseDataVo>> map = new HashMap<>(BaseDataConstant.MAP_SIZE);
        for (BaseDataEnum value : values) {

            if (value.getPId().equals(BaseDataConstant.IS_PARENT)) {
                List<BaseDataVo> list = new ArrayList<>();
                for (BaseDataEnum baseDataEnum : values) {
                    if (baseDataEnum.getPId().equals(value.getId())) {
                        BaseDataVo baseDataVo = new BaseDataVo();
                        baseDataVo.setId(baseDataEnum.getId());
                        baseDataVo.setBaseName(baseDataEnum.getBaseName());
                        baseDataVo.setPId(baseDataEnum.getPId());
                        list.add(baseDataVo);
                    }

                }
                map.put(value.getBaseName(), list);
            }
        }
        return map;
    }


    /**
     * 开户页面所需的基础数据
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<BaseDataVo> findopenAccount() throws Exception {
        List<BaseDataVo> list = baseDataMapper.findopenAccount(BaseDataConstant.USER_ROLE);
        return list;
    }

    /**
     * 发标页面需要的基础数据
     *
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, List<BaseDataVo>> findBidIssuingData() throws Exception {

        Map<String, List<BaseDataVo>> map = new HashMap<>(BaseDataConstant.MAP_SIZE);
        for (BidIssuingDataEnum value : BidIssuingDataEnum.values()) {
            if (value.getPId().equals(BaseDataConstant.IS_PARENT)) {
                List<BaseDataVo> list = new ArrayList<>();
                for (BidIssuingDataEnum baseDataEnum : BidIssuingDataEnum.values()) {
                    if (baseDataEnum.getPId().equals(value.getId())) {
                        BaseDataVo baseDataVo = new BaseDataVo();
                        baseDataVo.setId(baseDataEnum.getId());
                        baseDataVo.setBaseName(baseDataEnum.getName());
                        baseDataVo.setPId(baseDataEnum.getPId());
                        list.add(baseDataVo);
                    }

                }
                map.put(value.getName(), list);
            }
        }

        return map;
    }
}
