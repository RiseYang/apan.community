package cn.yanghub.myblog.service.impl;

/**
 * @className: TagServiceImpl
 * @author: yang
 * @date: 2026/4/9 下午5:56
 * @Version: 1.0
 * @description:
 */
import cn.yanghub.myblog.entity.Tag;
import cn.yanghub.myblog.mapper.TagMapper;
import cn.yanghub.myblog.service.TagService;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final TagMapper tagMapper;

    @Override
    public List<Tag> getTagsByPostId(Long postId) {
        return tagMapper.selectTagsByPostId(postId);
    }

    @Override
    public List<Tag> getFeaturedTags(Integer limit) {
        return tagMapper.selectFeaturedTags(limit);
    }

    @Override
    public List<Tag> getHotTags(Integer limit) {
        return tagMapper.selectHotTags(limit);
    }

}
