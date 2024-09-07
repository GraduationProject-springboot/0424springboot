
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
 * 赛道
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/saidao")
public class SaidaoController {
    private static final Logger logger = LoggerFactory.getLogger(SaidaoController.class);

    private static final String TABLE_NAME = "saidao";

    @Autowired
    private SaidaoService saidaoService;


    @Autowired
    private TokenService tokenService;

    @Autowired
    private DictionaryService dictionaryService;//字典
    @Autowired
    private GonggaoService gonggaoService;//公告
    @Autowired
    private JiaolianService jiaolianService;//教练
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
        params.put("saidaoDeleteStart",1);params.put("saidaoDeleteEnd",1);
        CommonUtil.checkMap(params);
        PageUtils page = saidaoService.queryPage(params);

        //字典表数据转换
        List<SaidaoView> list =(List<SaidaoView>)page.getList();
        for(SaidaoView c:list){
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
        SaidaoEntity saidao = saidaoService.selectById(id);
        if(saidao !=null){
            //entity转view
            SaidaoView view = new SaidaoView();
            BeanUtils.copyProperties( saidao , view );//把实体数据重构到view中
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
    public R save(@RequestBody SaidaoEntity saidao, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,saidao:{}",this.getClass().getName(),saidao.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");

        Wrapper<SaidaoEntity> queryWrapper = new EntityWrapper<SaidaoEntity>()
            .eq("saidao_name", saidao.getSaidaoName())
            .eq("saidao_types", saidao.getSaidaoTypes())
            .eq("shangxia_types", saidao.getShangxiaTypes())
            .eq("saidao_delete", 1)
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        SaidaoEntity saidaoEntity = saidaoService.selectOne(queryWrapper);
        if(saidaoEntity==null){
            saidao.setSaidaoClicknum(1);
            saidao.setShangxiaTypes(1);
            saidao.setSaidaoDelete(1);
            saidao.setInsertTime(new Date());
            saidao.setCreateTime(new Date());
            saidaoService.insert(saidao);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody SaidaoEntity saidao, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,saidao:{}",this.getClass().getName(),saidao.toString());
        SaidaoEntity oldSaidaoEntity = saidaoService.selectById(saidao.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
        if("".equals(saidao.getSaidaoPhoto()) || "null".equals(saidao.getSaidaoPhoto())){
                saidao.setSaidaoPhoto(null);
        }
        if("".equals(saidao.getSaidaoContent()) || "null".equals(saidao.getSaidaoContent())){
                saidao.setSaidaoContent(null);
        }

            saidaoService.updateById(saidao);//根据id更新
            return R.ok();
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<SaidaoEntity> oldSaidaoList =saidaoService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        ArrayList<SaidaoEntity> list = new ArrayList<>();
        for(Integer id:ids){
            SaidaoEntity saidaoEntity = new SaidaoEntity();
            saidaoEntity.setId(id);
            saidaoEntity.setSaidaoDelete(2);
            list.add(saidaoEntity);
        }
        if(list != null && list.size() >0){
            saidaoService.updateBatchById(list);
        }

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
            List<SaidaoEntity> saidaoList = new ArrayList<>();//上传的东西
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
                            SaidaoEntity saidaoEntity = new SaidaoEntity();
//                            saidaoEntity.setSaidaoName(data.get(0));                    //赛道名称 要改的
//                            saidaoEntity.setSaidaoUuidNumber(data.get(0));                    //赛道编号 要改的
//                            saidaoEntity.setSaidaoPhoto("");//详情和图片
//                            saidaoEntity.setSaidaoTypes(Integer.valueOf(data.get(0)));   //赛道类型 要改的
//                            saidaoEntity.setSaidaoOldMoney(data.get(0));                    //赛道原价 要改的
//                            saidaoEntity.setSaidaoNewMoney(data.get(0));                    //现价/天 要改的
//                            saidaoEntity.setSaidaoClicknum(Integer.valueOf(data.get(0)));   //赛道热度 要改的
//                            saidaoEntity.setSaidaoContent("");//详情和图片
//                            saidaoEntity.setShangxiaTypes(Integer.valueOf(data.get(0)));   //是否上架 要改的
//                            saidaoEntity.setSaidaoDelete(1);//逻辑删除字段
//                            saidaoEntity.setInsertTime(date);//时间
//                            saidaoEntity.setCreateTime(date);//时间
                            saidaoList.add(saidaoEntity);


                            //把要查询是否重复的字段放入map中
                                //赛道编号
                                if(seachFields.containsKey("saidaoUuidNumber")){
                                    List<String> saidaoUuidNumber = seachFields.get("saidaoUuidNumber");
                                    saidaoUuidNumber.add(data.get(0));//要改的
                                }else{
                                    List<String> saidaoUuidNumber = new ArrayList<>();
                                    saidaoUuidNumber.add(data.get(0));//要改的
                                    seachFields.put("saidaoUuidNumber",saidaoUuidNumber);
                                }
                        }

                        //查询是否重复
                         //赛道编号
                        List<SaidaoEntity> saidaoEntities_saidaoUuidNumber = saidaoService.selectList(new EntityWrapper<SaidaoEntity>().in("saidao_uuid_number", seachFields.get("saidaoUuidNumber")).eq("saidao_delete", 1));
                        if(saidaoEntities_saidaoUuidNumber.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(SaidaoEntity s:saidaoEntities_saidaoUuidNumber){
                                repeatFields.add(s.getSaidaoUuidNumber());
                            }
                            return R.error(511,"数据库的该表中的 [赛道编号] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                        saidaoService.insertBatch(saidaoList);
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
    * 个性推荐
    */
    @IgnoreAuth
    @RequestMapping("/gexingtuijian")
    public R gexingtuijian(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("gexingtuijian方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        CommonUtil.checkMap(params);
        List<SaidaoView> returnSaidaoViewList = new ArrayList<>();

        //查看收藏
        Map<String, Object> params1 = new HashMap<>(params);params1.put("sort","id");params1.put("yonghuId",request.getSession().getAttribute("userId"));
        params1.put("shangxiaTypes",1);
        params1.put("saidaoYesnoTypes",2);
        PageUtils pageUtils = saidaoCollectionService.queryPage(params1);
        List<SaidaoCollectionView> collectionViewsList =(List<SaidaoCollectionView>)pageUtils.getList();
        Map<Integer,Integer> typeMap=new HashMap<>();//购买的类型list
        for(SaidaoCollectionView collectionView:collectionViewsList){
            Integer saidaoTypes = collectionView.getSaidaoTypes();
            if(typeMap.containsKey(saidaoTypes)){
                typeMap.put(saidaoTypes,typeMap.get(saidaoTypes)+1);
            }else{
                typeMap.put(saidaoTypes,1);
            }
        }
        List<Integer> typeList = new ArrayList<>();//排序后的有序的类型 按最多到最少
        typeMap.entrySet().stream().sorted((o1, o2) -> o2.getValue() - o1.getValue()).forEach(e -> typeList.add(e.getKey()));//排序
        Integer limit = Integer.valueOf(String.valueOf(params.get("limit")));
        for(Integer type:typeList){
            Map<String, Object> params2 = new HashMap<>(params);params2.put("saidaoTypes",type);
            params2.put("shangxiaTypes",1);
            params2.put("saidaoYesnoTypes",2);
            PageUtils pageUtils1 = saidaoService.queryPage(params2);
            List<SaidaoView> saidaoViewList =(List<SaidaoView>)pageUtils1.getList();
            returnSaidaoViewList.addAll(saidaoViewList);
            if(returnSaidaoViewList.size()>= limit) break;//返回的推荐数量大于要的数量 跳出循环
        }
        params.put("shangxiaTypes",1);
        params.put("saidaoYesnoTypes",2);
        //正常查询出来商品,用于补全推荐缺少的数据
        PageUtils page = saidaoService.queryPage(params);
        if(returnSaidaoViewList.size()<limit){//返回数量还是小于要求数量
            int toAddNum = limit - returnSaidaoViewList.size();//要添加的数量
            List<SaidaoView> saidaoViewList =(List<SaidaoView>)page.getList();
            for(SaidaoView saidaoView:saidaoViewList){
                Boolean addFlag = true;
                for(SaidaoView returnSaidaoView:returnSaidaoViewList){
                    if(returnSaidaoView.getId().intValue() ==saidaoView.getId().intValue()) addFlag=false;//返回的数据中已存在此商品
                }
                if(addFlag){
                    toAddNum=toAddNum-1;
                    returnSaidaoViewList.add(saidaoView);
                    if(toAddNum==0) break;//够数量了
                }
            }
        }else {
            returnSaidaoViewList = returnSaidaoViewList.subList(0, limit);
        }

        for(SaidaoView c:returnSaidaoViewList)
            dictionaryService.dictionaryConvert(c, request);
        page.setList(returnSaidaoViewList);
        return R.ok().put("data", page);
    }

    /**
    * 前端列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));

        CommonUtil.checkMap(params);
        PageUtils page = saidaoService.queryPage(params);

        //字典表数据转换
        List<SaidaoView> list =(List<SaidaoView>)page.getList();
        for(SaidaoView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Integer id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        SaidaoEntity saidao = saidaoService.selectById(id);
            if(saidao !=null){

                //点击数量加1
                saidao.setSaidaoClicknum(saidao.getSaidaoClicknum()+1);
                saidaoService.updateById(saidao);

                //entity转view
                SaidaoView view = new SaidaoView();
                BeanUtils.copyProperties( saidao , view );//把实体数据重构到view中

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
    public R add(@RequestBody SaidaoEntity saidao, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,saidao:{}",this.getClass().getName(),saidao.toString());
        Wrapper<SaidaoEntity> queryWrapper = new EntityWrapper<SaidaoEntity>()
            .eq("saidao_name", saidao.getSaidaoName())
            .eq("saidao_uuid_number", saidao.getSaidaoUuidNumber())
            .eq("saidao_types", saidao.getSaidaoTypes())
            .eq("saidao_clicknum", saidao.getSaidaoClicknum())
            .eq("shangxia_types", saidao.getShangxiaTypes())
            .eq("saidao_delete", saidao.getSaidaoDelete())
//            .notIn("saidao_types", new Integer[]{102})
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        SaidaoEntity saidaoEntity = saidaoService.selectOne(queryWrapper);
        if(saidaoEntity==null){
            saidao.setSaidaoClicknum(1);
            saidao.setSaidaoDelete(1);
            saidao.setInsertTime(new Date());
            saidao.setCreateTime(new Date());
        saidaoService.insert(saidao);

            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

}

