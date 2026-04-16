package cn.yanghub.myblog.controller;

import cn.yanghub.myblog.common.Result;
import cn.yanghub.myblog.dto.CommentCreateDTO;
import cn.yanghub.myblog.vo.CommentVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @className: CommentController
 * @author: yang
 * @date: 2026/4/9 下午5:42
 * @Version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

//    private final CommentService commentService;
//
//    @GetMapping
//    public Result<List<CommentVO>> list(@RequestParam Long postId) {
//        List<CommentVO> comments = commentService.getCommentsByPostId(postId);
//        return Result.success(comments);
//    }
//
//    @PostMapping
//    public Result<CommentVO> create(@Valid @RequestBody CommentCreateDTO createDTO) {
//        CommentVO comment = commentService.createComment(createDTO);
//        return Result.success(comment);
//    }
//
//    @DeleteMapping("/{id}")
//    public Result<Void> delete(@PathVariable Long id) {
//        commentService.removeById(id);
//        return Result.success();
//    }
}