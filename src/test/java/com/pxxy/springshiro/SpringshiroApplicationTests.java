package com.pxxy.springshiro;

import com.pxxy.springshiro.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringshiroApplicationTests {
    @Autowired
    private UserService userService;
    @Test
    public void contextLoads() {
    }
    @Test
    public void testEmail(){
        String s = userService.sendCode();
        System.out.println(s);
    }
}
