package cn.yanghub.myblog.service;

import cn.yanghub.myblog.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TagService extends IService<Tag> {

    List<Tag> getTagsByPostId(Long postId);

    List<Tag> getFeaturedTags(Integer limit);

    List<Tag> getHotTags(Integer limit);


}
