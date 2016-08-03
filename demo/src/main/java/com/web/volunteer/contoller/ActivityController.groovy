package com.web.volunteer.contoller

import com.alibaba.fastjson.JSONObject
import com.web.volunteer.domain.Activity
import com.web.volunteer.domain.Pend
import com.web.volunteer.service.impl.ActivityServiceImpl
import com.web.volunteer.service.impl.UserServiceImpl
import com.web.volunteer.util.CacheUtil
import groovy.json.JsonBuilder
import org.apache.commons.lang.StringUtils
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by qiangyipeng on 2016/5/22.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/activity")
class ActivityController {
    private final Logger logger = LoggerFactory.getLogger(ActivityController.class);
    @Autowired
    private ActivityServiceImpl activityService;
    @Autowired
    private UserServiceImpl userService

    /**
     * 发起活动
     * @param activity
     * @return
     */
    @RequestMapping(value = "/launch",method = RequestMethod.POST)
    String launch(Activity activity){
        def map = [:]
        if (activity.sponsor==0 || StringUtils.isBlank(activity.name) || StringUtils.isBlank(activity.startTime) || StringUtils.isBlank(activity.endTime)){
            map.put("msg","信息有误，发起活动失败！")
            map.put("code",0)
        }else if (!userService.isFullInfo(activity.sponsor)){//先判断发起人信息是否完整
            map.put("msg","请完善个人后再发起（姓名、邮箱、手机）！")
            map.put("code",0)
        }else{
            def type = activityService.launch(activity)
            map.put("code",type)
            if (type == 1)
                map.put("msg","活动发起成功！")
            else
                map.put("msg","活动发起异常！")
        }
        return new JsonBuilder(map).toString()
    }

    /**
     * 修改活动内容
     * @param activity
     * @return
     */
    @RequestMapping(value = "/saveActivity")
    String saveActivity(Activity newActivity,HttpServletRequest request){
        def map = [:]
        try {
            def activity = activityService.findOneById(newActivity.id)
            activity.name = StringUtils.isBlank(newActivity.name)?activity.name:newActivity.name
            activity.hour = StringUtils.isBlank(request.getParameter("hour"))?activity.hour:newActivity.hour
            activity.startTime = StringUtils.isBlank(newActivity.startTime)?activity.startTime:newActivity.startTime
            activity.endTime = StringUtils.isBlank(newActivity.endTime)?activity.endTime:newActivity.endTime
            activity.details = StringUtils.isBlank(newActivity.details)?activity.details:newActivity.details
            activityService.save(activity)
            map.put("msg","更新活动信息成功！")
            map.put("type",1)
        }catch (Exception e){
            logger.error(e.message)
            map.put("msg","更新活动信息失败！")
            map.put("type",0)
        }
        return new JsonBuilder(map).toString()
    }

    /**
     * 报名活动
     * @param pend
     * @param request
     * @return
     */
    @RequestMapping(value = "/join")
    String join(Pend pend,HttpServletRequest request){
        def map = [:]
        if (StringUtils.isBlank(request.getParameter("uid")) || StringUtils.isBlank(request.getParameter("activityId"))){
            map.put("msg","信息错误，加入活动失败！")
            map.put("code",0)
        }else if (!userService.isFullInfo(pend.uid)){//先判断报名人信息是否完整
            map.put("msg","请完善个人后再报名（姓名、邮箱、手机）！")
            map.put("code",0)
        }else if (activityService.hasJoined(pend.uid,pend.activityId)){//先判断报名人是否已经参与
            map.put("msg","您曾报名|受邀|参与|发起过该活动！")
            map.put("code",0)
        }else {
            def user = userService.findOneUser(pend.uid)
            pend.username = user.name
            activityService.addPend(pend,1)
            map.put("msg","报名成功！")
            map.put("code",1)
        }
        return new JsonBuilder(map).toString()
    }

    /**
     * 邀请好友
     * @param pend
     * @param request
     * @return
     */
    @RequestMapping(value = "/invite")
    String invite(Pend pend,HttpServletRequest request){
        def map = [:]
        if (StringUtils.isBlank(request.getParameter("uid")) || StringUtils.isBlank(request.getParameter("activityId"))){
            map.put("msg","信息错误，邀请朋友失败！")
            map.put("code",0)
        }else if (!userService.isFullInfo(pend.uid)){//先判断报名人信息是否完整
            map.put("msg","该好友资料不完善，无法邀请！")
            map.put("code",0)
        }else if (activityService.hasJoined(pend.uid,pend.activityId)){//先判断报名人是否已经参与
            map.put("msg","该好友曾报名|受邀|参与|发起过该活动！")
            map.put("code",0)
        }else {
            activityService.addPend(pend,2)
            map.put("msg","邀请成功！")
            map.put("code",1)
        }
        return new JsonBuilder(map).toString()
    }

    /**
     * 审批
     * @param pend
     * @param request
     * @return
     */
    @RequestMapping(value = "/approveUser")
    String approveUser(Pend pend,HttpServletRequest request,@RequestParam(value = "result",defaultValue = "0") Integer result){
        def map = [:]
        def type
        if (StringUtils.isBlank(request.getParameter("id")) || StringUtils.isBlank(request.getParameter("uid"))
                || StringUtils.isBlank(request.getParameter("activityId"))){
            map.put("msg","信息错误，审批失败！")
            map.put("type",0)
        }else {
            if (result == null || result == 0) {//拒绝
                type = activityService.reject(pend)
            }else {//批准
                type = activityService.approve(pend)
            }
            map.put("msg","")
            if (type != 1){
                map.put("msg","审批过程发生异常！")
            }
            map.put("code",type)
        }
        return new JsonBuilder(map).toString()
    }

    @RequestMapping(value = "/searchAC")
    String searchAC(@RequestParam(value = "page",defaultValue = "0") Integer page){
        def map = [:]
        def activityList = activityService.queryByPage(page)
        map.put("activityList",activityList)
        return new JsonBuilder(map).toString()
    }

    @RequestMapping(value = "/queryByName")
    String queryByName(@RequestParam(value = "name") String name){
        def map = [:]
        def activityList = activityService.queryByName(name)
        map.put("activityList",activityList)
        return new JsonBuilder(map).toString()
    }

    @RequestMapping(value = "/fuzzyQueryAC")
    String fuzzyQueryAC(@RequestParam(value = "name") String name){
        def map = [:]
        def nameList = activityService.fuzzyQuery(name)
        map.put("nameList",nameList)
        return new JsonBuilder(map).toString()
    }

    @RequestMapping(value = "/signIn")
    String signIn(@RequestParam(value = "activityId") Long activityId,
                  @RequestParam(value = "uid") Long uid,
                  @RequestParam(value = "type") Integer type){
        def map = [:]
        if (type==0&&CacheUtil.getCache(uid+"sign"+activityId)!=null){
            map.put("msg","您已签到过，不必重复操作！");
            map.put("code",0);
            return new JsonBuilder(map).toString()
        }else {
            if (activityService.signIn(uid,activityId,type)){
                map.put("msg","");
                map.put("code",1);
            }else {
                map.put("msg","签到发生异常，请退出重试！");
                map.put("code",0);
            }
        }
        return new JsonBuilder(map).toString()
    }

    @RequestMapping(value = "/feedback")
    String feedback(@RequestParam(value = "activityId") Long activityId,
                  @RequestParam(value = "uid") Long uid,
                  @RequestParam(value = "feedback") String feedback){
        def map = [:]
        if (activityService.feedback(uid,activityId,feedback)){
            map.put("msg","");
            map.put("code",1);
        }else {
            map.put("msg","提交发生异常，请退出重试！");
            map.put("code",0);
        }
        return new JsonBuilder(map).toString()
    }

    @RequestMapping(value = "/download")
    String download(@RequestParam(value = "activityId") Long activityId,HttpServletResponse response){
        def map = [:]
        def activity = activityService.findOneById(activityId)
        if (activity==null){
            map.put("msg","活动id有误，该id不存在！");
            map.put("code",0);
        }else {
            def downloadList = activityService.downloadList(activityId)
            if (downloadList==null||downloadList.size()==0){
                map.put("msg","该活动无反馈信息！")
                map.put("code",0)
//                return new JsonBuilder(map).toString()
            }
            try {
                HSSFWorkbook workbook = new HSSFWorkbook()
                HSSFSheet sheet = workbook.createSheet()
                workbook.setSheetName(0, "0")
                //设定sheet名不能被修改
                HSSFRow row = sheet.createRow((short) 0)
                HSSFCell cell;
                def title = ["姓名", "学院", "班级", "联系方式", "邮箱", "完成情况", "反馈"] as String[]
                // 写入各个字段的名称
                for (int i = 0; i < title.length; i++) {
                    cell = row.createCell(i)
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING)
                    cell.setCellValue(title[i])
                }
                int iRow = 1
                // 写入各条记录，每条记录对应Excel中的一行
                for (JSONObject rowInfo:downloadList){
                    row = sheet.createRow((short) iRow)
                    def info = (List<String>)rowInfo.get("infoList")
                    for (int i = 0; i < info.size(); i++) {
                        cell = row.createCell(i)
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING)
                        cell.setCellValue(info.get(i))
                    }
                    iRow++
                }
                String realPath = "/www/test/activityExcel/"+activity.name+".xls"
                FileOutputStream fOut = new FileOutputStream(realPath);
                workbook.write(fOut);
                fOut.flush();
                fOut.close();
                ByteArrayOutputStream os = new ByteArrayOutputStream()
                workbook.write(os)
                byte[] content = os.toByteArray();
                InputStream is = new ByteArrayInputStream(content);
                // 设置response参数，可以打开下载页面
                response.reset();
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.setHeader("Content-Disposition", "attachment;filename="+ new String((activity.name + ".xls").getBytes(), "iso-8859-1"));
                ServletOutputStream out = response.getOutputStream();
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                try {
                    bis = new BufferedInputStream(is);
                    bos = new BufferedOutputStream(out);
                    byte[] buff = new byte[2048];
                    int bytesRead;
                    // Simple read/write loop.
                    while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                        bos.write(buff, 0, bytesRead);
                    }
                } catch (final IOException e) {
                    throw e;
                } finally {
                    if (bis != null)
                        bis.close();
                    if (bos != null)
                        bos.close();
                }
                File file = new File(realPath);
                if (file.isFile() & file.exists())
                    file.delete();
                return null
            }catch (Exception e){
                logger.error(e.message)
                map.put("msg","服务器被海王类袭击，表格下载异常！");
                map.put("code",0);
            }
        }
//        return new JsonBuilder(map).toString()
    }
}
