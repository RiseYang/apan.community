package cn.yanghub.myblog.service.impl;

/**
 * @className: CategoryServiceImpl
 * @author: yang
 * @date: 2026/4/9 下午5:54
 * @Version: 1.0
 * @description:
 */
import cn.yanghub.myblog.entity.Category;
import cn.yanghub.myblog.mapper.CategoryMapper;
import cn.yanghub.myblog.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> getNavCategories() {
        return categoryMapper.selectNavCategories();
    }

    @Override
    public List<Category> getRootCategories() {
        return categoryMapper.selectRootCategories();
    }

    @Override
    public List<Category> getChildren(Long parentId) {
        return categoryMapper.selectChildren(parentId);
    }
}