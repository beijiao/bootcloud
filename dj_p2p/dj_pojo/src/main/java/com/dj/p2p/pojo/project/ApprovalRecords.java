package com.dj.p2p.pojo.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yyw
 * 审核轨迹
 */
@Data
@TableName("p2p_approval_records")
public class ApprovalRecords implements Serializable {


    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    /**
     * 什么状态的时候
     */
    private String status;

    /**
     * 同意还是拒绝
     */

    private String opinion;
    /**
     * 审核时间
     */

    private Date createTime;

    /**
     * 标的id
     */
    private Integer markId;
    /**
     * 审批时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GTM+8")
    @TableField(exist = false)
    private Date approvalTime;
}
