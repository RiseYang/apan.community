package cn.yanghub.myblog.service.impl;


import cn.yanghub.myblog.dto.PostCreateDTO;
import cn.yanghub.myblog.dto.PostUpdateDTO;
import cn.yanghub.myblog.entity.Post;
import cn.yanghub.myblog.mapper.PostMapper;
import cn.yanghub.myblog.service.PostService;
import cn.yanghub.myblog.vo.PostVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @className: PostServiceImpl
 * @author: yang
 * @date: 2026/4/9 下午5:22
 * @Version: 1.0
 * @description:
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    private final PostMapper postMapper;

    @Override
    public PostVO getPostById(Long id) {
        PostVO postVO = postMapper.selectPostVOById(id);
        if (postVO == null) {
            throw new RuntimeException("文章不存在");
        }
        // 异步增加浏览量
        postMapper.incrementViewCount(id);
        return postVO;
    }

    @Override
    public PostVO getPostBySlug(String slug) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getSlug, slug)
                .eq(Post::getStatus, Post.PostStatus.published)
                .eq(Post::getDeletedAt, null);
        Post post = postMapper.selectOne(wrapper);
        if (post == null) {
            throw new RuntimeException("文章不存在");
        }
        return getPostById(post.getId());
    }

    @Override
    public IPage<PostVO> getPostPage(Integer page, Integer size, Long categoryId, String keyword) {
        Page<PostVO> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Post::getStatus, Post.PostStatus.published)
                .eq(Post::getDeletedAt, null)
                .orderByDesc(Post::getIsTop)
                .orderByDesc(Post::getPublishedAt);

        if (categoryId != null) {
            wrapper.eq(Post::getCategoryId, categoryId);
        }

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Post::getTitle, keyword)
                    .or()
                    .like(Post::getExcerpt, keyword));
        }

        return postMapper.selectPostVOPage(pageParam, wrapper);
    }

    @Override
    @Transactional
    public PostVO createPost(PostCreateDTO createDTO) {
        Post post = new Post();
        BeanUtils.copyProperties(createDTO, post);

        // 设置作者ID（从SecurityContext获取）
        // post.setAuthorId(getCurrentUserId());

        if (post.getStatus() == Post.PostStatus.published) {
            post.setPublishedAt(LocalDateTime.now());
        }

        // 计算阅读时间（假设每分钟阅读200字）
        if (post.getContent() != null) {
            int wordCount = post.getContent().length();
            post.setWordCount(wordCount);
            post.setReadingTime(Math.max(1, wordCount / 200));
        }

        postMapper.insert(post);
        return postMapper.selectPostVOById(post.getId());
    }

    @Override
    @Transactional
    public PostVO updatePost(Long id, @Valid PostUpdateDTO updateDTO) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new RuntimeException("文章不存在");
        }

        BeanUtils.copyProperties(updateDTO, post, "id", "authorId", "createdAt");

        if (post.getStatus() == Post.PostStatus.published && post.getPublishedAt() == null) {
            post.setPublishedAt(LocalDateTime.now());
        }

        postMapper.updateById(post);
        return postMapper.selectPostVOById(id);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        postMapper.deleteById(id);
    }

    @Override
    public List<PostVO> getFeaturedPosts(Integer limit) {
        return postMapper.selectFeaturedPosts(limit);
    }

    @Override
    public List<PostVO> getTopPosts(Integer limit) {
        return postMapper.selectTopPosts(limit);
    }
}
