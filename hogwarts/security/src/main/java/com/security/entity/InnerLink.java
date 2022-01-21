package com.security.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 内链表
 * </p>
 *
 * @author 作者
 * @since 2022-01-17
 */
@TableName("sys_inner_link")
public class InnerLink extends Model<InnerLink> {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 内链规则Id
     */
    private Integer innerRuleId;

    /**
     * 关键词
     */
    private String name;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 匹配文章数量
     */
    private Integer releaseNum;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 启用标识
     */
    private Integer isShow;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInnerRuleId() {
        return innerRuleId;
    }

    public void setInnerRuleId(Integer innerRuleId) {
        this.innerRuleId = innerRuleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getReleaseNum() {
        return releaseNum;
    }

    public void setReleaseNum(Integer releaseNum) {
        this.releaseNum = releaseNum;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "InnerLink{" +
        "id=" + id +
        ", innerRuleId=" + innerRuleId +
        ", name=" + name +
        ", url=" + url +
        ", releaseNum=" + releaseNum +
        ", sort=" + sort +
        ", isShow=" + isShow +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        "}";
    }
}
