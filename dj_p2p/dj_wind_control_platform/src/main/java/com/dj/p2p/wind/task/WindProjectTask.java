package com.dj.p2p.wind.task;

import com.dj.p2p.pojo.project.Mark;
import com.dj.p2p.pojo.project.ProjectVO;
import com.dj.p2p.wind.common.MarkEnum;
import com.dj.p2p.wind.common.constant.NumberConstant;
import com.dj.p2p.wind.mapper.MarkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
public class WindProjectTask {

    @Autowired
    private MarkMapper markMapper;


    @Scheduled(cron = "0 * * * * ?")
    public void updateStatus() throws ParseException {
        List<ProjectVO> financialManagementProjects = markMapper.findFinancialManagementProjects(NumberConstant.CODE_TWO, null);

        for (ProjectVO projectVO : financialManagementProjects) {

            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(projectVO.getSalesTime());
            rightNow.add(Calendar.DATE, projectVO.getFundraisingTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = dateFormat.format(rightNow.getTime());
            Date parse = dateFormat.parse(format);
            BigDecimal multiply = projectVO.getCurrentMoney().divide(projectVO.getLoanAmount()).multiply(new BigDecimal(100.0));
            Date date = new Date();
            // 如果当前时间小于筹款日期 并且 筹款进度达到百分之百
            if (date.getTime() < parse.getTime() && multiply.equals(NumberConstant.ONE_HUNDRED)) {
                if (projectVO.getStatus().equals(MarkEnum.BORROWING.getName())) {
                    Mark mark = new Mark();
                    mark.setId(projectVO.getId());
                    mark.setStatus(MarkEnum.TO_BE_LENT.getId());
                    markMapper.updateById(mark);
                }
            }
            if (date.getTime() >= parse.getTime() && !multiply.equals(NumberConstant.ONE_HUNDRED)) {
                if (projectVO.getStatus().equals(MarkEnum.BORROWING.getName())) {
                    Mark mark = new Mark();
                    mark.setId(projectVO.getId());
                    mark.setStatus(MarkEnum.FAIL_TO_BE_SOLD_AT_AUCTION.getId());
                    markMapper.updateById(mark);
                }
            }

        }
    }


}
