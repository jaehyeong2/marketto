package jjfactory.webclient.business.category.service;

import jjfactory.webclient.business.category.domain.Category;
import jjfactory.webclient.business.category.dto.req.CategoryCreate;
import jjfactory.webclient.business.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Long save(CategoryCreate dto){
        Category category = Category.create(dto);
        categoryRepository.save(category);

        return category.getId();
    }

    public void delete(Long id){
        categoryRepository.deleteById(id);
    }

}
