package cn.edu.hutb.pojo.bo;


import cn.edu.hutb.validate.CheckUrl;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SaveOrUpdateFriendLinkBO {

    /**
     * id不存在则是保存，存在就是更新
     */
    private String id;

    @NotBlank(message = "友情链接名不能为空")
    private String linkName;

    @NotBlank(message = "友情链接地址不能为空")
    @CheckUrl
    private String linkUrl;

    @NotNull(message = "请选择保留或删除")
    private Integer isDelete;

}