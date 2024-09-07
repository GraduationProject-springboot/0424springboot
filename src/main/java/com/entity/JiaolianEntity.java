package com.entity;

import com.annotation.ColumnInfo;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import java.util.*;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.utils.DateUtil;


/**
 * 教练
 *
 * @author 
 * @email
 */
@TableName("jiaolian")
public class JiaolianEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public JiaolianEntity() {

	}

	public JiaolianEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ColumnInfo(comment="主键",type="int(11)")
    @TableField(value = "id")

    private Integer id;


    /**
     * 账户
     */
    @ColumnInfo(comment="账户",type="varchar(200)")
    @TableField(value = "username")

    private String username;


    /**
     * 密码
     */
    @ColumnInfo(comment="密码",type="varchar(200)")
    @TableField(value = "password")

    private String password;


    /**
     * 教练编号
     */
    @ColumnInfo(comment="教练编号",type="varchar(200)")
    @TableField(value = "jiaolian_uuid_number")

    private String jiaolianUuidNumber;


    /**
     * 教练姓名
     */
    @ColumnInfo(comment="教练姓名",type="varchar(200)")
    @TableField(value = "jiaolian_name")

    private String jiaolianName;


    /**
     * 教练手机号
     */
    @ColumnInfo(comment="教练手机号",type="varchar(200)")
    @TableField(value = "jiaolian_phone")

    private String jiaolianPhone;


    /**
     * 教练身份证号
     */
    @ColumnInfo(comment="教练身份证号",type="varchar(200)")
    @TableField(value = "jiaolian_id_number")

    private String jiaolianIdNumber;


    /**
     * 教练照片
     */
    @ColumnInfo(comment="教练照片",type="varchar(200)")
    @TableField(value = "jiaolian_photo")

    private String jiaolianPhoto;


    /**
     * 擅长
     */
    @ColumnInfo(comment="擅长",type="varchar(200)")
    @TableField(value = "jiaolian_shanchang")

    private String jiaolianShanchang;


    /**
     * 性别
     */
    @ColumnInfo(comment="性别",type="int(11)")
    @TableField(value = "sex_types")

    private Integer sexTypes;


    /**
     * 教练邮箱
     */
    @ColumnInfo(comment="教练邮箱",type="varchar(200)")
    @TableField(value = "jiaolian_email")

    private String jiaolianEmail;


    /**
     * 账户状态
     */
    @ColumnInfo(comment="账户状态",type="int(11)")
    @TableField(value = "jinyong_types")

    private Integer jinyongTypes;


    /**
     * 预约价格/天
     */
    @ColumnInfo(comment="预约价格/天",type="decimal(10,2)")
    @TableField(value = "new_money")

    private Double newMoney;


    /**
     * 教练介绍
     */
    @ColumnInfo(comment="教练介绍",type="longtext")
    @TableField(value = "shangjia_content")

    private String shangjiaContent;


    /**
     * 所获荣誉
     */
    @ColumnInfo(comment="所获荣誉",type="longtext")
    @TableField(value = "shangjia_rongyu_content")

    private String shangjiaRongyuContent;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="创建时间",type="timestamp")
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;


    /**
	 * 获取：主键
	 */
    public Integer getId() {
        return id;
    }
    /**
	 * 设置：主键
	 */

    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 获取：账户
	 */
    public String getUsername() {
        return username;
    }
    /**
	 * 设置：账户
	 */

    public void setUsername(String username) {
        this.username = username;
    }
    /**
	 * 获取：密码
	 */
    public String getPassword() {
        return password;
    }
    /**
	 * 设置：密码
	 */

    public void setPassword(String password) {
        this.password = password;
    }
    /**
	 * 获取：教练编号
	 */
    public String getJiaolianUuidNumber() {
        return jiaolianUuidNumber;
    }
    /**
	 * 设置：教练编号
	 */

    public void setJiaolianUuidNumber(String jiaolianUuidNumber) {
        this.jiaolianUuidNumber = jiaolianUuidNumber;
    }
    /**
	 * 获取：教练姓名
	 */
    public String getJiaolianName() {
        return jiaolianName;
    }
    /**
	 * 设置：教练姓名
	 */

    public void setJiaolianName(String jiaolianName) {
        this.jiaolianName = jiaolianName;
    }
    /**
	 * 获取：教练手机号
	 */
    public String getJiaolianPhone() {
        return jiaolianPhone;
    }
    /**
	 * 设置：教练手机号
	 */

    public void setJiaolianPhone(String jiaolianPhone) {
        this.jiaolianPhone = jiaolianPhone;
    }
    /**
	 * 获取：教练身份证号
	 */
    public String getJiaolianIdNumber() {
        return jiaolianIdNumber;
    }
    /**
	 * 设置：教练身份证号
	 */

    public void setJiaolianIdNumber(String jiaolianIdNumber) {
        this.jiaolianIdNumber = jiaolianIdNumber;
    }
    /**
	 * 获取：教练照片
	 */
    public String getJiaolianPhoto() {
        return jiaolianPhoto;
    }
    /**
	 * 设置：教练照片
	 */

    public void setJiaolianPhoto(String jiaolianPhoto) {
        this.jiaolianPhoto = jiaolianPhoto;
    }
    /**
	 * 获取：擅长
	 */
    public String getJiaolianShanchang() {
        return jiaolianShanchang;
    }
    /**
	 * 设置：擅长
	 */

    public void setJiaolianShanchang(String jiaolianShanchang) {
        this.jiaolianShanchang = jiaolianShanchang;
    }
    /**
	 * 获取：性别
	 */
    public Integer getSexTypes() {
        return sexTypes;
    }
    /**
	 * 设置：性别
	 */

    public void setSexTypes(Integer sexTypes) {
        this.sexTypes = sexTypes;
    }
    /**
	 * 获取：教练邮箱
	 */
    public String getJiaolianEmail() {
        return jiaolianEmail;
    }
    /**
	 * 设置：教练邮箱
	 */

    public void setJiaolianEmail(String jiaolianEmail) {
        this.jiaolianEmail = jiaolianEmail;
    }
    /**
	 * 获取：账户状态
	 */
    public Integer getJinyongTypes() {
        return jinyongTypes;
    }
    /**
	 * 设置：账户状态
	 */

    public void setJinyongTypes(Integer jinyongTypes) {
        this.jinyongTypes = jinyongTypes;
    }
    /**
	 * 获取：预约价格/天
	 */
    public Double getNewMoney() {
        return newMoney;
    }
    /**
	 * 设置：预约价格/天
	 */

    public void setNewMoney(Double newMoney) {
        this.newMoney = newMoney;
    }
    /**
	 * 获取：教练介绍
	 */
    public String getShangjiaContent() {
        return shangjiaContent;
    }
    /**
	 * 设置：教练介绍
	 */

    public void setShangjiaContent(String shangjiaContent) {
        this.shangjiaContent = shangjiaContent;
    }
    /**
	 * 获取：所获荣誉
	 */
    public String getShangjiaRongyuContent() {
        return shangjiaRongyuContent;
    }
    /**
	 * 设置：所获荣誉
	 */

    public void setShangjiaRongyuContent(String shangjiaRongyuContent) {
        this.shangjiaRongyuContent = shangjiaRongyuContent;
    }
    /**
	 * 获取：创建时间
	 */
    public Date getCreateTime() {
        return createTime;
    }
    /**
	 * 设置：创建时间
	 */

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Jiaolian{" +
            ", id=" + id +
            ", username=" + username +
            ", password=" + password +
            ", jiaolianUuidNumber=" + jiaolianUuidNumber +
            ", jiaolianName=" + jiaolianName +
            ", jiaolianPhone=" + jiaolianPhone +
            ", jiaolianIdNumber=" + jiaolianIdNumber +
            ", jiaolianPhoto=" + jiaolianPhoto +
            ", jiaolianShanchang=" + jiaolianShanchang +
            ", sexTypes=" + sexTypes +
            ", jiaolianEmail=" + jiaolianEmail +
            ", jinyongTypes=" + jinyongTypes +
            ", newMoney=" + newMoney +
            ", shangjiaContent=" + shangjiaContent +
            ", shangjiaRongyuContent=" + shangjiaRongyuContent +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
        "}";
    }
}
