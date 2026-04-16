package cn.yanghub.myblog.service;

import cn.yanghub.myblog.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CategoryService extends IService<Category> {
    List<Category> getNavCategories();
    List<Category> getRootCategories();
    List<Category> getChildren(Long parentId);
}
