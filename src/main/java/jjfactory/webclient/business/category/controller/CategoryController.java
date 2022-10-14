package jjfactory.webclient.business.category.controller;

import jjfactory.webclient.business.category.dto.req.CategoryCreate;
import jjfactory.webclient.business.category.dto.req.CategoryModify;
import jjfactory.webclient.business.category.dto.res.CategoryNameRes;
import jjfactory.webclient.business.category.service.CategoryService;
import jjfactory.webclient.global.config.auth.PrincipalDetails;
import jjfactory.webclient.global.dto.res.ApiListRes;
import jjfactory.webclient.global.dto.res.ApiRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    public ApiListRes<CategoryNameRes> findCategories(){
        return new ApiListRes<>(categoryService.findCategories());
    }

    @PostMapping("/admin/category")
    public ApiRes<Long> createCategory(@RequestBody CategoryCreate dto,
                                       @AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiRes<>(categoryService.save(dto));
    }

    @PutMapping("/admin/category/{id}")
    public ApiRes<Long> modifyCategory(@RequestBody CategoryModify dto,
                                       @PathVariable Long id,
                                       @AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiRes<>(categoryService.modify(dto,id));
    }

    @DeleteMapping("/admin/category")
    public ApiRes<String> deleteCategory(@RequestParam Long categoryId,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiRes<>(categoryService.delete(categoryId));
    }
}
