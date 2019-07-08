package com.dj.p2p.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dj.p2p.pojo.Account;
import com.dj.p2p.pojo.Bill;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @author yyw
 */
public interface AccountMapper extends BaseMapper<Account> {

    /**
     * 查询我的借款的账单和 我的出借的账单的列表
     *
     * @param userId
     * @return
     * @throws DataAccessException
     */
    List<Bill> findBillByUserId(@Param("userId") Integer userId, @Param("markId") Integer markId,@Param("isWho") Integer isWho) throws DataAccessException;

}
