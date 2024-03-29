package cn.com.wudskq.service.impl;

import cn.com.wudskq.mapper.TSysResMapper;
import cn.com.wudskq.mapper.TSysRoleMapper;
import cn.com.wudskq.mapper.TSysRoleResMapper;
import cn.com.wudskq.model.SysUserDetails;
import cn.com.wudskq.model.TSysRes;
import cn.com.wudskq.model.TSysRole;
import cn.com.wudskq.model.query.ResInfoQueryDTO;
import cn.com.wudskq.model.vo.TreeSelectVo;
import cn.com.wudskq.service.TSysResService;
import cn.com.wudskq.utils.JWTTokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author wudskq
 */
@Service
public class TSysResServiceImpl implements TSysResService {
 
    @Resource
    private TSysResMapper tSysResMapper;
 
    @Resource
    private TSysRoleMapper tSysRoleMapper;
 
    @Resource
    private TSysRoleResMapper tSysRoleResMapper;


    /**
     * 根据用户id查询用户拥有的资源
     * [userId]
     * @return {@link List< TSysRes>}
     * @throws
     */
    @Override
    public List<TSysRes> findResByUserId(String userId) {

        //根据当前登录用户获取角色
        List<TSysRole> roleList = tSysRoleMapper.findRoleByUserId(userId);
        if(null == roleList  || 0 == roleList.size()){ //如果用户没有角色返回没有权限
            return null;
        }

        //根据角色获取菜单资源id关系集合
        List<String> tSysRoleResList = tSysRoleResMapper.selectRoleResByRoleIds(roleList);

        //如果用户没有角色返回没有权限
        if(tSysRoleResList == null || tSysRoleResList.size() == 0){
            return null;
        }
        //根据资源id获取菜单资源
        return tSysResMapper.selectBatchIds(tSysRoleResList);
    }

    @Override
    public List<TreeSelectVo> getResTree(String token) {
        //解析token 根据当前用户ID获取菜单权限列表
        SysUserDetails sysUserDetails = JWTTokenUtil.parseAccessToken(token);

        //查询选取菜单资源
        QueryWrapper<TSysRes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",0);

        //admin拥有全部权限
        if(!"admin".equals(sysUserDetails.getUsername())){
            List<Long> resIds = tSysRoleResMapper.getResIdByUserId(sysUserDetails.getId());
            if(null == resIds || 0 == resIds.size()){
                return null;
            }
            queryWrapper.in("id",resIds);
        }

        List<TSysRes> sysResList = tSysResMapper.selectList(queryWrapper);
        //构建菜单资源树
        return buildSysResTree(sysResList);
    }

    @Override
    public List<TSysRes> getResLIst(ResInfoQueryDTO resInfoQuery) {
        PageHelper.startPage(resInfoQuery.getPageNum(),resInfoQuery.getPageSize());
        List<TSysRes> resLIst = tSysResMapper.getResLIst(resInfoQuery);

        resLIst.forEach(obj->{
            //判断是否为有下属子菜单(即是否为相对父菜单)
            QueryWrapper<TSysRes> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("pid",obj.getId()).eq("status",0);

            //子菜单数量
            Integer subMenuCount = tSysResMapper.selectCount(queryWrapper);
            if(null != subMenuCount && 0 < subMenuCount){
                obj.setHasChildren(true);
            }
        });
        return  resLIst;
    }

    @Override
    public TSysRes getResDetail(Long id) {
        return tSysResMapper.getResDetail(id);
    }

    @Override
    public void saveRes(TSysRes sysRes) {
         tSysResMapper.insert(sysRes);
    }

    @Override
    public void updateRes(TSysRes sysRes) {
        tSysResMapper.updateById(sysRes);
    }

    @Override
    public void removeRes(List<Long> ids) {
        tSysResMapper.removeRes(ids);
    }


    /**
     * 构建菜单资源树
     * @param sysResList
     * @return
     */
    private List<TreeSelectVo> buildSysResTree(List<TSysRes> sysResList) {
        List<TreeSelectVo> resultList = new ArrayList<>(); //result
        //获取所有ID 顶级菜单节点默认pid为0
        List<Long> idList = new ArrayList<>();
        for (TSysRes sysRes : sysResList) {
            //存储列表节点ID
            idList.add(sysRes.getId());
        }
        //迭代判断是否为顶级节点
        for (Iterator<TSysRes> iterator = sysResList.iterator(); iterator.hasNext();){
            TSysRes sysRes = iterator.next();
            //判断是否为顶级节点
            if(!idList.contains(sysRes.getPid()) && 0 == sysRes.getPid()){
                //生成顶级节点
                TreeSelectVo treeSelectNode = new TreeSelectVo(sysRes);
                //设置顶级节点级别为0
                treeSelectNode.setLevel(0);
                //递归查找顶级节点下属所有子节点
                recursionFn(sysResList, treeSelectNode);
                //结果中添加顶级节点
                resultList.add(treeSelectNode);
            }
        }
        if (resultList.isEmpty())
        {
            List<TreeSelectVo> collect = sysResList.stream().map(TreeSelectVo::new).collect(Collectors.toList());
            resultList = collect;
        }
        return resultList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<TSysRes> list, TreeSelectVo t)
    {
        // 得到子节点列表
        List<TreeSelectVo> childList = getChildList(list, t);

        childList.forEach(obj->{
            // 子节点向下查询是否有子节点
            recursionFn(list,obj);
        });

        //设置子节点列表级别为3(其他)
        childList.forEach(obj->{obj.setLevel(3);});
        t.setChildren(childList);
    }

    /**
     * 得到子节点列表
     */
    private List<TreeSelectVo> getChildList(List<TSysRes> list, TreeSelectVo t)
    {
        //结果集
        List<TreeSelectVo> tlist = new ArrayList<>();
        //迭代判断
        Iterator<TSysRes> it = list.iterator();
        while (it.hasNext())
        {
            TSysRes n = it.next();
            //节点列表中的节点PID等于父节点的ID
            //代表该节点为顶级节点的子节点
            if(null != n && null != n.getPid() && null != t && null != t.getId())
            {
                if (n.getPid().longValue() == t.getId().longValue())
                {
                    TreeSelectVo treeSelect = new TreeSelectVo(n);
                    tlist.add(treeSelect);
                }
            }
        }
        return tlist;
    }
}


