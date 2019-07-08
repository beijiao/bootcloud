package com.dj.p2p.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dj.p2p.pojo.BaseData;
import com.dj.p2p.pojo.BaseDataVo;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface BaseDataMapper extends BaseMapper<BaseData> {


    /**
     * 开户页面所需的基础数据
     * @param userRole 角色的Id
     * @return
     *
     */
    List<BaseDataVo> findopenAccount(Integer userRole) throws DataAccessException;

}
