package cn.yanghub.myblog.mapper;

import cn.yanghub.myblog.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> selectHotTags(Integer limit);

    List<Tag> selectFeaturedTags(Integer limit);

    List<Tag> selectTagsByPostId(Long postId);
}
