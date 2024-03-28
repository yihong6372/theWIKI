package net.geekh.wiki.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author YIHONG
 * @Description
 * @Date 2024/3/24 16:44
 */

@Data
public class RePasswordForm {


    private String newPassword;


    private String oldPassword;

    @JsonProperty("checkPass")
    private String password2;

}
