package jjfactory.webclient.business.category.controller;

import jjfactory.webclient.business.category.dto.req.CategoryCreate;
import jjfactory.webclient.business.category.service.CategoryService;
import jjfactory.webclient.global.dto.res.ApiRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/category")
@RequiredArgsConstructor
@RestController
public class CategoryController {
    private final CategoryService categoryService;
    //TODO ADMIN만
    @PostMapping
    public ApiRes<Long> createCategory(@RequestBody CategoryCreate dto){
        return new ApiRes<>(categoryService.save(dto));
    }

    @DeleteMapping
    public ApiRes<String> createCategory(@RequestParam Long categoryId){
        return new ApiRes<>(categoryService.delete(categoryId));
    }
}
