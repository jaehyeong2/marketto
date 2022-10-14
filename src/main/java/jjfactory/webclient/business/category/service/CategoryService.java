package jjfactory.webclient.business.category.service;

import jjfactory.webclient.business.category.domain.Category;
import jjfactory.webclient.business.category.dto.req.CategoryCreate;
import jjfactory.webclient.business.category.dto.req.CategoryModify;
import jjfactory.webclient.business.category.dto.res.CategoryNameRes;
import jjfactory.webclient.business.category.repository.CategoryRepository;
import jjfactory.webclient.business.member.domain.Member;
import jjfactory.webclient.global.ex.BusinessException;
import jjfactory.webclient.global.ex.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Long save(CategoryCreate dto){
        duplicateCheck(dto.getName());

        Category category = Category.create(dto);
        categoryRepository.save(category);

        return category.getId();
    }

    private void duplicateCheck(String name) {
        if(categoryRepository.findByName(name) != null){
            throw new BusinessException(ErrorCode.DUPLICATE_ENTITY);
        }
    }

    public String delete(Long id){
        categoryRepository.deleteById(id);
        return "ok";
    }

    public Long modify(CategoryModify dto,Long categoryId){
        Category category = getCategory(categoryId);

        duplicateCheck(dto.getName());

        category.changeName(dto);
        return categoryId;
    }

    private Category getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
        });
        return category;
    }

    public List<CategoryNameRes> findCategories(){
        return categoryRepository.findAll().stream()
                .map(c->new CategoryNameRes(c.getName()))
                .collect(Collectors.toList());
    }

}
