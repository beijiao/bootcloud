package com.dj.p2p.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;
import java.util.Map;
/**
 *基础数据实体类
 * @author yyw
 */
@Data
@TableName("p2p_base_data")
public class BaseData {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */

    private String baseName;
    /**
     * 父id
     */

    private Integer pId;

    /**
     * 是否删除 0 未 1 删
     */
    private Integer isDel;

    private List<Map<String, List<BaseDataVo>>> mapList;
}
