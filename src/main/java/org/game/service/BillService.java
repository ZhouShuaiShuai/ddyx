package org.game.service;

import lombok.extern.slf4j.Slf4j;
import org.game.dao.JkBillDao;
import org.game.dao.UserDao;
import org.game.dao.YeBillDao;
import org.game.pojo.JkBill;
import org.game.pojo.User;
import org.game.pojo.YeBill;
import org.game.result.PageResult;
import org.game.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Zhouyf
 * @Data 2020-07-20  18:53
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BillService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JkBillDao jkBillDao;

    @Autowired
    private YeBillDao yeBillDao;


    public Result saveMoney(User user, BigDecimal money) {
        user.setJkmoney(user.getJkmoney().add(money));
        user.setMoney(user.getMoney().subtract(money));
        userDao.saveAndFlush(user);

        JkBill jkBill = new JkBill(money, "从余额存入", user);
        jkBillDao.save(jkBill);

        YeBill yeBill = new YeBill(money, "存入小金库", user);
        yeBillDao.save(yeBill);

        return new Result("存入小金库 ：" + money.toString());
    }

    public Result takeOutMoney(User user, BigDecimal money) {
        user.setJkmoney(user.getJkmoney().subtract(money));
        user.setMoney(user.getMoney().add(money));
        userDao.saveAndFlush(user);

        JkBill jkBill = new JkBill(money, "取出到余额", user);
        jkBillDao.save(jkBill);

        YeBill yeBill = new YeBill(money, "小金库取出", user);
        yeBillDao.save(yeBill);

        return new Result("从小金库取出 ：" + money.toString());
    }

    public Result transferMoney(User user, Integer otherUserId, BigDecimal money) {
        Optional<User> optional = userDao.findById(otherUserId);
        if (!optional.isPresent()) return new Result("对方用户不存在！", null);
        User otherUser = optional.get();

        user.setJkmoney(user.getJkmoney().subtract(money));
        otherUser.setJkmoney(otherUser.getJkmoney().add(money));
        user = userDao.saveAndFlush(user);
        otherUser = userDao.saveAndFlush(otherUser);

        JkBill jkBill = new JkBill(money, "转账到用户" + otherUser.getName(), user);
        JkBill otherJkBill = new JkBill(money, "收到" + user.getName() + "转账", otherUser);
        jkBillDao.save(jkBill);
        jkBillDao.save(otherJkBill);
        return new Result("成功转账给用户：" + otherUser.getName() + " " + money.toString() + "金豆！");
    }

    public Result getYeLog(Integer userId, Pageable pageable){
        Page<YeBill> billPage = yeBillDao.findByUserId(userId,pageable);
        return new PageResult(billPage);
    }

    public Result getJkLog(Integer userId, Pageable pageable){
        Page<JkBill> billPage = jkBillDao.findByUserId(userId,pageable);
        return new PageResult(billPage);
    }
}
