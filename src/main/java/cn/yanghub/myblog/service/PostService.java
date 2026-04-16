package cn.yanghub.myblog.service;

import cn.yanghub.myblog.dto.PostCreateDTO;
import cn.yanghub.myblog.dto.PostUpdateDTO;
import cn.yanghub.myblog.entity.Post;
import cn.yanghub.myblog.vo.PostVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;


import java.util.List;

public interface PostService extends IService<Post> {
    PostVO getPostById(Long id);
    PostVO getPostBySlug(String slug);
    IPage<PostVO> getPostPage(Integer page, Integer size, Long categoryId, String keyword);
    PostVO createPost(PostCreateDTO createDTO);
    PostVO updatePost(Long id, @Valid PostUpdateDTO updateDTO);
    void deletePost(Long id);
    List<PostVO> getFeaturedPosts(Integer limit);
    List<PostVO> getTopPosts(Integer limit);
}

