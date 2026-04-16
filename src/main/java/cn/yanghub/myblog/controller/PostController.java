package cn.yanghub.myblog.controller;

import cn.yanghub.myblog.common.PageResult;
import cn.yanghub.myblog.common.Result;
import cn.yanghub.myblog.dto.PostCreateDTO;
import cn.yanghub.myblog.dto.PostUpdateDTO;
import cn.yanghub.myblog.service.PostService;
import cn.yanghub.myblog.vo.PostVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @className: PostController
 * @author: yang
 * @date: 2026/4/9 下午5:22
 * @Version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public Result<PageResult<PostVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {

        IPage<PostVO> postPage = postService.getPostPage(page, size, categoryId, keyword);
        return Result.success(new PageResult<>(postPage));
    }

    @GetMapping("/{id}")
    public Result<PostVO> getById(@PathVariable Long id) {
        PostVO post = postService.getPostById(id);
        return Result.success(post);
    }

    @GetMapping("/slug/{slug}")
    public Result<PostVO> getBySlug(@PathVariable String slug) {
        PostVO post = postService.getPostBySlug(slug);
        return Result.success(post);
    }

    @GetMapping("/featured")
    public Result<List<PostVO>> getFeatured(@RequestParam(defaultValue = "5") Integer limit) {
        List<PostVO> posts = postService.getFeaturedPosts(limit);
        return Result.success(posts);
    }

    @GetMapping("/top")
    public Result<List<PostVO>> getTop(@RequestParam(defaultValue = "5") Integer limit) {
        List<PostVO> posts = postService.getTopPosts(limit);
        return Result.success(posts);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('admin', 'editor', 'author')")
    public Result<PostVO> create(@Valid @RequestBody PostCreateDTO createDTO) {
        PostVO post = postService.createPost(createDTO);
        return Result.success(post);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'editor', 'author')")
    public Result<PostVO> update(@PathVariable Long id, @Valid @RequestBody PostUpdateDTO updateDTO) {
        PostVO post = postService.updatePost(id, updateDTO);
        return Result.success(post);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'editor')")
    public Result<Void> delete(@PathVariable Long id) {
        postService.deletePost(id);
        return Result.success();
    }
}