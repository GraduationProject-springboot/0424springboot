
package com.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.*;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 教练
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/jiaolian")
public class JiaolianController {
    private static final Logger logger = LoggerFactory.getLogger(JiaolianController.class);

    private static final String TABLE_NAME = "jiaolian";

    @Autowired
    private JiaolianService jiaolianService;


    @Autowired
    private TokenService tokenService;

    @Autowired
    private DictionaryService dictionaryService;//字典
    @Autowired
    private GonggaoService gonggaoService;//公告
    @Autowired
    private JiaolianYuyueService jiaolianYuyueService;//教练预约
    @Autowired
    private LiuyanService liuyanService;//留言板
    @Autowired
    private QijuService qijuService;//滑雪器具
    @Autowired
    private QijuCollectionService qijuCollectionService;//器具收藏
    @Autowired
    private QijuCommentbackService qijuCommentbackService;//器具评价
    @Autowired
    private QijuOrderService qijuOrderService;//器具订单
    @Autowired
    private SaidaoService saidaoService;//赛道
    @Autowired
    private SaidaoCollectionService saidaoCollectionService;//赛道收藏
    @Autowired
    private SaidaoLiuyanService saidaoLiuyanService;//赛道留言
    @Autowired
    private SaidaoYuyueService saidaoYuyueService;//赛道预约
    @Autowired
    private YonghuService yonghuService;//用户
    @Autowired
    private UsersService usersService;//管理员


    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永不会进入");
        else if("用户".equals(role))
            params.put("yonghuId",request.getSession().getAttribute("userId"));
        else if("教练".equals(role))
            params.put("jiaolianId",request.getSession().getAttribute("userId"));
        CommonUtil.checkMap(params);
        PageUtils page = jiaolianService.queryPage(params);

        //字典表数据转换
        List<JiaolianView> list =(List<JiaolianView>)page.getList();
        for(JiaolianView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        JiaolianEntity jiaolian = jiaolianService.selectById(id);
        if(jiaolian !=null){
            //entity转view
            JiaolianView view = new JiaolianView();
            BeanUtils.copyProperties( jiaolian , view );//把实体数据重构到view中
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody JiaolianEntity jiaolian, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,jiaolian:{}",this.getClass().getName(),jiaolian.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");

        Wrapper<JiaolianEntity> queryWrapper = new EntityWrapper<JiaolianEntity>()
            .eq("username", jiaolian.getUsername())
            .or()
            .eq("jiaolian_phone", jiaolian.getJiaolianPhone())
            .or()
            .eq("jiaolian_id_number", jiaolian.getJiaolianIdNumber())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        JiaolianEntity jiaolianEntity = jiaolianService.selectOne(queryWrapper);
        if(jiaolianEntity==null){
            jiaolian.setCreateTime(new Date());
            jiaolian.setPassword("123456");
            jiaolianService.insert(jiaolian);
            return R.ok();
        }else {
            return R.error(511,"账户或者教练手机号或者教练身份证号已经被使用");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody JiaolianEntity jiaolian, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,jiaolian:{}",this.getClass().getName(),jiaolian.toString());
        JiaolianEntity oldJiaolianEntity = jiaolianService.selectById(jiaolian.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
        if("".equals(jiaolian.getJiaolianPhoto()) || "null".equals(jiaolian.getJiaolianPhoto())){
                jiaolian.setJiaolianPhoto(null);
        }
        if("".equals(jiaolian.getShangjiaContent()) || "null".equals(jiaolian.getShangjiaContent())){
                jiaolian.setShangjiaContent(null);
        }
        if("".equals(jiaolian.getShangjiaRongyuContent()) || "null".equals(jiaolian.getShangjiaRongyuContent())){
                jiaolian.setShangjiaRongyuContent(null);
        }

            jiaolianService.updateById(jiaolian);//根据id更新
            return R.ok();
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<JiaolianEntity> oldJiaolianList =jiaolianService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        jiaolianService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName, HttpServletRequest request){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        Integer yonghuId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //.eq("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
        try {
            List<JiaolianEntity> jiaolianList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            JiaolianEntity jiaolianEntity = new JiaolianEntity();
//                            jiaolianEntity.setUsername(data.get(0));                    //账户 要改的
//                            jiaolianEntity.setPassword("123456");//密码
//                            jiaolianEntity.setJiaolianUuidNumber(data.get(0));                    //教练编号 要改的
//                            jiaolianEntity.setJiaolianName(data.get(0));                    //教练姓名 要改的
//                            jiaolianEntity.setJiaolianPhone(data.get(0));                    //教练手机号 要改的
//                            jiaolianEntity.setJiaolianIdNumber(data.get(0));                    //教练身份证号 要改的
//                            jiaolianEntity.setJiaolianPhoto("");//详情和图片
//                            jiaolianEntity.setJiaolianShanchang(data.get(0));                    //擅长 要改的
//                            jiaolianEntity.setSexTypes(Integer.valueOf(data.get(0)));   //性别 要改的
//                            jiaolianEntity.setJiaolianEmail(data.get(0));                    //教练邮箱 要改的
//                            jiaolianEntity.setJinyongTypes(Integer.valueOf(data.get(0)));   //账户状态 要改的
//                            jiaolianEntity.setNewMoney(data.get(0));                    //预约价格/天 要改的
//                            jiaolianEntity.setShangjiaContent("");//详情和图片
//                            jiaolianEntity.setShangjiaRongyuContent("");//详情和图片
//                            jiaolianEntity.setCreateTime(date);//时间
                            jiaolianList.add(jiaolianEntity);


                            //把要查询是否重复的字段放入map中
                                //账户
                                if(seachFields.containsKey("username")){
                                    List<String> username = seachFields.get("username");
                                    username.add(data.get(0));//要改的
                                }else{
                                    List<String> username = new ArrayList<>();
                                    username.add(data.get(0));//要改的
                                    seachFields.put("username",username);
                                }
                                //教练编号
                                if(seachFields.containsKey("jiaolianUuidNumber")){
                                    List<String> jiaolianUuidNumber = seachFields.get("jiaolianUuidNumber");
                                    jiaolianUuidNumber.add(data.get(0));//要改的
                                }else{
                                    List<String> jiaolianUuidNumber = new ArrayList<>();
                                    jiaolianUuidNumber.add(data.get(0));//要改的
                                    seachFields.put("jiaolianUuidNumber",jiaolianUuidNumber);
                                }
                                //教练手机号
                                if(seachFields.containsKey("jiaolianPhone")){
                                    List<String> jiaolianPhone = seachFields.get("jiaolianPhone");
                                    jiaolianPhone.add(data.get(0));//要改的
                                }else{
                                    List<String> jiaolianPhone = new ArrayList<>();
                                    jiaolianPhone.add(data.get(0));//要改的
                                    seachFields.put("jiaolianPhone",jiaolianPhone);
                                }
                                //教练身份证号
                                if(seachFields.containsKey("jiaolianIdNumber")){
                                    List<String> jiaolianIdNumber = seachFields.get("jiaolianIdNumber");
                                    jiaolianIdNumber.add(data.get(0));//要改的
                                }else{
                                    List<String> jiaolianIdNumber = new ArrayList<>();
                                    jiaolianIdNumber.add(data.get(0));//要改的
                                    seachFields.put("jiaolianIdNumber",jiaolianIdNumber);
                                }
                        }

                        //查询是否重复
                         //账户
                        List<JiaolianEntity> jiaolianEntities_username = jiaolianService.selectList(new EntityWrapper<JiaolianEntity>().in("username", seachFields.get("username")));
                        if(jiaolianEntities_username.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(JiaolianEntity s:jiaolianEntities_username){
                                repeatFields.add(s.getUsername());
                            }
                            return R.error(511,"数据库的该表中的 [账户] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                         //教练编号
                        List<JiaolianEntity> jiaolianEntities_jiaolianUuidNumber = jiaolianService.selectList(new EntityWrapper<JiaolianEntity>().in("jiaolian_uuid_number", seachFields.get("jiaolianUuidNumber")));
                        if(jiaolianEntities_jiaolianUuidNumber.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(JiaolianEntity s:jiaolianEntities_jiaolianUuidNumber){
                                repeatFields.add(s.getJiaolianUuidNumber());
                            }
                            return R.error(511,"数据库的该表中的 [教练编号] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                         //教练手机号
                        List<JiaolianEntity> jiaolianEntities_jiaolianPhone = jiaolianService.selectList(new EntityWrapper<JiaolianEntity>().in("jiaolian_phone", seachFields.get("jiaolianPhone")));
                        if(jiaolianEntities_jiaolianPhone.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(JiaolianEntity s:jiaolianEntities_jiaolianPhone){
                                repeatFields.add(s.getJiaolianPhone());
                            }
                            return R.error(511,"数据库的该表中的 [教练手机号] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                         //教练身份证号
                        List<JiaolianEntity> jiaolianEntities_jiaolianIdNumber = jiaolianService.selectList(new EntityWrapper<JiaolianEntity>().in("jiaolian_id_number", seachFields.get("jiaolianIdNumber")));
                        if(jiaolianEntities_jiaolianIdNumber.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(JiaolianEntity s:jiaolianEntities_jiaolianIdNumber){
                                repeatFields.add(s.getJiaolianIdNumber());
                            }
                            return R.error(511,"数据库的该表中的 [教练身份证号] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                        jiaolianService.insertBatch(jiaolianList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }

    /**
    * 登录
    */
    @IgnoreAuth
    @RequestMapping(value = "/login")
    public R login(String username, String password, String captcha, HttpServletRequest request) {
        JiaolianEntity jiaolian = jiaolianService.selectOne(new EntityWrapper<JiaolianEntity>().eq("username", username));
        if(jiaolian==null || !jiaolian.getPassword().equals(password))
            return R.error("账号或密码不正确");
        else if(jiaolian.getJinyongTypes() != 1)
            return R.error("账户已被禁用");
        String token = tokenService.generateToken(jiaolian.getId(),username, "jiaolian", "教练");
        R r = R.ok();
        r.put("token", token);
        r.put("role","教练");
        r.put("username",jiaolian.getJiaolianName());
        r.put("tableName","jiaolian");
        r.put("userId",jiaolian.getId());
        return r;
    }

    /**
    * 注册
    */
    @IgnoreAuth
    @PostMapping(value = "/register")
    public R register(@RequestBody JiaolianEntity jiaolian, HttpServletRequest request) {
//    	ValidatorUtils.validateEntity(user);
        Wrapper<JiaolianEntity> queryWrapper = new EntityWrapper<JiaolianEntity>()
            .eq("username", jiaolian.getUsername())
            .or()
            .eq("jiaolian_phone", jiaolian.getJiaolianPhone())
            .or()
            .eq("jiaolian_id_number", jiaolian.getJiaolianIdNumber())
            ;
        JiaolianEntity jiaolianEntity = jiaolianService.selectOne(queryWrapper);
        if(jiaolianEntity != null)
            return R.error("账户或者教练手机号或者教练身份证号已经被使用");
        jiaolian.setJiaolianUuidNumber(String.valueOf(new Date().getTime()));
        jiaolian.setJinyongTypes(1);//启用
        jiaolian.setNewMoney(0.0);
        jiaolian.setCreateTime(new Date());
        jiaolianService.insert(jiaolian);

        return R.ok();
    }

    /**
     * 重置密码
     */
    @GetMapping(value = "/resetPassword")
    public R resetPassword(Integer  id, HttpServletRequest request) {
        JiaolianEntity jiaolian = jiaolianService.selectById(id);
        jiaolian.setPassword("123456");
        jiaolianService.updateById(jiaolian);
        return R.ok();
    }

	/**
	 * 修改密码
	 */
	@GetMapping(value = "/updatePassword")
	public R updatePassword(String  oldPassword, String  newPassword, HttpServletRequest request) {
        JiaolianEntity jiaolian = jiaolianService.selectById((Integer)request.getSession().getAttribute("userId"));
		if(newPassword == null){
			return R.error("新密码不能为空") ;
		}
		if(!oldPassword.equals(jiaolian.getPassword())){
			return R.error("原密码输入错误");
		}
		if(newPassword.equals(jiaolian.getPassword())){
			return R.error("新密码不能和原密码一致") ;
		}
        jiaolian.setPassword(newPassword);
		jiaolianService.updateById(jiaolian);
		return R.ok();
	}



    /**
     * 忘记密码
     */
    @IgnoreAuth
    @RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request) {
        JiaolianEntity jiaolian = jiaolianService.selectOne(new EntityWrapper<JiaolianEntity>().eq("username", username));
        if(jiaolian!=null){
            jiaolian.setPassword("123456");
            jiaolianService.updateById(jiaolian);
            return R.ok();
        }else{
           return R.error("账号不存在");
        }
    }


    /**
    * 获取用户的session用户信息
    */
    @RequestMapping("/session")
    public R getCurrJiaolian(HttpServletRequest request){
        Integer id = (Integer)request.getSession().getAttribute("userId");
        JiaolianEntity jiaolian = jiaolianService.selectById(id);
        if(jiaolian !=null){
            //entity转view
            JiaolianView view = new JiaolianView();
            BeanUtils.copyProperties( jiaolian , view );//把实体数据重构到view中

            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }
    }


    /**
    * 退出
    */
    @GetMapping(value = "logout")
    public R logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return R.ok("退出成功");
    }



    /**
    * 前端列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));

        CommonUtil.checkMap(params);
        PageUtils page = jiaolianService.queryPage(params);

        //字典表数据转换
        List<JiaolianView> list =(List<JiaolianView>)page.getList();
        for(JiaolianView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Integer id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        JiaolianEntity jiaolian = jiaolianService.selectById(id);
            if(jiaolian !=null){


                //entity转view
                JiaolianView view = new JiaolianView();
                BeanUtils.copyProperties( jiaolian , view );//把实体数据重构到view中

                //修改对应字典表字段
                dictionaryService.dictionaryConvert(view, request);
                return R.ok().put("data", view);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
    * 前端保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody JiaolianEntity jiaolian, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,jiaolian:{}",this.getClass().getName(),jiaolian.toString());
        Wrapper<JiaolianEntity> queryWrapper = new EntityWrapper<JiaolianEntity>()
            .eq("username", jiaolian.getUsername())
            .or()
            .eq("jiaolian_phone", jiaolian.getJiaolianPhone())
            .or()
            .eq("jiaolian_id_number", jiaolian.getJiaolianIdNumber())
//            .notIn("jiaolian_types", new Integer[]{102})
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        JiaolianEntity jiaolianEntity = jiaolianService.selectOne(queryWrapper);
        if(jiaolianEntity==null){
            jiaolian.setCreateTime(new Date());
            jiaolian.setPassword("123456");
        jiaolianService.insert(jiaolian);

            return R.ok();
        }else {
            return R.error(511,"账户或者教练手机号或者教练身份证号已经被使用");
        }
    }

}

