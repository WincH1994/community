package com.soulout.community.service;

import com.soulout.community.dto.PaginationDTO;
import com.soulout.community.dto.QuestionDTO;
import com.soulout.community.dto.QuestionQueryDTO;
import com.soulout.community.exception.CustomizeErrorCode;
import com.soulout.community.exception.CustomizeException;
import com.soulout.community.mapper.QuestionExtMapper;
import com.soulout.community.mapper.QuestionMapper;
import com.soulout.community.mapper.UserMapper;
import com.soulout.community.model.Question;
import com.soulout.community.model.QuestionExample;
import com.soulout.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO<QuestionDTO> list(String search, String tag, Integer page, Integer size) {

        if(StringUtils.isNotBlank(search)){
            String[] searchs = StringUtils.split(search, " ");
            search = String.join("|", searchs);
        }else{
            search = null;
        }

        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();

        Integer totalPage;

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        questionQueryDTO.setTag(tag);
        Integer totalCount = (int)questionExtMapper.countBySearch(questionQueryDTO);

        if(totalCount % size == 0 ){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / size + 1;
        }

        if(page < 1){
            page = 1;
        }

        if(page > totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage,page);

        if(page < 1){
            page = 1;
        }

        if(page > totalPage){
            page = totalPage;
        }

        Integer offset = page < 1 ? 0 :size * (page - 1);

        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question : questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);


        return paginationDTO;
    }

    public PaginationDTO<QuestionDTO> listByUserId(Long userId, Integer page, Integer size) {

        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();

        Integer totalPage;

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);

        if(totalCount % size == 0 ){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / size + 1;
        }

        if(page < 1){
            page = 1;
        }

        if(page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);
        Integer offset =size * (page - 1);

        if(offset < 0){
            offset = 0;
        }

        QuestionExample example = new QuestionExample();
        example.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question : questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);


        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND );
        }
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();

        BeanUtils.copyProperties(question,questionDTO);

        questionDTO.setUser(user);

        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() != null){
            //更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModify(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            if(updated != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }else{
            //新增
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModify(question.getGmtCreate());
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO quertDTO) {
        if(StringUtils.isBlank(quertDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(quertDTO.getTag(), ",");

        String regexTag = String.join("|", tags);

        Question question = new Question();
        question.setId(quertDTO.getId());
        question.setTag(regexTag);


        List<Question> questions = questionExtMapper.selectRelated(question);

        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());

        return questionDTOS;
    }
}
