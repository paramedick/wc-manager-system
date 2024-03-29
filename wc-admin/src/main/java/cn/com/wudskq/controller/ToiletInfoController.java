package cn.com.wudskq.controller;

import cn.com.wudskq.annotation.InterfaceCall;
import cn.com.wudskq.annotation.OperatorLog;
import cn.com.wudskq.annotation.ProhibitResubmit;
import cn.com.wudskq.model.ToiletInfo;
import cn.com.wudskq.model.query.ToiletInfoQueryDTO;
import cn.com.wudskq.service.ToiletInfoService;
import cn.com.wudskq.vo.Response;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @author chenfangchao
 * @title: ToiletInfoController
 * @projectName wc-manager-system
 * @description: TODO
 * @date 2022/6/28 2:59 AM
 */
@Api(tags = "公厕管理")
@RestController
@RequestMapping("/system")
public class ToiletInfoController extends BaseController{

    @Autowired
    private ToiletInfoService toiletInfoService;

    @ApiOperation(value = "获取公厕信息列表")
    @PreAuthorize(value = "hasPermission('/system/toilet/list','res_system:toilet:query')")
    @InterfaceCall(interfaceName = "获取公厕信息列表",requestMode = "POST")
    @OperatorLog(module = "公厕管理", function = "公厕信息", action = "获取公厕信息列表", requestMode = "POST")
    @PostMapping("/toilet/list")
    public Response getToiletList(@RequestBody(required = false) ToiletInfoQueryDTO toiletInfoQuery) {
        List<ToiletInfo> toiletList = toiletInfoService.getToiletList(toiletInfoQuery);
        if (null != toiletList && 0 < toiletList.size()) {
            PageInfo<ToiletInfo> pageInfo = new PageInfo<ToiletInfo>(toiletList);
            return Response.success(Collections.singletonList(pageInfo.getList()), pageInfo.getTotal());
        }
        return Response.success();
    }


    @ApiOperation(value = "获取公厕详细信息")
    @PreAuthorize(value = "hasPermission('/system/toilet/detail','res_system:toilet:query')")
    @InterfaceCall(interfaceName = "获取公厕详细信息",requestMode = "GET")
    @OperatorLog(module = "公厕管理", function = "公厕信息", action = "获取公厕详细信息", requestMode = "GET")
    @GetMapping("/toilet/detail")
    public Response getToiletDetail(@RequestParam("id") Long id) {
        ToiletInfo toiletDetail = toiletInfoService.getToiletDetail(id);
        return Response.success(toiletDetail);
    }

    @ApiOperation(value = "新增公厕信息")
    @ProhibitResubmit
    @PreAuthorize(value = "hasPermission('/system/save/toilet','res_system:toilet:add')")
    @InterfaceCall(interfaceName = "新增公厕信息",requestMode = "POST")
    @OperatorLog(module = "公厕管理", function = "公厕信息", action = "新增公厕信息", requestMode = "POST")
    @PostMapping("/save/toilet")
    public Response saveToilet(@RequestBody ToiletInfo toiletInfo) {
        toiletInfoService.saveToilet(toiletInfo);
        return Response.success();
    }

    @ApiOperation(value = "更新公厕信息")
    @ProhibitResubmit
    @PreAuthorize(value = "hasPermission('/system/update/toilet','res_system:toilet:edit')")
    @InterfaceCall(interfaceName = "更新公厕信息",requestMode = "PUT")
    @OperatorLog(module = "公厕管理", function = "公厕信息", action = "更新公厕信息", requestMode = "PUT")
    @PutMapping("/update/toilet")
    public Response updateToilet(@RequestBody ToiletInfo toiletInfo) {
        toiletInfoService.updateToilet(toiletInfo);
        return Response.success();
    }

    @ApiOperation(value = "删除公厕信息(逻辑删除)")
    @PreAuthorize(value = "hasPermission('/system/remove/toilet','res_system:toilet:delete')")
    @InterfaceCall(interfaceName = "删除公厕信息",requestMode = "DELETE")
    @OperatorLog(module = "公厕管理", function = "公厕信息", action = "删除公厕信息", requestMode = "DELETE")
    @DeleteMapping("/remove/toilet")
    public Response removeToilet(@RequestParam("ids") List<Long> ids) {
        toiletInfoService.removeToilet(ids);
        return Response.success();
    }

}
