package com.github.hwywl.antnest;

import com.github.hwywl.antnest.vo.User;
import org.junit.Test;

public class AntNestTests {

    @Test
    public void contextLoads() {
        User user = new User();

        user.setName("  sdsd\n" +
                "xd\n" +
                "dd\n" +
                "ss\\rdd\\r\\n最后的");
        System.out.println(user.getName());
    }

}
