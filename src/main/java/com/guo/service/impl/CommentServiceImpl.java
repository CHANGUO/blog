package com.guo.service.impl;

import com.guo.Dao.CommentRepository;
import com.guo.po.Comment;
import com.guo.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {


    @Autowired
    private CommentRepository commentRepository;



    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = new Sort(Sort.Direction.ASC,"createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId, sort);



        return eachComment(comments);
    }

    /****
     * copy elements  avoid sql persist
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentsView = new ArrayList<>();

        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }

        combineChildren(commentsView);
        return commentsView;
    }


    private List<Comment> tempRelyList= new ArrayList();
    private void combineChildren(List<Comment> comments){

        for(Comment comment : comments){
            List<Comment> replies = comment.getReplyComments();
            for(Comment reply : replies){
                recursively(reply);
            }
            comment.setReplyComments(tempRelyList);

            tempRelyList = new ArrayList<>();
        }

    }

    private void recursively(Comment comment){
        tempRelyList.add(comment);
        if(comment.getReplyComments().size() > 0){
            List<Comment> replies = comment.getReplyComments();

            for (Comment reply:replies) {
                tempRelyList.add(reply);
                if(reply.getReplyComments().size() > 0){
                    recursively(reply);
                }
            }
        }


    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if(parentCommentId != -1){
            comment.setParentComment(commentRepository.findOne(parentCommentId));
        }else {

            comment.setParentComment(null);
        }

        comment.setCreateTime(new Date());
        return commentRepository.save(comment);

    }
}
