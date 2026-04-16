package cn.yanghub.myblog.service.impl;

/**
 * @className: CommentServiceImpl
 * @author: yang
 * @date: 2026/4/9 下午5:46
 * @Version: 1.0
 * @description:
 */
import cn.yanghub.myblog.dto.CommentCreateDTO;
import cn.yanghub.myblog.vo.CommentVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl {

//    private final CommentMapper commentMapper;
//
//    @Override
//    public List<CommentVO> getCommentsByPostId(Long postId) {
//        return commentMapper.selectRootComments(postId);
//    }
//
//    @Override
//    public CommentVO createComment(CommentCreateDTO createDTO) {
//        Comment comment = new Comment();
//        BeanUtils.copyProperties(createDTO, comment);
//        comment.setStatus(Comment.CommentStatus.pending);
//
//        // 如果有父评论，设置rootId
//        if (comment.getParentId() != null) {
//            Comment parent = getById(comment.getParentId());
//            if (parent != null) {
//                comment.setRootId(parent.getRootId() != null ? parent.getRootId() : parent.getId());
//            }
//        }
//
//        save(comment);
//
//        // 返回VO
//        CommentVO vo = new CommentVO();
//        BeanUtils.copyProperties(comment, vo);
//        return vo;
//    }
}
