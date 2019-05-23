package com.github.hwywl.antnest.vo;

import com.github.hwywl.antnest.annotation.process.TrimAll;
import lombok.Data;

@Data
public class User {

    @TrimAll
    private String name;
}
