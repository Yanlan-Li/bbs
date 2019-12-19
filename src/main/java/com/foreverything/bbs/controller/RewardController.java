package com.foreverything.bbs.controller;

import com.foreverything.bbs.entities.Reward;
import com.foreverything.bbs.entities.Topic;
import com.foreverything.bbs.service.RewardService;
import com.foreverything.bbs.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName RewardController
 * @Author Yanlan_Li
 * Date 21:59 2019/12/17
 * @Description
 */
@Controller
public class RewardController {
    @Autowired
    RewardService rewardService;

    //获取reward列表
    @GetMapping("/reward")
    public ModelAndView getReward(){
        ModelAndView mv = new ModelAndView();
        mv.addObject("rewards", rewardService.getAllReward());
//        setViewName()用来设置跳转页面
       mv.setViewName("rewardPage");
        return mv;
    }

  //发布新悬赏
    @PostMapping("/reward")
    public String createNewReward(Reward reward, HttpServletRequest request){

        if (null==reward.getTitle()||null==reward.getContent()){
//            失败跳转，不同区域把topic改成自己区域的名称就行
            return "redirect:/new/reward";
        }else if(!rewardService.isEnough(reward.getPoints(), (Integer) request.getSession().getAttribute("userID"))) {
            return "redirect:/new/reward";//积分不够
        }else{
            Long id=rewardService.insertReward(reward);
            if (id>0){
//                成功跳转，同样只改topic
                return "redirect:/reward";
            }else{
                //            失败跳转，不同区域把topic改成自己区域的名称就行
                return "redirect:/new/reward";
            }
        }
    }

    @PutMapping("/update/reward")
    public ModelAndView updateReward(Reward reward){
        ModelAndView mv=new ModelAndView();
        if (null==reward.getContent()||null==reward.getTitle()){
            mv.addObject("msg","标题或内容为空！");

//            TODO 跳转到原修改帖子界面
        }else{
            if (rewardService.putReward(reward)>0){
                mv.addObject("msg","修改成功");
               // mv.setViewName("RewardPage");//               TODO 跳转到讨悬赏页面

            }else{
                mv.addObject("msg","修改失败！");

//                TODO 跳转到原修改帖子页面
            }
        }
        //mv.setViewName("rewardPage");
        return mv;
    }
}
