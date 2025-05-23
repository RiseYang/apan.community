package cn.yanghub.myblog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: HelloController
 * @author: yang
 * @date: 2025/5/23 23:54
 * @Version: 1.0
 * @description:
 */
@RestController
@RequestMapping
public class HelloController {


    @GetMapping("/hello")
    public String hello(){
        return "index.html";
    }
}