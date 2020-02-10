package com.soulout.community.schedule;

import com.soulout.community.cache.HotTagCache;
import com.soulout.community.mapper.QuestionMapper;
import com.soulout.community.model.Question;
import com.soulout.community.model.QuestionExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class HotTagTask {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private HotTagCache hotTagCache;

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 3)
    //@Scheduled(cron = "0 0 1 * * *")
    public void reportCurrentTime() {

        int offset = 0;
        int limit = 20;

        List<Question> list = new ArrayList<>();
        log.info("HotTagTask start {}", new Date());


        Map<String,Integer> priorities = new HashMap<>();
        while(offset == 0 || list.size()<20){
            list = questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds(offset,limit));
            for (Question question : list) {
                String[] tags = StringUtils.split(question.getTag(),",");
                for (String tag : tags) {
                    Integer priority = priorities.get(tag);

                    if(priority != null){
                        priorities.put(tag,priority + 5 + question.getCommentCount());
                    }else {
                        priorities.put(tag,5 + question.getCommentCount());
                    }
                    
                }
            }
            offset += limit;
        }
        hotTagCache.updateTags(priorities);

        log.info("HotTagTask stop {}", new Date());
    }
}
