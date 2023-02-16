package cn.edu.hutb.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 表名：admin_user
 * 表注释：运营管理平台的admin级别用户
*/
@Table(name = "admin_user")
public class AdminUser {
    @Id
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 人脸入库图片信息，该信息保存到mongoDB的gridFS中
     */
    @Column(name = "face_id")
    private String faceId;

    /**
     * 管理人员的姓名
     */
    @Column(name = "admin_name")
    private String adminName;

    /**
     * 创建时间 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取人脸入库图片信息，该信息保存到mongoDB的gridFS中
     *
     * @return faceId - 人脸入库图片信息，该信息保存到mongoDB的gridFS中
     */
    public String getFaceId() {
        return faceId;
    }

    /**
     * 设置人脸入库图片信息，该信息保存到mongoDB的gridFS中
     *
     * @param faceId 人脸入库图片信息，该信息保存到mongoDB的gridFS中
     */
    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    /**
     * 获取管理人员的姓名
     *
     * @return adminName - 管理人员的姓名
     */
    public String getAdminName() {
        return adminName;
    }

    /**
     * 设置管理人员的姓名
     *
     * @param adminName 管理人员的姓名
     */
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    /**
     * 获取创建时间 创建时间
     *
     * @return createTime - 创建时间 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间 创建时间
     *
     * @param createTime 创建时间 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间 更新时间
     *
     * @return updateTime - 更新时间 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间 更新时间
     *
     * @param updateTime 更新时间 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}